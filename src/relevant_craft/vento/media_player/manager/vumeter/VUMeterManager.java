package relevant_craft.vento.media_player.manager.vumeter;

import javafx.application.Platform;
import relevant_craft.vento.media_player.gui.main.elements.visualization.VUMeter;

public class VUMeterManager {
    private final double MIN_LEVEL = -60.0;
    private final double MAX_LEVEL = +3.0;
    private final VUMeter vuMeter;

    private double buffer;
    private long lastUpdate;

    /**
     * Init VU Meter manager
     */
    public VUMeterManager(VUMeter vuMeter) {
        this.vuMeter = vuMeter;
        this.buffer = 0;
        this.lastUpdate = System.currentTimeMillis();

        this.initVUMeterChecker();
    }

    /**
     * Set VU Meter level
     */
    public void setLevel(float[] samples) {
        double level = 0;
        for (float sample : samples) {
            level += Math.pow(sample, 2);
        }
        level = Math.sqrt(level / samples.length);

        //compare with buffer
        if (level > buffer) {
            buffer = level;
        } else {
            buffer -= 0.01;     //decrease speed
        }

        //render
        vuMeter.setLevel(convertAndNormalize(buffer));
        lastUpdate = System.currentTimeMillis();
    }

    /**
     * Convert to db and normalize
     */
    private double convertAndNormalize(double level) {
        if (level < 0) {
            level = 0;
        }

        //convert to db
        double dbLevel = 10 * Math.log10(level);

        //normalize
        dbLevel = (((dbLevel - MIN_LEVEL) * (MAX_LEVEL - MIN_LEVEL)) / (0.0 - MIN_LEVEL)) + MIN_LEVEL;

        return dbLevel;
    }

    /**
     * Init VU Meter level checker
     */
    private void initVUMeterChecker() {
        Thread checker = new Thread(() -> {
            while (true) {
                if (buffer > 0.0) {
                    if (System.currentTimeMillis() - lastUpdate > 30) {
                        buffer -= 0.02;     //decrease speed
                        Platform.runLater(() -> vuMeter.setLevel(convertAndNormalize(buffer)));
                    }
                }

                try {
                    Thread.sleep(30);
                } catch (Exception ignored) {}
            }
        });
        checker.start();
    }
}
