package View;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class BaseGem extends Circle {
    private final int value;
    
    protected BaseGem(double radius, Color fill, int value) {
        super(radius, fill);
        this.value = value;
        setStroke(Color.color(0, 0, 0, 0.35));
        setStrokeWidth(1);
    }

    public int getValue() {
        return value;
    }
}
