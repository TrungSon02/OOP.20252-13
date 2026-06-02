package Controller;

import java.util.List;
import java.util.Random;

import Model.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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

    // NEW: the StackPane wrapping each cell — this is the container we draw marbles into.
    @FXML private StackPane cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5;
    @FXML private StackPane cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11;

    private Label[] allCells;
    private Shape[] allOverlays;

    // NEW: marble rendering support
    private StackPane[] allCellPanes;
    private Pane[] cellPiles; // one transparent layer per cell that holds its marbles
    private static final double MARBLE_RADIUS = 7.0;
    private static final int MARBLES_PER_ROW = 4;
    private static final Color[] MARBLE_COLORS = new Color[] {
        Color.web("#E8C547"), Color.web("#D64545"), Color.web("#3FA34D"),
        Color.web("#3A86FF"), Color.web("#F5F5DC"), Color.web("#A06CD5")
    };
    private final Random rng = new Random();

    private Game game;
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

        // NEW: collect the StackPanes so we can draw marbles into each cell
        allCellPanes = new StackPane[]{
            cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5,
            cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11
        };
        cellPiles = new Pane[allCellPanes.length];

        for (int i = 0; i <= 11; i++) {
            allCells[i].setText("5");
        }

        scoreP1.setText("Score: 0");
        scoreP2.setText("Score: 0");

        highlightAvailableSquareState1(game.getCurrentPlayer());
        initializePlayerData();

        // NEW: draw marbles after the first layout pass, so the cells already have a real size
        Platform.runLater(() -> {
            if (cellPane1 != null && cellPane1.getScene() != null) {
                cellPane1.getScene().getRoot().applyCss();
                cellPane1.getScene().getRoot().layout();
            }
            renderAllMarbles();
        });
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
            if(checkState1(game.getCurrentPlayer(), shapeID)){
                resetAllSquares();
                highlightAvailableSquareState2(shapeID);
                selectedSquare = shapeID;
                state = 2;
            }
        }
        else if(state == 2){
            if(checkState2(shapeID)){
                if(shapeID == selectedSquare){
                    state = 1;
                    resetAllSquares();
                    highlightAvailableSquareState1(game.getCurrentPlayer());
                }else{
                    int direction = shapeID - selectedSquare;
                    if(direction == -11){
                       direction = 1; //Case when the selectedSquare = 11 and the direction square is 0
                    }
                    onPlayerMove(selectedSquare,direction);
                }
            }
        }
    }

    public void onPlayerMove(int startingSquare, int direction){
        state = -1;
        resetAllSquares();
        List<Pair<Integer, Integer>> moveSequence = game.proccessingTurn(startingSquare, direction);
        animateMoves(moveSequence, () -> {
            updateScoreUI(game.getCurrentPlayer(), game.getPlayers()[game.getCurrentPlayer()].getScore());

            List<Pair<Integer, Integer>> fillSequence = game.postTurnProcessing();
            animateMoves(fillSequence, () -> {
                state = 1;
                highlightAvailableSquareState1(game.getCurrentPlayer());
            });
        });
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
                renderMarblesInCell(cellIndex, newValue); // NEW: keep marbles in sync with the count
            }));
        }
        timeline.setOnFinished(e -> onFinished.run()); //When timeline finishes, run the code in -> {}
        timeline.play();
    }

    public void updateScoreUI(int playerIndex, int score){
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

    public boolean checkState1(int currentPlayer, int shapeID){
        if(!hasGem(shapeID)){
            return false;
        }
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
        int LeftNeighbour = (selectedSquare + 1 + 12) % 12;
        int RightNeighbour = (selectedSquare - 1 + 12) % 12;
        if(shapeID == LeftNeighbour || shapeID == RightNeighbour || shapeID == selectedSquare){
            return true;
        }
        return false;
    }

    public void highlightAvailableSquareState1(int currentPlayer){
        if(currentPlayer == 0){
            for(int i = 1; i<= 5;i++){
                if(hasGem(i)){
                    setYellowSquare(allOverlays[i]);
                }
            }
        }else {
            for(int i = 7; i<= 11;i++){
                if(hasGem(i)){
                    setYellowSquare(allOverlays[i]);
                }
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

    public boolean hasGem(int shapeID){
        String gemAmount = allCells[shapeID].getText();
        if(Integer.parseInt(gemAmount) == 0){
            return false;
        }
        return true;
    }

    // ---------------- NEW: marble rendering ----------------

    /** Redraw the marbles in every cell from the current label values. */
    private void renderAllMarbles() {
        for (int i = 0; i < allCellPanes.length; i++) {
            renderMarblesInCell(i, parseCount(allCells[i]));
        }
    }

    /** Draw `count` marbles inside cell `cellIndex`, replacing whatever was there before. */
    private void renderMarblesInCell(int cellIndex, int count) {
        if (cellIndex < 0 || cellIndex >= allCellPanes.length) return;
        if (allCellPanes[cellIndex] == null) return;

        Pane pile = ensurePile(cellIndex);
        pile.getChildren().clear();
        for (int i = 0; i < count; i++) {
            pile.getChildren().add(makeMarble(cellIndex, i));
        }
        if (allCells[cellIndex] != null) {
            allCells[cellIndex].toFront(); // keep the number readable on top of the marbles
        }
    }

    /** Lazily create a transparent layer inside the cell's StackPane that holds the marbles. */
    private Pane ensurePile(int cellIndex) {
        if (cellPiles[cellIndex] != null) return cellPiles[cellIndex];

        Pane pile = new Pane();
        pile.setMouseTransparent(true);  // never intercept clicks meant for the overlay/cell
        pile.setPickOnBounds(false);
        pile.setManaged(false);          // we position marbles by absolute coords, so don't let StackPane re-center this layer
        cellPiles[cellIndex] = pile;

        StackPane sp = allCellPanes[cellIndex];
        // Insert just below the count label so the number stays on top of the marbles
        int labelIdx = sp.getChildren().indexOf(allCells[cellIndex]);
        if (labelIdx >= 0) {
            sp.getChildren().add(labelIdx, pile);
        } else {
            sp.getChildren().add(pile);
        }
        return pile;
    }

    /** Build a single marble, positioned in a centered grid pattern inside the cell. */
    private Circle makeMarble(int cellIndex, int idx) {
        Circle marble = new Circle(MARBLE_RADIUS, MARBLE_COLORS[rng.nextInt(MARBLE_COLORS.length)]);
        marble.setStroke(Color.color(0, 0, 0, 0.35));
        marble.setStrokeWidth(1);

        Bounds cb = allCellPanes[cellIndex].getLayoutBounds();
        double cellW = cb.getWidth()  > 0 ? cb.getWidth()  : 150;
        double cellH = cb.getHeight() > 0 ? cb.getHeight() : 150;

        int row = idx / MARBLES_PER_ROW;
        int col = idx % MARBLES_PER_ROW;

        double spacing = MARBLE_RADIUS * 2.2;
        double startX = cellW / 2.0 - (MARBLES_PER_ROW - 1) * spacing / 2.0;
        double startY = cellH / 2.0 - spacing;

        double jitterX = (rng.nextDouble() - 0.5) * 3;
        double jitterY = (rng.nextDouble() - 0.5) * 3;

        marble.setLayoutX(startX + col * spacing + jitterX);
        marble.setLayoutY(startY + row * spacing + jitterY);
        return marble;
    }

    /** Safely parse a label's text as an int, returns 0 if it can't. */
    private int parseCount(Label label) {
        if (label == null) return 0;
        try {
            return Integer.parseInt(label.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
