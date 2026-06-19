package Model;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class Game {
    private Board board;
    private Player[] players;
    private int currentPlayer;
    private boolean isFinished;
    private int winnerId = -1;

    public Game() {
        players = new Player[2];
        players[0] = new Player(0);
        players[1] = new Player(1);
        this.board = new Board(); 
        this.currentPlayer = 0;
        this.isFinished = false;
    }

    public int getWinnerId() {
        return this.winnerId;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getSquareValue(int id){
        return board.getSquareValue(id);
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public String getPlayerName(int id){
        return players[id].getName();
    }

    public String getPlayerAvatar(int id){
        return players[id].getAvatar();
    }

    public int getPlayerScore(int playerIndex){
        return players[playerIndex].getScore();
    }

    public boolean isBigCell(int cellIndex) {
        return cellIndex == 0 || cellIndex == 6;
    }

    private void switchPlayer() {
        this.currentPlayer = 1 - this.currentPlayer;
    }

    public List<Pair<Integer, Integer>> proccessingTurn(int startIndex, int direction){
        //Implement this method: Call moveGem -> Call handle capture
        //return: A complete sequence of moves from the start to the end (combine the existing moves in moveGem with new captured moves)
        List<Pair<Integer, Integer>> moveSequence = board.moveGem(startIndex, direction);
        if (moveSequence != null && !moveSequence.isEmpty()) {
            int stopIndex = moveSequence.get(moveSequence.size() - 1).getKey();
            List<Pair<Integer, Integer>> captureMoves = handleCapture(stopIndex, direction);
            moveSequence.addAll(captureMoves);
        }
        return moveSequence;
    }

    public List<Pair<Integer, Integer>> postTurnProcessing(){
        //Call checkEnding method from Board and update isFinished if needed, if game still continues, call checkEmpty from Board -> call fillGem if empty -> Switch players
        //If we need to fill empty square, return a list of moves. Else return an empty list
        List<Pair<Integer, Integer>> fillMoves = new ArrayList<>();
        if (board.checkEnding()) {
            this.isFinished = true;
            determineWinner();
            return fillMoves; 
        }
        int emptyPlayer = board.checkEmpty();
        if (emptyPlayer != -1) {
            board.fillGem(emptyPlayer);
            players[emptyPlayer].updateScore(-5);
            int startIndex = (emptyPlayer == 0) ? 1 : 7;
            for (int i = startIndex; i < startIndex + 5; i++) {
                fillMoves.add(new Pair<>(i, 1));
            }
        }
        switchPlayer();
        return fillMoves;
    }

    public boolean isValidSquareState1(int squareID){
        return getAvailableSquares().contains(squareID);
    }

    public boolean isValidDirection(int rootSquare, int directionSquare){
        int leftNeighbour = (rootSquare + 1 + 12) % 12;
        int rightNeighbour = (rootSquare - 1 + 12) % 12;
        if(directionSquare == leftNeighbour || directionSquare == rightNeighbour || directionSquare == rootSquare){
            return true;
        }
        return false;
    }

    public int getDirection(int rootSquare, int directionSquare){
        int direction = directionSquare - rootSquare;
        if(direction == -11){
            direction = 1; //Case when the selectedSquare = 11 and the direction square is 0
        }
        return direction;
    }

    public List<Integer> getAvailableSquares(){
        int start = 1, end = 5;
        if(currentPlayer == 1){
            start = 7;
            end = 11;
        }
        
        List<Integer> available = new ArrayList<>();
        for(int i = start; i <= end; i++){
            if(board.getSquareValue(i) > 0){
                available.add(i);
            }
        }
        return available;
    }

    private List<Pair<Integer, Integer>> handleCapture(int stopIndex, int direction) {
        List<Pair<Integer, Integer>> captureMoves = new ArrayList<>();
        int currentIndex = stopIndex;

        while (true) {
            int emptyIdx = Math.floorMod(currentIndex + direction, 12);
            int targetIdx = Math.floorMod(emptyIdx + direction, 12);

            if (board.getSquareValue(emptyIdx) == 0 && board.getSquareValue(targetIdx) > 0) {
                int capturedGems = board.getSquareValue(targetIdx); 
                board.clearSquare(targetIdx); 
                players[currentPlayer].updateScore(capturedGems);
                
                captureMoves.add(new Pair<>(targetIdx, 0));
                
                currentIndex = targetIdx;
            } else {
                break;
            }
        }
        return captureMoves;
    }

    private void determineWinner(){
        int score0 = players[0].getScore();
        int score1 = players[1].getScore();
        
        for (int i = 1; i <= 5; i++) {
            score0 += board.getSquareValue(i);
        }
        for (int i = 7; i <= 11; i++) {
            score1 += board.getSquareValue(i);
        }

        if (score0 > score1) {
            this.winnerId = 0;
        } else if (score1 > score0) {
            this.winnerId = 1;
        } else {
            this.winnerId = -1;
        }
    }

    public void collectRemainingGem(int playerID){
        int start = 1, end = 5;
        if(playerID == 1){
            start = 7;
            end = 11;
        }
        for (int i = start; i <= end; i++) {
            players[playerID].updateScore(board.getSquareValue(i));
        }
    }

}