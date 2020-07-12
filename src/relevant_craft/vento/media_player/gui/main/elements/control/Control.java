package relevant_craft.vento.media_player.gui.main.elements.control;

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
        //TODO remove test cover
        cover.setImage(PictureManager.loadImage(Pictures.TEST_COVER.getIconName()));
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
        //TODO remove test value
        songName.setText("По проводам");
        this.getChildren().add(songName);

        artistName = new Text();
        artistName.setX(cover.getFitWidth() + 10);
        artistName.setY(39 + fixY);
        artistName.setFill(Color.WHITE);
        artistName.setFont(FontManager.loadFont(Fonts.SEGOE_UI_BOLD.getFontName(), 13));
        //TODO remove test value
        artistName.setText("ChipaChip");
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
    public Text getSongName() {
        return songName;
    }

    /**
     * Return artist name
     */
    public Text getArtistName() {
        return artistName;
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
