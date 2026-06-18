package Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.Objects;

public class SceneNavigator {
    private static final SceneNavigator instance = new SceneNavigator();

    private SceneNavigator() {}

    public static SceneNavigator getInstance() {
        return instance;
    }

    public void loadScene(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            String css = Objects.requireNonNull(getClass().getResource("/asset/css/application.css")).toExternalForm();
            scene.getStylesheets().add(css);

            stage.hide();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e) {
            System.err.println("Fail to find FXML file: ");
            e.printStackTrace();
        }
    }

    public void goHome(ActionEvent event) {
        loadScene("/fxml/menu.fxml", event);
    }

    public void goPlay(ActionEvent event) {
        loadScene("/fxml/main_game.fxml", event);
    }

    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to quit the game?");

        ButtonType buttonTypeExit = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeExit, buttonTypeCancel);

        java.util.Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeExit) {
            Platform.exit();
            System.exit(0);
        }
    }
}