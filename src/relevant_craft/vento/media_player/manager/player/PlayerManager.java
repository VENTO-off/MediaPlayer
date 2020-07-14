package relevant_craft.vento.media_player.manager.player;

import relevant_craft.vento.media_player.gui.main.MainGui;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.navigation.Navigation;
import relevant_craft.vento.media_player.gui.main.elements.playlist.Playlist;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.gui.main.elements.visualization.Visualization;
import relevant_craft.vento.media_player.manager.equalizer.EqualizerManager;
import relevant_craft.vento.media_player.manager.vumeter.VUMeterManager;

import java.io.FileNotFoundException;

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

    private double volumeLevel;

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
        this.leftVU = new VUMeterManager(visualization.getVuLeft());
        this.rightVU = new VUMeterManager(visualization.getVuRight());
        this.equalizer = new EqualizerManager(visualization.getEqualizer());

        this.control.getPlayButton().addClickListener(this::onPlayButtonClick);
        this.control.getMuteButton().addClickListener(this::onMuteButtonClick);
        this.control.getSongSlider().addClickListener(this::onSongSliderClick);
        this.control.getVolumeSlider().addClickListener(this::onVolumeSliderClick);

        this.playerEngine.addTimeListener(this::onTimeUpdate);
        this.playerEngine.addLoadListener(this::onAudioLoad);
        this.playerEngine.addSamplesListener(this::onSamplesUpdate);

        //TODO remove
        try {
            playerEngine.loadAudio("C:/Users/VENTO/Downloads/Jo Cohen & Sex Whales - We Are.mp3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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
        control.getSongName().setText(playerEngine.getTitle());
        control.getArtistName().setText(playerEngine.getAuthor());
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
}
