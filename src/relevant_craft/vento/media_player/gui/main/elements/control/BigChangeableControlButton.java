package relevant_craft.vento.media_player.gui.main.elements.control;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class BigChangeableControlButton extends ChangeableControlButton {
    private static final double radius = 20;

    private final Circle stroke;

    /**
     * Init button for control bar
     */
    public BigChangeableControlButton(double positionX, double positionY, Pictures icon1, Pictures icon2) {
        super(positionX, positionY, icon1, icon2);

        circle.setRadius(radius);

        stroke = new Circle(radius);
        stroke.setStroke(Color.WHITE);
        stroke.setStrokeWidth(2);
        stroke.setFill(Color.TRANSPARENT);
        stroke.setDisable(true);
        this.getChildren().add(0, stroke);
    }

    /**
     * Get button size
     */
    public static double getSize() {
        return radius * 2;
    }
}
