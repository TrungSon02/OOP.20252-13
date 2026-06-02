package View;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Draws gems inside each cell. This is pure view work, pulled out of the
 * controller: the controller just tells the renderer "cell i now has n gems".
 *
 * Each cell gets one transparent, unmanaged {@link Pane} ("pile") inserted just
 * below its count label, into which the gems are placed by absolute position.
 */
public class GemRenderer {

    private static final int GEMS_PER_ROW = 4;

    private final StackPane[] cellPanes;
    private final Label[] cellLabels;
    private final boolean[] bigCell;
    private final Pane[] piles;

    /**
     * @param cellPanes  the StackPane wrapping each cell (index = cell id)
     * @param cellLabels the count label of each cell (kept on top of the gems)
     * @param bigCell    true for cells that should use {@link BigGem} (the castles)
     */
    public GemRenderer(StackPane[] cellPanes, Label[] cellLabels, boolean[] bigCell) {
        this.cellPanes = cellPanes;
        this.cellLabels = cellLabels;
        this.bigCell = bigCell;
        this.piles = new Pane[cellPanes.length];
    }

    /** Redraw every cell from a full board snapshot. */
    public void renderAll(int[] counts) {
        for (int i = 0; i < cellPanes.length; i++) {
            render(i, counts[i]);
        }
    }

    /** Redraw a single cell with {@code count} gems, replacing what was there. */
    public void render(int cellIndex, int count) {
        if (cellIndex < 0 || cellIndex >= cellPanes.length) return;
        if (cellPanes[cellIndex] == null) return;

        Pane pile = ensurePile(cellIndex);
        pile.getChildren().clear();

        Bounds cb = cellPanes[cellIndex].getLayoutBounds();
        double cellW = cb.getWidth()  > 0 ? cb.getWidth()  : 150;
        double cellH = cb.getHeight() > 0 ? cb.getHeight() : 150;

        for (int i = 0; i < count; i++) {
            BaseGem gem = bigCell[cellIndex] ? new BigGem() : new SmallGem();
            positionGem(gem, i, cellW, cellH);
            pile.getChildren().add(gem);
        }

        if (cellLabels[cellIndex] != null) {
            cellLabels[cellIndex].toFront(); // keep the number readable on top of the gems
        }
    }

    /** Lay a gem out in a centered grid based on its own radius. */
    private void positionGem(BaseGem gem, int idx, double cellW, double cellH) {
        double spacing = gem.getRadius() * 2.2;
        int row = idx / GEMS_PER_ROW;
        int col = idx % GEMS_PER_ROW;

        double startX = cellW / 2.0 - (GEMS_PER_ROW - 1) * spacing / 2.0;
        double startY = cellH / 2.0 - spacing;

        double jitterX = (Math.random() - 0.5) * 3;
        double jitterY = (Math.random() - 0.5) * 3;

        gem.setLayoutX(startX + col * spacing + jitterX);
        gem.setLayoutY(startY + row * spacing + jitterY);
    }

    /** Create (once) the transparent gem layer inside a cell's StackPane. */
    private Pane ensurePile(int cellIndex) {
        if (piles[cellIndex] != null) return piles[cellIndex];

        Pane pile = new Pane();
        pile.setMouseTransparent(true);  // never intercept clicks meant for the overlay/cell
        pile.setPickOnBounds(false);
        pile.setManaged(false);          // gems use absolute coords; don't let StackPane re-center the layer
        piles[cellIndex] = pile;

        StackPane sp = cellPanes[cellIndex];
        int labelIdx = sp.getChildren().indexOf(cellLabels[cellIndex]);
        if (labelIdx >= 0) {
            sp.getChildren().add(labelIdx, pile); // below the label, above the cell background
        } else {
            sp.getChildren().add(pile);
        }
        return pile;
    }
}
