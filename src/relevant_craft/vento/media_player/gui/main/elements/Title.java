package relevant_craft.vento.media_player.gui.main.elements;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import relevant_craft.vento.media_player.VENTO;
import relevant_craft.vento.media_player.manager.color.Colors;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class Title extends Pane {
    private final AnchorPane layout;
    private final Stage stage;

    private TitleButton close;
    private TitleButton minimize;
    private Text titleText;
    private double xOffset;
    private double yOffset;

    /**
     * Init title bar
     */
    public Title(AnchorPane layout, Stage stage) {
        super();

        this.layout = layout;
        this.stage = stage;

        this.initTitle();
        this.initMinimizeButton();
        this.initCloseButton();
        this.initTitleText();

        layout.getChildren().add(this);
    }

    /**
     * Init title layout
     */
    private void initTitle() {
        this.setPrefSize(layout.getPrefWidth(), 30);
        this.setBackground(new Background(new BackgroundFill(Colors.TITLE_COLOR.getColor(), new CornerRadii(5, 5, 0, 0, false), Insets.EMPTY)));
        this.setOnMousePressed(this::onDrag);
        this.setOnMouseDragged(this::onRelease);

        DropShadow shadow = new DropShadow(5, Color.web(Color.BLACK.toString(), 0.3));
        shadow.setWidth(0);
        shadow.setOffsetY(shadow.getRadius());
        this.setEffect(shadow);
    }

    /**
     * Init minimize button
     */
    private void initMinimizeButton() {
        minimize = new TitleButton(
                this.getPrefWidth() - TitleButton.getSize() - 46,
                this.getPrefHeight() / 2 - TitleButton.getSize() / 2,
                Pictures.MINIMIZE_ICON,
                Colors.MINIMIZE_COLOR_GLOW.getColor());
        this.getChildren().add(minimize);
    }

    /**
     * Init close button
     */
    private void initCloseButton() {
        close = new TitleButton(
                this.getPrefWidth() - TitleButton.getSize() - 16,
                this.getPrefHeight() / 2 - TitleButton.getSize() / 2,
                Pictures.CLOSE_ICON,
                Colors.CLOSE_COLOR_GLOW.getColor());
        this.getChildren().add(close);

        //TODO remove
        close.addClickListener(Platform::exit);
    }

    /**
     * Init title text
     */
    private void initTitleText() {
        titleText = new Text("Richen" + " v" + VENTO.version);
        titleText.setX(14);
        titleText.setY(21);
        titleText.setFill(Color.WHITE);
        titleText.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 18));
        this.getChildren().add(titleText);
    }

    /**
     * Event on drag window
     */
    private void onDrag(MouseEvent e) {
        xOffset = stage.getX() - e.getScreenX();
        yOffset = stage.getY() - e.getScreenY();
    }

    /**
     * Event on release window
     */
    private void onRelease(MouseEvent e) {
        stage.setX(e.getScreenX() + xOffset);
        stage.setY(e.getScreenY() + yOffset);
    }
}
