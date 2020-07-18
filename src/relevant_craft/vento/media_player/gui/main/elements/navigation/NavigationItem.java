package relevant_craft.vento.media_player.gui.main.elements.navigation;

import relevant_craft.vento.media_player.manager.picture.Pictures;

import java.io.Serializable;
import java.util.UUID;

public class NavigationItem implements Serializable {
    private final Pictures icon;
    private final String displayName;
    private final UUID uuid;

    /**
     * Init navigation item
     */
    public NavigationItem(Pictures icon, String displayName, UUID uuid) {
        this.icon = icon;
        this.displayName = displayName;
        this.uuid = uuid;
    }

    /**
     * Return icon
     */
    public Pictures getIcon() {
        return icon;
    }

    /**
     * Return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Return UUID
     */
    public UUID getUUID() {
        return uuid;
    }
}
