package relevant_craft.vento.media_player.gui.main.elements.control;

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

    protected final double opacity = 0.3;
    protected final Duration animationTime = Duration.millis(100);
    private final long clickTimeout = 250;

    protected final Circle circle;
    protected final ImageView image;
    private final BorderPane imageArea;

    protected Timeline animation;
    private ClickListener clickListener;
    protected long lastClick;

    /**
     * Init button for control bar
     */
    public ControlButton(double positionX, double positionY, Pictures icon) {
        super();
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
        this.lastClick = 0;

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
    protected void onClick(MouseEvent e) {
        if (!canClick()) {
            return;
        }
        lastClick = System.currentTimeMillis();

        if (clickListener != null) {
            clickListener.onClick();
        }
    }

    /**
     * Play animation on mouse press
     */
    protected void onPressed(MouseEvent e) {
        if (!canClick()) {
            return;
        }

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
     * Check button timeout after last click
     */
    protected boolean canClick() {
        return System.currentTimeMillis() - lastClick > clickTimeout;
    }

    /**
     * Get button size
     */
    public static double getSize() {
        return radius * 2;
    }
}
