package relevant_craft.vento.media_player.manager.picture;

/**
 * List of all available pictures
 */
public enum Pictures {
    MINIMIZE_ICON("minimize icon.png"),
    CLOSE_ICON("close_icon.png"),
    LOGO("logo.png"),
    PLAY_ICON("play_icon.png"),
    PAUSE_ICON("pause_icon.png"),
    PREV_ICON("prev_icon.png"),
    NEXT_ICON("next_icon.png"),
    REPEAT_ICON("repeat_icon.png"),
    RANDOM_ICON("random_icon.png"),
    VOLUME_ICON("volume_icon.png"),
    MUTE_ICON("mute_icon.png"),
    PLAYLIST_ICON("playlist_icon.png"),
    VU_METER("vu_meter.png"),
    VU_METER_LAYER("vu_meter_layer.png"),
    SEARCH_ICON("search_icon.png"),
    CLEAR_ICON("clear_icon.png"),
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
