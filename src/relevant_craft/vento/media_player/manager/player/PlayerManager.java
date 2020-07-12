package relevant_craft.vento.media_player.manager.player;

import relevant_craft.vento.media_player.gui.main.MainGui;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.navigation.Navigation;
import relevant_craft.vento.media_player.gui.main.elements.playlist.Playlist;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.gui.main.elements.visualization.Visualization;

import java.io.FileNotFoundException;

public class PlayerManager {
    private final MainGui mainGui;
    private final Title title;
    private final Control control;
    private final Navigation navigation;
    private final Visualization visualization;
    private final Playlist playlist;

    private PlayerEngine playerEngine;
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

        control.getPlayButton().addClickListener(this::onPlayButtonClick);
        control.getMuteButton().addClickListener(this::onMuteButtonClick);
        control.getSongSlider().addClickListener(this::onSongSliderClick);
        control.getVolumeSlider().addClickListener(this::onVolumeSliderClick);
    }

    /**
     * Event on play/pause button click
     */
    private void onPlayButtonClick() {
        if (playerEngine != null) {
            playerEngine.pause(!control.getPlayButton().isSelected());
            return;
        }

        playerEngine = new PlayerEngine("E:\\Музыка\\ChipaChip\\(2020) На этажах\\01. На этажах (feat. Sam Wick).mp3", control);
        control.getSongName().setText(playerEngine.getTitle());
        control.getArtistName().setText(playerEngine.getAuthor());
        control.getSongSlider().setTotalValue(playerEngine.getSecondsTotal());
        try {
            playerEngine.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event on mute button click
     */
    private void onMuteButtonClick() {
        if (volumeLevel == 0.0) {
            volumeLevel = 1.0;
        }

        control.getVolumeSlider().setProgress(volumeLevel);
        if (playerEngine != null) {
            playerEngine.setVolume(volumeLevel);
            playerEngine.setMuted(control.getMuteButton().isSelected());
        }
    }

    /**
     * Event on song slider click
     */
    private void onSongSliderClick(double percentage) {
        if (playerEngine != null) {
            playerEngine.setPosition(percentage);
        }
    }

    /**
     * Event on volume slider click
     */
    private void onVolumeSliderClick(double percentage) {
        volumeLevel = percentage;
        control.getMuteButton().setSelected(volumeLevel == 0.0);

        if (playerEngine != null) {
            playerEngine.setVolume(percentage);
            playerEngine.setMuted(control.getMuteButton().isSelected());
        }
    }
}
