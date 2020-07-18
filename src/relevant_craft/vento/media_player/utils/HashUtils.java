package relevant_craft.vento.media_player.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Formatter;

public class HashUtils {

    /**
     * Get file's md5 checksum
     */
    public static String md5(String path) {
        if (!new File(path).exists()) return "0";
        FileInputStream fis = null;
        DigestInputStream dis = null;
        BufferedInputStream bis = null;
        Formatter formatter = null;
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);
            dis = new DigestInputStream(bis, messagedigest);
            while (dis.read() != -1) ;
            byte[] abyte0 = messagedigest.digest();
            formatter = new Formatter();
            byte[] abyte1 = abyte0;
            int i = abyte1.length;
            for (int j = 0; j < i; j++) {
                byte byte0 = abyte1[j];
                formatter.format("%02x", new Object[]{Byte.valueOf(byte0)});
            }
            return formatter.toString();
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                fis.close();
            } catch (Exception ignored) {}
            try {
                dis.close();
            } catch (Exception ignored) {}
            try {
                bis.close();
            } catch (Exception ignored) {}
            try {
                formatter.close();
            } catch (Exception ignored) {}
        }
    }
}
