package relevant_craft.vento.media_player.manager.playlist;

import java.util.List;
import java.util.UUID;

public class PlaylistData {
    private final UUID uuid;
    private final String displayName;
    private final List<SongData> songs;

    public PlaylistData(UUID uuid, String displayName, List<SongData> songs) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.songs = songs;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<SongData> getSongs() {
        return songs;
    }
}
