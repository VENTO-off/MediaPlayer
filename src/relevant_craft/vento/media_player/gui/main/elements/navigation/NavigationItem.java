package relevant_craft.vento.media_player.gui.main.elements.navigation;

import relevant_craft.vento.media_player.manager.picture.Pictures;

import java.io.Serializable;

public class NavigationItem implements Serializable {
    private final Pictures icon;
    private final String displayName;

    public NavigationItem(Pictures icon, String displayName) {
        this.icon = icon;
        this.displayName = displayName;
    }

    public Pictures getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }
}
