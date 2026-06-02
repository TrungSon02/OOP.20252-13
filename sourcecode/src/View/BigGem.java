package View;

import javafx.scene.paint.Color;

/**
 * A "quan" — a big gem that lives in the castle (big) cells.
 *
 * Per the rules of Ô Ăn Quan, a big gem is worth 5 small gems, so its value is
 * derived directly from {@link SmallGem#VALUE} to keep that relationship explicit.
 */
public class BigGem extends BaseGem {

    public static final double RADIUS = 16.0;
    /** A big gem is worth 5 small gems. */
    public static final int VALUE = 5 * SmallGem.VALUE;

    public BigGem() {
        super(RADIUS, Color.web("#C0392B"), VALUE);
        // a heavier outline so the quan reads clearly as the "big" piece
        setStroke(Color.web("#7B241C"));
        setStrokeWidth(2);
    }
}
