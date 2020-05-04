package relevant_craft.vento.media_player.gui.main.elements;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import relevant_craft.vento.media_player.manager.color.Colors;

public class ControlSlider extends Pane {
    private static final double height = 3;
    private static final double radius = 7.5;

    private final Rectangle progressTotal;
    private final Rectangle progressCurrent;
    private final Circle progressButton;
    private Timeline buttonAnimation;
    private Timeline progressAnimation;

    /**
     * Init slider for control bar
     */
    public ControlSlider(double positionX, double positionY, double width) {
        super();
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
        this.setPrefWidth(width);
        this.setPrefHeight(radius);
        this.setOnMouseClicked(this::onClick);

        //init total progress
        progressTotal = new Rectangle(width, height);
        progressTotal.setFill(Colors.SLIDER_TOTAL_COLOR.getColor());
        progressTotal.setArcWidth(3);
        progressTotal.setArcHeight(3);
        progressTotal.setLayoutY(this.getPrefHeight() / 2 - progressTotal.getHeight() / 2);
        this.getChildren().add(progressTotal);

        //init current progress
        progressCurrent = new Rectangle(0, height);
        progressCurrent.setFill(Color.WHITE);
        progressCurrent.setArcWidth(3);
        progressCurrent.setArcHeight(3);
        progressCurrent.setLayoutY(this.getPrefHeight() / 2 - progressCurrent.getHeight() / 2);
        this.getChildren().add(progressCurrent);

        //init button
        progressButton = new Circle(radius, Color.WHITE);
        progressButton.setCenterX(0);
        progressButton.setCenterY(this.getPrefHeight() / 2);
        progressButton.setEffect(new DropShadow(7, Color.web(Color.BLACK.toString(), 0.5)));
        progressButton.setCursor(Cursor.HAND);
        progressButton.setOnMouseDragged(this::onDrag);
        this.getChildren().add(progressButton);
    }

    /**
     * Event on click
     */
    private void onClick(MouseEvent e) {
        if (e.getX() < 0 || e.getX() > this.getPrefWidth()) {
            return;
        }

        if (buttonAnimation != null && buttonAnimation.getStatus() == Animation.Status.RUNNING) buttonAnimation.stop();
        buttonAnimation = new Timeline(new KeyFrame(Duration.millis(50), new KeyValue(progressButton.centerXProperty(), e.getX())));
        buttonAnimation.play();

        if (progressAnimation != null && progressAnimation.getStatus() == Animation.Status.RUNNING) progressAnimation.stop();
        progressAnimation = new Timeline(new KeyFrame(Duration.millis(50), new KeyValue(progressCurrent.widthProperty(), e.getX())));
        progressAnimation.play();
    }

    /**
     * Event on drag button
     */
    private void onDrag(MouseEvent e) {
        if (e.getX() < 0 || e.getX() > this.getPrefWidth()) {
            return;
        }

        progressButton.setCenterX(e.getX());
        progressCurrent.setWidth(e.getX());
    }

}
