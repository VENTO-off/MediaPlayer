package relevant_craft.vento.media_player.gui.main.elements.playlist;

import java.io.Serializable;

public class PlaylistItem implements Serializable {
    private final String displayName;
    private final long time;
    private final String audioFormat;
    private final int samplingRate;
    private final int bitRate;
    private final long size;

    public PlaylistItem(String displayName, long time, String audioFormat, int samplingRate, int bitRate, long size) {
        this.displayName = displayName;
        this.time = time;
        this.audioFormat = audioFormat;
        this.samplingRate = samplingRate;
        this.bitRate = bitRate;
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getTime() {
        return time;
    }

    public String getAudioFormat() {
        return audioFormat;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public int getBitRate() {
        return bitRate;
    }

    public long getSize() {
        return size;
    }
}
