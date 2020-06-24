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

    /**
     * Init visualization bar
     */
    public Visualization(AnchorPane layout, Title title, Navigation navigation) {
        super();

        this.layout = layout;
        this.title = title;
        this.navigation = navigation;

        this.initVisualization();

        layout.getChildren().add(0, this);
    }

    /**
     * Init visualization layout
     */
    private void initVisualization() {
        this.setPrefSize(594, 129);
        this.setBackground(new Background(new BackgroundFill(Colors.NAVIGATION_COLOR.getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setLayoutX((layout.getPrefWidth() - navigation.getPrefWidth()) / 2 - this.getPrefWidth() / 2 + navigation.getPrefWidth());
        this.setLayoutY(title.getPrefHeight());
        this.setEffect(new DropShadow(5, Color.web(Color.BLACK.toString(), 0.50)));

        Line separator = new Line();
        separator.setStroke(Color.web(Color.WHITE.toString(), 0.1));
        separator.setStrokeWidth(2);
        separator.setStartX(0);
        separator.setStartY(this.getPrefHeight() - 27);
        separator.setEndX(this.getPrefWidth());
        separator.setEndY(separator.getStartY());
        this.getChildren().add(separator);
    }
}
