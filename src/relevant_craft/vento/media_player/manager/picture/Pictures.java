package relevant_craft.vento.media_player.manager.picture;

public enum Pictures {
    MINIMIZE_ICON("minimize icon.png"),
    CLOSE_ICON("close_icon.png"),
    LOGO("logo.png"),
    PLAY_ICON("play_icon.png"),
    PREV_ICON("prev_icon.png"),
    NEXT_ICON("next_icon.png"),
    REPEAT_ICON("repeat_icon.png"),
    RANDOM_ICON("random_icon.png"),
    VOLUME_ICON("volume_icon.png"),
    MUTE_ICON("mute_icon.png"),
    TEST_COVER("test_cover.png"),
    ;

    private final String iconName;

    Pictures(String iconName) {
        this.iconName = iconName;
    }

    public String getIconName() {
        return iconName;
    }
}
