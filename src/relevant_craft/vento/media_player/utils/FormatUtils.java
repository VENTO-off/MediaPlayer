package relevant_craft.vento.media_player.utils;

import java.text.DecimalFormat;

public class FormatUtils {

    public static String formatTime(long time) {
        long days = (time / 86400);
        long hours = ((time % 86400) / 3600);
        long minutes = (((time % 86400) % 3600) / 60);
        long seconds = (((time % 86400) % 3600) % 60);

        DecimalFormat df = new DecimalFormat("00");

        return String.join(":", df.format(days), df.format(hours), df.format(minutes), df.format(seconds));
    }

    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};

    public static String formatSize(long bytes) {
        final int kilobyte = 1024;

        if (bytes < kilobyte) {
            return bytes + " " + SIZE_UNITS[0];
        }

        DecimalFormat df = new DecimalFormat("0.#");
        int exponent = (int) (Math.log(bytes) / Math.log(kilobyte));

        return df.format(bytes / Math.pow(kilobyte, exponent)) + " " + SIZE_UNITS[exponent];
    }
}
