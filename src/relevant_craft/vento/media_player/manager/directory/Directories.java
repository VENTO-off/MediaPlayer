package relevant_craft.vento.media_player.manager.directory;

public enum Directories {
    PLAYLISTS_DIR("playlists"),
    COVERS_DIR("covers"),
    ;

    private final String directory;

    Directories(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return directory;
    }
}
