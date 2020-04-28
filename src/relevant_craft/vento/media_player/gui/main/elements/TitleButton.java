package relevant_craft.vento.media_player.gui.main.elements;


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
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class TitleButton extends StackPane {

    private static final double size = 22;
    private static final Color middleColor = Color.web("#303030");
    private static final Color borderColor = Color.web("#232323");

    private Rectangle rectangle;
    private Rectangle color;
    private ImageView image;
    private Timeline animation;
    private ClickListener clickListener;

    public TitleButton(double positionX, double positionY, Pictures icon, Color activeColor) {
        super();
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);

        rectangle = new Rectangle(size, size);
        rectangle.setStroke(middleColor);
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
                new Stop(0, middleColor),
                new Stop(1, borderColor)
        ));
        rectangle.setDisable(true);
        this.setOnMouseEntered(this::onHover);
        this.setOnMouseExited(this::onRelease);
        this.setOnMouseClicked(this::onClick);
        this.getChildren().add(rectangle);

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
                new Stop(1, borderColor)
        ));
        color.setDisable(true);
        color.setOpacity(0.0);
        this.getChildren().add(color);

        image = new ImageView(PictureManager.loadImage(icon.getIconName()));
        image.setDisable(true);
        this.getChildren().add(image);
    }

    public interface ClickListener {
        void onClick();
    }

    public void addClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void onHover(MouseEvent e) {
        this.setCursor(Cursor.HAND);

        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
        }

        animation = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(color.opacityProperty(), 1.0)));
        animation.play();
    }

    private void onRelease(MouseEvent e) {
        this.setCursor(Cursor.DEFAULT);

        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
        }

        animation = new Timeline(new KeyFrame(Duration.millis(1000), new KeyValue(color.opacityProperty(), 0.0)));
        animation.play();
    }

    private void onClick(MouseEvent mouseEvent) {
        if (clickListener != null) {
            clickListener.onClick();
        }
    }

    public static double getSize() {
        return size;
    }
}
