package View;

import java.util.Random;

import javafx.scene.paint.Color;

/**
 * A "dân" — an ordinary small gem. Worth 1 point.
 * Picks a random colour from the board palette each time one is created.
 */
public class SmallGem extends BaseGem {

    public static final double RADIUS = 7.0;
    public static final int VALUE = 1;

    private static final Color[] COLORS = new Color[] {
        Color.web("#E8C547"), Color.web("#D64545"), Color.web("#3FA34D"),
        Color.web("#3A86FF"), Color.web("#F5F5DC"), Color.web("#A06CD5")
    };
    private static final Random RNG = new Random();

    public SmallGem() {
        super(RADIUS, COLORS[RNG.nextInt(COLORS.length)], VALUE);
    }
}
