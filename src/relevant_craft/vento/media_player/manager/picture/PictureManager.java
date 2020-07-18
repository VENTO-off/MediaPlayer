package relevant_craft.vento.media_player.manager.picture;

import javafx.scene.image.Image;

import java.io.File;

public class PictureManager {

    /**
     * Load picture by name
     */
    public static Image loadImage(String name) {
        return new Image(PictureManager.class.getResourceAsStream("pictures" + File.separator + name));
    }
}
