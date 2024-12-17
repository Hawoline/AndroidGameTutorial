package ru.hawoline.androidgametutorial.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import ru.hawoline.androidgametutorial.MainActivity;
import ru.hawoline.androidgametutorial.R;

public enum GameCharacters {
  PLAYER(R.drawable.player_spritesheet),
  SKELETON(R.drawable.skeleton_spritesheet);

  private Bitmap spriteSheet;
  private Bitmap[][] sprites = new Bitmap[7][4];
  private BitmapFactory.Options options = new BitmapFactory.Options();
  GameCharacters(int resId) {
    options.inScaled = false;
    spriteSheet = BitmapFactory.decodeResource(MainActivity.getGameContext().getResources(), resId, options);
    for (int row = 0; row < sprites.length; row++) {
      for (int column = 0; column < sprites[row].length; column++) {
        sprites[row][column] = getScaledBitmap(Bitmap.createBitmap(spriteSheet, 16 * column, 16 * row, 16, 16));
      }
    }
  }

  public Bitmap getSpriteSheet() {
    return spriteSheet;
  }

  public Bitmap getSprite(int row, int column) {
    return sprites[row][column];
  }

  private Bitmap getScaledBitmap(Bitmap bitmap) {
    return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 6, bitmap.getHeight() * 6, false);
  }
}
