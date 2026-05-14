package Model;

import java.util.*;
import javafx.util.Pair;


public class Board {
    // FIX: Remove all non constant attributes (and related parts) except the first one, change the logic to table[] (treating all squares the same)
    private int[] table;     // 10 small squares (5 per player) indices 1-5 for player1, 7-11 for player2, 0 for player 1, 6 for player 2
    private static final int TOTAL_SMALL_SQUARES = 10;
    private static final int P1_HOME = 0;
    private static final int P2_HOME = (TOTAL_SMALL_SQUARES + 2) / 2;
    private static final int INITIAL_PAWNS = 5;
    private static final int CLOCKWISE = 1;
    private static final int COUNTER_CLOCKWISE = -1;

    //FIX: Init all square (including big ones to 5)
    public Board() {
        table = new int[TOTAL_SMALL_SQUARES + 2];
        for (int i = 0; i < TOTAL_SMALL_SQUARES + 2; i++) {
            table[i] = INITIAL_PAWNS;
        }
    }
    
    // Getter methods for accessing board state
    public int[] getTable() {
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

    public List<Pair<Integer, Integer>> moveGem(int startPosition, int direction) {
        // use .getKey() to get the square number, use .getValue() to get the number of pawns in the square
        List<Pair<Integer, Integer>> moveSequence = new ArrayList<>();

        int pawnsToMove = table[startPosition];
        moveSequence.add(new Pair<>(startPosition, table[startPosition]));
        table[startPosition] = 0;
        
        int currentPos = startPosition;
        int remainingPawns = pawnsToMove;
        
        // Distribute pawms
        boolean canMove = true;
        while (canMove){
            while (remainingPawns > 0) {
                currentPos = Math.floorMod(currentPos + direction, TOTAL_SMALL_SQUARES + 2);
                table[currentPos]++;
                moveSequence.add(new Pair<>(currentPos, table[currentPos]));    
                remainingPawns--;
            }

            int nextSquare = Math.floorMod(currentPos + direction, TOTAL_SMALL_SQUARES + 2);
            if (nextSquare == P1_HOME || nextSquare == P2_HOME){
                canMove = false;
                break;
            }
            if (table[nextSquare] > 0){
                remainingPawns = table[nextSquare];
                table[nextSquare] = 0;
                currentPos = nextSquare;
                continue;
            } else {
                canMove = false;
            }
        } 
        return moveSequence;
    }

    //FIX: Change the return value to match player 0 and player 1
    public int checkEmpty() {
        // returns 0 if player 0's squares are empty, 1 if player 1's squares are empty and -1 if none is empty
        // Check player 0's squares
        boolean player0Empty = true;
        for (int i = 1; i < P2_HOME; i++) {
            if (table[i] > 0) {
                player0Empty = false;
                break;
            }
        }
        
        // Check player 1's squares
        boolean player1Empty = true;
        for (int i = P2_HOME + 1; i < TOTAL_SMALL_SQUARES + 2; i++) {
            if (table[i] > 0) {
                player1Empty = false;
                break;
            }
        }
        
        if (player0Empty){
            return 0;
        } else if (player1Empty){
            return 1;
        } else {
            return -1;
        }
    }
    
    public boolean clearSquare(int square){
        if (square < 0 || square > 11){
            return false;
        }
        table[square] = 0;
        return true;
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
}