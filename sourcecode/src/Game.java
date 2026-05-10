import java.util.ArrayList;
import java.util.List;

public class Game {
    private int[] board; //FIX: no need to create a new board logic. Use private Board board instead
    private Player[] players;
    private int currentPlayer;
    private boolean isFinished;

    public Game(Player player1, Player player2) {
        this.players = new Player[]{player1, player2}; 
        this.board = new int[12]; //FIX: board = new Board() instead
        this.currentPlayer = 0;
        this.isFinished = false;
        initBoard(); //FIX: Remove this 
    }

    // FIX: Remove this method. This belongs to Board class
    private void initBoard() {
        board[0] = 10;
        board[6] = 10;
        for (int i = 1; i <= 5; i++) board[i] = 5;
        for (int i = 7; i <= 11; i++) board[i] = 5;
    }

    // FIX: Remove this method. This belongs to Board class
    public List<Integer> sowGems(int startIndex, int direction) {
        List<Integer> moveSequence = new ArrayList<>();
        int gemsInHand = board[startIndex];
        board[startIndex] = 0;
        int currentIndex = startIndex;
        moveSequence.add(startIndex);

        while (gemsInHand > 0) {
            // Sowing gems around the board
            while (gemsInHand > 0) {
                currentIndex = (currentIndex + direction + 12) % 12;
                board[currentIndex]++;
                gemsInHand--;
                moveSequence.add(currentIndex);
            }

            // Check if next square allows for a chain move
            int nextIdx = (currentIndex + direction + 12) % 12;
            if (board[nextIdx] > 0 && nextIdx != 0 && nextIdx != 6) {
                gemsInHand = board[nextIdx];
                board[nextIdx] = 0;
                currentIndex = nextIdx;
                moveSequence.add(currentIndex);
            }
        }
        
        handleCapture(currentIndex, direction);
        return moveSequence;
    }

    // FIX1: Replace all the board[id] to board.getBoard()[id]
    // FIX2: Replace board[targetIdx] = 0, use a method clearSquare(id) in Board class instead
    // FIX3: IMPORTANT: forgot to handle deadend case. (Case when empty index being the big Square 0 or 6)
    public int handleCapture(int stopIndex, int direction) {
        int totalCaptured = 0;
        int currentIndex = stopIndex;

        while (true) {
            int emptyIdx = (currentIndex + direction + 12) % 12;
            int targetIdx = (emptyIdx + direction + 12) % 12;

            // Capture condition: one empty square followed by one with gems
            if (board[emptyIdx] == 0 && board[targetIdx] > 0) {
                int capturedGems = board[targetIdx]; 
                board[targetIdx] = 0;
                totalCaptured += capturedGems;
                currentIndex = targetIdx;
            } else {
                break;
            }
        }

        if (totalCaptured > 0) {
            players[currentPlayer].addScore(totalCaptured);
        }
        
        return totalCaptured;
    }

    // FIX: Remove this method
    public void replenishIfEmpty() {
        int start = (currentPlayer == 0) ? 1 : 7;
        boolean isEmpty = true;
        for (int i = start; i < start + 5; i++) {
            if (board[i] > 0) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty && players[currentPlayer].getScore() >= 5) {
            players[currentPlayer].addScore(-5);
            for (int i = start; i < start + 5; i++) {
                board[i] = 1;
            }
        }
    }

    public void switchPlayer() {
        this.currentPlayer = 1 - this.currentPlayer;
    }

    // Game ends when both big squares are empty
    public void updateGameState() {
        if (board[0] == 0 && board[6] == 0) {
            this.isFinished = true;
        }
    }

    //FIX: return the board instance instead
    public int[] getBoardState() {
        return this.board;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    //FIX: Remove this, no point
    public Player getActivePlayerObject() {
        return this.players[this.currentPlayer];
    }
}