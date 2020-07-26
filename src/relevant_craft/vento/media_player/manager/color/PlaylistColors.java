package relevant_craft.vento.media_player.manager.color;

import javafx.scene.paint.Color;

/**
 * List of all default playlist colors
 */
public enum PlaylistColors {
    DEFAULT_1("#00808c"),
    DEFAULT_2("#a3a63f"),
    DEFAULT_3("#ab6d0a"),
    DEFAULT_4("#b8a48b"),
    ;

    private final Color color;

    PlaylistColors(String colorHex) {
        this.color = Color.web(colorHex);
    }

    public Color getColor() {
        return color;
    }

    public static Color getOrderedColor(int number) {
        int fractional = number % values().length;
        int index = (fractional == 0 ? values().length : fractional);

        return values()[index - 1].getColor();
    }
}
