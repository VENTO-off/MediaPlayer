package relevant_craft.vento.media_player.manager.directory;

import relevant_craft.vento.media_player.VENTO;

import java.io.File;

public class DirectoryManager {
    private final File PLAYER_DIR;

    private static DirectoryManager instance;

    /**
     * Init directory manager
     */
    public DirectoryManager() {
        this.PLAYER_DIR = new File(System.getProperty("user.home") + File.separator + "." + VENTO.PLAYER.toLowerCase());

        instance = this;

        this.checkDirectories();
    }

    /**
     * Check all directories (create if not exists)
     */
    private void checkDirectories() {
        if (!PLAYER_DIR.exists()) {
            PLAYER_DIR.mkdirs();
        }

        for (Directories dirName : Directories.values()) {
            File current = new File(PLAYER_DIR + File.separator + dirName);
            if (!current.exists()) {
                current.mkdir();
            }
        }
    }

    /**
     * Get directory
     */
    public File getDirectory(Directories dirName) {
        return new File(PLAYER_DIR + File.separator + dirName);
    }

    /**
     * Return the singleton DirectoryManager instance
     */
    public static DirectoryManager getInstance() {
        return instance;
    }
}
