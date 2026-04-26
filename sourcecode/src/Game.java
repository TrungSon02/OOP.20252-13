public class Game {
    // Attributes
    private Board board;
    private Player[] players;
    private int currentPlayer;
    private boolean isFinished;

    // Constructor
    public Game(Player player1Name, Player player2Name){
        this.players = new Player[]{player1Name, player2Name};
        this.board = new Board();
        this.currentPlayer = 0;
        this.isFinished = false;
    }

    // Logic to calculate and add score after a move ends
    public int handleCapture(){
        int totalCaptured = 0;
        // TODO: Call board.captureGems(stopIndex) to get captured gems
        // Then add totalCaptured to players[currentPlayer].addScore()
        return totalCaptured;
    }

    // Switch turn between player1 (0) and player2 (1)
    public void switchPlayer(){
        this.currentPlayer = 1 - this.currentPlayer;
    }

    // Update game status based on board state
    public void updateGameState(){
        this.isFinished = this.board.checkEnding();
    }

    // Getters
    public int getCurrentPlayer(){
        return this.currentPlayer;
    }

    public Board getBoard(){
        return this.board;
    }

    public boolean isFinished(){
        return this.isFinished;
    }

    // Return the current Player object
    public Player getActivePlayerObject(){
        return this.players[this.currentPlayer];
    }
}
