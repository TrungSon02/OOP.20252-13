import java.util.ArrayList;
import java.util.List;

public class Board {
    // TODO 1: Add attributes
    private int[] smallSquares;     // 10 small squares (5 per player) indices 0-4 for player1, 5-9 for player2
    private int p1Square;    // Player 1's home square
    private int p2Square;    // Player 2's home square
    private static final int TOTAL_SMALL_SQUARES = 10;
    private static final int INITIAL_PAWNS_SMALL = 5;
    private static final int INITIAL_PAWNS_BIG = 1;
    
    public Board() {
        smallSquares = new int[TOTAL_SMALL_SQUARES];
        for (int i = 0; i < TOTAL_SMALL_SQUARES; i++) {
            smallSquares[i] = INITIAL_PAWNS_SMALL;
        }
        p1Square = INITIAL_PAWNS_BIG;
        p2Square = INITIAL_PAWNS_BIG;
    }
    
    // Getter methods for accessing board state
    public int[] getSmallSquares() {
        return smallSquares;
    }
    
    public int getPlayer1BigSquare() {
        return p1Square;
    }
    
    public int getPlayer2BigSquare() {
        return p2Square;
    }
    

    public List<Integer> moveGem(int startPosition, boolean isPlayer1) {
        // TODO 2 implement moveGem: Create a list that stores all move. After finish moving, return that list
        List<Integer> moveSequence = new ArrayList<Integer>();
        
        if (startPosition < 0 || startPosition >= TOTAL_SMALL_SQUARES) {
            return moveSequence; // Invalid position
        }
        
        int pawnsToMove = smallSquares[startPosition];
        if (pawnsToMove <= 0) {
            return moveSequence; // No gems to move
        }
        
        // Add start position to sequence
        moveSequence.add(startPosition);
        smallSquares[startPosition] = 0;
        
        int currentPos = startPosition;
        int remainingPawns = pawnsToMove;
        
        // Distribute pawms
        while (remainingPawns > 0) {
            currentPos = (currentPos + 1) % (TOTAL_SMALL_SQUARES + 2); // +2 for big squares
            
            if (currentPos == TOTAL_SMALL_SQUARES) {
                // Player 1's big square
                p1Square++;
                moveSequence.add(-1); // Indicator for big square
            } else if (currentPos == TOTAL_SMALL_SQUARES + 1) {
                // Player 2's big square
                p2Square++;
                moveSequence.add(-2); // Indicator for big square
            } else {
                // Small square
                smallSquares[currentPos]++;
                moveSequence.add(currentPos);
            }
            
            remainingPawns--;
        }
        
        return moveSequence;
    }

    
    public boolean checkEmpty() {
        // TODO 3: check if the 5 squares of each player is empty
        // Check player 1's squares
        boolean player1Empty = true;
        for (int i = 0; i < 5; i++) {
            if (smallSquares[i] > 0) {
                player1Empty = false;
                break;
            }
        }
        
        // Check player 2's squares
        boolean player2Empty = true;
        for (int i = 5; i < TOTAL_SMALL_SQUARES; i++) {
            if (smallSquares[i] > 0) {
                player2Empty = false;
                break;
            }
        }
        
        return player1Empty || player2Empty;
    }
    
    public boolean checkEnding() {
        // TODO 4: check if the 2 big squares are BOTH empty
        return p1Square == 0 && p2Square == 0;
    }
    
    public void fillGem() {
        // TODO 5: locate the 5 empty squares then add 1 gem to each
        for (int i = 0; i < TOTAL_SMALL_SQUARES; i++) {
            if (smallSquares[i] == 0) {
                smallSquares[i] = 1;
            }
        }
    }
    
    // for debugging
    public void printBoard() {
        System.out.println("P2: " + p2Square);
        for (int i = TOTAL_SMALL_SQUARES - 1; i >= 5; i--) {
            System.out.print(smallSquares[i] + " ");
        }
        System.out.println();
        System.out.print("P1: " + p1Square);
        for (int i = 0; i < 5; i++) {
            System.out.print(smallSquares[i] + " ");
        }
        System.out.println();
    }
}