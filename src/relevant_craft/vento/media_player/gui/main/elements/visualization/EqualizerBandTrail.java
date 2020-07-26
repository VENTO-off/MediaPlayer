package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import relevant_craft.vento.media_player.manager.color.ColorManager;

public class EqualizerBandTrail extends Pane {
    private final ColorManager colorManager;

    private boolean isActivated;
    private Color currentColor;

    /**
     * Init equalizer band trail
     */
    public EqualizerBandTrail(double positionY, ColorManager colorManager) {
        super();

        this.colorManager = colorManager;
        this.colorManager.addChangeColorListener(this::onChangeColor);

        this.isActivated = false;
        this.currentColor = colorManager.getFinalColor();

        this.setPrefSize(4, 2);
        this.setLayoutY(positionY);
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Activate equalizer band trail
     */
    public synchronized void setActive(boolean status) {
        isActivated = status;

        if (isActivated) {
            updateColor(currentColor);
        } else {
            this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Set color
     */
    private synchronized void updateColor(Color color) {
        this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Set Y position
     */
    public void setPositionY(double positionY) {
        this.setLayoutY(positionY);
    }

    /**
     * Event on change color
     */
    private void onChangeColor(Color color) {
        currentColor = color;
    }
}
