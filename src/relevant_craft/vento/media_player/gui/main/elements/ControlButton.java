package relevant_craft.vento.media_player.gui.main.elements;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class ControlButton extends StackPane {
    private static final double radius = 14;
    private static final double opacity = 0.3;
    private static final Duration animationTime = Duration.millis(100);

    private Circle circle;
    private ImageView image;
    private BorderPane imageArea;
    private Timeline animation;
    private ClickListener clickListener;

    /**
     * Init button for control bar
     */
    public ControlButton(double positionX, double positionY, Pictures icon) {
        super();
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);

        //init button's background
        circle = new Circle(radius);
        circle.setFill(Color.WHITE);
        circle.setDisable(true);
        circle.setOpacity(0.0);
        this.setOnMouseClicked(this::onClick);
        this.setOnMousePressed(this::onPressed);
        this.setOnMouseReleased(this::onReleased);
        this.setCursor(Cursor.HAND);
        this.getChildren().add(circle);

        //render image
        image = new ImageView(PictureManager.loadImage(icon.getIconName()));
        imageArea = new BorderPane();
        imageArea.setPrefSize(radius * 2, radius * 2);
        imageArea.setCenter(image);
        this.getChildren().add(imageArea);
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
     * Notify listener on click
     */
    private void onClick(MouseEvent mouseEvent) {
        if (clickListener != null) {
            clickListener.onClick();
        }
    }

    /**
     * Play animation on mouse press
     */
    private void onPressed(MouseEvent e) {
        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
        }

        animation = new Timeline(new KeyFrame(animationTime, new KeyValue(circle.opacityProperty(), opacity)));
        animation.play();
    }

    /**
     * Play animation on mouse release
     */
    private void onReleased(MouseEvent e) {
        animation = new Timeline(new KeyFrame(animationTime, new KeyValue(circle.opacityProperty(), 0.0)));
        animation.setDelay(Duration.millis(250));
        animation.play();
    }

    /**
     * Get button size
     */
    public static double getSize() {
        return radius * 2;
    }
}
