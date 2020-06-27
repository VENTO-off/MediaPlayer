package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import relevant_craft.vento.media_player.manager.color.Colors;

import java.util.ArrayList;
import java.util.List;

public class EqualizerBand extends Pane {
    private static final int WIDTH = 4;
    private static final int HEIGHT = 85;
    private static final int TRAILS = 28;

    private final List<EqualizerBandTrail> trails;

    /**
     * Init equalizer band
     */
    public EqualizerBand(double positionX) {
        super();

        this.trails = new ArrayList<>();

        //init layout
        this.setPrefWidth(WIDTH);
        this.setPrefHeight(HEIGHT);
        this.setLayoutX(positionX);

        //render background
        LinearGradient gradient = new LinearGradient(
                0,
                0,
                0,
                1,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Colors.NAVIGATION_COLOR.getColor()),
                new Stop(1, Color.web(Color.WHITE.toString(), 0.1))
        );
        this.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        //init trails
        for (int i = 0; i < TRAILS; i++) {
            EqualizerBandTrail trail = new EqualizerBandTrail(HEIGHT - i * 3);
            this.trails.add(trail);
            this.getChildren().add(trail);
        }
    }

    /**
     * Set band percentage
     */
    public void setPercentage(double percent) {
        final int trails = (int) (TRAILS * percent);

        int i = 0;
        for (EqualizerBandTrail trail : this.trails) {
            trail.setActive(i++ <= trails);
        }
    }

    /**
     * Get band width
     */
    public static int getBandWidth() {
        return WIDTH;
    }

    /**
     * Get band height
     */
    public static int getBandHeight() {
        return HEIGHT;
    }
}
