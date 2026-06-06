package Controller;

import View.CastleCellRenderer;
import View.CellRenderer;
import View.SmallCellRenderer;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GemRenderer {
    private final StackPane[] allCellPanes;
    private final Label[] allCells;
    
    private final Pane[] cellPiles;
    private final boolean[] checkBigGemExistence = { true, true };
    
    private final CellRenderer[] cellRenderers;

    public GemRenderer(StackPane[] allCellPanes, Label[] allCells) {
        this.allCellPanes = allCellPanes;
        this.allCells = allCells;
        this.cellPiles = new Pane[12];

        cellRenderers = new CellRenderer[12];
        for (int i = 0; i < 12; i++) {
            if (i == 0 || i == 6) {
                cellRenderers[i] = new CastleCellRenderer();
            } else {
                cellRenderers[i] = new SmallCellRenderer();
            }
        }
    }

    public void renderAllGems() {
        for (int i = 0; i < allCellPanes.length; i++) {
            renderGemsInCell(i, parseCount(allCells[i]));
        }
    }

    public void renderGemsInCell(int cellIndex, int count) {
        if (cellIndex < 0 || cellIndex >= allCellPanes.length) return;
        if (allCellPanes[cellIndex] == null) return;

        Pane pile = ensurePile(cellIndex);
        pile.getChildren().clear();

        Bounds cb = allCellPanes[cellIndex].getLayoutBounds();
        double cellW = cb.getWidth()  > 0 ? cb.getWidth()  : 150;
        double cellH = cb.getHeight() > 0 ? cb.getHeight() : 150;

        cellRenderers[cellIndex].render(pile, count, cellW, cellH);

        if (allCells[cellIndex] != null) {
            allCells[cellIndex].toFront();
        }
    }

    public void removeBigGem(int cellIndex) {
        ((CastleCellRenderer) cellRenderers[cellIndex]).unsetBigGem();
    }
    
    /** Lazily create a transparent layer inside the cell's StackPane that holds the gems. */
    private Pane ensurePile(int cellIndex) {
        if (cellPiles[cellIndex] != null) return cellPiles[cellIndex];

        Pane pile = new Pane();
        pile.setMouseTransparent(true);
        pile.setPickOnBounds(false);
        pile.setManaged(false);
        cellPiles[cellIndex] = pile;

        StackPane sp = allCellPanes[cellIndex];
        int labelIdx = sp.getChildren().indexOf(allCells[cellIndex]);
        if (labelIdx >= 0) {
            sp.getChildren().add(labelIdx, pile);
        } else {
            sp.getChildren().add(pile);
        }
        return pile;
    }

    /** Safely parse a label's text as an int, returns 0 if it can't. */
    private int parseCount(Label label) {
        if (label == null) return 0;
        try {
            return Integer.parseInt(label.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}