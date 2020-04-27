package relevant_craft.vento.media_player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import relevant_craft.vento.media_player.gui.main.MainGui;

public class VENTO extends Application {

    @Override
    public void start(Stage stage) {
        MainGui mainGui = new MainGui(stage);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle("Media Player");
        stage.setScene(new Scene(mainGui.getRoot()));
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
