package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class MainGameController extends BaseController{

    @FXML private Label labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5;
    @FXML private Label labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11;   
    @FXML private Label scoreP1, scoreP2;

    private Label[] allCells;

    @FXML
    public void initialize() {
        allCells = new Label[]{
                labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5, 
                labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11
            };

        for (int i = 1; i <= 11; i++) {
                if (i == 6) continue; 
                if (allCells[i] != null) {
                    allCells[i].setText("5");
                }
            }
        
        if (labelCell0 != null) labelCell0.setText("1");
        if (labelCell6 != null) labelCell6.setText("1");

        scoreP1.setText("Score: 0");
        scoreP2.setText("Score: 0");
        
    }

    public void getInput(){
        //NAM
        //TODO: Handle player input via one of two methods:
        //First method: Drag and Drop
        //Second method: Select a square -> then select direction
        //NOTE: Check player's turn for the available squares
        //After getting input, call onPlayerMove(square, direction)
    }

    public void onPlayerMove(){
        //Son 
        //TODO: Already done on paper, write it back here
    }

    public void animateMove(){
        //Hieu Anh 
        //TODO 1: Make a new folder in image, try to make the collection of images containing 1,2,3,4,... gems (make several collection)
        //TODO 2: Based on the list of move (parameter of this method) change the images of each square 
        //TODO 3: Add a number indicating the total gems on each square then update that number after each move
    }
    
    public void updateScoreUI(int playerIndex, int score){
        //Hieu Anh
        //TODO: Based on the param, Update the score Label for the given player
            if (playerIndex == 0) {
            scoreP1.setText("Score: " + score);
        } else {
            scoreP2.setText("Score: " + score);
        }
    }
}
