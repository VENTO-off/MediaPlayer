package relevant_craft.vento.media_player.gui.main.elements;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import relevant_craft.vento.media_player.manager.color.Colors;

public class Control extends Pane {
    private final AnchorPane layout;

    /**
     * Init control bar
     */
    public Control(AnchorPane layout) {
        super();

        this.layout = layout;

        this.initControl();

        layout.getChildren().add(this);
    }

    /**
     * Init control layout
     */
    public void initControl() {
        //TODO pass average color
        final String averageColor = "#6b80c1";

        this.setPrefSize(layout.getPrefWidth(), 70);
        LinearGradient gradient = new LinearGradient(
                0,
                0,
                1,
                0,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(averageColor)),
                new Stop(1, Colors.LAYOUT_COLOR.getColor())
        );
        this.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(0, 0, 5, 5, false), Insets.EMPTY)));
        this.setLayoutY(layout.getPrefHeight() - this.getPrefHeight());

        DropShadow shadow = new DropShadow(10, Color.web(Color.BLACK.toString(), 0.3));
        shadow.setWidth(0);
        shadow.setOffsetY(shadow.getRadius() * -1);
        this.setEffect(shadow);
    }
}
