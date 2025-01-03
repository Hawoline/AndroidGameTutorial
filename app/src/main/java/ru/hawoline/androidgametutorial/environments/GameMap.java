package ru.hawoline.androidgametutorial.environments;

import android.graphics.Canvas;
import ru.hawoline.androidgametutorial.helpers.GameConstants;

public class GameMap {
  private int[][] spriteIds;
  public GameMap(int[][] spriteIds) {
    this.spriteIds = spriteIds;
  }
/*
0 0 0 0 0
0 0 0 0 0
0 0 0 0 0
0 0 0 0 0
 */
  public void draw(Canvas canvas) {
    for (int j = 0; j < spriteIds.length; j++) {
      for (int i = 0; i < spriteIds[j].length; i++) {
        canvas.drawBitmap(Floor.OUTSIDE.getSprite(spriteIds[j][i]), i * GameConstants.Sprite.SIZE, j * GameConstants.Sprite.SIZE, null);
      }
    }
  }
}
