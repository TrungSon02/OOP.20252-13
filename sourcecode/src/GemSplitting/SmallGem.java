package View;

import javafx.scene.paint.Color;

/** A regular small piece, used in the ten small cells. */
public class SmallGem extends BaseGem {

    private static final double RADIUS = 7.0;
    private static final Color[] COLORS = {
        Color.web("#E8C547"), Color.web("#D64545"), Color.web("#3FA34D"),
        Color.web("#3A86FF"), Color.web("#F5F5DC"), Color.web("#A06CD5")
    };

    public SmallGem() {
        super(RADIUS, COLORS[RNG.nextInt(COLORS.length)]);
    }
}
