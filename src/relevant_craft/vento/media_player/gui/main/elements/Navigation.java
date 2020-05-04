package relevant_craft.vento.media_player.gui.main.elements;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import relevant_craft.vento.media_player.manager.color.Colors;

public class Navigation extends Pane {
    private final AnchorPane layout;
    private final Title title;
    private final Control control;

    /**
     * Init navigation bar
     */
    public Navigation(AnchorPane layout, Title title, Control control) {
        super();

        this.layout = layout;
        this.title = title;
        this.control = control;

        this.initNavigation();

        layout.getChildren().add(0, this);
    }

    /**
     * Init navigation layout
     */
    private void initNavigation() {
        this.setPrefWidth(173);
        this.setPrefHeight(layout.getPrefHeight() - title.getPrefHeight() - control.getPrefHeight());
        this.setBackground(new Background(new BackgroundFill(Colors.NAVIGATION_COLOR.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setLayoutY(title.getPrefHeight());
    }
}
