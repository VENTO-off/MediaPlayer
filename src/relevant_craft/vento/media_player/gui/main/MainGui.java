package relevant_craft.vento.media_player.gui.main;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainGui {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 550;

    private static final String layoutColor = "#303030";
    private static final String titleColor = "#232323";

    private static MainGui instance;
    private double xOffset;
    private double yOffset;
    private Stage stage;
    private Pane root;
    private AnchorPane layout;
    private Pane title;

    public MainGui(Stage primaryStage) {
        instance = this;
        stage = primaryStage;

        //create region
        root = new Pane();
        root.setPrefSize(WIDTH + 20, HEIGHT + 20);

        layout = initLayout();

        title = initTitle();
        layout.getChildren().add(title);

        root.getChildren().setAll(layout);
    }

    private void onDrag(MouseEvent e) {
        xOffset = stage.getX() - e.getScreenX();
        yOffset = stage.getY() - e.getScreenY();
    }

    private void onRelease(MouseEvent e) {
        stage.setX(e.getScreenX() + xOffset);
        stage.setY(e.getScreenY() + yOffset);
    }

    private AnchorPane initLayout() {
        AnchorPane layout = new AnchorPane();
        layout.setPrefWidth(WIDTH);
        layout.setPrefHeight(HEIGHT);
        layout.setStyle(
                "-fx-background-color: " + layoutColor + "; -fx-background-radius: 5px;"
        );
        layout.setEffect(new DropShadow(10, Color.web(Color.BLACK.toString(), 0.75)));
        layout.setLayoutX(root.getPrefWidth() / 2 - layout.getPrefWidth() / 2);
        layout.setLayoutY(root.getPrefHeight() / 2 - layout.getPrefHeight() / 2);

        return layout;
    }

    private Pane initTitle() {
        Pane title = new Pane();
        title.setPrefSize(WIDTH, 30);
        title.setStyle(
                "-fx-background-color: " + titleColor + "; -fx-background-radius: 5px 5px 0px 0px;"
        );
        title.setOnMousePressed(this::onDrag);
        title.setOnMouseDragged(this::onRelease);
        DropShadow shadow = new DropShadow(10, Color.web(Color.BLACK.toString(), 0.75));
        title.setEffect(shadow);


        return title;
    }

    public static MainGui getInstance() {
        return instance;
    }

    public Pane getRoot() {
        return root;
    }

    public AnchorPane getLayout() {
        return layout;
    }
}
