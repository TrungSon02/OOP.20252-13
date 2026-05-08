package Controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class MenuController extends BaseController{
    @FXML
    public void showInstruction(ActionEvent event){
        //PHUC
        //TODO: Call loadScene and pass in instruction.fxml path
        loadScene("/fxml/instruction.fxml", event);
    }
}
