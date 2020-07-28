package relevant_craft.vento.media_player.gui.main.elements.playlist;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class LoaderAnimation extends Transition {
    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    private int lastIndex;

    /**
     * Init Loader animation
     */
    public LoaderAnimation(double width, double height) {
        this.count = 10;
        this.columns = 10;
        this.offsetX = 0;
        this.offsetY = 0;
        this.width = 45;
        this.height = 45;
        this.imageView = new ImageView(PictureManager.loadImage(Pictures.LOADER.getIconName()));
        this.imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        this.imageView.setLayoutX(width / 2 - this.width / 2D);
        this.imageView.setLayoutY(height / 2 - this.height);

        this.setCycleDuration(Duration.millis(500));
        this.setInterpolator(Interpolator.LINEAR);
        this.setCycleCount(Animation.INDEFINITE);
        this.play();
    }

    /**
     * Interpolate animation
     */
    protected void interpolate(double i) {
        final int index = Math.min((int) Math.floor(i * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }

    /**
     * Return ImageView of loader
     */
    public ImageView getImageView() {
        return imageView;
    }
}
