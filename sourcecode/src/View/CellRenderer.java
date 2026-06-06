package View;

import javafx.scene.layout.Pane;

public interface CellRenderer {
    void render(Pane pile, int count, double cellW, double cellH);
}
