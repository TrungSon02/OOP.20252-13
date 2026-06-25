package Model;

import java.util.*;
import javafx.util.Pair;

public class Board {
    private int[] table;     // 10 small squares (5 per player) indices 1-5 for player1, 7-11 for player2, 0 for player 1, 6 for player 2
    private static final int TOTAL_SMALL_SQUARES = 10;
    private static final int P1_HOME = 0;
    private static final int P2_HOME = (TOTAL_SMALL_SQUARES + 2) / 2;
    private static final int INITIAL_PAWNS = 5;

    public Board() {
        table = new int[TOTAL_SMALL_SQUARES + 2];
        for (int i = 0; i < TOTAL_SMALL_SQUARES + 2; i++) {
            table[i] = INITIAL_PAWNS;
        }
    }

    public int getSquareValue(int index){
        return table[index];
    }

    public List<Pair<Integer, Integer>> moveGem(int startPosition, int direction) {
        List<Pair<Integer, Integer>> moveSequence = new ArrayList<>();

        int pawnsToMove = table[startPosition];
        moveSequence.add(new Pair<>(startPosition, 0));
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
                moveSequence.add(new Pair<>(nextSquare, 0));
                currentPos = nextSquare;
                continue;
            } else {
                canMove = false;
            }
        } 
        return moveSequence;
    }

    public boolean checkEmpty(int playerID) {
        int start = 1, end = 5;
        if(playerID == 1){
            start = 7;
            end = 11;
        }
        for (int i = start; i <= end; i++) {
            if (table[i] > 0) {
                return false;
            }
        }
        return true;
    }
    
    public void clearSquare(int square){
        table[square] = 0;
    }

    public boolean checkEnding() {
        return this.table[0] == 0 && this.table[6] == 0;
    }
    
    public void fillGem(int player) {
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
    }
}