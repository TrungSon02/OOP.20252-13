package View;

import javafx.scene.layout.Pane;

public class SmallCellRenderer implements CellRenderer{
    private static final int GEMS_PER_ROW = 4;

    @Override
    public void render(Pane pile, int count, double cellW, double cellH) {
        for (int i = 0; i < count; i++) {
            SmallGem gem = new SmallGem();
            GemLayoutUtil.placeInGrid(gem, i, GEMS_PER_ROW, cellW / 2.0, cellH / 2.0 - SmallGem.RADIUS * 2.2);
            pile.getChildren().add(gem);
        }
    }
}
