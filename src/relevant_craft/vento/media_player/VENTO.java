package relevant_craft.vento.media_player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import relevant_craft.vento.media_player.gui.main.MainGui;
import relevant_craft.vento.media_player.manager.color.ColorManager;
import relevant_craft.vento.media_player.manager.directory.DirectoryManager;
import relevant_craft.vento.media_player.manager.player.PlayerManager;
import relevant_craft.vento.media_player.manager.settings.SettingsManager;

public class VENTO extends Application {
    public static final String PLAYER   = "Richen";
    public static final String VERSION  = "1.0";

    @Override
    public void start(Stage stage) {
        //init settings manager
        new SettingsManager();

        //init directory manager
        new DirectoryManager();

        //init color manager
        new ColorManager();

        //init gui
        MainGui mainGui = new MainGui(stage);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle(PLAYER + " v" + VERSION);
        stage.setScene(new Scene(mainGui));
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.show();

        //init player manager
        new PlayerManager();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
