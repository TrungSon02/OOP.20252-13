package Controller;

import java.util.List;

import Model.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.util.Duration;

public class MainGameController extends BaseController {

    @FXML private Label labelPlayer1, labelPlayer2;

    @FXML private Label labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5;
    @FXML private Label labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11;   
    @FXML private Label scoreP1, scoreP2;
    @FXML private ImageView avatarP1, avatarP2;
    @FXML private Label[] allCells;

    private Game game;
    private static final Duration STEP_DELAY = Duration.millis(1000); // tweak speed here

    @FXML
    public void initialize() {
        this.game = new Game();
        allCells = new Label[]{
                labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5, 
                labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11
            };

        for (int i = 0; i <= 11; i++) {
                if (allCells[i] != null) {
                    allCells[i].setText("5");
                }
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
}
