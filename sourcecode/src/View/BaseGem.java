package View;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Base class for every gem drawn on the Ô Ăn Quan board.
 *
 * A gem is just a coloured {@link Circle} that also knows its own point value.
 * Concrete gems decide their radius, colour and value:
 *   - {@link SmallGem} ("dân")  -> worth 1
 *   - {@link BigGem}   ("quan") -> worth 5 small gems
 */
public abstract class BaseGem extends Circle {

    private final int value;

    /**
     * @param radius circle radius in pixels
     * @param fill   fill colour of the gem
     * @param value  how many points this gem is worth
     */
    protected BaseGem(double radius, Color fill, int value) {
        super(radius, fill);
        this.value = value;
        // shared look so all gems feel like the same set of pieces
        setStroke(Color.color(0, 0, 0, 0.35));
        setStrokeWidth(1);
    }

    /** Point value of this gem (small gem = 1, big gem = 5). */
    public int getValue() {
        return value;
    }
}
