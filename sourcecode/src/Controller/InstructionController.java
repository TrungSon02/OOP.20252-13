package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InstructionController extends BaseController{

    @FXML private Label instructionLabel;

    @FXML //Overiding JavaFX's interface
    public void initialize(){
        //PHUC
        //TODO: Change the instruction text
        setInstructionContent(); 
    }

    private void setInstructionContent() {
    String content = "WELCOME TO Ô ĂN QUAN\n\n"
        + "1. Setup: The board has 10 small cells (Rice fields) and 2 big cells (Castles).\n\n"
        + "2. Distribution: Pick gems from one of your cells and distribute them.\n\n"
        + "3. Capturing: Capture gems if your last gem falls before an empty cell.\n\n"
        + "4. Chain Moves: If the next cell is NOT empty, continue your turn.\n\n"
        + "5. Objective: Collect more gems than your opponent to win!";

        if (instructionLabel != null) {
            instructionLabel.setText(content);
        }
    }
}