package relevant_craft.vento.media_player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import relevant_craft.vento.media_player.gui.main.MainGui;
import relevant_craft.vento.media_player.manager.player.PlayerManager;

public class VENTO extends Application {

    public static final String version = "1.0";

    @Override
    public void start(Stage stage) {
        MainGui mainGui = new MainGui(stage);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Media Player");
        stage.setScene(new Scene(mainGui));
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.show();

        PlayerManager playerManager = new PlayerManager();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
