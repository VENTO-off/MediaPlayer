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

    public PlayerManager() {
        this.mainGui = MainGui.getInstance();
        this.title = mainGui.getTitle();
        this.control = mainGui.getControl();
        this.navigation = mainGui.getNavigation();
        this.visualization = mainGui.getVisualization();
        this.playlist = mainGui.getPlaylist();

        control.getPlayButton().addClickListener(() -> {
            if (playerEngine != null) {
                playerEngine.pause();
                return;
            }
            playerEngine = new PlayerEngine("C:/Users/VENTO/Downloads/ChipaChip - Веном.mp3", control);
            control.getSongName().setText(playerEngine.getTitle());
            control.getArtistName().setText(playerEngine.getAuthor());
            control.getSongSlider().setTotalValue(playerEngine.getSecondsTotal());
            try {
                playerEngine.play();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        control.getMuteButton().addClickListener(() -> {
            if (playerEngine != null) {
                playerEngine.mute();
            }
        });

        control.getSongSlider().addClickListener(progress -> {
            if (playerEngine != null) {
                playerEngine.setPosition(progress);
            }
        });
    }
}
