package relevant_craft.vento.media_player.gui.main;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import relevant_craft.vento.media_player.gui.main.elements.control.Control;
import relevant_craft.vento.media_player.gui.main.elements.navigation.Navigation;
import relevant_craft.vento.media_player.gui.main.elements.playlist.Playlist;
import relevant_craft.vento.media_player.gui.main.elements.title.Title;
import relevant_craft.vento.media_player.gui.main.elements.visualization.Visualization;
import relevant_craft.vento.media_player.manager.color.Colors;

public class MainGui extends Pane {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 550;

    private static MainGui instance;

    private final Stage stage;
    private final AnchorPane layout;
    private final Title title;
    private final Control control;
    private final Navigation navigation;
    private final Visualization visualization;
    private final Playlist playlist;

    /**
     * Init main window
     */
    public MainGui(Stage primaryStage) {
        //create region
        super();
        this.setPrefSize(WIDTH + 20, HEIGHT + 20);
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        instance = this;
        this.stage = primaryStage;

        //init main gui
        this.layout = initLayout();
        this.title = new Title(layout, stage);
        this.control = new Control(layout);
        this.navigation = new Navigation(stage, layout, title, control);
        this.visualization = new Visualization(layout, title, navigation);
        this.playlist = new Playlist(stage, layout, title, control, navigation, visualization);

        this.getChildren().setAll(layout);
    }

    /**
     * Init main window layout
     */
    private AnchorPane initLayout() {
        AnchorPane layout = new AnchorPane();
        layout.setPrefWidth(WIDTH);
        layout.setPrefHeight(HEIGHT);
        layout.setBackground(new Background(new BackgroundFill(Colors.LAYOUT_COLOR.getColor(), new CornerRadii(5), Insets.EMPTY)));
        layout.setEffect(new DropShadow(10, Color.web(Color.BLACK.toString(), 0.75)));
        layout.setLayoutX(this.getPrefWidth() / 2 - layout.getPrefWidth() / 2);
        layout.setLayoutY(this.getPrefHeight() / 2 - layout.getPrefHeight() / 2);

        return layout;
    }

    /**
     * Return the singleton MainGui instance
     */
    public static MainGui getInstance() {
        return instance;
    }

    /**
     * Return stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Return the layout of main window
     */
    public AnchorPane getLayout() {
        return layout;
    }

    /**
     * Return title bar
     */
    public Title getTitle() {
        return title;
    }

    /**
     * Return control bar
     */
    public Control getControl() {
        return control;
    }

    /**
     * Return navigation bar
     */
    public Navigation getNavigation() {
        return navigation;
    }

    /**
     * Return visualization bar
     */
    public Visualization getVisualization() {
        return visualization;
    }

    /**
     * Return playlist bar
     */
    public Playlist getPlaylist() {
        return playlist;
    }
}
