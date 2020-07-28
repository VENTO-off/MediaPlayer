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
import relevant_craft.vento.media_player.manager.vumeter.VUMeterManager;
import relevant_craft.vento.media_player.utils.FileUtils;
import relevant_craft.vento.media_player.utils.SongUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    private double volumeLevel;
    private PlaylistData currentPlaylist;
    private Thread playlistLoader;
    private PlaylistItem currentSong;
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
        this.lastSongClick = System.currentTimeMillis();

        this.title.getCloseButton().addClickListener(this::onCloseButtonClick);
        this.title.getMinimizeButton().addClickListener(this::onMinimizeButtonClick);

        this.control.getPlayButton().addClickListener(this::onPlayButtonClick);
        this.control.getMuteButton().addClickListener(this::onMuteButtonClick);
        this.control.getSongSlider().addClickListener(this::onSongSliderClick);
        this.control.getVolumeSlider().addClickListener(this::onVolumeSliderClick);
        this.control.getNextButton().addClickListener(this::onNextButtonClick);
        this.control.getPreviousButton().addClickListener(this::onPreviousButtonClick);

        this.playerEngine.addTimeListener(this::onTimeUpdate);
        this.playerEngine.addLoadListener(this::onAudioLoad);
        this.playerEngine.addSamplesListener(this::onSamplesUpdate);

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

        //load song
        playerEngine.loadAudio(currentSong);

        //play song
        playerEngine.play();

        //select song
        playlist.selectElement(currentSong, false);
    }

    /**
     * Load next/previous song
     */
    private void loadNextSong(boolean isNext) {
        //get next song index
        int newIndex = currentPlaylist.getSongs().indexOf(currentSong) + (isNext ? 1 : -1);
        if (newIndex < 0) {
            newIndex = currentPlaylist.getSongs().size() - 1;
        } else if (newIndex >= currentPlaylist.getSongs().size() - 1) {
            newIndex = 0;
        }

        //set current song
        currentSong = currentPlaylist.getSongs().get(newIndex);

        //load next song
        try {
            loadSong();
        } catch (FileNotFoundException e) {
            System.err.println("Error loading audio \"" + currentSong.getPath() + "\"!");
            loadNextSong(isNext);
        }
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
        if (System.currentTimeMillis() - lastSongClick <= 250) {
            return;
        }
        lastSongClick = System.currentTimeMillis();

        //set current song
        currentSong = data;

        //load and play song
        try {
            loadSong();
        } catch (FileNotFoundException e) {
            System.err.println("Error loading audio \"" + currentSong.getPath() + "\"!");
            loadNextSong(true);
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
                playlist.setLoaderText("Обработка треков");
            });

            //load audio data for each file
            final List<File> allFiles = FileUtils.getAllFiles(files);

            //update loader text
            Platform.runLater(() -> playlist.setLoaderText("Обработка треков (" + songs.size() + "/" + allFiles.size() + ")"));

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
                    Platform.runLater(() -> playlist.setLoaderText("Обработка треков (" + songs.size() + "/" + allFiles.size() + ")"));
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
