package relevant_craft.vento.media_player.gui.main.elements.control;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class PressableControlButton extends ControlButton {
    private boolean isSelected;

    /**
     * Init button for control bar
     */
    public PressableControlButton(double positionX, double positionY, Pictures icon) {
        super(positionX, positionY, icon);
        this.setOnMouseReleased(null);
    }

    /**
     * Change icon on click
     */
    protected void onClick(MouseEvent e) {
        isSelected = !isSelected;
        super.onClick(e);
    }

    /**
     * Play animation on mouse press
     */
    protected void onPressed(MouseEvent e) {
        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
        }

        animation = new Timeline(new KeyFrame(animationTime, new KeyValue(circle.opacityProperty(), (circle.getOpacity() > 0.0 ? 0.0 : opacity))));
        animation.play();
    }

    /**
     * Get selected value
     */
    public boolean isSelected() {
        return isSelected;
    }
}
