package View;

import javafx.scene.paint.Color;

/** A larger, richer-looking piece used in the two big "castle" cells (0 and 6). */
public class BigGem extends BaseGem {

    private static final double RADIUS = 12.0;
    private static final Color[] COLORS = {
        Color.web("#FFD700"), Color.web("#F4A300"), Color.web("#C0392B")
    };

    public BigGem() {
        super(RADIUS, COLORS[RNG.nextInt(COLORS.length)]);
        setStroke(Color.web("#7A5C00"));
        setStrokeWidth(1.5);
    }
}
