package ru.hawoline.androidgametutorial.environments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.hawoline.androidgametutorial.MainActivity;
import ru.hawoline.androidgametutorial.R;
import ru.hawoline.androidgametutorial.helpers.GameConstants;
import ru.hawoline.androidgametutorial.helpers.interfaces.BitmapMethods;

public enum Floor implements BitmapMethods {
  OUTSIDE(R.drawable.tileset_floor, 22, 26);

  private Bitmap[] sprites;
  Floor(int resId, int tilesInWidth, int tilesInHeight) {
    options.inScaled = false;
    sprites = new Bitmap[tilesInHeight * tilesInWidth];
    Bitmap spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);
    for (int row = 0; row < tilesInHeight; row++) {
      for (int column = 0; column < tilesInWidth; column++) {
        int index = row * tilesInWidth + column;
            sprites[index] = getScaledBitmap(Bitmap.createBitmap(spriteSheet,
                GameConstants.Sprite.DEFAULT_SIZE * column,
                GameConstants.Sprite.DEFAULT_SIZE * row,
                GameConstants.Sprite.DEFAULT_SIZE,
                GameConstants.Sprite.DEFAULT_SIZE)
            );
      }
    }
  }

  public Bitmap getSprite(int id) {
    return sprites[id];
  }
}
