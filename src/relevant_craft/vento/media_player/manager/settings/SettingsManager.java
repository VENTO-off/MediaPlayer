package relevant_craft.vento.media_player.manager.settings;

import relevant_craft.vento.media_player.VENTO;

import java.util.prefs.Preferences;

public class SettingsManager {
    private final Preferences data;

    private static SettingsManager instance;

    private String lastPlaylist;
    private double volumeLevel;
    private boolean isMuted;
    private boolean isRepeatEnabled;
    private boolean isRandomEnabled;

    /**
     * Init settings manager
     */
    public SettingsManager() {
        data = Preferences.userRoot().node(String.join("/", VENTO.PLAYER.toLowerCase(), "settings"));

        instance = this;

        lastPlaylist = data.get("lastPlaylist", "default");
        volumeLevel = data.getDouble("volumeLevel", 1.0);
        isMuted = data.getBoolean("isMuted", false);
        isRepeatEnabled = data.getBoolean("isRepeatEnabled", false);
        isRandomEnabled = data.getBoolean("isRandomEnabled", false);
    }

    /**
     * Save last playlist
     */
    public void setLastPlaylist(String lastPlaylist) {
        this.lastPlaylist = lastPlaylist;
        data.put("lastPlaylist", this.lastPlaylist);
    }

    /**
     * Save volume level
     */
    public void setVolumeLevel(double volumeLevel) {
        this.volumeLevel = volumeLevel;
        data.putDouble("volumeLevel", this.volumeLevel);
    }

    /**
     * Save muted value
     */
    public void setMuted(boolean muted) {
        this.isMuted = muted;
        data.putBoolean("isMuted", this.isMuted);
    }

    /**
     * Save repeat button value
     */
    public void setRepeatEnabled(boolean repeatEnabled) {
        this.isRepeatEnabled = repeatEnabled;
        data.putBoolean("isRepeatEnabled", this.isRepeatEnabled);
    }

    /**
     * Save random button value
     */
    public void setRandomEnabled(boolean randomEnabled) {
        this.isRandomEnabled = randomEnabled;
        data.putBoolean("isRandomEnabled", this.isRandomEnabled);
    }

    /**
     * Return last playlist
     */
    public String getLastPlaylist() {
        return lastPlaylist;
    }

    /**
     * Return volume level
     */
    public double getVolumeLevel() {
        return volumeLevel;
    }

    /**
     * Return muted value
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Return repeat button value
     */
    public boolean isRepeatEnabled() {
        return isRepeatEnabled;
    }

    /**
     * Return random button value
     */
    public boolean isRandomEnabled() {
        return isRandomEnabled;
    }

    /**
     * Return the singleton SettingsManager instance
     */
    public static SettingsManager getInstance() {
        return instance;
    }
}
