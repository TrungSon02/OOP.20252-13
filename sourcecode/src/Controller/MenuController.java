package Controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class MenuController{
    private final SceneNavigator navigator = SceneNavigator.getInstance();

    @FXML
    public void showInstruction(ActionEvent event){
        //PHUC
        //Call loadScene and pass in instruction.fxml path
        navigator.loadScene("/fxml/instruction.fxml", event);
    }

    @FXML
    public void goPlay(ActionEvent event) {
        navigator.goPlay(event);
    }

    @FXML
    public void exit() {
        navigator.exit();
    }
}
