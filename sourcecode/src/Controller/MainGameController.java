package Controller;

import javafx.fxml.FXML;

public class MainGameController extends BaseController{
    @FXML
    public void initialize(){
        //PHUC
        //TODO: 
        //Set up initial gem count for each square (5 small gems each, 1 big gem in each Quan)
        //Set up initial images for all squares
        //Initialize scores to 0
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
    
    public void updateScoreUI(){
        //Hieu Anh
        //TODO: Based on the param, Update the score Label for the given player
    }
}
