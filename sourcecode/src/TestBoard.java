public class TestBoard {
    public static void main(String args[]) {
        Board board = new Board();
        
        System.out.println("=== INITIAL BOARD STATE ===");
        board.printBoard();
        
        // Simulate a series of moves
        board.moveGem(2, 1, Board.counterClockwise());
        board.printBoard();

        board.moveGem(9, 2, Board.counterClockwise());
        board.printBoard();
        
        board.moveGem(3, 1, Board.clockwise());
        board.printBoard();
        
        board.moveGem(9, 2, Board.clockwise());
        board.printBoard();
        
        board.moveGem(1, 1, Board.counterClockwise());
        board.printBoard();
        board.moveGem(7, 2, Board.clockwise());
        board.printBoard();
        board.moveGem(5, 1, Board.clockwise());
        board.printBoard();
        board.moveGem(9, 2, Board.clockwise());
        board.printBoard();
        board.moveGem(1, 1, Board.counterClockwise());
        board.printBoard();
        board.moveGem(11, 2, Board.counterClockwise());
        board.printBoard();
        board.moveGem(5, 1, Board.clockwise());
        board.printBoard();
        board.moveGem(11, 2, Board.counterClockwise());
        board.printBoard();

        board.fillGem(1);
        board.printBoard();

        board.moveGem(1, 1, Board.counterClockwise());
        board.printBoard();

        board.fillGem(2);
        board.printBoard();

        board.moveGem(11, 2, Board.counterClockwise());
        board.printBoard();

        System.out.println("\n=== FINAL STATS ===");
        System.out.println("Player 1 - Captured Pawns: " + board.getP1Pawn() + ", Captured Kings: " + board.getP1King());
        System.out.println("Player 2 - Captured Pawns: " + board.getP2Pawn() + ", Captured Kings: " + board.getP2King());
        System.out.println("Total Score Player 1: " + (board.getP1Pawn() + (board.getP1King() * 5)));
        System.out.println("Total Score Player 2: " + (board.getP2Pawn() + (board.getP2King() * 5)));
    }
}