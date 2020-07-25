package relevant_craft.vento.media_player.utils;

import org.tritonus.share.sampled.file.TAudioFileFormat;
import relevant_craft.vento.media_player.gui.main.elements.playlist.PlaylistItem;
import relevant_craft.vento.media_player.manager.player.PlayerEngine;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Map;

public class SongUtils {

    /**
     * Load audio data
     */
    public static PlaylistItem getAudioData(File file) throws Exception {
        String path = file.getAbsolutePath();
        String title = null;
        String artist = null;
        int bitRate;
        int sampleRate;
        long length;
        long size = file.length();
        String audioFormat;
        String hash = HashUtils.md5(file.getAbsolutePath());

        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        AudioFormat format = PlayerEngine.getOutFormat(in.getFormat());

        //sample rate
        sampleRate = (int) in.getFormat().getSampleRate();

        AudioFileFormat baseFormat = AudioSystem.getAudioFileFormat(file);

        //audio format
        audioFormat = baseFormat.getType().getExtension();

        if (baseFormat instanceof TAudioFileFormat) {
            Map<?, ?> props = baseFormat.properties();

            //bitrate
            bitRate = (int) in.getFormat().properties().get("bitrate");

            //seconds length
            length = ((Long) props.get("duration") / 1000000);

            //title
            title = (String) props.get("title");
            if (title != null && title.isEmpty()) {
                title = null;
            }

            //artist
            artist = (String) props.get("author");
            if (artist != null && artist.isEmpty()) {
                artist = null;
            }
        } else {
            //seconds length
            length = (long) (baseFormat.getFrameLength() / baseFormat.getFormat().getFrameRate());

            //bitrate
            bitRate = (int) (format.getSampleSizeInBits() * format.getSampleRate() * format.getChannels());
        }

        in.close();

        //parse title from file name
        if (title == null) {
            String fileName = file.getName();
            int extensionIndex = fileName.lastIndexOf(".");
            if (extensionIndex != -1) {
                fileName = fileName.substring(0, extensionIndex);
            }

            if (fileName.contains(" - ")) {
                String[] data = fileName.split(" - ");
                artist = data[0];
                title = data[1];
            } else {
                title = fileName;
                artist = null;
            }
        }

        //trim title and artist
        if (title != null && !title.isEmpty()) {
            title = title.trim();
        }
        if (artist != null && !artist.isEmpty()) {
            artist = artist.trim();
        }

        return new PlaylistItem(path, title, artist, bitRate, sampleRate, length, size, audioFormat, hash);
    }

}
