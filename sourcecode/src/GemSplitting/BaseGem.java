package View;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Base class for every gem drawn on the board.
 * A gem is just a styled {@link Circle}; concrete subclasses decide their
 * size and colour palette. This class only holds the look shared by all gems.
 */
public abstract class BaseGem extends Circle {

    /** Shared source of randomness for colour/jitter so subclasses don't each make their own. */
    protected static final Random RNG = new Random();

    protected BaseGem(double radius, Color fill) {
        super(radius, fill);
        setStroke(Color.color(0, 0, 0, 0.35));
        setStrokeWidth(1);
    }
}
