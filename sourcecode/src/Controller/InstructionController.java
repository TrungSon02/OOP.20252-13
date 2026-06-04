package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InstructionController extends BaseController {

    @FXML private Label instructionLabel;

    @FXML // Overriding JavaFX's interface
    public void initialize() {
        // PHUC
        setInstructionContent(); 
    }

    private void setInstructionContent() {
        String content = "1. Setup: The board consists of 10 small squares (Citizen fields) and 2 half-circles at the ends (Mandarin squares).\n\n"
            + "2. Selection: On your turn, valid squares with pieces are highlighted in YELLOW. Click a square to select it (Click that square again to deselect).\n\n"
            + "3. Direction: Upon selection, its 2 adjacent squares turn YELLOW. Click one of them to choose your movement direction.\n\n"
            + "4. Chain Moves: Distribute gems one by one. If your last gem lands next to a non-empty square, scoop them up and continue.\n\n"
            + "5. Capturing: If your last gem lands next to an empty square, you capture all pieces in the square immediately following it.\n\n"
            + "6. Objective: The game ends when both Mandarin squares are empty. Collect more gems than your opponent to win!";

        if (instructionLabel != null) {
            instructionLabel.setText(content);
        }
    }
}