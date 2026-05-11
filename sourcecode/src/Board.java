import java.util.ArrayList;
import java.util.List;

//TODO (new): Add a clearSquare method to reset the value of a square to 0
public class Board {
    // FIX: Remove all non constant attributes (and related parts) except the first one, change the logic to table[] (treating all squares the same)
    private int[] table;     // 10 small squares (5 per player) indices 1-5 for player1, 7-11 for player2, 0 for player 1, 6 for player 2
    private static final int TOTAL_SMALL_SQUARES = 10;
    private static final int P1_HOME = 0;
    private static final int P2_HOME = (TOTAL_SMALL_SQUARES + 2) / 2;
    private static final int INITIAL_PAWNS = 5;
    private static final int INITIAL_KINGS = 1;
    private static final int CLOCKWISE = 1;
    private static final int COUNTER_CLOCKWISE = -1;

    //FIX: Init all square (including big ones to 5)
    public Board() {
        table = new int[TOTAL_SMALL_SQUARES + 2];
        for (int i = 0; i < TOTAL_SMALL_SQUARES + 2; i++) {
            table[i] = INITIAL_PAWNS;
        }
        table[P1_HOME] = 0; // square used to virtualize p1 big square
        table[P2_HOME] = 0; // square used to virtualize p2 big square
    }
    
    // Getter methods for accessing board state
    public int[] gettable() {
        return table;
    }
    
    public static int clockwise(){
        return CLOCKWISE;
    }

    public static int counterClockwise(){
        return COUNTER_CLOCKWISE;
    }

    //FIX1: Player must be 0 or 1*
    //FIX2: Remove all the validation\
    //FIX4: Remove all the capture and add score
    //FIX3: moveSequence store pair (square_id, value_of_that_square)

    public List<Pair<Integer, Integer>> moveGem(int startPosition, int player, int direction) {
        // TODO 2 implement moveGem: Create a list that stores all move. After finish moving, return that list
        List<Integer> moveSequence = new ArrayList<Integer>();
        boolean isP1 = (player == 0);
        boolean isP2 = (player == 1);

        int pawnsToMove = table[startPosition];
        moveSequence.add(Pair.with(startPosition, table[startPosition]));
        table[startPosition] = 0;
        
        int currentPos = startPosition;
        int remainingPawns = pawnsToMove;
        
        // Distribute pawms
        while (remainingPawns > 0) {
            currentPos = Math.floorMod(currentPos + direction, TOTAL_SMALL_SQUARES + 2);
            table[currentPos]++;
            moveSequence.add(Pair.with(currentPos, table[currentPos]));    
            remainingPawns--;
        }

        return moveSequence;
    }

    //FIX: Change the return value to match player 0 and player 1
    public int checkEmpty() {
        // TODO 3: check if the 5 squares of each player is empty
        // returns 0 if neither player's squares are empty, 1 if player 1's squares are empty and 2 if player 2's squares are empty
        // Check player 1's squares
        boolean player1Empty = true;
        for (int i = 1; i < P2_HOME; i++) {
            if (table[i] > 0) {
                player1Empty = false;
                break;
            }
        }
        
        // Check player 2's squares
        boolean player2Empty = true;
        for (int i = P2_HOME + 1; i < TOTAL_SMALL_SQUARES + 2; i++) {
            if (table[i] > 0) {
                player2Empty = false;
                break;
            }
        }
        
        if (player1Empty){
            return 0;
        } else if (player2Empty){
            return 1;
        } else {
            return -1;
        }
    }
    
    //FIX: change to check square 0 and 6
    public boolean checkEnding() {
        return this.table[0] == 0 && this.table[6] == 0;
    }
    
    //FIX1: Remove validation
    //FIX2: Remove the check if the player has 5 empty squares, instead use the input parameter as which player needs to be filled
    public boolean fillGem(int player) {
        int startIndex;
        if (player == 0) {
            startIndex = P1_HOME + 1;
        } else {
            startIndex = P2_HOME + 1;
        }
        
        // Add 1 pawn to each empty square
        for (int i = startIndex; i < startIndex + 5; i++) {
            table[i] = 1;
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
            System.out.print(table[i] + " ");
        }
        System.out.println();
        System.out.print("P2 squares (11 down to 7): ");
        for (int i = TOTAL_SMALL_SQUARES + 1; i > P2_HOME; i--) {
            System.out.print(table[i] + " ");
        }
        System.out.println();
        System.out.println("P2 Home (index " + P2_HOME + "): " + this.getP2Square());
        System.out.println("P2 King: " + p2King + ", P2 Pawn: " + p2Pawn);
        System.out.println("==================");
    }
}