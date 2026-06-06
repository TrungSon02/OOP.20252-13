package View;

import javafx.scene.layout.Pane;

public class CastleCellRenderer implements CellRenderer{
    private static final int GEMS_PER_ROW = 4;
    private boolean hasBigGem;

    public CastleCellRenderer() {
        this.hasBigGem = true;
    }

    @Override
    public void render(Pane pile, int count, double cellW, double cellH) {
        int smallCount = hasBigGem ? count - BigGem.VALUE : count;
        if (smallCount < 0) smallCount = 0;

        if (hasBigGem) {
            BigGem quan = new BigGem();
            quan.setLayoutX(cellW / 2.0);
            quan.setLayoutY(cellH / 2.0);
            pile.getChildren().add(quan);
        }

        double topY = cellH / 2.0 + BigGem.RADIUS + SmallGem.RADIUS;
        for (int i = 0; i < smallCount; i++) {
            SmallGem gem = new SmallGem();
            GemLayoutUtil.placeInGrid(gem, i, GEMS_PER_ROW, cellW / 2.0, topY);
            pile.getChildren().add(gem);
        }
    }

    public void unsetBigGem(){
        this.hasBigGem = false;
    }

}
