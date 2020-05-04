package relevant_craft.vento.media_player.manager.font;

public enum Fonts {
    SEGOE_UI("segoe_ui.ttf"),
    ;

    private String fontName;

    Fonts(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }
}
