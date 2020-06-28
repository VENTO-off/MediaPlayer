package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import relevant_craft.vento.media_player.gui.main.elements.navigation.Navigation;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.manager.color.Colors;

public class Visualization extends Pane {
    private final AnchorPane layout;
    private final Title title;
    private final Navigation navigation;

    private Line separator;
    private VUMeter vuLeft;
    private VUMeter vuRight;
    private Equalizer equalizer;
    private PlaylistInfo playlistInfo;
    private PlaylistSearch playlistSearch;

    /**
     * Init visualization bar
     */
    public Visualization(AnchorPane layout, Title title, Navigation navigation) {
        super();

        this.layout = layout;
        this.title = title;
        this.navigation = navigation;

        this.initVisualization();
        this.initVUMeters();
        this.initEqualizer();
        this.initPlaylistInfo();
        this.initPlaylistSearch();

        layout.getChildren().add(0, this);
    }

    /**
     * Init visualization layout
     */
    private void initVisualization() {
        this.setPrefSize(594, 130);
        this.setBackground(new Background(new BackgroundFill(Colors.NAVIGATION_COLOR.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setLayoutX((int) ((layout.getPrefWidth() - navigation.getPrefWidth()) / 2 - this.getPrefWidth() / 2 + navigation.getPrefWidth()));
        this.setLayoutY(title.getPrefHeight());
        this.setEffect(new DropShadow(5, Color.web(Color.BLACK.toString(), 0.50)));

        //render separator
        separator = new Line();
        separator.setStroke(Color.web(Color.WHITE.toString(), 0.1));
        separator.setStrokeWidth(2);
        separator.setStartX(0);
        separator.setStartY(this.getPrefHeight() - 27);
        separator.setEndX(this.getPrefWidth());
        separator.setEndY(separator.getStartY());
        this.getChildren().add(separator);
    }

    /**
     * Init VU meters
     */
    private void initVUMeters() {
        vuLeft = new VUMeter(9, getVisualizationHeight() / 2 - VUMeter.getVUHeight() / 2);
        this.getChildren().add(vuLeft);

        vuRight = new VUMeter(158, getVisualizationHeight() / 2 - VUMeter.getVUHeight() / 2);
        this.getChildren().add(vuRight);

        vuLeft.setLevel(-0.5);
        vuRight.setLevel(0);
    }

    /**
     * Init equalizer
     */
    private void initEqualizer() {
        equalizer = new Equalizer(this.getPrefWidth() - Equalizer.getEqualizerWidth() - 9, getVisualizationHeight() / 2 - Equalizer.getEqualizerHeight() / 2);
        this.getChildren().add(equalizer);
    }

    /**
     * Init playlist info
     */
    private void initPlaylistInfo() {
        final double fixY = 11;

        playlistInfo = new PlaylistInfo(11, separator.getStartY() + fixY + 8);
        this.getChildren().add(playlistInfo);
        //TODO remove test value
        playlistInfo.setPlaylistInfo("Playlist #1", 19, 3376, 135266304);
    }

    /**
     * Init playlist search
     */
    private void initPlaylistSearch() {
        playlistSearch = new PlaylistSearch(this.getPrefWidth() - PlaylistSearch.getPlaylistSearchWidth() - 9,
                separator.getStartY() +
                        (this.getPrefHeight() - separator.getStartY()) / 2 -
                        PlaylistSearch.getPlaylistSearchHeight() / 2 +
                        separator.getStrokeWidth() / 2);
        this.getChildren().add(playlistSearch);
    }

    /**
     * Get visualization area height
     */
    private double getVisualizationHeight() {
        return (this.getPrefHeight() - (this.getPrefHeight() - separator.getStartY()));
    }
}
