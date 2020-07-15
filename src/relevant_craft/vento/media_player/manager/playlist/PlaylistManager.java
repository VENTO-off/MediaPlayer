package relevant_craft.vento.media_player.manager.playlist;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import relevant_craft.vento.media_player.manager.directory.Directories;
import relevant_craft.vento.media_player.manager.directory.DirectoryManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.UUID;

public class PlaylistManager {
    private final File PLAYLIST_DIR;
    private final HashMap<UUID, String> playlists;
    private final Gson gson;

    /**
     * Init playlist manager
     */
    public PlaylistManager() {
        this.PLAYLIST_DIR = DirectoryManager.getInstance().getDirectory(Directories.PLAYLISTS_DIR);
        this.playlists = new HashMap<>();
        this.gson = new Gson();

        this.loadPlaylistNames();
    }

    /**
     * Load all playlist names
     */
    private void loadPlaylistNames() {
        if (PLAYLIST_DIR.listFiles() == null) {
            return;
        }

        //read all .json files
        for (File playlistFile : PLAYLIST_DIR.listFiles()) {
            try (FileReader reader = new FileReader(playlistFile)) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (jsonObject.has("uuid") && jsonObject.has("displayName")) {
                    //get uuid and name
                    String uuid = jsonObject.get("uuid").getAsString();
                    String displayName = jsonObject.get("displayName").getAsString();

                    if (!playlistFile.getName().equals(uuid)) {
                        continue;
                    }

                    playlists.put(UUID.fromString(uuid), displayName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load playlist from .json
     */
    public PlaylistData loadPlaylist(UUID uuid) throws Exception {
        try (FileReader reader = new FileReader(PLAYLIST_DIR + File.separator + uuid.toString())) {
            return gson.fromJson(JsonParser.parseReader(reader), PlaylistData.class);
        }
    }

    /**
     * Save playlist to .json
     */
    public void savePlaylist(PlaylistData playlist) throws Exception {
        try (FileWriter writer = new FileWriter(PLAYLIST_DIR + File.separator + playlist.getUUID().toString())) {
            gson.toJson(playlist, writer);
        }
    }

    /**
     * Return all playlists
     */
    public HashMap<UUID, String> getPlaylists() {
        return playlists;
    }
}
