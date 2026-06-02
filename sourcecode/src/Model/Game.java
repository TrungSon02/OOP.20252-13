package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.util.Pair;

public class Game {
    private Board board;
    private Player[] players;
    private int currentPlayer;
    private boolean isFinished;

    public Game() {
        players = new Player[2];
        players[0] = new Player(0);
        players[1] = new Player(1);
        this.board = new Board();
        this.currentPlayer = 0;
        this.isFinished = false;
    }

    public List<Pair<Integer, Integer>> handleCapture(int stopIndex, int direction) {
        List<Pair<Integer, Integer>> captureMoves = new ArrayList<>();
        int totalCaptured = 0;
        int currentIndex = stopIndex;
        int[] table = board.getTable();
        while (true) {
            int emptyIdx = Math.floorMod(currentIndex + direction, 12);
            int targetIdx = Math.floorMod(emptyIdx + direction, 12);
            if (table[emptyIdx] == 0 && table[targetIdx] > 0) {
                int capturedGems = table[targetIdx];
                board.clearSquare(targetIdx);
                totalCaptured += capturedGems;

                captureMoves.add(new Pair<>(targetIdx, 0));

                currentIndex = targetIdx;
            } else {
                break;
            }
        }
        if (totalCaptured > 0) {
            players[currentPlayer].updateScore(totalCaptured);
        }

        return captureMoves;
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

    public Player[] getPlayers() {
        return this.players;
    }

    public List<Pair<Integer, Integer>> proccessingTurn(int startIndex, int direction){
        List<Pair<Integer, Integer>> moveSequence = board.moveGem(startIndex, direction);
        if (moveSequence != null && !moveSequence.isEmpty()) {
            int stopIndex = moveSequence.get(moveSequence.size() - 1).getKey();
            List<Pair<Integer, Integer>> captureMoves = handleCapture(stopIndex, direction);
            moveSequence.addAll(captureMoves);
        }
        return moveSequence;
    }

    public List<Pair<Integer, Integer>> postTurnProcessing(){
        List<Pair<Integer, Integer>> fillMoves = new ArrayList<>();
        if (board.checkEnding()) {
            this.isFinished = true;
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

    // ---------------- Read-only queries for the View/Controller (MVC) ----------------
    // These let the controller work against Game alone, without reaching into Board or Player.

    /** Number of gems currently in a square (0 if out of range). */
    public int getGemCount(int square) {
        int[] table = board.getTable();
        if (square < 0 || square >= table.length) return 0;
        return table[square];
    }

    /** Defensive copy of the whole board, for rendering. */
    public int[] getBoardSnapshot() {
        int[] table = board.getTable();
        return Arrays.copyOf(table, table.length);
    }

    /** True for the two big "castle" cells. */
    public boolean isCastleSquare(int square) {
        return square == 0 || square == 6;
    }

    /** Whether the current player may start a move from this square. */
    public boolean canSelectSource(int square) {
        if (getGemCount(square) <= 0) return false;
        if (currentPlayer == 0) return square >= 1 && square <= 5;
        return square >= 7 && square <= 11;
    }

    public String getPlayerName(int index)   { return players[index].getName(); }
    public int    getPlayerScore(int index)  { return players[index].getScore(); }
    public String getPlayerAvatar(int index) { return players[index].getAvatar(); }
}
