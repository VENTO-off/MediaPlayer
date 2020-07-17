package relevant_craft.vento.media_player.gui.main.elements.playlist;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
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

public class Playlist extends ScrollPane {
    private final Stage stage;
    private final AnchorPane layout;
    private final Title title;
    private final Control control;
    private final Navigation navigation;
    private final Visualization visualization;
    private final ContentScrollBar scrollBar;

    private Pane content;
    private ImageView cover;

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

        //TODO manage content
        content = new VBox();
        content.setPrefWidth(this.getPrefWidth());
        content.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        PlaylistList playlist = new PlaylistList();
        playlist.addElement(new PlaylistItem("ChipaChip - Глобус (Scratch by DJ Vader One)", 181, "MP3", 44, 320, 7329546));
        playlist.addElement(new PlaylistItem("ChipaChip - Мороженка (feat. 4SGM, Лёша Свик)", 226, "MP3", 44, 320, 9122611));
        playlist.addElement(new PlaylistItem("ChipaChip - Минутная лайф", 184, "MP3", 44, 320, 7434403));
        playlist.addElement(new PlaylistItem("ChipaChip - Флэшбэк", 142, "MP3", 44, 320, 5735710));
        playlist.addElement(new PlaylistItem("ChipaChip - Монолог", 181, "MP3", 44, 320, 7319060));
        playlist.addElement(new PlaylistItem("ChipaChip - Репит", 157, "MP3", 44, 320, 6364856));
        playlist.addElement(new PlaylistItem("ChipaChip - По проводам", 212, "MP3", 44, 320, 8545894));
        playlist.calculateOrderNumbers();

        content.setPrefHeight(playlist.calculateHeight() + 10);
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
}
