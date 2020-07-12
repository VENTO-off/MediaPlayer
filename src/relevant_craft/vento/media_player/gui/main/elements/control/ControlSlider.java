package relevant_craft.vento.media_player.gui.main.elements.control;

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
import relevant_craft.vento.media_player.utils.TimeUtils;

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

    private boolean isPressed;
    private Timeline buttonAnimation;
    private Timeline progressAnimation;
    private ClickListener clickListener;

    /**
     * Init slider for control bar
     */
    public ControlSlider(double positionX, double positionY, double width, Control control, boolean displayValues) {
        super();
        this.setLayoutX((int) positionX);
        this.setLayoutY((int) positionY);
        this.setPrefWidth(width);
        this.setPrefHeight(radius);
        this.setOnMouseClicked(this::onClick);
        this.control = control;
        this.isPressed = false;

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
        progressButton.setOnMouseReleased(this::onMouseReleased);
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
            control.getChildren().add(currentValue);

            //init total value
            totalValue = new Text();
            totalValue.setX(positionX + width + 5);
            totalValue.setY(positionY - (radius / 2) + fixY);
            totalValue.setFill(Color.WHITE);
            totalValue.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 12));
            totalValue.setWrappingWidth(valueWidth);
            control.getChildren().add(totalValue);
        } else {
            currentValue = null;
            totalValue = null;
        }
    }

    /**
     * Click listener
     */
    public interface ClickListener {
        void onClick(double percentage);
    }

    /**
     * Add click listener
     */
    public void addClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Get slider percentage (after mouse event)
     */
    private double getProgress(MouseEvent e) {
        double min = radius;
        double max = this.getPrefWidth() - radius;

        double percentage = ((e == null ? progressButton.getCenterX() : e.getX()) - min) / (max - min);
        if (percentage < 0.0) {
            percentage = 0.0;
        } else if (percentage > 1.0) {
            percentage = 1.0;
        }

        return percentage;
    }

    /**
     * Get slider percentage
     */
    public double getProgress() {
        return getProgress(null);
    }

    /**
     * Set slider percentage
     */
    public void setProgress(double percentage) {
        if (isPressed) {
            return;
        }

        if (percentage < 0) {
            percentage = 0;
        } else if (percentage > 1) {
            percentage = 1;
        }

        double x = radius + ((int) (percentage * 100)) * (this.getPrefWidth() - radius * 2) / 100;

        progressButton.setCenterX(x);
        progressCurrent.setWidth(x);
    }

    /**
     * Set current value
     */
    public void setCurrentValue(long seconds) {
        currentValue.setText(TimeUtils.formatPlaylistTime(seconds));
    }

    /**
     * Set total value
     */
    public void setTotalValue(long seconds) {
        totalValue.setText(TimeUtils.formatPlaylistTime(seconds));
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

        //notify listener on click
        if (clickListener != null) {
            clickListener.onClick(getProgress(e));
        }
    }

    /**
     * Event on drag button
     */
    private void onDrag(MouseEvent e) {
        if (e.getX() < radius || e.getX() > this.getPrefWidth() - radius) {
            return;
        }

        isPressed = true;

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
     * Notify listener on mouse release after drag
     */
    private void onMouseReleased(MouseEvent e) {
        double progress = this.getProgress(e);

        if (clickListener != null) {
            clickListener.onClick(progress);
        }

        isPressed = false;

        setProgress(progress);
    }

    /**
     * Get slider height
     */
    public static double getRadius() {
        return radius;
    }
}
