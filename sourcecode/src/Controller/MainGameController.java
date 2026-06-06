package Controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import Model.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;


public class MainGameController extends BaseController {
    @FXML private Label labelPlayer1, labelPlayer2;

    @FXML private Label labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5;
    @FXML private Label labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11;
    @FXML private Label scoreP0, scoreP1;
    @FXML private ImageView avatarP0, avatarP1;

    @FXML private Rectangle overlay1, overlay2, overlay3, overlay4, overlay5, overlay7, overlay8, overlay9, overlay10, overlay11;
    @FXML private Arc overlay0, overlay6;

    @FXML private StackPane cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5;
    @FXML private StackPane cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11;

    private Label[] allCellLabels;
    private Shape[] allOverlays;

    private Game game;
    private GemRenderer gemRenderer;
    private SquareHighlighter highlighter;
    private static final Duration STEP_DELAY = Duration.millis(700); // tweak speed here

    private int state = 1;
    private int selectedSquare = -1;

    @FXML
    public void initialize() {
        this.game = new Game();
        allCellLabels = new Label[]{
                labelCell0, labelCell1, labelCell2, labelCell3, labelCell4, labelCell5,
                labelCell6, labelCell7, labelCell8, labelCell9, labelCell10, labelCell11
            };

        allOverlays = new Shape[]{
            overlay0, overlay1, overlay2, overlay3, overlay4, overlay5,
            overlay6, overlay7, overlay8, overlay9, overlay10, overlay11
        };

        StackPane[] allCellPanes = new StackPane[]{
            cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5,
            cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11
        };
        gemRenderer = new GemRenderer(allCellPanes, allCellLabels);
        highlighter = new SquareHighlighter(allOverlays);

        for (int i = 0; i <= 11; i++) {
            allCellLabels[i].setText("5");
        }

        scoreP0.setText("Score: 0");
        scoreP1.setText("Score: 0");

        highlighter.highlightAvailableSquareState1(game.getAvailableSquares());
        initializePlayerData();


        Platform.runLater(() -> gemRenderer.renderAllGems());
    }

    private void initializePlayerData() {
        labelPlayer1.setText("Player " + game.getPlayerName(0));
        labelPlayer2.setText("Player " + game.getPlayerName(1));
        String avatarPathP0 = game.getPlayerAvatar(0);
        String avatarPathP1 = game.getPlayerAvatar(1);

        try {
            Image imgPlayer0 = new Image(getClass().getResourceAsStream(avatarPathP0));
            Image imgPlayer1 = new Image(getClass().getResourceAsStream(avatarPathP1));

            if (avatarP0 != null) avatarP0.setImage(imgPlayer0);
            if (avatarP1 != null) avatarP1.setImage(imgPlayer1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInput(MouseEvent event){
        Shape clickedShape = (Shape) event.getSource();
        String fxid = clickedShape.getId();
        int shapeID = convertStringToInt(fxid);

        if(state == 1){
            if(game.isValidSquareState1(shapeID)){
                highlighter.resetAllSquares();
                highlighter.highlightAvailableSquareState2(shapeID);
                selectedSquare = shapeID;
                state = 2;
            }
        }
        else if(state == 2){
            if(game.isValidDirection(selectedSquare, shapeID)){
                if(shapeID == selectedSquare){
                    state = 1;
                    highlighter.resetAllSquares();
                    highlighter.highlightAvailableSquareState1(game.getAvailableSquares());
                }else{
                    int direction = game.getDirection(selectedSquare, shapeID);
                    onPlayerMove(selectedSquare,direction);
                }
            }
        }
    }

    private void onPlayerMove(int startingSquare, int direction){
        state = -1;
        highlighter.resetAllSquares();
        List<Pair<Integer, Integer>> moveSequence = game.proccessingTurn(startingSquare, direction);
        animateMoves(moveSequence, () -> {
            updateScoreUI();

            List<Pair<Integer, Integer>> fillSequence = game.postTurnProcessing();
            animateMoves(fillSequence, () -> {
                updateScoreUI();
                if (game.isFinished()) {
                    postGameVisualEffect(() -> {
                        loadEndingScene();
                    });

                } else {
                    state = 1;
                    highlighter.highlightAvailableSquareState1(game.getAvailableSquares());
                }
            });
        });
    }

    private void loadEndingScene() {
        try {
            Stage currentStage = (Stage) scoreP0.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ending.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            String css = Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm();
            scene.getStylesheets().add(css);

            EndingController ending = loader.getController();
            int winnerID = game.getWinnerId();
            if(winnerID != -1){
                ending.displayWinner(game.getPlayerName(winnerID), game.getPlayerAvatar(winnerID));
            }
            else{
                ending.displayWinner("It's a Tie!", null); 
            }
            
            currentStage.hide();
            currentStage.setScene(scene);
            currentStage.setFullScreen(true);
            currentStage.setFullScreenExitHint("");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void animateMoves(List<Pair<Integer, Integer>> moves, Runnable onFinished) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < moves.size(); i++) {
            Pair<Integer, Integer> step = moves.get(i);
            int cellIndex = step.getKey();
            int newValue  = step.getValue();
            Duration when = STEP_DELAY.multiply(i + 1);

            timeline.getKeyFrames().add(new KeyFrame(when, e -> {
                if (allCellLabels[cellIndex] != null) { 
                    allCellLabels[cellIndex].setText(String.valueOf(newValue));
                }
                if (game.isBigCell(cellIndex) && newValue == 0) {
                    gemRenderer.removeBigGem(cellIndex);
                }
                gemRenderer.renderGemsInCell(cellIndex, newValue);
            }));
        }
        timeline.setOnFinished(e -> onFinished.run()); //When timeline finishes, run the code in {}
        timeline.play();
    }

    private void updateScoreUI(){
        scoreP0.setText("Score: " + game.getPlayerScore(0));
        scoreP1.setText("Score: " + game.getPlayerScore(1));
    }

    private int convertStringToInt(String str){
        str=str.replaceAll("[^0-9]", "");
        return Integer.parseInt(str);
    }

    private void postGameVisualEffect(Runnable onFinished){
        Timeline timeline = new Timeline();
        
        timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(1), e -> {
            for(int i = 1; i <= 5; i++){
                allCellLabels[i].setText("0");
                gemRenderer.renderGemsInCell(i, 0);
            }
            updateScoreUI();
        }));
        timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(2), e -> {
            for(int i = 7; i <= 11; i++){
                allCellLabels[i].setText("0");
                gemRenderer.renderGemsInCell(i, 0);
            }
            updateScoreUI();
        }));

        
        for(int i = 0; i < 6; i++){
            final double on  = 3 + i * 1.0; 
            final double off = 3.5 + i * 1.0; 
            timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(on), e -> {
                if(game.getWinnerId() == 0){
                    scoreP0.setTextFill(Color.GOLD);
                }
                else if(game.getWinnerId() == 1){
                    scoreP1.setTextFill(Color.GOLD);
                }
            }));
            timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(off), e -> {
                if(game.getWinnerId() == 0){
                    scoreP0.setTextFill(Color.WHITE);
                }
                else if(game.getWinnerId() == 1){
                    scoreP1.setTextFill(Color.WHITE);
                }
            }));
        }

        timeline.setOnFinished(e -> onFinished.run());
        timeline.play();
    }
}