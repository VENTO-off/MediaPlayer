package relevant_craft.vento.media_player.gui.main;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import relevant_craft.vento.media_player.gui.main.elements.TitleButton;

public class MainGui {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 550;

    private static final String layoutColor = "#303030";
    private static final String titleColor = "#232323";
    private static final String navigationColor = "#272727";

    private static MainGui instance;
    private double xOffset;
    private double yOffset;
    private final Stage stage;
    private final Pane root;
    private AnchorPane layout;
    private Pane title;
    private Pane control;
    private Pane navigation;
    private Pane visualization;
    private TitleButton close;
    private TitleButton minimize;

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
        initVisualization();

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
        title.setEffect(createShadow(5, 0.3, false));

        minimize = new TitleButton(
                title.getPrefWidth() - TitleButton.getSize() - 46,
                title.getPrefHeight() / 2 - TitleButton.getSize() / 2,
                "minimize icon.png");
        title.getChildren().add(minimize);

        close = new TitleButton(
                title.getPrefWidth() - TitleButton.getSize() - 16,
                title.getPrefHeight() / 2 - TitleButton.getSize() / 2,
                "close_icon.png");
        title.getChildren().add(close);

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
        control.setEffect(createShadow(10, 0.3, true));

        layout.getChildren().add(control);
    }

    private DropShadow createShadow(double radius, double opacity, boolean negativeOffsetY) {
        DropShadow shadow = new DropShadow(radius, Color.web(Color.BLACK.toString(), opacity));
        shadow.setWidth(0);
        shadow.setOffsetY(shadow.getRadius() * (negativeOffsetY ? -1 : 1));

        return shadow;
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

    private void initVisualization() {
        visualization = new Pane();
        visualization.setPrefSize(594, 129);
        visualization.setStyle(
                "-fx-background-color: " + navigationColor + ";"
        );
        visualization.setLayoutX((layout.getPrefWidth() - navigation.getPrefWidth()) / 2 - visualization.getPrefWidth() / 2 + navigation.getPrefWidth());
        visualization.setLayoutY(title.getPrefHeight());
        visualization.setEffect(new DropShadow(5, Color.web(Color.BLACK.toString(), 0.50)));

        Line separator = new Line();
        separator.setStroke(Color.web("#FFFFFF", 0.1));
        separator.setStrokeWidth(2);
        separator.setStartX(0);
        separator.setStartY(visualization.getPrefHeight() - 27);
        separator.setEndX(visualization.getPrefWidth());
        separator.setEndY(separator.getStartY());
        visualization.getChildren().add(separator);

        layout.getChildren().add(0, visualization);
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
