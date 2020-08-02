package relevant_craft.vento.media_player.gui.main.elements.playlist;

import javafx.animation.Animation;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;

import java.util.Collections;

public class Loader extends Pane {
    private LoaderAnimation animation;
    private ImageView loader;
    private Text text;
    private String currentText;
    private int dots;
    private Thread textAnimation;

    /**
     * Init loader area
     */
    public Loader(double width, double height, double positionX, double positionY) {
        super();

        this.dots = 1;

        this.setPrefWidth(width);
        this.setPrefHeight(height);
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);

        this.initLoaderAnimation();
        this.initLoaderText();

        this.setVisible(false);
    }

    /**
     * Init loader animation
     */
    private void initLoaderAnimation() {
        animation = new LoaderAnimation(this.getPrefWidth(), this.getPrefHeight());

        loader = animation.getImageView();
        this.getChildren().add(loader);
    }

    /**
     * Init loader text
     */
    private void initLoaderText() {
        text = new Text();
        text.setY(loader.getLayoutY() + loader.getImage().getHeight() + 30);
        text.setWrappingWidth(this.getPrefWidth());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.WHITE);
        text.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 14));
        this.getChildren().add(text);
    }

    /**
     * Show loader area
     */
    public void show() {
        animation.play();

        textAnimation = new Thread(() -> {
            while (true) {
                renderText();

                dots++;
                if (dots > 3) {
                    dots = 1;
                }

                sleep(500);
            }
        });
        textAnimation.start();

        this.setVisible(true);
    }

    /**
     * Hide loader area
     */
    public void hide() {
        this.setVisible(false);

        currentText = null;
        if (animation != null && animation.getStatus() == Animation.Status.RUNNING) {
            animation.stop();
        }
        if (textAnimation != null && textAnimation.isAlive()) {
            textAnimation.stop();
        }
    }

    /**
     * Update loader text
     */
    public void updateText(String value) {
        currentText = value;
        renderText();
    }

    /**
     * Render text
     */
    private void renderText() {
        text.setText((currentText == null ? "" : currentText) + String.join("", Collections.nCopies(dots, ".")));
    }

    /**
     * Sleep thread
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {}
    }
}
