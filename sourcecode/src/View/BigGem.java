package View;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BigGem extends Circle {

    public static final double RADIUS = 16.0;
    public static final int VALUE = 5;

    public BigGem() {
        super(RADIUS, Color.web("#C0392B"));
        setStroke(Color.web("#7B241C"));
        setStrokeWidth(2);
    }
}
