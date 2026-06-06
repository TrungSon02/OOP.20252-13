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
        String content = "1. Setup: The board consists of 10 small squares and 2 half-circles at the ends (Mandarin).\n\n"
            + "2. Selection: On your turn, valid squares are highlighted in YELLOW. Click a square to select it (Click that square again to deselect).\n\n"
            + "3. Direction: Upon selection, its 2 adjacent cells turn YELLOW. Click one of them to choose your movement direction.\n\n"
            + "4. Chain Moves: Distribute gems one by one. If your last gem lands next to a non-empty square, scoop them up and continue.\n\n"
            + "5. Capturing: If your last gem lands next to an empty cell, then a cell with gems, you capture those gems and add them to your score. Capturing can happen in a streak\n\n"
            + "6. Objective: The game ends when both Mandarin squares are empty. Collect more gems than your opponent to win!";

        if (instructionLabel != null) {
            instructionLabel.setText(content);
        }
    }
}