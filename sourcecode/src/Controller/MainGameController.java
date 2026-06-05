package Controller;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Objects;

import Model.Game;
import View.BaseGem;
import View.BigGem;
import View.SmallGem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    // The StackPane wrapping each cell — this is the container we draw gems into.
    @FXML private StackPane cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5;
    @FXML private StackPane cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11;

    private Label[] allCells;
    private Shape[] allOverlays;

    // Gem rendering support
    private StackPane[] allCellPanes;
    private Pane[] cellPiles; // one transparent layer per cell that holds its gems
    private static final int GEMS_PER_ROW = 4;
    private final Random rng = new Random(); // used only for slight positional jitter

    /**
     * Animation-synced view of whether each castle still holds its quan.
     * Slot 0 = cell 0 (player 1's castle), slot 1 = cell 6 (player 2's castle).
     *
     * We deliberately do NOT read game.checkBigGemExistence() while rendering: the model
     * resolves the whole turn (move + capture) up front, so its quan flag is already at the
     * end-of-turn value before the animation replays. This local flag is flipped at the exact
     * animation frame the castle is emptied, so the quan stays drawn until it is actually taken.
     */
    private final boolean[] checkBigGemExistence = { true, true };

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

        // Collect the StackPanes so we can draw gems into each cell
        allCellPanes = new StackPane[]{
            cellPane0, cellPane1, cellPane2, cellPane3, cellPane4, cellPane5,
            cellPane6, cellPane7, cellPane8, cellPane9, cellPane10, cellPane11
        };
        cellPiles = new Pane[allCellPanes.length];

        // ok just redeclaring it to be sure
        checkBigGemExistence[0] = true;
        checkBigGemExistence[1] = true;

        for (int i = 0; i <= 11; i++) {
            allCells[i].setText("5");
        }

        scoreP1.setText("Score: 0");
        scoreP2.setText("Score: 0");

        highlightAvailableSquareState1(game.getCurrentPlayer());
        initializePlayerData();

        // Draw gems after the first layout pass, so the cells already have a real size
        Platform.runLater(() -> {
            if (cellPane1 != null && cellPane1.getScene() != null) {
                cellPane1.getScene().getRoot().applyCss();
                cellPane1.getScene().getRoot().layout();
            }
            renderAllGems();
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
                if (game.isFinished()) {
                    postGameVisualEffect(() -> {
                        loadEndingScene();
                    });
                    
                } else {
                    state = 1;
                    highlightAvailableSquareState1(game.getCurrentPlayer());
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
                if (isBigCell(cellIndex) && newValue == 0) {
                    checkBigGemExistence[castleSlot(cellIndex)] = false;
                }
                renderGemsInCell(cellIndex, newValue); // keep gems in sync with the count
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

    public void postGameVisualEffect(Runnable onFinished){
        Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(1), e -> {
            for(int i = 1; i <= 5; i++){
                allCells[i].setText("0");
            }
            updateScoreUI(0, game.getPlayers()[0].getScore());
        }));
        timeline.getKeyFrames().add(new KeyFrame(STEP_DELAY.multiply(2), e -> {
            for(int i = 7; i <= 11; i++){
                allCells[i].setText("0");
            }
            updateScoreUI(1, game.getPlayers()[1].getScore());
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

    // ---------------- Gem rendering ----------------

    /** The two "castle" arc cells hold the immovable big gem (quan); all other cells hold small gems. */
    private boolean isBigCell(int cellIndex) {
        return cellIndex == 0 || cellIndex == 6;
    }

    /** Map a castle cell index to its quanAlive slot: cell 0 -> 0, cell 6 -> 1. */
    private int castleSlot(int cellIndex) {
        return cellIndex == 0 ? 0 : 1;
    }

    /** Redraw the gems in every cell from the current label values. */
    private void renderAllGems() {
        for (int i = 0; i < allCellPanes.length; i++) {
            renderGemsInCell(i, parseCount(allCells[i]));
        }
    }

    /** Draw the gems for cell `cellIndex` given its current `count`, replacing whatever was there before. */
    private void renderGemsInCell(int cellIndex, int count) {
        if (cellIndex < 0 || cellIndex >= allCellPanes.length) return;
        if (allCellPanes[cellIndex] == null) return;

        Pane pile = ensurePile(cellIndex);
        pile.getChildren().clear();

        Bounds cb = allCellPanes[cellIndex].getLayoutBounds();
        double cellW = cb.getWidth()  > 0 ? cb.getWidth()  : 150;
        double cellH = cb.getHeight() > 0 ? cb.getHeight() : 150;

        if (isBigCell(cellIndex)) {
            renderCastle(pile, cellIndex, count, cellW, cellH);
        } else {
            // Small square: every gem here is, and stays, a small gem.
            for (int i = 0; i < count; i++) {
                SmallGem gem = new SmallGem();
                placeInGrid(gem, i, GEMS_PER_ROW, cellW / 2.0, cellH / 2.0 - SmallGem.RADIUS * 2.2);
                pile.getChildren().add(gem);
            }
        }

        if (allCells[cellIndex] != null) {
            allCells[cellIndex].toFront(); // keep the number readable on top of the gems
        }
    }

    /**
     * A castle shows exactly ONE big gem (the quan), worth {@link BigGem#VALUE}, which never moves,
     * plus any small gems that have travelled in from the small squares (they stay small).
     *
     * Whether the quan is present comes from the animation-synced {@link #quanAlive} flag, NOT from
     * the model, so the big gem stays drawn until the very frame the castle is captured. Once a
     * castle has been captured, quanAlive stays false, so it never grows a phantom quan even if it
     * later accumulates 5+ small gems.
     */
    private void renderCastle(Pane pile, int cellIndex, int count, double cellW, double cellH) {
        boolean quanPresent = checkBigGemExistence[castleSlot(cellIndex)];
        int smallCount = quanPresent ? count - BigGem.VALUE : count;
        if (smallCount < 0) smallCount = 0;

        if (quanPresent) {
            BigGem quan = new BigGem();
            quan.setLayoutX(cellW / 2.0);   // centered, and never repositioned by a move
            quan.setLayoutY(cellH / 2.0);
            pile.getChildren().add(quan);
        }

        // Small gems collect just below the quan.
        double topY = cellH / 2.0 + BigGem.RADIUS + SmallGem.RADIUS;
        for (int i = 0; i < smallCount; i++) {
            SmallGem gem = new SmallGem();
            placeInGrid(gem, i, GEMS_PER_ROW, cellW / 2.0, topY);
            pile.getChildren().add(gem);
        }
    }

    /** Place `gem` at slot `idx` of a centered grid (center column = `centerX`, first row at `topY`). */
    private void placeInGrid(BaseGem gem, int idx, int perRow, double centerX, double topY) {
        double spacing = gem.getRadius() * 2.2;
        int row = idx / perRow;
        int col = idx % perRow;
        double startX = centerX - (perRow - 1) * spacing / 2.0;
        double jitterX = (rng.nextDouble() - 0.5) * 3;
        double jitterY = (rng.nextDouble() - 0.5) * 3;
        gem.setLayoutX(startX + col * spacing + jitterX);
        gem.setLayoutY(topY + row * spacing + jitterY);
    }

    /** Lazily create a transparent layer inside the cell's StackPane that holds the gems. */
    private Pane ensurePile(int cellIndex) {
        if (cellPiles[cellIndex] != null) return cellPiles[cellIndex];

        Pane pile = new Pane();
        pile.setMouseTransparent(true);  // never intercept clicks meant for the overlay/cell
        pile.setPickOnBounds(false);
        pile.setManaged(false);          // we position gems by absolute coords, so don't let StackPane re-center this layer
        cellPiles[cellIndex] = pile;

        StackPane sp = allCellPanes[cellIndex];
        // Insert just below the count label so the number stays on top of the gems
        int labelIdx = sp.getChildren().indexOf(allCells[cellIndex]);
        if (labelIdx >= 0) {
            sp.getChildren().add(labelIdx, pile);
        } else {
            sp.getChildren().add(pile);
        }
        return pile;
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