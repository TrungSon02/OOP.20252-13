package View;

import javafx.scene.paint.Color;

public class BigGem extends BaseGem {

    public static final double RADIUS = 16.0;
    public static final int VALUE = 5;

    public BigGem() {
        super(RADIUS, Color.web("#C0392B"), VALUE);
        setStroke(Color.web("#7B241C"));
        setStrokeWidth(2);
    }
}
