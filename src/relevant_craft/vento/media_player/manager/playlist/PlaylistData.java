package relevant_craft.vento.media_player.manager.playlist;

import relevant_craft.vento.media_player.gui.main.elements.playlist.PlaylistItem;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlaylistData {
    private final UUID uuid;
    private final String displayName;
    private final List<PlaylistItem> songs;

    /**
     * Init playlist data
     */
    public PlaylistData(UUID uuid, String displayName, List<PlaylistItem> songs) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.songs = songs;
    }

    /**
     * Return UUID of playlist
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Return song in playlist
     */
    public List<PlaylistItem> getSongs() {
        return songs;
    }

    /**
     * Calculate total time of all songs
     */
    public long getTime() {
        long time = 0;
        for (PlaylistItem song : songs) {
            time += song.getLength();
        }

        return time;
    }

    /**
     * Calculate total size of all songs
     */
    public long getSize() {
        long size = 0;
        for (PlaylistItem song : songs) {
            size += song.getSize();
        }

        return size;
    }

    /**
     * Add songs to playlist at index
     */
    public void addSongs(List<PlaylistItem> songs, int index) {
        this.songs.addAll(index, songs);
    }

    /**
     * Set song index (change order)
     */
    public void setSongOrder(PlaylistItem currentSong, int index) {
        //find this song
        currentSong = getSongByHash(currentSong.getHash());
        if (currentSong == null) {
            return;
        }

        //get current index
        int oldIndex = songs.indexOf(currentSong);
        if (oldIndex == -1) {
            return;
        }

        //set new position
        Collections.swap(songs, oldIndex, index);
    }

    /**
     * Get song by hash
     */
    public PlaylistItem getSongByHash(String md5) {
        for (PlaylistItem song : songs) {
            if (song.getHash().equals(md5)) {
                return song;
            }
        }

        return null;
    }
}
