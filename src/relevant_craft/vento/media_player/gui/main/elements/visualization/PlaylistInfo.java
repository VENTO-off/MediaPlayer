package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;
import relevant_craft.vento.media_player.utils.FormatUtils;

public class PlaylistInfo extends Text {

    /**
     * Init playlist info
     */
    public PlaylistInfo(double positionX, double positionY) {
        super();

        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
        this.setFill(Color.WHITE);
        this.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 14));
        this.setOpacity(0.35);
    }

    /**
     * Set playlist info
     */
    public void setPlaylistInfo(String name, int songs, long time, long size) {
        this.setText(String.join(" / ", name, String.valueOf(songs), FormatUtils.formatTime(time), FormatUtils.formatSize(size)));
    }
}
