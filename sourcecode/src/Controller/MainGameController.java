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
    @FXML private Label scoreP1, scoreP2;
    @FXML private ImageView avatarP1, avatarP2;

    @FXML private Rectangle overlay1, overlay2, overlay3, overlay4, overlay5, overlay7, overlay8, overlay9, overlay10, overlay11;
    @FXML private Arc overlay0, overlay6;

    @FXML private StackPane cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5;
    @FXML private StackPane cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11;

    private Label[] allCells;
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
        allCells = new Label[]{
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
        gemRenderer = new GemRenderer(allCellPanes, allCells);
        highlighter = new SquareHighlighter(allOverlays);

        for (int i = 0; i <= 11; i++) {
            allCells[i].setText("5");
        }

        scoreP1.setText("Score: 0");
        scoreP2.setText("Score: 0");

        highlighter.highlightAvailableSquareState1(game.getAvailableSquares());
        initializePlayerData();


        Platform.runLater(() -> gemRenderer.renderAllGems());
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

    public void onPlayerMove(int startingSquare, int direction){
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
            Stage currentStage = (Stage) scoreP1.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ending.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            String css = Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm();
            scene.getStylesheets().add(css);

            EndingController ending = loader.getController();
            ending.displayWinner(game.getWinner().getName(), game.getWinner().getAvatar());

            currentStage.hide();
            currentStage.setScene(scene);
            currentStage.setFullScreen(true);
            currentStage.setFullScreenExitHint("");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void animateMoves(List<Pair<Integer, Integer>> moves, Runnable onFinished) {
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
                // A castle only ever reaches 0 when it is captured. Flip the quan off at that
                // exact frame (not earlier), so the big gem stays visible until it is actually taken.
                if (game.isBigCell(cellIndex) && newValue == 0) {
                    gemRenderer.removeBigGem(cellIndex);
                }
                gemRenderer.renderGemsInCell(cellIndex, newValue);
            }));
        }
        timeline.setOnFinished(e -> onFinished.run()); //When timeline finishes, run the code in -> {}
        timeline.play();
    }

    public void updateScoreUI(){
        scoreP1.setText("Score: " + game.getPlayerScore(0));
        scoreP2.setText("Score: " + game.getPlayerScore(1));
    }

    public static int convertStringToInt(String str){
        str=str.replaceAll("[^0-9]", "");
        return Integer.parseInt(str);
    }

    public void postGameVisualEffect(Runnable onFinished){
        Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(1), e -> {
            for(int i = 1; i <= 5; i++){
                allCells[i].setText("0");
                gemRenderer.renderGemsInCell(i, 0);
            }
            updateScoreUI();
        }));
        timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(2), e -> {
            for(int i = 7; i <= 11; i++){
                allCells[i].setText("0");
                gemRenderer.renderGemsInCell(i, 0);
            }
            updateScoreUI();
        }));

        
        for(int i = 0; i < 6; i++){
            final double on  = 3 + i * 1.0; 
            final double off = 3.5 + i * 1.0; 
            timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(on), e -> {
                Label winnerScore = game.getWinnerId() == 0 ? scoreP1 : scoreP2;
                winnerScore.setTextFill(Color.GOLD);
            }));
            timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(off), e -> {
                Label winnerScore = game.getWinnerId() == 0 ? scoreP1 : scoreP2;
                winnerScore.setTextFill(Color.WHITE);
            }));
        }

        timeline.setOnFinished(e -> onFinished.run());
        timeline.play();
    }
}