package relevant_craft.vento.media_player.gui.main.elements;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import relevant_craft.vento.media_player.manager.color.Colors;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class Control extends Pane {
    private static final double height = 70;

    private final AnchorPane layout;

    private ControlSlider songSlider;
    private ControlSlider volumeSlider;
    private ImageView cover;
    private Text songName;
    private Text artistName;

    /**
     * Init control bar
     */
    public Control(AnchorPane layout) {
        super();

        this.layout = layout;

        this.initControl();
        this.initSongSlider();
        this.initVolumeSlider();
        this.initCoverArt();
        this.initSongInfo();

        layout.getChildren().add(this);
    }

    /**
     * Init control layout
     */
    private void initControl() {
        //TODO pass average color
        final String averageColor = "#6b80c1";

        this.setPrefSize(layout.getPrefWidth(), height);
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

    /**
     * Init song slider
     */
    private void initSongSlider() {
        songSlider = new ControlSlider(267, 48, 265);
        this.getChildren().add(songSlider);
    }

    /**
     * Init volume slider
     */
    private void initVolumeSlider() {
        volumeSlider = new ControlSlider(686, 36, 100);
        this.getChildren().add(volumeSlider);
    }

    /**
     * Init cover art
     */
    private void initCoverArt() {
        cover = new ImageView();
        cover.setImage(PictureManager.loadImage(Pictures.LOGO.getIconName()));
        cover.setFitWidth(height);
        cover.setFitHeight(height);
        cover.setPreserveRatio(true);
        this.getChildren().add(cover);
    }

    /**
     * Init song and artist name
     */
    private void initSongInfo() {
        final double fixY = 11;

        songName = new Text();
        songName.setX(cover.getFitWidth() + 10);
        songName.setY(20 + fixY);
        songName.setFill(Color.WHITE);
        songName.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 16));
        this.getChildren().add(songName);

        artistName = new Text();
        artistName.setX(cover.getFitWidth() + 10);
        artistName.setY(39 + fixY);
        artistName.setFill(Color.WHITE);
        artistName.setFont(FontManager.loadFont(Fonts.SEGOE_UI_BOLD.getFontName(), 13));
        this.getChildren().add(artistName);
    }
}
