package relevant_craft.vento.media_player.manager.playlist;

public class SongData {
    private final String path;
    private final String title;
    private final String artist;
    private final int bitRate;
    private final int sampleRate;
    private final long length;
    private final long size;

    public SongData(String path, String title, String artist, int bitRate, int sampleRate, long length, long size) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.bitRate = bitRate;
        this.sampleRate = sampleRate;
        this.length = length;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getBitRate() {
        return bitRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public long getLength() {
        return length;
    }

    public long getSize() {
        return size;
    }
}
