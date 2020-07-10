package relevant_craft.vento.media_player.utils;

import java.text.DecimalFormat;

public class TimeUtils {

    /**
     * Format time to human format (playlist info only)
     */
    public static String formatPlaylistInfoTime(long time) {
        long days = (time / 86400);
        long hours = ((time % 86400) / 3600);
        long minutes = (((time % 86400) % 3600) / 60);
        long seconds = (((time % 86400) % 3600) % 60);

        DecimalFormat df = new DecimalFormat("00");

        return String.join(":", df.format(days), df.format(hours), df.format(minutes), df.format(seconds));
    }

    /**
     * Format time to human format
     */
    public static String formatPlaylistTime(long time) {
        long hours = ((time % 86400) / 3600);
        long minutes = (((time % 86400) % 3600) / 60);
        long seconds = (((time % 86400) % 3600) % 60);

        DecimalFormat df = new DecimalFormat("00");

        if (hours == 0) {
            return String.join(":", df.format(minutes), df.format(seconds));
        }

        return String.join(":", df.format(hours), df.format(minutes), df.format(seconds));
    }
}
