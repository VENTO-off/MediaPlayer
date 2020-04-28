package relevant_craft.vento.media_player.gui.main.elements;


import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import relevant_craft.vento.media_player.manager.picture.PictureManager;

public class TitleButton extends StackPane {

    private static final double size = 22;
    private static final Color middleColor = Color.valueOf("#303030");
    private static final Color borderColor = Color.valueOf("#232323");

    private Rectangle rectangle;
    private ImageView image;
    private ClickListener clickListener;

    public TitleButton(double positionX, double positionY, String icon) {
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
        rectangle.setOnMouseEntered(this::onHover);
        rectangle.setOnMouseExited(this::onRelease);
        rectangle.setOnMouseClicked(this::onClick);
        this.getChildren().add(rectangle);

        image = new ImageView(PictureManager.loadImage(icon));
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
    }

    private void onRelease(MouseEvent e) {
        this.setCursor(Cursor.DEFAULT);
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
