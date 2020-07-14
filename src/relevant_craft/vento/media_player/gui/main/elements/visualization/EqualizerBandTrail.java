package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class EqualizerBandTrail extends Pane {
    private boolean isActivated;

    /**
     * Init equalizer band trail
     */
    public EqualizerBandTrail(double positionY) {
        super();

        this.isActivated = false;

        this.setPrefSize(4, 2);
        this.setLayoutY(positionY);
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Activate equalizer band trail
     */
    public void setActive(boolean status) {
        if (isActivated == status) {
            return;
        }

        isActivated = status;

        if (isActivated) {
            //TODO pass average color
            final String averageColor = "#6b80c1";
            this.setBackground(new Background(new BackgroundFill(Color.web(averageColor), CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    /**
     * Set Y position
     */
    public void setPositionY(double positionY) {
        this.setLayoutY(positionY);
    }
}
