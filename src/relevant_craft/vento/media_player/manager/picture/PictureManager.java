package relevant_craft.vento.media_player.manager.picture;

import java.io.File;

public class PictureManager {

    public static javafx.scene.image.Image loadImage(String name) {
        return new javafx.scene.image.Image(PictureManager.class.getResourceAsStream("pictures" + File.separator + name));
    }
}
