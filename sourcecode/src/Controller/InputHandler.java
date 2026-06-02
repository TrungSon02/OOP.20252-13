package Controller;

import Model.Game;

/**
 * Owns the player's click interaction (extracted from the controller):
 *   1. pick a source square that belongs to the current player and has gems
 *   2. pick a direction (a neighbour of the source), which triggers the move
 * Clicking the source again cancels the selection.
 *
 * Validation is delegated to {@link Game}; the handler never touches the View.
 * It reports outcomes through {@link Listener} so the controller stays in
 * charge of highlighting and animating.
 */
public class InputHandler {

    /** Callbacks the controller implements to react to input. */
    public interface Listener {
        void onSourceSelected(int square);          // highlight the source + its neighbours
        void onSelectionCleared();                  // go back to "pick a source" highlighting
        void onMoveRequested(int square, int direction);
    }

    public static final int STATE_SELECT_SOURCE = 1;
    public static final int STATE_SELECT_DIRECTION = 2;
    public static final int STATE_LOCKED = -1;      // input ignored while a move animates

    private static final int CELL_COUNT = 12;

    private final Game game;
    private final Listener listener;

    private int state = STATE_SELECT_SOURCE;
    private int selectedSquare = -1;

    public InputHandler(Game game, Listener listener) {
        this.game = game;
        this.listener = listener;
    }

    /** Handle a click on the cell with the given index. */
    public void handleClick(int square) {
        if (state == STATE_SELECT_SOURCE) {
            if (game.canSelectSource(square)) {
                selectedSquare = square;
                state = STATE_SELECT_DIRECTION;
                listener.onSourceSelected(square);
            }
        } else if (state == STATE_SELECT_DIRECTION) {
            if (square == selectedSquare) {
                clearSelection();
                listener.onSelectionCleared();
            } else if (isNeighbour(square, selectedSquare)) {
                int direction = directionFrom(selectedSquare, square);
                int source = selectedSquare;
                state = STATE_LOCKED;
                listener.onMoveRequested(source, direction);
            }
        }
        // STATE_LOCKED: ignore everything until unlock()
    }

    /** Re-enable input once an animated move has fully finished. */
    public void unlock() {
        clearSelection();
    }

    private void clearSelection() {
        state = STATE_SELECT_SOURCE;
        selectedSquare = -1;
    }

    private boolean isNeighbour(int square, int origin) {
        return square == leftOf(origin) || square == rightOf(origin);
    }

    private int leftOf(int square)  { return (square + 1) % CELL_COUNT; }
    private int rightOf(int square) { return (square - 1 + CELL_COUNT) % CELL_COUNT; }

    /** Normalise the step to +1 (clockwise) or -1 (counter-clockwise), handling the 0/11 wrap. */
    private int directionFrom(int origin, int target) {
        int direction = target - origin;
        if (direction == -11) direction = 1;
        if (direction == 11)  direction = -1;
        return direction;
    }
}
