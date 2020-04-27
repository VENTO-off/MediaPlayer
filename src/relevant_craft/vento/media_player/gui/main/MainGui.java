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
    private static final String navigationColor = "#272727";

    private static MainGui instance;
    private double xOffset;
    private double yOffset;
    private Stage stage;
    private Pane root;
    private AnchorPane layout;
    private Pane title;
    private Pane control;
    private Pane navigation;

    public MainGui(Stage primaryStage) {
        instance = this;
        stage = primaryStage;

        //create region
        root = new Pane();
        root.setPrefSize(WIDTH + 20, HEIGHT + 20);

        //init main gui
        initLayout();
        initTitle();
        initControl();
        initNavigation();

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

    private void initLayout() {
        layout = new AnchorPane();
        layout.setPrefWidth(WIDTH);
        layout.setPrefHeight(HEIGHT);
        layout.setStyle(
                "-fx-background-color: " + layoutColor + ";" +
                        "-fx-background-radius: 5px;"
        );
        layout.setEffect(new DropShadow(10, Color.web(Color.BLACK.toString(), 0.75)));
        layout.setLayoutX(root.getPrefWidth() / 2 - layout.getPrefWidth() / 2);
        layout.setLayoutY(root.getPrefHeight() / 2 - layout.getPrefHeight() / 2);
    }

    private void initTitle() {
        title = new Pane();
        title.setPrefSize(layout.getPrefWidth(), 30);
        title.setStyle(
                "-fx-background-color: " + titleColor + ";" +
                        "-fx-background-radius: 5px 5px 0px 0px;"
        );
        title.setOnMousePressed(this::onDrag);
        title.setOnMouseDragged(this::onRelease);

        DropShadow shadow = new DropShadow(5, Color.web(Color.BLACK.toString(), 0.5));
        shadow.setWidth(0);
        shadow.setOffsetY(shadow.getRadius());
        title.setEffect(shadow);

        layout.getChildren().add(title);
    }

    private void initControl() {
        //TODO pass average color
        final String averageColor = "#6b80c1";

        control = new Pane();
        control.setPrefSize(layout.getPrefWidth(), 70);
        control.setStyle(
                "-fx-background-color: linear-gradient(to right, " + averageColor + ", " + layoutColor + ");" +
                        "-fx-background-radius: 0px 0px 5px 5px;"
        );
        control.setLayoutY(layout.getPrefHeight() - control.getPrefHeight());

        DropShadow shadow = new DropShadow(10, Color.web(Color.BLACK.toString(), 0.3));
        shadow.setWidth(0);
        shadow.setOffsetY(shadow.getRadius() * -1);
        control.setEffect(shadow);

        layout.getChildren().add(control);
    }

    private void initNavigation() {
        navigation = new Pane();
        navigation.setPrefWidth(173);
        navigation.setPrefHeight(layout.getPrefHeight() - title.getPrefHeight() - control.getPrefHeight());
        navigation.setStyle(
                "-fx-background-color: " + navigationColor + ";"
        );
        navigation.setLayoutY(title.getPrefHeight());

        layout.getChildren().add(0, navigation);
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
