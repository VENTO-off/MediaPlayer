package relevant_craft.vento.media_player.manager.picture;

public enum Pictures {
    MINIMIZE_ICON("minimize icon.png"),
    CLOSE_ICON("close_icon.png"),
    ;

    private String iconName;

    Pictures(String iconName) {
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }
}
