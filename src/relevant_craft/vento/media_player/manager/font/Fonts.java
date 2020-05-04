package relevant_craft.vento.media_player.manager.font;

public enum Fonts {
    SEGOE_UI("segoe_ui_regular.ttf"),
    SEGOE_UI_BOLD("segoe_ui_bold.ttf"),
    ;

    private String fontName;

    Fonts(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }
}
