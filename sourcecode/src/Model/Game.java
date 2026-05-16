package Model;
public class Game {
    private Board board;
    private Player[] players;
    private int currentPlayer;
    private boolean isFinished;

    public Game() {
        players = new Player[2];
        players[0] = new Player();
        players[1] = new Player();
        this.board = new Board(); 
        this.currentPlayer = 0;
        this.isFinished = false;
    }

    public int handleCapture(int stopIndex, int direction) {
        int totalCaptured = 0;
        int currentIndex = stopIndex;
        int[] table = board.getTable();

        while (true) {
            int emptyIdx = (currentIndex + direction + 12) % 12;
            int targetIdx = (emptyIdx + direction + 12) % 12;

            if (table[emptyIdx] == 0 && table[targetIdx] > 0) {
                int capturedGems = table[targetIdx]; 
                board.clearSquare(targetIdx); 
                totalCaptured += capturedGems;
                currentIndex = targetIdx;
            } else {
                break;
            }
        }

        if (totalCaptured > 0) {
            players[currentPlayer].updateScore(totalCaptured);
        }
        
        return totalCaptured;
    }

    public void switchPlayer() {
        this.currentPlayer = 1 - this.currentPlayer;
    }

    public void updateGameState() {
        if (board.getTable()[0] == 0 && board.getTable()[6] == 0) {
            this.isFinished = true;
        }
    }

    public Board getBoardState() {
        return this.board;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean isFinished() {
        return this.isFinished;
    }
<<<<<<< HEAD
}
=======

    public Player[] getPlayers() {
        return this.players;
    }
}
>>>>>>> feature/Controller
