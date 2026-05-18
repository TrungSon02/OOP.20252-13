package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;



public class MainGameController extends BaseController{

    @FXML private Label labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5;
    @FXML private Label labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11;   
    @FXML private Label scoreP1, scoreP2;
    @FXML private Rectangle overlay1, overlay2, overlay3, overlay4, overlay5, overlay7, overlay8, overlay9, overlay10, overlay11;
    @FXML private Arc overlay0, overlay6;

    private int state = 1; 
    //state = 1: light up all squares
    //state = 2: select direction
    //state = 3: move
    private Label[] allCells;
    private Shape[] allOverlays;

    @FXML
    public void initialize() {
        allCells = new Label[]{

                labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5, 
                labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11
            };

        allOverlays = new Shape[]{
            overlay0, overlay1, overlay2, overlay3, overlay4, overlay5,
            overlay6, overlay7, overlay8, overlay9, overlay10, overlay11
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
        //TODO: Handle player input : Select a square -> then select direction
        //NOTE: Check player's turn for the available squares
        //After getting input, call onPlayerMove(square, direction)
        //called whenever player clicks
        //need to check the state of game
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

    private void setYellowSquare(Shape shape){
        shape.setFill(Color.rgb(255, 255, 0, 0.15)); 
        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(50);
        glow.setSpread(0.7);
        shape.setEffect(glow);
    }

    private void setRedSquare(Shape shape){
        shape.setFill(Color.rgb(255, 0, 0, 0.15)); 
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(50);
        glow.setSpread(0.7);
        shape.setEffect(glow);
    }

    private void resetSquare(Shape shape){
        shape.setFill(Color.TRANSPARENT);
        shape.setEffect(null);
    }

    public boolean checkState1(){
        
    }
    public boolean checkState2(){
        
    }

    public void convertToID(){

    }
    public void highlightMoveOption(int idx){
        setRedSquare(allOverlays[idx]);
        
    }

    public void highlightAvailableSquare(Shape shape,int currentPlayer){
        //NAM
        //TODO: Check which player's turn to highlight the correct square
        if(currentPlayer == 0){
            for(int i = 1; i<= 5;i++){
                setYellowSquare(allOverlays[i]);
            }
        }else {
            for(int i = 7; i<= 11;i++){
                setYellowSquare(allOverlays[i]);
            }
        }
    }

}