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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import relevant_craft.vento.media_player.manager.color.Colors;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;

public class ControlSlider extends Pane {
    private static final double height = 3;
    private static final double radius = 7.5;
    private static final double valueWidth = 50;

    private final Control control;
    private final Rectangle progressTotal;
    private final Rectangle progressCurrent;
    private final Circle progressButton;
    private final Text currentValue;
    private final Text totalValue;
    private Timeline buttonAnimation;
    private Timeline progressAnimation;

    /**
     * Init slider for control bar
     */
    public ControlSlider(double positionX, double positionY, double width, Control control, boolean displayValues) {
        super();
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
        this.setPrefWidth(width);
        this.setPrefHeight(radius);
        this.setOnMouseClicked(this::onClick);
        this.control = control;

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
        progressButton.setCenterX(radius);
        progressButton.setCenterY(this.getPrefHeight() / 2);
        progressButton.setEffect(new DropShadow(7, Color.web(Color.BLACK.toString(), 0.5)));
        progressButton.setCursor(Cursor.HAND);
        progressButton.setOnMouseDragged(this::onDrag);
        this.getChildren().add(progressButton);

        if (displayValues) {
            final double fixY = 11;

            //init current value
            currentValue = new Text();
            currentValue.setX(positionX - 5 - valueWidth);
            currentValue.setY(positionY - (radius / 2) + fixY);
            currentValue.setFill(Color.WHITE);
            currentValue.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 12));
            currentValue.setWrappingWidth(valueWidth);
            currentValue.setTextAlignment(TextAlignment.RIGHT);
            //TODO remove test value
            currentValue.setText("0:55");
            control.getChildren().add(currentValue);

            //init total value
            totalValue = new Text();
            totalValue.setX(positionX + width + 5);
            totalValue.setY(positionY - (radius / 2) + fixY);
            totalValue.setFill(Color.WHITE);
            totalValue.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 12));
            totalValue.setWrappingWidth(valueWidth);
            //TODO remove test value
            totalValue.setText("3:32");
            control.getChildren().add(totalValue);
        } else {
            currentValue = null;
            totalValue = null;
        }
    }

    /**
     * Event on click
     */
    private void onClick(MouseEvent e) {
        if (e.getX() < 0 || e.getX() > this.getPrefWidth()) {
            return;
        }

        double x = e.getX();
        if (e.getX() < radius) {
            x = radius;
        } else if (e.getX() > this.getPrefWidth() - radius) {
            x = this.getPrefWidth() - radius;
        }

        if (buttonAnimation != null && buttonAnimation.getStatus() == Animation.Status.RUNNING) buttonAnimation.stop();
        buttonAnimation = new Timeline(new KeyFrame(Duration.millis(50), new KeyValue(progressButton.centerXProperty(), x)));
        buttonAnimation.play();

        if (progressAnimation != null && progressAnimation.getStatus() == Animation.Status.RUNNING) progressAnimation.stop();
        progressAnimation = new Timeline(new KeyFrame(Duration.millis(50), new KeyValue(progressCurrent.widthProperty(), x)));
        progressAnimation.play();
    }

    /**
     * Event on drag button
     */
    private void onDrag(MouseEvent e) {
        if (e.getX() < radius || e.getX() > this.getPrefWidth() - radius) {
            return;
        }

        double x = e.getX();
        if (e.getX() < radius) {
            x = radius;
        } else if (e.getX() > this.getPrefWidth() - radius) {
            x = this.getPrefWidth() - radius;
        }

        progressButton.setCenterX(x);
        progressCurrent.setWidth(x);
    }

    /**
     * Get slider height
     */
    public static double getRadius() {
        return radius;
    }
}
