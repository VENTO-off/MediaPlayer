package relevant_craft.vento.media_player.utils;

import java.text.DecimalFormat;

public class SizeUtils {
    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};

    /**
     * Format size to human format
     */
    public static String formatSize(long bytes) {
        final int kilobyte = 1024;

        if (bytes < kilobyte) {
            return bytes + " " + SIZE_UNITS[0];
        }

        DecimalFormat df = new DecimalFormat("0.#");
        int exponent = (int) (Math.log(bytes) / Math.log(kilobyte));

        return String.join(" ", df.format(bytes / Math.pow(kilobyte, exponent)), SIZE_UNITS[exponent]);
    }
}
