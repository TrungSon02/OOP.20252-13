package View;

import java.util.Random;

public class GemLayoutUtil {
    private static final Random rng = new Random();

    public static void placeInGrid(BaseGem gem, int idx, int perRow, double centerX, double topY) {
        double spacing = gem.getRadius() * 2.2;
        int row = idx / perRow;
        int col = idx % perRow;
        double startX = centerX - (perRow - 1) * spacing / 2.0;
        double jitterX = (rng.nextDouble() - 0.5) * 3;
        double jitterY = (rng.nextDouble() - 0.5) * 3;
        gem.setLayoutX(startX + col * spacing + jitterX);
        gem.setLayoutY(topY + row * spacing + jitterY);
    }
}
