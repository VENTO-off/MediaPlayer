package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import relevant_craft.vento.media_player.manager.color.Colors;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class PlaylistSearch extends Pane {
    private static final double WIDTH = 159;
    private static final double HEIGHT = 22;

    private final ImageView searchIcon;
    private final ImageView clearIcon;
    private final TextField searchField;

    /**
     * Init playlist search
     */
    public PlaylistSearch(double positionX, double positionY) {
        super();

        //init layout
        this.setPrefWidth(WIDTH);
        this.setPrefHeight(HEIGHT);
        this.setLayoutX(positionX);
        this.setLayoutY((int) positionY);
        this.setBackground(new Background(new BackgroundFill(Colors.SEARCH_COLOR.getColor(), new CornerRadii(3), Insets.EMPTY)));
        this.setCursor(Cursor.TEXT);

        //render search icon
        this.searchIcon = new ImageView(PictureManager.loadImage(Pictures.SEARCH_ICON.getIconName()));
        this.searchIcon.setLayoutX(3);
        this.searchIcon.setLayoutY((int) (this.getPrefHeight() / 2 - this.searchIcon.getImage().getHeight() / 2));
        this.searchIcon.setOpacity(0.35);
        this.getChildren().add(this.searchIcon);

        //render clear icon
        this.clearIcon = new ImageView(PictureManager.loadImage(Pictures.CLOSE_ICON.getIconName()));
        this.clearIcon.setLayoutX(this.getPrefWidth() - this.clearIcon.getImage().getWidth() - 5);
        this.clearIcon.setLayoutY((int) (this.getPrefHeight() / 2 - this.clearIcon.getImage().getHeight() / 2));
        this.clearIcon.setOpacity(0.35);
        this.clearIcon.setVisible(false);
        this.clearIcon.setCursor(Cursor.HAND);
        this.clearIcon.setOnMouseClicked(this::onClearClick);
        this.getChildren().add(this.clearIcon);

        //render search field
        this.searchField = new TextField();
        this.searchField.setPromptText("Поиск");
        this.searchField.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 14));
        this.searchField.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        this.searchField.setLayoutX(15);
        this.searchField.setLayoutY(-3);
        this.searchField.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.35); -fx-prompt-text-fill: rgba(255, 255, 255, 0.35); -fx-accent: rgba(255, 255, 255, 0.1);");
        this.searchField.setPrefWidth(this.getPrefWidth() - this.searchField.getLayoutX() - (this.getPrefWidth() - this.clearIcon.getLayoutX()));
        this.searchField.setPrefHeight(this.getPrefHeight());
        this.setOnMouseClicked(this::onClick);
        this.searchField.setOnKeyTyped(this::onType);
        this.removeFocus();
        this.getChildren().add(this.searchField);
    }

    /**
     * Remove focus from search field
     */
    private void removeFocus() {
        Platform.runLater(this::requestFocus);
    }

    /**
     * Event on mouse click
     */
    private void onClick(MouseEvent e) {
        this.searchField.requestFocus();
    }

    /**
     * Event on type
     */
    private void onType(KeyEvent e) {
        this.clearIcon.setVisible(!this.searchField.getText().isEmpty());
    }

    /**
     * Event on clear icon click
     */
    private void onClearClick(MouseEvent e) {
        this.searchField.clear();
        this.clearIcon.setVisible(false);
        this.removeFocus();
    }

    /**
     * Get playlist search width
     */
    public static double getPlaylistSearchWidth() {
        return WIDTH;
    }

    /**
     * Get playlist search height
     */
    public static double getPlaylistSearchHeight() {
        return HEIGHT;
    }
}
