package relevant_craft.vento.media_player.manager.font;

import javafx.scene.text.Font;

import java.io.File;

public class FontManager {

    /**
     * Load font by name
     */
    public static Font loadFont(String name, int size) {
        return Font.loadFont(FontManager.class.getResourceAsStream("fonts" + File.separator + name), size);
    }
}
