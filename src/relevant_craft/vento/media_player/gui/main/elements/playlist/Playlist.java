package relevant_craft.vento.media_player.gui.main.elements.playlist;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.navigation.Navigation;
import relevant_craft.vento.media_player.gui.main.elements.scrollbar.ContentScrollBar;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.gui.main.elements.visualization.Visualization;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

import java.util.List;

public class Playlist extends ScrollPane {
    private final Stage stage;
    private final AnchorPane layout;
    private final Title title;
    private final Control control;
    private final Navigation navigation;
    private final Visualization visualization;
    private final ContentScrollBar scrollBar;

    private Pane content;
    private PlaylistList playlist;
    private ImageView cover;
    private Loader loader;
    private Thread renderer;

    /**
     * Init playlist bar
     */
    public Playlist(Stage stage, AnchorPane layout, Title title, Control control, Navigation navigation, Visualization visualization) {
        super();

        this.stage = stage;
        this.layout = layout;
        this.title = title;
        this.control = control;
        this.navigation = navigation;
        this.visualization = visualization;

        this.initPlaylist();
        this.initCoverArt();
        this.scrollBar = this.initScrollbar();
        this.initLoader();

        layout.getChildren().add(0, this);
    }

    /**
     * Init playlist layout
     */
    private void initPlaylist() {
        this.setPrefWidth(layout.getPrefWidth() - navigation.getPrefWidth());
        this.setPrefHeight(layout.getPrefHeight() - title.getPrefHeight() - visualization.getPrefHeight() - control.getPrefHeight() - 20);
        this.setLayoutX(navigation.getPrefWidth());
        this.setLayoutY(layout.getPrefHeight() / 2 - this.getPrefHeight() / 2 - title.getPrefHeight() / 2 + visualization.getPrefHeight() / 2);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVmax(100);
        this.setMaxHeight(this.getPrefHeight());
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        this.stage.addEventFilter(WindowEvent.WINDOW_SHOWN, event -> {
            //viewport
            Region viewport = (Pane) this.lookup(".viewport");
            viewport.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        content = new VBox();
        content.setPrefWidth(this.getPrefWidth());
        content.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        playlist = new PlaylistList();
        content.getChildren().add(playlist);

        this.setContent(content);
    }

    /**
     * Init scroll bar
     */
    private ContentScrollBar initScrollbar() {
        ContentScrollBar scrollBar = new ContentScrollBar(stage, this);
        scrollBar.setThumbHeight(content.getPrefHeight());
        layout.getChildren().add(scrollBar);

        return scrollBar;
    }

    /**
     * Init cover art
     */
    private void initCoverArt() {
        cover = new ImageView();
        //TODO remove test cover
        cover.setImage(PictureManager.loadImage(Pictures.TEST_COVER.getIconName()));
        cover.setFitHeight(layout.getPrefHeight() - title.getPrefHeight());
        cover.setPreserveRatio(true);
        cover.setLayoutX(layout.getPrefWidth() / 2 - cover.getFitWidth() / 2 - navigation.getPrefWidth());
        cover.setLayoutY(title.getPrefHeight());
        cover.setOpacity(0.05);
        cover.setDisable(true);
        layout.getChildren().add(0, cover);
    }

    /**
     * Init loader area
     */
    private void initLoader() {
        loader = new Loader(this.getPrefWidth(), this.getPrefHeight(), this.getLayoutX(), this.getLayoutY());
        layout.getChildren().add(loader);
    }

    /**
     * Show loader
     */
    public void showLoader() {
        loader.show();
        playlist.setEffect(new GaussianBlur());
    }

    /**
     * Hide loader
     */
    public void hideLoader() {
        loader.hide();
        playlist.setEffect(null);
    }

    /**
     * Set loader text
     */
    public void setLoaderText(String value) {
        loader.updateText(value);
    }

    /**
     * Calculate playlist height
     */
    private void calculateHeight() {
        content.setPrefHeight(playlist.calculateHeight() + 10);
        scrollBar.setThumbHeight(content.getPrefHeight());
    }

    /**
     * Add element to playlist
     */
    public void add(PlaylistItem data) {
        playlist.addElement(data);
        playlist.calculateOrderNumbers();
        calculateHeight();
    }

    /**
     * Add all elements to playlist (runs a new thread)
     */
    public void addAdd(List<PlaylistItem> data, PlaylistItem itemToSelect) {
        if (renderer != null && renderer.isAlive()) {
            renderer.stop();
        }

        renderer = new Thread(() -> {
            //render
            playlist.addAllElements(data);

            Platform.runLater(() -> {
                //rerender
                playlist.calculateOrderNumbers();
                calculateHeight();

                //hide loader
                hideLoader();
            });

            sleep(20);

            Platform.runLater(() -> selectElement(itemToSelect, true));
        });
        renderer.start();
    }

    /**
     * Select element in playlist
     */
    public void selectElement(PlaylistItem element, boolean doScroll) {
        //select current track
        int index = playlist.selectElement(element);

        //scroll to current track
        if (index != 0 && doScroll) {
            if (index <= 6) {
                this.setVvalue(0);
            } else if (index >= playlist.getSize() - 6) {
                this.setVvalue(100);
            } else {
                this.setVvalue(index * 100D / playlist.getSize());
            }
        }
    }

    /**
     * Clear playlist
     */
    public void clear() {
        playlist.clear();
    }

    /**
     * Return playlist list
     */
    public PlaylistList getPlaylist() {
        return playlist;
    }

    /**
     * Sleep thread
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {}
    }
}
