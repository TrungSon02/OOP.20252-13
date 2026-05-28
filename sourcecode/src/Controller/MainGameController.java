package Controller;

import java.util.List;

import Model.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MainGameController extends BaseController {

    @FXML private Label labelPlayer1, labelPlayer2;

    @FXML private Label labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5;
    @FXML private Label labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11;   
    @FXML private Label scoreP1, scoreP2;
    @FXML private ImageView avatarP1, avatarP2;
    @FXML private Label[] allCells;
    @FXML private Shape[] allOverlays;

    private Game game;
    private static final Duration STEP_DELAY = Duration.millis(1000); // tweak speed here
    @FXML private Rectangle overlay1, overlay2, overlay3, overlay4, overlay5, overlay7, overlay8, overlay9, overlay10, overlay11;
    @FXML private Arc overlay0, overlay6;

    private int state = 0;
    private int selectedSquare = -1;

    @FXML
    public void initialize() {
        this.game = new Game();
        allCells = new Label[]{
                labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5, 
                labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11
            };

        allOverlays = new Shape[]{
            overlay0, overlay1, overlay2, overlay3, overlay4, overlay5,
            overlay6, overlay7, overlay8, overlay9, overlay10, overlay11
        };
        

        for (int i = 0; i <= 11; i++) {
            allCells[i].setText("5");
        }
    
        scoreP1.setText("Score: 0");
        scoreP2.setText("Score: 0");
        initializePlayerData();
    }

    private void initializePlayerData() {
        labelPlayer1.setText("Player " + game.getPlayers()[0].getName());
        labelPlayer2.setText("Player " + game.getPlayers()[1].getName());
        String avatarPathP1 = game.getPlayers()[0].getAvatar();
        String avatarPathP2 = game.getPlayers()[1].getAvatar();
        
        try {
            Image imgPlayer1 = new Image(getClass().getResourceAsStream(avatarPathP1));
            Image imgPlayer2 = new Image(getClass().getResourceAsStream(avatarPathP2));
            
            if (avatarP1 != null) avatarP1.setImage(imgPlayer1);
            if (avatarP2 != null) avatarP2.setImage(imgPlayer2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInput(MouseEvent event){
        //NAM
        //TODO: Handle player input : Select a square -> then select direction
        //NOTE: Check player's turn for the available squares
        //After getting input, call onPlayerMove(square, direction)
        //called whenever player clicks
        //need to check the state of game
        Shape clickedShape = (Shape) event.getSource();
        String fxid = clickedShape.getId();
        int shapeID = convertStringToInt(fxid);
        System.out.println(shapeID);

        /* 
        if(state == 1){
            if(checkState1(currentPlayer, shapeID)){
                state = 2;
                resetAllSquares();
                highlightAvailableSquareState2(shapeID);
                selectedSquare = shapeID; 
            }
        }
        if(state == 2){
            if(checkState2(shapeID)){
                if(shapeID == selectedSquare){
                    state = 1;
                    resetAllSquares();
                    highlightAvailableSquareState1(clickedShape, currentPlayer);
                }else{
                    int direction = shapeID - selectedSquare;
                    if(direction == -11){
                       direction = 1; 
                    }
                    onPlayerMove(selectedSquare,direction);
                    resetAllSquares();
                    state = 0;
                }
            }
            
        }
        */
        
    }
    public void onPlayerMove(int selectedSquare, int direction){
        
    }

    public void animateMoves(List<Pair<Integer, Integer>> moves) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < moves.size(); i++) {
            Pair<Integer, Integer> step = moves.get(i);
            int cellIndex = step.getKey();
            int newValue  = step.getValue();
            Duration when = STEP_DELAY.multiply(i + 1);

            timeline.getKeyFrames().add(new KeyFrame(when, e -> {
                if (allCells[cellIndex] != null) {  // this line is a safety check
                    allCells[cellIndex].setText(String.valueOf(newValue));// actual action here
                }
            }));
        }
        timeline.play();
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

    public void resetAllSquares(){
        for(Shape shape : allOverlays){
            resetSquare(shape);
        }
    }
    //TODO: check squares state
    public boolean checkState1(int currentPlayer, int shapeID){
        if(currentPlayer == 0){
            if(shapeID < 1 || shapeID > 5){
                return false;
            }
        }else if(currentPlayer == 1){
            if(shapeID < 7 || shapeID > 11){
                return false;
            }
        }
        return true;
    }
    public boolean checkState2(int shapeID){
        int LeftNeighbour = (shapeID + 1 + 12) % 12;
        int RightNeighbour = (shapeID - 1 + 12) % 12;
        if(shapeID == LeftNeighbour || shapeID == RightNeighbour || shapeID == selectedSquare){
            return true;
        }
        return false;
    }


    public void highlightAvailableSquareState1(Shape shape,int currentPlayer){
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

    public void highlightAvailableSquareState2(int shapeID){
        setRedSquare(allOverlays[shapeID]);
        int LeftNeighbour = (shapeID + 1 + 12) % 12;
        int RightNeighbour = (shapeID - 1 + 12) % 12;
        setYellowSquare(allOverlays[LeftNeighbour]);
        setYellowSquare(allOverlays[RightNeighbour]);

    }

    public static int convertStringToInt(String str){
        str=str.replaceAll("[^0-9]", "");
        
        return Integer.parseInt(str);
    }
}