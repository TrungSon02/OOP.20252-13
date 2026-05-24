package Controller;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class BaseController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void loadScene(String fxmlPath, ActionEvent event){
        //PHUC
        //Get a String input param (the path to a fxml file). Load the scene + Load the css (same command as in App.java)
        try{
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            String css = Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm();
            scene.getStylesheets().add(css);

            stage.hide();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.show();

        } catch (IOException e){
            System.err.println("Fail to find FXML file: ");
            e.printStackTrace();
        }
    }

    @FXML
    public void goHome(ActionEvent event){
        //PHUC
        //1 line: Use the loadScene method above to load the menu.fxml scene
        loadScene("/fxml/menu.fxml", event);
    }

    @FXML
    public void goPlay(ActionEvent event){
        //PHUC
        //1 line: Use the loadScene method above to load the main_game.fxml scene
        loadScene("/fxml/main_game.fxml", event);
    }

    @FXML
    public void exit(){
        //PHUC
        //Exit the game (Like alt f4). (Watch the Code Bro YT vid for the sample code for exiting game)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to quit the game?");
        alert.setContentText("Any unsaved progress will be lost.");

        ButtonType buttonTypeExit = new ButtonType("Quit");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(buttonTypeExit, buttonTypeCancel);

        java.util.Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeExit) {
            Platform.exit();
            System.exit(0);
        }
    }

}
