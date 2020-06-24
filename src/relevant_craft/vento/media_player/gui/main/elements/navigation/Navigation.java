package relevant_craft.vento.media_player.gui.main.elements.navigation;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.manager.color.Colors;

public class Navigation extends ScrollPane {
    private final Stage stage;
    private final AnchorPane layout;
    private final Title title;
    private final Control control;
    private final NavigationScrollBar scrollBar;

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
        stage.addEventFilter(WindowEvent.WINDOW_SHOWN, event -> {
            //viewport
            Region viewport = (Pane) this.lookup(".viewport");
            viewport.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        //TODO manage content
        content = new Pane();
        content.setPrefWidth(100);
        int y = 10;
        for (int i = 0; i <= 125; i++) {
            Text t = new Text("text " + i);
            t.setLayoutX(10);
            t.setLayoutY(y);
            y += 10;
            content.getChildren().add(t);
        }
        content.setPrefHeight(y);
        content.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff0000"), CornerRadii.EMPTY, Insets.EMPTY)));

        this.setContent(content);
    }

    /**
     * Init scroll bar
     */
    private NavigationScrollBar initScrollbar() {
        NavigationScrollBar scrollBar = new NavigationScrollBar(stage, this);
        scrollBar.setThumbHeight(content.getPrefHeight());
        layout.getChildren().add(scrollBar);

        return scrollBar;
    }
}
