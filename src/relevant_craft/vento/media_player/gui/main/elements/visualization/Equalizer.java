package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Equalizer extends Pane {
    private static final int BANDS = 32;
    private static final int SPACE = 1;

    private final List<EqualizerBand> bands;

    /**
     * Init equalizer
     */
    public Equalizer(double positionX, double positionY) {
        super();

        this.bands = new ArrayList<>();

        //init layout
        this.setPrefWidth(getEqualizerWidth());
        this.setPrefHeight(getEqualizerHeight());
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);

        //init bands
        for (int i = 0; i < BANDS; i++) {
            EqualizerBand band = new EqualizerBand(i * 5);
            this.bands.add(band);
            this.getChildren().add(band);
        }
    }

    /**
     * Set band percentage
     */
    public void setBandLevel(int band, double percentage) {
        if (band < 0 || band >= BANDS) {
            return;
        }

        if (percentage < 0.0) {
            percentage = 0.0;
        } else if (percentage > 1.0) {
            percentage = 1.0;
        }

        bands.get(band).setPercentage(percentage);
    }

    /**
     * Set flow trail percentage
     */
    public void setFlowTrailLevel(int band, double percentage) {
        if (band < 0 || band >= BANDS) {
            return;
        }

        if (percentage < 0.0) {
            percentage = 0.0;
        } else if (percentage > 1.0) {
            percentage = 1.0;
        }

        bands.get(band).setFlowPercentage(percentage);
    }

    /**
     * Get equalizer width
     */
    public static double getEqualizerWidth() {
        return (EqualizerBand.getBandWidth() + SPACE) * BANDS;
    }

    /**
     * Get equalizer height
     */
    public static double getEqualizerHeight() {
        return EqualizerBand.getBandHeight();
    }
}
