package View;

import java.util.List;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class SquareHighlighter {
    private Shape[] allOverlays;

    public SquareHighlighter(Shape[] allOverlays){
        this.allOverlays = allOverlays;
    }

    public void setYellowSquare(int squareID){
        setYellowSquare(allOverlays[squareID]);
    }

    public void setYellowSquare(Shape shape){
        shape.setFill(Color.rgb(255, 255, 0, 0.15));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(50);
        glow.setSpread(0.7);
        shape.setEffect(glow);
    }

    public void setRedSquare(int squareID){
        setRedSquare(allOverlays[squareID]);
    }

    public void setRedSquare(Shape shape){
        shape.setFill(Color.rgb(255, 0, 0, 0.15));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.RED);
        glow.setRadius(50);
        glow.setSpread(0.7);
        shape.setEffect(glow);
    }

    public void resetSquare(int squareID){
        resetSquare(allOverlays[squareID]);
    }

    public void resetSquare(Shape shape){
        shape.setFill(Color.TRANSPARENT);
        shape.setEffect(null);
    }

    public void resetAllSquares(){
        for(Shape shape : allOverlays){
            resetSquare(shape);
        }
    }

    public void highlightAvailableSquareState1(List<Integer> squares){
        for(int squareID : squares){
            setYellowSquare(allOverlays[squareID]);
        }
    }

    public void highlightAvailableSquareState2(int shapeID){
        setRedSquare(allOverlays[shapeID]);
        int LeftNeighbour = (shapeID + 1 + 12) % 12;
        int RightNeighbour = (shapeID - 1 + 12) % 12;
        setYellowSquare(allOverlays[LeftNeighbour]);
        setYellowSquare(allOverlays[RightNeighbour]);
    }

}
