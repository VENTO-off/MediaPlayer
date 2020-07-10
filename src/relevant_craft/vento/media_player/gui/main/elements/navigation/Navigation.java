package relevant_craft.vento.media_player.gui.main.elements.navigation;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.scrollbar.ContentScrollBar;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.manager.color.Colors;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class Navigation extends ScrollPane {
    private final Stage stage;
    private final AnchorPane layout;
    private final Title title;
    private final Control control;
    private final ContentScrollBar scrollBar;

    private Pane content;

    /**
     * Init navigation bar
     */
    public Navigation(Stage stage, AnchorPane layout, Title title, Control control) {
        super();

        this.stage = stage;
        this.layout = layout;
        this.title = title;
        this.control = control;

        this.initNavigation();
        this.scrollBar = this.initScrollbar();

        layout.getChildren().add(0, this);
    }

    /**
     * Init navigation layout
     */
    private void initNavigation() {
        this.setPrefWidth(173);
        this.setPrefHeight(layout.getPrefHeight() - title.getPrefHeight() - control.getPrefHeight());
        this.setLayoutY(title.getPrefHeight());
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVmax(100);
        this.setMaxHeight(this.getPrefHeight());
        this.setBackground(new Background(new BackgroundFill(Colors.NAVIGATION_COLOR.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        this.stage.addEventFilter(WindowEvent.WINDOW_SHOWN, event -> {
            //viewport
            Region viewport = (Pane) this.lookup(".viewport");
            viewport.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        //TODO manage content
        content = new VBox();
        content.setPrefWidth(this.getPrefWidth());
        content.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        NavigationList playlists = new NavigationList("ПЛЕЙЛИСТЫ");
        playlists.addElement(new NavigationItem(Pictures.PLAYLIST_ICON, "Playlist #1"));
        playlists.addElement(new NavigationItem(Pictures.PLAYLIST_ICON, "Playlist #2"));
        playlists.addElement(new NavigationItem(Pictures.PLAYLIST_ICON, "Playlist #3"));
        playlists.addElement(new NavigationItem(Pictures.PLAYLIST_ICON, "Playlist #4"));
        playlists.addElement(new NavigationItem(Pictures.PLAYLIST_ICON, "Playlist #5"));

        content.setPrefHeight(playlists.calculateHeight() + 20);
        content.getChildren().add(playlists);

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
}
