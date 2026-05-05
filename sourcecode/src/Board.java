import java.util.ArrayList;
import java.util.List;


public class Board {
    // TODO 1: Add attributes
    private int[] smallSquares;     // 10 small squares (5 per player) indices 1-5 for player1, 7-11 for player2, 0 for player 1, 6 for player 2
    private int p1Pawn;    // Player 1's captured pawn
    private int p2Pawn;    // Player 2's captured pawn
    private int p1King;    // Player 1's captured King
    private int p2King;    // Player 2's captured King
    private boolean king1Captured;
    private boolean king2Captured;
    private static final int TOTAL_SMALL_SQUARES = 10;
    private static final int P1_HOME = 0;
    private static final int P2_HOME = (TOTAL_SMALL_SQUARES + 2) / 2;
    private static final int INITIAL_PAWNS = 5;
    private static final int INITIAL_KINGS = 1;
    private static final int CLOCKWISE = 1;
    private static final int COUNTER_CLOCKWISE = -1;


    public Board() {
        smallSquares = new int[TOTAL_SMALL_SQUARES + 2];
        for (int i = 0; i < TOTAL_SMALL_SQUARES + 2; i++) {
            smallSquares[i] = INITIAL_PAWNS;
        }
        smallSquares[P1_HOME] = 0; // square used to virtualize p1 big square
        smallSquares[P2_HOME] = 0; // square used to virtualize p2 big square
        setP1Pawn(0);
        setP2Pawn(0);
        setP1King(0);
        setP2King(0);
        setKing1Captured(false);
        setKing2Captured(false);
    }
    
    // Getter methods for accessing board state
    public int[] getSmallSquares() {
        return smallSquares;
    }
    
    public int getP1Pawn() {
        return p1Pawn;
    }

    public int getP2Pawn() {
        return p2Pawn;
    }

    public int getPawn(int player){
        if (player == 1){
            return this.getP1Pawn();
        } else {
            return this.getP2Pawn();
        }
    }

    public int getP1King() {
        return p1King;
    }

    public int getP2King() {
        return p2King;
    }

    public int getKing(int player){
        if (player == 1){
            return this.getP1King();
        } else {
            return this.getP2King();
        }
    }

    public int getP1Square() {
        return smallSquares[P1_HOME] + (this.isKing1Captured() ? 0 : 1);
    }

    public int getP2Square() {
        return smallSquares[P2_HOME] + (this.isKing2Captured() ? 0 : 1);
    }

    public boolean isKing1Captured() {
        return king1Captured;
    }

    public boolean isKing2Captured() {
        return king2Captured;
    }


    public void setP1Pawn(int p1Pawn) {
        this.p1Pawn = p1Pawn;
    }

    public void setP2Pawn(int p2Pawn) {
        this.p2Pawn = p2Pawn;
    }


    public void setPawn(int player, int pawn){
        if (player != 1 && player != 2) {
            throw new IllegalArgumentException("Player must be 1 or 2");
        }
        if (player == 1){
            setP1Pawn(pawn);
        } else {
            setP2Pawn(pawn);
        }
    }

    public void setP1King(int p1King) {
        this.p1King = p1King;
    }

    public void setP2King(int p2King) {
        this.p2King = p2King;
    }


    public void setKing(int player, int king){
        if (player != 1 && player != 2) {
            throw new IllegalArgumentException("Player must be 1 or 2");
        }
        if (king < 0) {
            throw new IllegalArgumentException("King count cannot be negative");
        }
        if (player == 1){
            setP1King(king);
        } else {
            setP2King(king);
        }
    }

    public void setKing1Captured(boolean king1Captured) {
        this.king1Captured = king1Captured;
    }

    public void setKing2Captured(boolean king2Captured) {
        this.king2Captured = king2Captured;
    }
    
    public static int clockwise(){
        return CLOCKWISE;
    }

    public static int counterClockwise(){
        return COUNTER_CLOCKWISE;
    }


    public List<Integer> moveGem(int startPosition, int player, int direction) {
        // TODO 2 implement moveGem: Create a list that stores all move. After finish moving, return that list
        List<Integer> moveSequence = new ArrayList<Integer>();
        boolean isP1 = (player == 1);
        boolean isP2 = (player == 2);

        if (startPosition < 0 || startPosition >= TOTAL_SMALL_SQUARES + 2) {
            throw new IllegalArgumentException("Invalid start position");
        }
        if (isP1 && startPosition > 5){
            throw new IllegalArgumentException("Invalid start position");
        }
        if (isP2 && startPosition < 7){
            throw new IllegalArgumentException("Invalid start position");
        }
        if (smallSquares[startPosition] == 0){
            throw new IllegalArgumentException("Invalid start position");
        }
        if (!isP1 && !isP2){
            throw new IllegalArgumentException("Player must be 1 or 2");
        }
        if (direction != 1 || direction != -1){
            throw new IllegalArgumentException("direction must be 1 (clockwise) or -1 (counter-clockwise)");

        }

        int pawnsToMove = smallSquares[startPosition];
        moveSequence.add(startPosition);
        smallSquares[startPosition] = 0;
        
        int currentPos = startPosition;
        int remainingPawns = pawnsToMove;
        
        // Distribute pawms
        boolean canMove = true;
        boolean captured = false;
        while (canMove){
            while (remainingPawns > 0) {
                currentPos = Math.floorMod(currentPos + direction, TOTAL_SMALL_SQUARES + 2);
                smallSquares[currentPos]++;
                moveSequence.add(currentPos);    
                remainingPawns--;
            }

            int nextSquare = Math.floorMod(currentPos + direction, TOTAL_SMALL_SQUARES + 2);
            if (nextSquare == P1_HOME || nextSquare == P2_HOME){
                canMove = false;
                break;
            }
            if (smallSquares[nextSquare] > 0){
                if (captured){
                    canMove = false;
                    continue;
                }
                remainingPawns = smallSquares[nextSquare];
                smallSquares[nextSquare] = 0;
                currentPos = nextSquare;
                continue;
            } else {
                int nextSquare2 = Math.floorMod(nextSquare + direction, TOTAL_SMALL_SQUARES + 2); //square 2 move forward
                if (smallSquares[nextSquare2] == 0){
                    canMove = false;
                } else {
                    if (nextSquare2 == P1_HOME){
                        if (!this.isKing1Captured()){
                            setKing(player, this.getKing(player) + 1);
                            setKing1Captured(true);
                        }
                    } else if (nextSquare2 == P2_HOME){
                        if (!this.isKing2Captured()){
                            setKing(player, this.getKing(player) + 1);
                            setKing2Captured(true);
                        }
                    }
                    setPawn(player, this.getPawn(player) + smallSquares[nextSquare2]);
                    smallSquares[nextSquare2] = 0;
                    currentPos = nextSquare2;
                    captured = true;
                }
            }
        } 
        return moveSequence;
    }

    
    public int checkEmpty() {
        // TODO 3: check if the 5 squares of each player is empty
        // returns 0 if neither player's squares are empty, 1 if player 1's squares are empty and 2 if player 2's squares are empty
        // Check player 1's squares
        boolean player1Empty = true;
        for (int i = 1; i < P2_HOME; i++) {
            if (smallSquares[i] > 0) {
                player1Empty = false;
                break;
            }
        }
        
        // Check player 2's squares
        boolean player2Empty = true;
        for (int i = P2_HOME + 1; i < TOTAL_SMALL_SQUARES + 2; i++) {
            if (smallSquares[i] > 0) {
                player2Empty = false;
                break;
            }
        }
        
        if (player1Empty){
            return 1;
        } else if (player2Empty){
            return 2;
        } else {
            return 0;
        }
    }
    
    public boolean checkEnding() {
        return this.getP1Square() == 0 && this.getP2Square() == 0;
    }
    
    public boolean fillGem(int player) {
        if (player != 1 && player != 2) {
            throw new IllegalArgumentException("Player must be 1 or 2");
        }
        int startIndex, playerPawn, opponentPawn;
        if (player == 1) {
            startIndex = P1_HOME + 1;
            playerPawn = this.getP1Pawn();
            opponentPawn = this.getP2Pawn();
        } else {
            startIndex = P2_HOME + 1;
            playerPawn = this.getP2Pawn();
            opponentPawn = this.getP1Pawn();
        }
        
        // Check if the player actually has 5 empty squares
        int emptyCount = 0;
        for (int i = startIndex; i < startIndex + 5; i++) {
            if (smallSquares[i] == 0){
                emptyCount++;
            }
        }
        
        // must have at least 1 piece per empty square.
        // feed from your captured score.
        if (emptyCount < 5){
            return false;
        }
        if (player == 1) {
            setP1Pawn(playerPawn - 5);
        } else {
            setP2Pawn(playerPawn - 5);
        }
        // Add 1 pawn to each empty square
        for (int i = startIndex; i < startIndex + 5; i++) {
            smallSquares[i] = 1;
        }
        return true;
    }
    
    // for debugging
    public void printBoard() {
        System.out.println("=== Board State ===");
        System.out.println("P1 King: " + p1King + ", P1 Pawn: " + p1Pawn);
        System.out.println("P1 Home (index " + P1_HOME + "): " + this.getP1Square());
        System.out.print("P1 squares (1 to 5): ");
        for (int i = 1; i < P2_HOME; i++) {
            System.out.print(smallSquares[i] + " ");
        }
        System.out.println();
        System.out.print("P2 squares (11 down to 7): ");
        for (int i = TOTAL_SMALL_SQUARES + 1; i > P2_HOME; i--) {
            System.out.print(smallSquares[i] + " ");
        }
        System.out.println();
        System.out.println("P2 Home (index " + P2_HOME + "): " + this.getP2Square());
        System.out.println("P2 King: " + p2King + ", P2 Pawn: " + p2Pawn);
        System.out.println("==================");
    }
}