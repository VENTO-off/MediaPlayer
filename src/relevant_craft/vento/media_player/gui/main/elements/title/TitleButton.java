package relevant_craft.vento.media_player.gui.main.elements.title;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import relevant_craft.vento.media_player.manager.color.Colors;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class TitleButton extends StackPane {
    private static final double size = 22;
    private static final Duration fadeIn = Duration.millis(200);
    private static final Duration fadeOut = Duration.millis(500);

    private Rectangle rectangle;
    private Rectangle color;
    private ImageView image;
    private Timeline animation;
    private ClickListener clickListener;

    /**
     * Init button for title bar
     */
    public TitleButton(double positionX, double positionY, Pictures icon, Color activeColor) {
        super();
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);

        //init button's border
        rectangle = new Rectangle(size, size);
        rectangle.setStroke(Colors.MIDDLE_COLOR_TITLE_BUTTON.getColor());
        rectangle.setStrokeWidth(1);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);
        rectangle.setFill(new RadialGradient(
                0,
                0,
                getSize() / 2,
                getSize() / 2,
                getSize() / 2,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Colors.MIDDLE_COLOR_TITLE_BUTTON.getColor()),
                new Stop(1, Colors.BORDER_COLOR_TITLE_BUTTON.getColor())
        ));
        rectangle.setDisable(true);
        this.setOnMouseEntered(this::onHover);
        this.setOnMouseExited(this::onExit);
        this.setOnMouseClicked(this::onClick);
        this.getChildren().add(rectangle);

        //glow on hover
        color = new Rectangle(size, size);
        color.setArcHeight(rectangle.getArcHeight());
        color.setArcWidth(rectangle.getArcWidth());
        color.setFill(new RadialGradient(
                0,
                0,
                getSize() / 2,
                getSize() / 2,
                getSize() / 2,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, activeColor),
                new Stop(1, Colors.BORDER_COLOR_TITLE_BUTTON.getColor())
        ));
        color.setDisable(true);
        color.setOpacity(0.0);
        this.getChildren().add(color);

        //render image
        image = new ImageView(PictureManager.loadImage(icon.getIconName()));
        image.setDisable(true);
        this.getChildren().add(image);
    }

    /**
     * Click listener
     */
    public interface ClickListener {
        void onClick();
    }

    /**
     * Add click listener
     */
    public void addClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Play animation on mouse hover
     */
    private void onHover(MouseEvent e) {
        this.setCursor(Cursor.HAND);

        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
        }

        animation = new Timeline(new KeyFrame(fadeIn, new KeyValue(color.opacityProperty(), 1.0)));
        animation.play();
    }

    /**
     * Play animation on mouse exit
     */
    private void onExit(MouseEvent e) {
        this.setCursor(Cursor.DEFAULT);

        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
        }

        animation = new Timeline(new KeyFrame(fadeOut, new KeyValue(color.opacityProperty(), 0.0)));
        animation.play();
    }

    /**
     * Notify listener on mouse click
     */
    private void onClick(MouseEvent e) {
        if (clickListener != null) {
            clickListener.onClick();
        }
    }

    /**
     * Get button size
     */
    public static double getSize() {
        return size;
    }
}
