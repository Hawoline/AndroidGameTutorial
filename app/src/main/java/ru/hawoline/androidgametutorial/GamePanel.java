package ru.hawoline.androidgametutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Random;
import ru.hawoline.androidgametutorial.entities.GameCharacters;
import ru.hawoline.androidgametutorial.helpers.GameConstants;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
  private SurfaceHolder holder;
  private GameLoop gameLoop;
  private Random random = new Random();
  private float x, y;
  private float yMoveDirection = 3;
  //private ArrayList<PointF> skeletons = new ArrayList<>();
  private PointF skeletonPosition;
  private int skeletonDirection = GameConstants.FaceDirection.DOWN;
  private long lastSkeletonDirectionChange = System.currentTimeMillis();

  private int playerFaceDirection = GameConstants.FaceDirection.RIGHT;
  private int playerAnimationIndexY;
  private int aniTick;
  private int aniSpeed = 10;

  public GamePanel(Context context) {
    super(context);
    holder = getHolder();
    holder.addCallback(this);
    gameLoop = new GameLoop(this);
    //for (int  i = 0; i < 50; i++) {
    //  skeletons.add(new PointF(random.nextInt(1080), random.nextInt(540)));
    //}
    skeletonPosition =  new PointF(random.nextInt(1080), random.nextInt(540));
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      float newX = event.getX();
      float newY = event.getY();

      float xDifference = Math.abs(newX - x);
      float yDifference = Math.abs(newY - y);
      if (xDifference > yDifference) {
        if (newX > x) {
          playerFaceDirection = GameConstants.FaceDirection.RIGHT;
        } else {
          playerFaceDirection = GameConstants.FaceDirection.LEFT;
        }
      } else {
        if (newY > y) {
          playerFaceDirection = GameConstants.FaceDirection.DOWN;
        } else {
          playerFaceDirection = GameConstants.FaceDirection.UP;
        }
      }
      x = event.getX();
      y = event.getY();
    }
    return true;
  }

  @Override public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
    gameLoop.startGameLoop();
  }

  @Override
  public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

  }

  @Override public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

  }

  public void render() {
    Canvas canvas = holder.lockCanvas();
    canvas.drawColor(Color.BLACK);
    canvas.drawBitmap(GameCharacters.PLAYER.getSprite(playerAnimationIndexY, playerFaceDirection), x, y, null);
    //for (int i = 0; i < skeletons.size();i++) {
    //  canvas.drawBitmap(GameCharacters.SKELETON.getSprite(0,0), skeletons.get(i).x, skeletons.get(i).y, null);
    //}
      canvas.drawBitmap(GameCharacters.SKELETON.getSprite(playerAnimationIndexY,skeletonDirection), skeletonPosition.x, skeletonPosition.y, null);

    holder.unlockCanvasAndPost(canvas);
  }

  public void update(double delta) {
    //for (PointF skeletonPosition: skeletons) {
    //  skeletonPosition.y += delta * 300;
    //  if (skeletonPosition.y > 1980) {
    //    skeletonPosition.y = 0;
    //  }
    //}

    if (System.currentTimeMillis() - lastSkeletonDirectionChange > 3000) {
      skeletonDirection = random.nextInt(4);
      lastSkeletonDirectionChange = System.currentTimeMillis();
    }
    switch (skeletonDirection) {
      case GameConstants.FaceDirection.DOWN:
        skeletonPosition.y += delta * 300;
        if (skeletonPosition.y > 1980) {
          skeletonDirection = GameConstants.FaceDirection.UP;
        }
        break;
      case GameConstants.FaceDirection.UP:
        skeletonPosition.y -= delta * 300;
        if (skeletonPosition.y < 0) {
          skeletonDirection = GameConstants.FaceDirection.DOWN;
        }
        break;
      case GameConstants.FaceDirection.RIGHT:
        skeletonPosition.x += delta * 300;
        if (skeletonPosition.x > 1080) {
          skeletonDirection = GameConstants.FaceDirection.LEFT;
        }
        break;
      case GameConstants.FaceDirection.LEFT:
        skeletonPosition.x -= delta * 300;
        if (skeletonPosition.x < 0) {
          skeletonDirection = GameConstants.FaceDirection.RIGHT;
        }
        break;
    }
    updateAnimation();
  }
  private void updateAnimation() {
    aniTick++;
    if (aniTick >= aniSpeed) {
      aniTick = 0;
      playerAnimationIndexY++;
      if (playerAnimationIndexY > 3) {
        playerAnimationIndexY = 0;
      }
    }
  }
}
