package relevant_craft.vento.media_player.manager.color;

import javafx.scene.paint.Color;

public enum Colors {
    LAYOUT_COLOR("#303030"),
    TITLE_COLOR("#232323"),
    NAVIGATION_COLOR("#272727"),
    MINIMIZE_COLOR_GLOW("#57646B"),
    CLOSE_COLOR_GLOW("#DB4848"),
    MIDDLE_COLOR_TITLE_BUTTON("#303030"),
    BORDER_COLOR_TITLE_BUTTON("#232323"),
    SLIDER_TOTAL_COLOR("#626365"),
    SEARCH_COLOR("#202020"),
    ;

    private final Color color;

    Colors(String colorHex) {
        this.color = Color.web(colorHex);
    }

    public Color getColor() {
        return color;
    }
}
