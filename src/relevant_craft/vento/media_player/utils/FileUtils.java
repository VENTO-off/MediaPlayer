package relevant_craft.vento.media_player.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * List all files in directory and subdirectories
     */
    public static List<File> getAllFiles(List<File> paths) {
        List<File> files = new ArrayList<>();

        for (File file : paths) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                files.addAll(getAllFilesInDirectory(file));
            }
        }

        return files;
    }

    private static List<File> getAllFilesInDirectory(File path) {
        List<File> files = new ArrayList<>();

        if (path.listFiles() == null) {
            return files;
        }

        for (File file : path.listFiles()) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                files.addAll(getAllFilesInDirectory(file));
            }
        }

        return files;
    }
}
