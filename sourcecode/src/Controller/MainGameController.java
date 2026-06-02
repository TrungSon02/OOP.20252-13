package Controller;

import java.util.List;

import Model.Game;
import View.GemRenderer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * Glue between the FXML view and the {@link Game} model.
 * Responsibilities are now narrow: wire up the scene, react to {@link InputHandler}
 * events (highlighting + kicking off moves), animate the model's move sequences,
 * and ask {@link GemRenderer} to paint gems. It holds no game rules and reads all
 * board/player state from {@code Game}.
 */
public class MainGameController extends BaseController implements InputHandler.Listener {

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
    private StackPane[] allCellPanes;

    private Game game;
    private InputHandler inputHandler;
    private GemRenderer gemRenderer;

    private static final int CELL_COUNT = 12;
    private static final Duration STEP_DELAY = Duration.millis(700); // tweak speed here

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
        allCellPanes = new StackPane[]{
            cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5,
            cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11
        };

        // Collaborators
        inputHandler = new InputHandler(game, this);

        boolean[] bigCell = new boolean[CELL_COUNT];
        for (int i = 0; i < CELL_COUNT; i++) bigCell[i] = game.isCastleSquare(i);
        gemRenderer = new GemRenderer(allCellPanes, allCells, bigCell);

        // Initial view from the model
        syncCellLabels();
        refreshScores();
        initializePlayerData();
        highlightSelectable();

        // Draw gems after the first layout pass, when cells have a real size
        Platform.runLater(() -> {
            if (cellPane1 != null && cellPane1.getScene() != null) {
                cellPane1.getScene().getRoot().applyCss();
                cellPane1.getScene().getRoot().layout();
            }
            gemRenderer.renderAll(game.getBoardSnapshot());
        });
    }

    private void initializePlayerData() {
        labelPlayer1.setText("Player " + game.getPlayerName(0));
        labelPlayer2.setText("Player " + game.getPlayerName(1));
        try {
            Image imgPlayer1 = new Image(getClass().getResourceAsStream(game.getPlayerAvatar(0)));
            Image imgPlayer2 = new Image(getClass().getResourceAsStream(game.getPlayerAvatar(1)));
            if (avatarP1 != null) avatarP1.setImage(imgPlayer1);
            if (avatarP2 != null) avatarP2.setImage(imgPlayer2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Input: just forward the clicked cell to the handler ----------------

    public void getInput(MouseEvent event) {
        Shape clicked = (Shape) event.getSource();
        int cell = convertStringToInt(clicked.getId()); // e.g. "overlay3" -> 3
        inputHandler.handleClick(cell);
    }

    // ---------------- InputHandler.Listener ----------------

    @Override
    public void onSourceSelected(int square) {
        resetAllSquares();
        setRedSquare(allOverlays[square]);
        setYellowSquare(allOverlays[(square + 1) % CELL_COUNT]);
        setYellowSquare(allOverlays[(square - 1 + CELL_COUNT) % CELL_COUNT]);
    }

    @Override
    public void onSelectionCleared() {
        highlightSelectable();
    }

    @Override
    public void onMoveRequested(int startingSquare, int direction) {
        resetAllSquares();
        List<Pair<Integer, Integer>> moveSequence = game.proccessingTurn(startingSquare, direction);
        animateMoves(moveSequence, () -> {
            refreshScores();
            List<Pair<Integer, Integer>> fillSequence = game.postTurnProcessing();
            animateMoves(fillSequence, () -> {
                refreshScores();
                if (game.isFinished()) {
                    // Game over — board left as-is. Hook here to navigate to the ending screen.
                    return;
                }
                inputHandler.unlock();
                highlightSelectable();
            });
        });
    }

    // ---------------- Animation ----------------

    private void animateMoves(List<Pair<Integer, Integer>> moves, Runnable onFinished) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < moves.size(); i++) {
            Pair<Integer, Integer> step = moves.get(i);
            int cellIndex = step.getKey();
            int newValue  = step.getValue();
            Duration when = STEP_DELAY.multiply(i + 1);

            timeline.getKeyFrames().add(new KeyFrame(when, e -> {
                if (allCells[cellIndex] != null) {
                    allCells[cellIndex].setText(String.valueOf(newValue));
                }
                gemRenderer.render(cellIndex, newValue); // keep gems in sync with the count
            }));
        }
        timeline.setOnFinished(e -> onFinished.run());
        timeline.play();
    }

    // ---------------- View helpers (read state from the model) ----------------

    private void syncCellLabels() {
        int[] counts = game.getBoardSnapshot();
        for (int i = 0; i < allCells.length; i++) {
            if (allCells[i] != null) allCells[i].setText(String.valueOf(counts[i]));
        }
    }

    private void refreshScores() {
        scoreP1.setText("Score: " + game.getPlayerScore(0));
        scoreP2.setText("Score: " + game.getPlayerScore(1));
    }

    /** Highlight every square the current player can currently start a move from. */
    private void highlightSelectable() {
        resetAllSquares();
        int from = (game.getCurrentPlayer() == 0) ? 1 : 7;
        for (int i = from; i <= from + 4; i++) {
            if (game.getGemCount(i) > 0) {
                setYellowSquare(allOverlays[i]);
            }
        }
    }

    private void setYellowSquare(Shape shape) {
        shape.setFill(Color.rgb(255, 255, 0, 0.15));
        shape.setEffect(glow(Color.YELLOW));
    }

    private void setRedSquare(Shape shape) {
        shape.setFill(Color.rgb(255, 0, 0, 0.15));
        shape.setEffect(glow(Color.RED));
    }

    private DropShadow glow(Color color) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(50);
        glow.setSpread(0.7);
        return glow;
    }

    private void resetAllSquares() {
        for (Shape shape : allOverlays) {
            shape.setFill(Color.TRANSPARENT);
            shape.setEffect(null);
        }
    }

    public static int convertStringToInt(String str) {
        str = str.replaceAll("[^0-9]", "");
        return Integer.parseInt(str);
    }
}
