package splendor.view;

import java.util.EnumMap;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * Helper class to highlight specific parts of the game board.
 */
final class GameBoardHighlighter {
  private EnumMap<HighlightableArea, Region> regions;
  private final Border highlight;

  GameBoardHighlighter() {
    regions = new EnumMap<>(HighlightableArea.class);
    highlight = new Border(
        new BorderStroke(
            Color.CYAN, BorderStrokeStyle.SOLID, new CornerRadii(10.0), BorderStroke.THIN
        )
    );
  }

  void setRegions(Region nobles,
                  Region allDevCards,
                  Region lvl3DevCards,
                  Region lvl2DevCards,
                  Region lvl1DevCard,
                  Region tokens,
                  Region cities) {
    regions.put(HighlightableArea.NOBLES, nobles);
    regions.put(HighlightableArea.DEV_CARDS_ALL, allDevCards);
    regions.put(HighlightableArea.DEV_CARDS_LVL_THREE, lvl3DevCards);
    regions.put(HighlightableArea.DEV_CARDS_LVL_TWO, lvl2DevCards);
    regions.put(HighlightableArea.DEV_CARDS_LVL_ONE, lvl1DevCard);
    regions.put(HighlightableArea.TOKENS, tokens);
    regions.put(HighlightableArea.CITIES, cities);
  }

  void highlightArea(HighlightableArea area, boolean b) {
    if (b) {
      regions.get(area).setBorder(highlight);
    } else {
      regions.get(area).setBorder(Border.EMPTY);
    }
  }
}
