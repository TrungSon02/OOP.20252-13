package Controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BaseController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void loadScene(){
        //PHUC
        //TODO: Get a String input param (the path to a fxml file). Load the scene + Load the css (same command as in App.java)
    }

    public void goHome(){
        //PHUC
        //TODO: 1 line: Use the loadScene method above to load the menu.fxml scene
    }

    public void goPlay(){
        //PHUC
        //TODO: 1 line: Use the loadScene method above to load the main_game.fxml scene
    }

    public void exit(){
        //PHUC
        //TODO: Exit the game (Like alt f4). (Watch the Code Bro YT vid for the sample code for exiting game)
    }

}
