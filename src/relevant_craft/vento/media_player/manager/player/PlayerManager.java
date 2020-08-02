package relevant_craft.vento.media_player.manager.player;

import javafx.application.Platform;
import relevant_craft.vento.media_player.gui.main.MainGui;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.navigation.Navigation;
import relevant_craft.vento.media_player.gui.main.elements.navigation.NavigationItem;
import relevant_craft.vento.media_player.gui.main.elements.playlist.Playlist;
import relevant_craft.vento.media_player.gui.main.elements.playlist.PlaylistItem;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.gui.main.elements.visualization.Visualization;
import relevant_craft.vento.media_player.manager.color.ColorManager;
import relevant_craft.vento.media_player.manager.equalizer.EqualizerManager;
import relevant_craft.vento.media_player.manager.playlist.PlaylistData;
import relevant_craft.vento.media_player.manager.playlist.PlaylistManager;
import relevant_craft.vento.media_player.manager.settings.SettingsManager;
import relevant_craft.vento.media_player.manager.vumeter.VUMeterManager;
import relevant_craft.vento.media_player.utils.FileUtils;
import relevant_craft.vento.media_player.utils.SongUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerManager {
    private final MainGui mainGui;
    private final Title title;
    private final Control control;
    private final Navigation navigation;
    private final Visualization visualization;
    private final Playlist playlist;
    private final PlayerEngine playerEngine;
    private final VUMeterManager leftVU;
    private final VUMeterManager rightVU;
    private final EqualizerManager equalizer;
    private final PlaylistManager playlistManager;
    private final ColorManager colorManager;
    private final SettingsManager settingsManager;

    private double volumeLevel;
    private PlaylistData currentPlaylist;
    private Thread playlistLoader;
    private Thread wait;
    private PlaylistItem currentSong;
    private Random random;
    private List<String> randomOrder;
    private long lastSongClick;

    /**
     * Init player manager
     */
    public PlayerManager() {
        this.mainGui = MainGui.getInstance();
        this.title = mainGui.getTitle();
        this.control = mainGui.getControl();
        this.navigation = mainGui.getNavigation();
        this.visualization = mainGui.getVisualization();
        this.playlist = mainGui.getPlaylist();
        this.playerEngine = new PlayerEngine(control);
        this.leftVU = new VUMeterManager(visualization.getLeftVU());
        this.rightVU = new VUMeterManager(visualization.getRightVU());
        this.equalizer = new EqualizerManager(visualization.getEqualizer());
        this.playlistManager = new PlaylistManager();
        this.colorManager = ColorManager.getInstance();
        this.settingsManager = SettingsManager.getInstance();
        this.random = new Random();
        this.randomOrder = new ArrayList<>();
        this.lastSongClick = System.currentTimeMillis();

        this.title.getCloseButton().addClickListener(this::onCloseButtonClick);
        this.title.getMinimizeButton().addClickListener(this::onMinimizeButtonClick);

        this.volumeLevel = settingsManager.getVolumeLevel();
        this.control.getVolumeSlider().setProgress(settingsManager.getVolumeLevel());
        this.control.getMuteButton().setSelected(settingsManager.isMuted());
        this.control.getRepeatButton().setSelected(settingsManager.isRepeatEnabled());
        this.control.getRandomButton().setSelected(settingsManager.isRandomEnabled());
        this.control.getPlayButton().addClickListener(this::onPlayButtonClick);
        this.control.getMuteButton().addClickListener(this::onMuteButtonClick);
        this.control.getSongSlider().addClickListener(this::onSongSliderClick);
        this.control.getVolumeSlider().addClickListener(this::onVolumeSliderClick);
        this.control.getNextButton().addClickListener(this::onNextButtonClick);
        this.control.getPreviousButton().addClickListener(this::onPreviousButtonClick);
        this.control.getRepeatButton().addClickListener(this::onRepeatButtonClick);
        this.control.getRandomButton().addClickListener(this::onRandomButtonClick);

        this.playerEngine.addTimeListener(this::onTimeUpdate);
        this.playerEngine.addLoadListener(this::onAudioLoad);
        this.playerEngine.addSamplesListener(this::onSamplesUpdate);
        this.playerEngine.addSongFinishListener(this::onSongFinish);

        this.renderPlaylistNames();
        this.navigation.getLocalPlaylists().addClickListener(this::onPlaylistClick);

        this.playlist.getPlaylist().addClickListener(this::onSongClick);
        this.playlist.getPlaylist().addDropFilesListener(this::onDropFiles);
        this.playlist.getPlaylist().addChangeOrderListener(this::onChangeOrder);
    }


    /******************************************************************************************************************
        Title events
     ******************************************************************************************************************/

    /**
     * Event on close button click
     */
    private void onCloseButtonClick() {
        if (playerEngine.isActive()) {
            playerEngine.stop();
        }

        System.exit(0);
    }

    /**
     * Event on minimize button click
     */
    private void onMinimizeButtonClick() {
        this.mainGui.getStage().setIconified(true);
    }


    /******************************************************************************************************************
        Control events
     ******************************************************************************************************************/

    /**
     * Event on play/pause button click
     */
    private void onPlayButtonClick() {
        if (playerEngine.isActive()) {
            playerEngine.setPaused(!control.getPlayButton().isSelected());
            return;
        }

        playerEngine.play();
    }

    /**
     * Event on mute button click
     */
    private void onMuteButtonClick() {
        if (volumeLevel == 0.0) {
            volumeLevel = 1.0;
        }

        control.getVolumeSlider().setProgress(volumeLevel);

        playerEngine.setVolume(volumeLevel);
        playerEngine.setMuted(control.getMuteButton().isSelected());

        settingsManager.setVolumeLevel(volumeLevel);
        settingsManager.setMuted(control.getMuteButton().isSelected());
    }

    /**
     * Event on song slider click
     */
    private void onSongSliderClick(double percentage) {
        playerEngine.setPosition(percentage);
    }

    /**
     * Event on volume slider click
     */
    private void onVolumeSliderClick(double percentage) {
        volumeLevel = percentage;
        control.getMuteButton().setSelected(volumeLevel == 0.0);

        playerEngine.setVolume(percentage);
        playerEngine.setMuted(control.getMuteButton().isSelected());

        settingsManager.setVolumeLevel(volumeLevel);
    }

    /**
     * Event on next button click
     */
    private void onNextButtonClick() {
        if (currentPlaylist == null || currentSong == null) {
            return;
        }

        loadNextSong(true);
    }

    /**
     * Event on previous button click
     */
    private void onPreviousButtonClick() {
        if (currentPlaylist == null || currentSong == null) {
            return;
        }

        loadNextSong(false);
    }

    /**
     * Event on repeat button click
     */
    private void onRepeatButtonClick() {
        if (control.getRandomButton().isSelected()) {
            control.getRandomButton().setSelected(false);
            randomOrder.clear();
        }

        settingsManager.setRepeatEnabled(control.getRepeatButton().isSelected());
        settingsManager.setRandomEnabled(control.getRandomButton().isSelected());
    }

    /**
     * Event on random button click
     */
    private void onRandomButtonClick() {
        if (control.getRepeatButton().isSelected()) {
            control.getRepeatButton().setSelected(false);
        }

        if (!control.getRandomButton().isSelected()) {
            randomOrder.clear();
        }

        settingsManager.setRepeatEnabled(control.getRepeatButton().isSelected());
        settingsManager.setRandomEnabled(control.getRandomButton().isSelected());
    }


    /******************************************************************************************************************
        PlayerEngine events
     ******************************************************************************************************************/

    /**
     * Event on update player time
     */
    private void onTimeUpdate(long currentSeconds, double progress) {
        control.getSongSlider().setCurrentValue(currentSeconds);
        control.getSongSlider().setProgress(progress);
    }

    /**
     * Event on audio load
     */
    private void onAudioLoad() {
        control.setSongName(playerEngine.getTitle());
        control.setArtistName(playerEngine.getAuthor());
        control.getSongSlider().setTotalValue(playerEngine.getSecondsTotal());
    }

    /**
     * Event on samples update
     */
    private void onSamplesUpdate(float[] samples, float[] leftSamples, float[] rightSamples) {
        equalizer.setLevels(samples);
        leftVU.setLevel(leftSamples);
        rightVU.setLevel(rightSamples);
    }

    /**
     * Event on song finish
     */
    private void onSongFinish() {
        loadNextSong(true);
    }


    /******************************************************************************************************************
        Playlist events and methods
     ******************************************************************************************************************/

    /**
     * Load playlist names
     */
    private void renderPlaylistNames() {
        navigation.setPlaylists(playlistManager.getPlaylists());
    }

    /**
     * Load and play song
     */
    private void loadSong() throws FileNotFoundException {
        //set new color
        colorManager.setColor(currentPlaylist.getSongs().indexOf(currentSong) + 1, null);

        //select song
        playlist.selectElement(currentSong, false);

        //load song
        playerEngine.loadAudio(currentSong);

        //play song
        playerEngine.play();
    }

    /**
     * Load next/previous song
     */
    private void loadNextSong(boolean isNext) {
        if (wait != null && wait.isAlive()) {
            wait.stop();
        }

        int newIndex;

        if (control.getRepeatButton().isSelected()) {           //if repeat enabled
            //get current song
            newIndex = currentPlaylist.getSongs().indexOf(currentSong);
        } else if (control.getRandomButton().isSelected()) {    //if random enabled
            //get song index from random history
            try {
                int index = randomOrder.indexOf(currentSong.getHash()) + (isNext ? 1 : -1);
                if (index <= 0) {
                    index = 0;
                }
                String hash = randomOrder.get(index);
                newIndex = currentPlaylist.getSongs().indexOf(currentPlaylist.getSongByHash(hash));
            } catch (Exception e) {
                if (randomOrder.size() == currentPlaylist.getSongs().size()) {
                    randomOrder.clear();
                }

                //get random song
                String hash;
                do {
                    newIndex = random.nextInt(currentPlaylist.getSongs().size());
                    hash = currentPlaylist.getSongs().get(newIndex).getHash();
                } while (randomOrder.contains(hash));
                randomOrder.add(hash);
            }
        } else {                                                //if next song
            //get next song index
            newIndex = currentPlaylist.getSongs().indexOf(currentSong) + (isNext ? 1 : -1);
            if (newIndex < 0) {
                newIndex = currentPlaylist.getSongs().size() - 1;
            } else if (newIndex >= currentPlaylist.getSongs().size() - 1) {
                newIndex = 0;
            }
        }

        //set current song
        currentSong = currentPlaylist.getSongs().get(newIndex);

        //load next song
        try {
            loadSong();
        } catch (FileNotFoundException e) {
            System.err.println("Error loading audio \"" + currentSong.getPath() + "\"!");
            waitAndLoadNextSong(isNext);
        }
    }

    /**
     * Wait and load next/previous song
     */
    private void waitAndLoadNextSong(boolean isNext) {
        if (wait != null && wait.isAlive()) {
            wait.stop();
        }

        wait = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> loadNextSong(isNext));
        });
        wait.start();
    }

    /**
     * Load playlist
     */
    private void renderPlaylist() {
        //clear all
        playlist.clear();

        //fill playlist with songs
        playlist.addAdd(currentPlaylist.getSongs(), currentSong);

        //update playlist info
        visualization.getPlaylistInfo().setPlaylistInfo(currentPlaylist.getDisplayName(), currentPlaylist.getSongs().size(), currentPlaylist.getTime(), currentPlaylist.getSize());
    }

    /**
     * Stop playlist loader thread
     */
    private void stopPlaylistLoader() {
        if (playlistLoader != null && playlistLoader.isAlive()) {
            playlistLoader.stop();
        }
    }

    /**
     * Event on playlist click
     */
    private void onPlaylistClick(NavigationItem data) {
        //do nothing if clicked current playlist
        if (currentPlaylist != null && currentPlaylist.getUUID().equals(data.getUUID())) {
            return;
        }

        //load playlist in thread
        stopPlaylistLoader();
        playlistLoader = new Thread(() -> {
            try {
                //show loader
                Platform.runLater(() -> {
                    playlist.showLoader();
                    playlist.setLoaderText("Загрузка плейлиста");
                });

                //load playlist
                currentPlaylist = playlistManager.loadPlaylist(data.getUUID());

                //render playlist
                Platform.runLater(this::renderPlaylist);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playlistLoader.start();
    }

    /**
     * Event on song click
     */
    private void onSongClick(PlaylistItem data, int index) {
        //do nothing if clicked current song
        if (currentSong != null && currentSong.getHash().equals(data.getHash())) {
            return;
        }

        //fix frequent clicks
        if (System.currentTimeMillis() - lastSongClick <= 500) {
            return;
        }
        lastSongClick = System.currentTimeMillis();

        //set current song
        currentSong = data;

        //change random order
        randomOrder.remove(currentSong.getHash());
        if (!control.getRandomButton().isSelected()) {
            randomOrder.clear();
        }
        randomOrder.add(currentSong.getHash());

        //load and play song
        try {
            loadSong();
        } catch (FileNotFoundException e) {
            System.err.println("Error loading audio \"" + currentSong.getPath() + "\"!");
            waitAndLoadNextSong(true);
        }
    }

    /**
     * Event on drop files in playlist
     */
    private void onDropFiles(final List<File> files, int index) {
        //load songs in thread
        stopPlaylistLoader();
        playlistLoader = new Thread(() -> {
            final List<PlaylistItem> songs = new ArrayList<>();

            //show loader
            Platform.runLater(() -> {
                playlist.showLoader();
                playlist.setLoaderText("Обработка файлов");
            });

            //load audio data for each file
            final List<File> allFiles = FileUtils.getAllFiles(files);

            //update loader text
            Platform.runLater(() -> playlist.setLoaderText("Обработка файлов (" + songs.size() + "/" + allFiles.size() + ")"));

            //process each file
            for (File file : allFiles) {
                try {
                    //load audio data
                    PlaylistItem song = SongUtils.getAudioData(file);

                    //check if song already in playlist
                    if (currentPlaylist.getSongByHash(song.getHash()) != null) {
                        continue;
                    }

                    //add song
                    songs.add(song);

                    //update loader text
                    Platform.runLater(() -> playlist.setLoaderText("Обработка файлов (" + songs.size() + "/" + allFiles.size() + ")"));
                } catch (Exception e) {
                    System.err.println("File \"" + file.getName() + "\" isn't an audio file!");
                }
            }

            //add songs
            currentPlaylist.addSongs(songs, index);

            //render
            Platform.runLater(this::renderPlaylist);

            //save
            try {
                playlistManager.savePlaylist(currentPlaylist);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playlistLoader.start();
    }

    /**
     * Event on change order in playlist
     */
    private void onChangeOrder(PlaylistItem data, int index) {
        //set new position
        currentPlaylist.setSongOrder(data, index);

        //save
        try {
            playlistManager.savePlaylist(currentPlaylist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
