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
    }
}