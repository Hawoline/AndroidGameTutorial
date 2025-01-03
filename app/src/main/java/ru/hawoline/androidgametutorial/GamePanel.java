package ru.hawoline.androidgametutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Random;
import ru.hawoline.androidgametutorial.entities.GameCharacters;
import ru.hawoline.androidgametutorial.environments.GameMap;
import ru.hawoline.androidgametutorial.helpers.GameConstants;
import ru.hawoline.androidgametutorial.inputs.TouchEvents;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
  private SurfaceHolder holder;
  private GameLoop gameLoop;
  private Random random = new Random();
  private float x, y;
  private PointF skeletonPosition;
  private int skeletonDirection = GameConstants.FaceDirection.DOWN;
  private long lastSkeletonDirectionChange = System.currentTimeMillis();

  private int playerFaceDirection = GameConstants.FaceDirection.RIGHT;
  private int playerAnimationIndexY;
  private int animationTick;
  private int aniSpeed = 10;
  private TouchEvents touchEvents;
  private PointF lastTouchDiff;
  private boolean movePlayer;

  private GameMap testMap;

  public GamePanel(Context context) {
    super(context);
    holder = getHolder();
    holder.addCallback(this);
    touchEvents = new TouchEvents(this);
    gameLoop = new GameLoop(this);
    skeletonPosition =  new PointF(random.nextInt(MainActivity.GAME_WIDTH), random.nextInt(MainActivity.GAME_HEIGHT));

    int[][] spriteIds = {
        {454, 276, 275, 275, 190, 275, 275, 279, 275, 275, 275, 297, 110, 0, 1, 1, 1, 2, 110, 132},
        {454, 275, 169, 232, 238, 275, 275, 275, 276, 275, 275, 297, 110, 22, 89, 23, 23, 24, 110, 132},
        {454, 275, 190, 276, 275, 275, 279, 275, 275, 275, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 275, 190, 279, 275, 275, 169, 233, 275, 275, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 275, 190, 276, 277, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 88, 23, 24, 110, 132},
        {454, 275, 235, 232, 232, 232, 260, 279, 276, 279, 275, 297, 110, 22, 23, 89, 23, 24, 110, 132},
        {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 275, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 277, 275, 275, 279, 275, 257, 232, 232, 232, 238, 297, 110, 22, 88, 23, 23, 24, 110, 132},
        {454, 275, 275, 275, 275, 275, 190, 279, 275, 275, 275, 297, 110, 22, 23, 23, 88, 24, 110, 132},
        {454, 275, 275, 275, 275, 275, 190, 279, 279, 279, 279, 297, 110, 22, 23, 23, 23, 24, 110, 132},
        {454, 169, 232, 232, 232, 232, 239, 232, 232, 232, 172, 297, 110, 22, 23, 89, 23, 24, 110, 132},
        {454, 190, 279, 275, 275, 275, 275, 275, 275, 275, 190, 297, 110, 44, 45, 45, 45, 46, 110, 132}
    };

    testMap = new GameMap(spriteIds);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return touchEvents.touchEvent(event);
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
    if (canvas == null) {
      return;
    }
    canvas.drawColor(Color.BLACK);
    testMap.draw(canvas);
    touchEvents.draw(canvas);
    canvas.drawBitmap(GameCharacters.PLAYER.getSprite(playerAnimationIndexY, playerFaceDirection), x, y, null);
    canvas.drawBitmap(GameCharacters.SKELETON.getSprite(playerAnimationIndexY,skeletonDirection), skeletonPosition.x, skeletonPosition.y, null);

    holder.unlockCanvasAndPost(canvas);
  }

  public void update(double delta) {
    if (System.currentTimeMillis() - lastSkeletonDirectionChange > 3000) {
      skeletonDirection = random.nextInt(4);
      lastSkeletonDirectionChange = System.currentTimeMillis();
    }
    switch (skeletonDirection) {
      case GameConstants.FaceDirection.DOWN:
        skeletonPosition.y += delta * 300;
        if (skeletonPosition.y > MainActivity.GAME_HEIGHT) {
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
        if (skeletonPosition.x > MainActivity.GAME_WIDTH) {
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

    updatePlayerMove(delta);
    updateAnimation();
  }

  private void updatePlayerMove(double delta) {
    if (!movePlayer) {
      return;
    }

    float baseSpeed = (float) (delta * 300);
    float ratio = Math.abs(lastTouchDiff.y) / Math.abs(lastTouchDiff.x);
    double angle = Math.atan(ratio);

    float xSpeed = (float) Math.cos(angle);
    float ySpeed = (float) Math.sin(angle);
    //System.out.println("Angle: " + Math.toDegrees(angle));
    //System.out.println("xSpeed: " + xSpeed + " | ySpeed: " + ySpeed);

    if (xSpeed > ySpeed) {
      if (lastTouchDiff.x > 0) {
        playerFaceDirection = GameConstants.FaceDirection.RIGHT;
      } else {
        playerFaceDirection = GameConstants.FaceDirection.LEFT;
      }
    } else {
      if (lastTouchDiff.y > 0) {
        playerFaceDirection = GameConstants.FaceDirection.DOWN;
      } else {
        playerFaceDirection = GameConstants.FaceDirection.UP;
      }
    }
    if (lastTouchDiff.x < 0) {
      xSpeed *= -1;
    }
    if (lastTouchDiff.y < 0) {
      ySpeed *= -1;
    }

    x += xSpeed * baseSpeed;
    y += ySpeed * baseSpeed;
  }

  private void updateAnimation() {
    if (!movePlayer) {
      return;
    }
    animationTick++;
    if (animationTick >= aniSpeed) {
      animationTick = 0;
      playerAnimationIndexY++;
      if (playerAnimationIndexY > 3) {
        playerAnimationIndexY = 0;
      }
    }
  }

  public void setPlayerMoveTrue(PointF lastTouchDiff) {
    movePlayer = true;
    this.lastTouchDiff = lastTouchDiff;
  }

  public void setPlayerMoveFalse() {
    movePlayer = false;
    resetAnimation();
  }

  private void resetAnimation() {
    animationTick = 0;
    playerAnimationIndexY = 0;
  }
}
