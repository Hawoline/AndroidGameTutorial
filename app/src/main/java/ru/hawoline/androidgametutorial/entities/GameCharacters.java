package ru.hawoline.androidgametutorial.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.hawoline.androidgametutorial.MainActivity;
import ru.hawoline.androidgametutorial.R;
import ru.hawoline.androidgametutorial.helpers.GameConstants;
import ru.hawoline.androidgametutorial.helpers.interfaces.BitmapMethods;

public enum GameCharacters implements BitmapMethods {
  PLAYER(R.drawable.player_spritesheet),
  SKELETON(R.drawable.skeleton_spritesheet);

  private Bitmap spriteSheet;
  private Bitmap[][] sprites = new Bitmap[7][4];
  GameCharacters(int resId) {
    options.inScaled = false;
    spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);
    for (int row = 0; row < sprites.length; row++) {
      for (int column = 0; column < sprites[row].length; column++) {
        sprites[row][column] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, GameConstants.Sprite.DEFAULT_SIZE * column, GameConstants.Sprite.DEFAULT_SIZE * row, GameConstants.Sprite.DEFAULT_SIZE, GameConstants.Sprite.DEFAULT_SIZE));
      }
    }
  }

  public Bitmap getSpriteSheet() {
    return spriteSheet;
  }

  public Bitmap getSprite(int row, int column) {
    return sprites[row][column];
  }
}
