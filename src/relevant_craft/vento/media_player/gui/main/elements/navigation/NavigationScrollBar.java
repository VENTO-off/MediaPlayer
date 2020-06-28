package relevant_craft.vento.media_player.gui.main.elements.navigation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class NavigationScrollBar extends ScrollBar {
    private final double defaultWidth = 3;
    private final double defaultOpacity = 0.2;
    private final double hoverWidth = 7;
    private final double hoverOpacity = 0.4;
    private final Duration animationTime = Duration.millis(200);
    private final Duration animationDelay = Duration.millis(500);
    private final double layoutX;
    private final Navigation navigation;
    private final Stage stage;

    private Timeline positionAnimation;
    private Timeline widthAnimation;
    private Timeline opacityAnimation;
    private boolean isPressed = false;
    private boolean isHovered = false;

    /**
     * Init scroll bar
     */
    public NavigationScrollBar(Stage stage, Navigation navigation) {
        super();

        this.stage = stage;
        this.navigation = navigation;

        //orientation
        this.setOrientation(Orientation.VERTICAL);

        //position
        this.layoutX = this.navigation.getPrefWidth() - defaultWidth - 2;
        this.setLayoutX(layoutX);
        this.setLayoutY(this.navigation.getLayoutY());

        //transparent background
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        //width by default
        this.setPrefWidth(defaultWidth);
        this.setMaxWidth(hoverWidth);
        this.setMinWidth(defaultWidth);

        //height
        this.setPrefHeight(navigation.getPrefHeight());

        //opacity by default
        this.setOpacity(defaultOpacity);

        //on scroll event
        this.valueProperty().addListener((observable, oldValue, newValue) -> navigation.setVvalue(newValue.doubleValue()));
        navigation.vvalueProperty().addListener((observable, oldValue, newValue) -> this.setValue(newValue.doubleValue()));

        this.stage.addEventFilter(WindowEvent.WINDOW_SHOWN, event -> {
            //track
            Region track = (Region) this.lookup(".track");
            track.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

            //track-background
            Region trackBackground = (Region) this.lookup(".track-background");
            trackBackground.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

            //thumb
            Region thumb = (Region) this.lookup(".thumb");
            thumb.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), new Insets(1.0, 0.0, 1.0, 0.0))));
            thumb.setOpacity(0.2);
            thumb.setOnMouseEntered(this::onHover);
            thumb.setOnMouseExited(this::onExit);
            thumb.setOnMousePressed(this::onPressed);
            thumb.setOnMouseReleased(this::onRelease);

            //increment button
            Region incrementButton = (Region) this.lookup(".increment-button");
            incrementButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

            //decrement button
            Region decrementButton = (Region) this.lookup(".decrement-button");
            decrementButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

            //increment arrow
            Region incrementArrow = (Region) this.lookup(".increment-arrow");
            incrementArrow.setShape(null);
            incrementArrow.setPadding(new Insets(0.5, 0, 0.5, 0));

            //decrement arrow
            Region decrementArrow = (Region) this.lookup(".decrement-arrow");
            decrementArrow.setShape(null);
            decrementArrow.setPadding(new Insets(0.5, 0, 0.5, 0));
        });
    }

    /**
     * Set thumb height
     */
    public void setThumbHeight(double contentHeight) {
        this.setVisible(!(contentHeight < this.getPrefHeight()));
        this.setVisibleAmount(this.getMax() * this.getPrefHeight() / contentHeight);
    }

    /**
     * Play animation to show scrollbar
     */
    private void showScrollbar(Region thumb) {
        thumb.setCursor(Cursor.HAND);

        if (positionAnimation != null && positionAnimation.getStatus() == Animation.Status.RUNNING) {
            positionAnimation.stop();
        }
        positionAnimation = new Timeline(new KeyFrame(animationTime, new KeyValue(this.layoutXProperty(), layoutX - (hoverWidth - defaultWidth))));
        positionAnimation.play();

        if (widthAnimation != null && widthAnimation.getStatus() == Animation.Status.RUNNING) {
            widthAnimation.stop();
        }
        widthAnimation = new Timeline(new KeyFrame(animationTime, new KeyValue(this.prefWidthProperty(), hoverWidth)));
        widthAnimation.play();

        if (opacityAnimation != null && opacityAnimation.getStatus() == Animation.Status.RUNNING) {
            opacityAnimation.stop();
        }
        opacityAnimation = new Timeline(new KeyFrame(animationTime, new KeyValue(this.opacityProperty(), hoverOpacity)));
        opacityAnimation.play();
    }

    /**
     * Play animation to hide scrollbar
     */
    private void hideScrollbar(Region thumb) {
        thumb.setCursor(Cursor.DEFAULT);

        positionAnimation = new Timeline(new KeyFrame(animationTime, new KeyValue(this.layoutXProperty(), layoutX)));
        positionAnimation.setDelay(animationDelay);
        positionAnimation.play();

        widthAnimation = new Timeline(new KeyFrame(animationTime, new KeyValue(this.prefWidthProperty(), defaultWidth)));
        widthAnimation.setDelay(animationDelay);
        widthAnimation.play();

        opacityAnimation = new Timeline(new KeyFrame(animationTime, new KeyValue(this.opacityProperty(), defaultOpacity)));
        opacityAnimation.setDelay(animationDelay);
        opacityAnimation.play();
    }

    /**
     * Event on mouse hover
     */
    private void onHover(MouseEvent e) {
        isHovered = true;

        Region thumb = (Region) e.getSource();
        showScrollbar(thumb);
    }

    /**
     * Event on mouse exit
     */
    private void onExit(MouseEvent e) {
        isHovered = false;

        if (isPressed) {
            return;
        }

        Region thumb = (Region) e.getSource();
        hideScrollbar(thumb);
    }

    /**
     * Event on mouse pressedцц
     */
    private void onPressed(MouseEvent e) {
        isPressed = true;
    }

    /**
     * Event on mouse release
     */
    private void onRelease(MouseEvent e) {
        isPressed = false;

        if (!isHovered) {
            Region thumb = (Region) e.getSource();
            hideScrollbar(thumb);
        }
    }
}
