package relevant_craft.vento.media_player.gui.main.elements.playlist;

import java.io.Serializable;

public class PlaylistItem implements Serializable {
    private final String path;
    private final String title;
    private final String artist;
    private final int bitRate;
    private final int sampleRate;
    private final long length;
    private final long size;
    private final String hash;

    /**
     * Init playlist item (song)
     */
    public PlaylistItem(String path, String title, String artist, int bitRate, int sampleRate, long length, long size, String hash) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.bitRate = bitRate;
        this.sampleRate = sampleRate;
        this.length = length;
        this.size = size;
        this.hash = hash;
    }

    /**
     * Return path of song
     */
    public String getPath() {
        return path;
    }

    /**
     * Return title of song
     */
    public String getTitle() {
        return title;
    }

    /**
     * Return artist of song
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Return bitrate of song
     */
    public int getBitRate() {
        return bitRate;
    }

    /**
     * Return sample rate of song
     */
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Return total seconds of song
     */
    public long getLength() {
        return length;
    }

    /**
     * Return file size
     */
    public long getSize() {
        return size;
    }

    /**
     * Return md5 hash of file
     */
    public String getHash() {
        return hash;
    }

    /**
     * Return display name of song
     */
    public String getDisplayName() {
        if (title != null && artist != null) {
            return String.join(" - ", artist, title);
        }

        return (title != null ? title : "Unknown");
    }

    /**
     * Return audio format of song
     */
    public String getAudioFormat() {
        int extensionIndex = path.lastIndexOf(".");
        if (extensionIndex == -1) {
            return "none".toUpperCase();
        }

        return path.substring(extensionIndex + 1).toUpperCase();
    }
}
