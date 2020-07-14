package relevant_craft.vento.media_player.gui.main.elements.visualization;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class VUMeter extends Pane {
    private static final double HEIGHT = 83;

    private final ImageView background;
    private final Line arrow;
    private final Rotate angle;

    private final int[][] dbLevels = new int[][] {
            //db level => angle
            {-20,   0},
            {-10,   13},
            {-7,    23},
            {-5,    32},
            {-3,    45},
            {-2,    51},
            {-1,    58},
            { 0,    71},
            {+1,    81},
            {+2,    91},
            {+3,    104}
    };

    /**
     * Init VU Meter
     */
    public VUMeter(double positionX, double positionY) {
        super();

        //render background
        this.background = new ImageView();
        this.background.setImage(PictureManager.loadImage(Pictures.VU_METER.getIconName()));
        this.getChildren().add(this.background);

        //init layout
        this.setPrefWidth(this.background.getImage().getWidth());
        this.setPrefHeight(this.background.getImage().getHeight());
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);

        //render arrow
        this.arrow = new Line();
        this.arrow.setStartX(73);
        this.arrow.setStartY(73);
        this.arrow.setEndX(27);
        this.arrow.setEndY(36);
        this.getChildren().add(this.arrow);

        //init angle
        this.angle = new Rotate();
        this.angle.setPivotX(this.arrow.getStartX());
        this.angle.setPivotY(this.arrow.getStartY());
        this.arrow.getTransforms().add(this.angle);
    }

    /**
     * Set VU level
     */
    public void setLevel(double db) {
        if (db < -20) {
            db = -20;
        } else if (db > 3) {
            db = 3;
        }

        for (int i = 0; i < dbLevels.length - 1; i++) {
            int[] currentValues = dbLevels[i];
            int[] nextValues = dbLevels[i + 1];

            int dbMin = currentValues[0];
            int dbMax = nextValues[0];
            int angleMin = currentValues[1];
            int angleMax = nextValues[1];

            if (db >= dbMin && db <= dbMax) {
                int dbDelta = Math.abs(dbMin) - Math.abs(dbMax);
                double dbRelative = Math.abs(dbMin) - Math.abs(db);
                double dbPercent = dbRelative * 100 / dbDelta;

                int angleDelta = angleMax - angleMin;
                double angleRelative = dbPercent * angleDelta / 100;

                this.angle.setAngle(angleMin + angleRelative);
                return;
            }
        }
    }

    /**
     * Get height
     */
    public static double getVUHeight() {
        return HEIGHT;
    }
}
