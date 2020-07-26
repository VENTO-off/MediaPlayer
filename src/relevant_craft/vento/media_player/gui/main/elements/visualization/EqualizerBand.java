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
import relevant_craft.vento.media_player.manager.color.ColorManager;
import relevant_craft.vento.media_player.manager.color.Colors;

import java.util.ArrayList;
import java.util.List;

public class EqualizerBand extends Pane {
    private static final int WIDTH = 4;
    private static final int HEIGHT = 85;
    private static final int TRAILS = 28;

    private final ColorManager colorManager;
    private final List<EqualizerBandTrail> trails;
    private final EqualizerBandTrail flowTrail;

    /**
     * Init equalizer band
     */
    public EqualizerBand(double positionX) {
        super();

        this.colorManager = ColorManager.getInstance();
        this.trails = new ArrayList<>();
        this.flowTrail = new EqualizerBandTrail(HEIGHT, this.colorManager);

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
            EqualizerBandTrail trail = new EqualizerBandTrail(HEIGHT - i * 3, colorManager);
            this.trails.add(trail);
            this.getChildren().add(trail);
        }

//        //init flow trail
//        this.flowTrail.setActive(true);
//        this.getChildren().add(this.flowTrail);

        //default percentage
        this.setPercentage(0.0);
    }

    /**
     * Set band percentage
     */
    public synchronized void setPercentage(double percentage) {
        final int trails = (int) (TRAILS * percentage);

        int i = 0;
        for (EqualizerBandTrail trail : this.trails) {
            trail.setActive(i++ <= trails);
        }
    }

    /**
     * Set flow trail percentage
     */
    public synchronized void setFlowPercentage(double percentage) {
        final double positionY = HEIGHT * percentage;

        flowTrail.setPositionY(HEIGHT - positionY);
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
