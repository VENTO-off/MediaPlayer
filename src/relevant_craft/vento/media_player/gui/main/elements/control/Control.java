package relevant_craft.vento.media_player.gui.main.elements.control;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import relevant_craft.vento.media_player.manager.color.ColorManager;
import relevant_craft.vento.media_player.manager.color.Colors;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;
import relevant_craft.vento.media_player.utils.TextUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Control extends Pane {
    private static final double height = 70;

    private final AnchorPane layout;
    private final ColorManager colorManager;

    private ControlSlider songSlider;
    private ControlSlider volumeSlider;
    private ImageView cover;
    private Text songName;
    private Text artistName;
    private BigChangeableControlButton playButton;
    private ControlButton previousButton;
    private ControlButton nextButton;
    private PressableControlButton repeatButton;
    private PressableControlButton randomButton;
    private ChangeableControlButton muteButton;

    /**
     * Init control bar
     */
    public Control(AnchorPane layout) {
        super();

        this.layout = layout;
        this.colorManager = ColorManager.getInstance();
        this.colorManager.addChangeColorListener(this::onChangeColor);

        this.initControl();
        this.initSongSlider();
        this.initVolumeSlider();
        this.initCoverArt();
        this.initSongInfo();
        this.initSongButtons();

        layout.getChildren().add(this);
    }

    /**
     * Init control layout
     */
    private void initControl() {
        this.setPrefSize(layout.getPrefWidth(), height);
        this.setLayoutY(layout.getPrefHeight() - this.getPrefHeight());
        this.updateColor(colorManager.getFinalColor());

        DropShadow shadow = new DropShadow(10, Color.web(Color.BLACK.toString(), 0.3));
        shadow.setWidth(0);
        shadow.setOffsetY(shadow.getRadius() * -1);
        this.setEffect(shadow);
    }

    /**
     * Set background color
     */
    private void updateColor(Color color) {
        LinearGradient gradient = new LinearGradient(
                0,
                0,
                1,
                0,
                true,
                CycleMethod.NO_CYCLE,
                new Stop(0, color),
                new Stop(1, Colors.LAYOUT_COLOR.getColor())
        );
        this.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(0, 0, 5, 5, false), Insets.EMPTY)));
    }

    /**
     * Init song slider
     */
    private void initSongSlider() {
        songSlider = new ControlSlider(267, 52, 265, this, true);
        this.getChildren().add(songSlider);
    }

    /**
     * Init volume slider
     */
    private void initVolumeSlider() {
        volumeSlider = new ControlSlider(686, height / 2 - ControlSlider.getRadius() / 2, 100, this, false);
        this.getChildren().add(volumeSlider);
    }

    /**
     * Init cover art
     */
    private void initCoverArt() {
        cover = new ImageView();
        cover.setImage(colorizeLogo(colorManager.getFinalColor()));
        cover.setFitWidth(height);
        cover.setFitHeight(height);
        cover.setPreserveRatio(true);
        this.getChildren().add(cover);
    }

    /**
     * Colorize logo
     */
    private Image colorizeLogo(Color color) {
        BufferedImage logo = SwingFXUtils.fromFXImage(PictureManager.loadImage(Pictures.LOGO.getIconName()), null);

        int width = logo.getWidth();
        int height = logo.getHeight();

        BufferedImage colored = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = colored.getGraphics();
        graphics.setColor(new java.awt.Color((int) color.getRed() * 255, (int) color.getGreen() * 255, (int) color.getBlue() * 255));
        graphics.drawRect(0, 0, width, height);
        graphics.drawImage(logo, 0, 0, null);
        graphics.dispose();

        return SwingFXUtils.toFXImage(colored, null);
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

    /**
     * Init song buttons
     */
    private void initSongButtons() {
        final double middleX = this.layout.getPrefWidth() / 2;

        playButton = new BigChangeableControlButton(middleX - BigChangeableControlButton.getSize() / 2,
                4,
                Pictures.PLAY_ICON,
                Pictures.PAUSE_ICON);
        this.getChildren().add(playButton);

        previousButton = new ControlButton(middleX - 30 - ControlButton.getSize(),
                10,
                Pictures.PREV_ICON);
        this.getChildren().add(previousButton);

        nextButton = new ControlButton(middleX + 30,
                10,
                Pictures.NEXT_ICON);
        this.getChildren().add(nextButton);

        repeatButton = new PressableControlButton(573,
                height / 2 - ControlButton.getSize() / 2,
                Pictures.REPEAT_ICON);
        this.getChildren().add(repeatButton);

        randomButton = new PressableControlButton(573 + ControlButton.getSize() + 12,
                height / 2 - ControlButton.getSize() / 2,
                Pictures.RANDOM_ICON);
        this.getChildren().add(randomButton);

        muteButton = new ChangeableControlButton(573 + ControlButton.getSize() * 2 + 12 * 2,
                height / 2 - ControlButton.getSize() / 2,
                Pictures.VOLUME_ICON,
                Pictures.MUTE_ICON);
        this.getChildren().add(muteButton);
    }

    /**
     * Event on change color
     */
    private void onChangeColor(Color color) {
        updateColor(color);
        cover.setImage(colorizeLogo(colorManager.getFinalColor()));
    }

    /**
     * Return song slider
     */
    public ControlSlider getSongSlider() {
        return songSlider;
    }

    /**
     * Return volume slider
     */
    public ControlSlider getVolumeSlider() {
        return volumeSlider;
    }

    /**
     * Return cover (playlist background)
     */
    public ImageView getCover() {
        return cover;
    }

    /**
     * Return song name
     */
    public void setSongName(String value) {
        TextUtils.setWidthText(songName, value, 250);
    }

    /**
     * Return artist name
     */
    public void setArtistName(String value) {
        TextUtils.setWidthText(artistName, value, 140);
    }

    /**
     * Return play/pause button
     */
    public BigChangeableControlButton getPlayButton() {
        return playButton;
    }

    /**
     * Return previous button
     */
    public ControlButton getPreviousButton() {
        return previousButton;
    }

    /**
     * Return next button
     */
    public ControlButton getNextButton() {
        return nextButton;
    }

    /**
     * Return repeat button
     */
    public PressableControlButton getRepeatButton() {
        return repeatButton;
    }

    /**
     * Return random button
     */
    public PressableControlButton getRandomButton() {
        return randomButton;
    }

    /**
     * Return mute button
     */
    public ChangeableControlButton getMuteButton() {
        return muteButton;
    }
}
