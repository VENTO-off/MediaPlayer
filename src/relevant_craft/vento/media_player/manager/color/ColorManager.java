package relevant_craft.vento.media_player.manager.color;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorManager {
    private final List<ChangeColorListener> changeColorListeners;

    private Color currentColor;
    private Color finalColor;
    private Thread animation;

    private static ColorManager instance;

    /**
     * Init color manager
     */
    public ColorManager() {
        this.changeColorListeners = new ArrayList<>();

        this.currentColor = PlaylistColors.DEFAULT_1.getColor();
        this.finalColor = PlaylistColors.DEFAULT_1.getColor();

        instance = this;
    }

    /**
     * Set new color
     */
    public void setColor(int index, Color color) {
        if (color != null) {
            finalColor = color;
        } else {
            finalColor = PlaylistColors.getOrderedColor(index);
        }

        animateColor();
    }

    /**
     * Animate from current color to final color
     */
    private void animateColor() {
        if (animation != null && animation.isAlive()) {
            animation.stop();
        }

        animation = new Thread(() -> {
            while (!currentColor.equals(finalColor)) {
                int red = convertToInt(currentColor.getRed());
                int green = convertToInt(currentColor.getGreen());
                int blue = convertToInt(currentColor.getBlue());

                int finalRed = convertToInt(finalColor.getRed());
                int finalGreen = convertToInt(finalColor.getGreen());
                int finalBlue = convertToInt(finalColor.getBlue());

                //animate red color
                if (red != finalRed) {
                    red += ((red < finalRed ? 1 : -1));
                }

                //animate green color
                if (green != finalGreen) {
                    green += ((green < finalGreen ? 1 : -1));
                }

                //animate blue color
                if (blue != finalBlue) {
                    blue += ((blue < finalBlue) ? 1 : -1);
                }

                currentColor = Color.rgb(red, green, blue);
                notifyChangeColorListener();

                try {
                    Thread.sleep(3);
                } catch (InterruptedException ignored) {}
            }
        });
        animation.start();
    }

    /**
     * Convert double to int (RGB only)
     */
    private int convertToInt(double value) {
        return (int) (255 * value);
    }

    /**
     * Get final color
     */
    public Color getFinalColor() {
        return finalColor;
    }

    /**
     * Change color listener
     */
    public interface ChangeColorListener {
        void onChange(Color color);
    }

    /**
     * Add change color listener
     */
    public void addChangeColorListener(ChangeColorListener changeColorListener) {
        this.changeColorListeners.add(changeColorListener);
    }

    /**
     * Notify all change color listeners
     */
    private void notifyChangeColorListener() {
        if (changeColorListeners == null) {
            return;
        }

        for (ChangeColorListener changeColorListener : changeColorListeners) {
            changeColorListener.onChange(currentColor);
        }
    }

    public boolean isActive() {
        return animation != null && animation.isAlive();
    }

    /**
     * Return the singleton ColorManager instance
     */
    public static ColorManager getInstance() {
        return instance;
    }
}
