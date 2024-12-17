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

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
  private SurfaceHolder holder;
  private GameLoop gameLoop;
  private Random random = new Random();
  private float x, y;
  private float yMoveDirection = 3;
  private ArrayList<PointF> skeletons = new ArrayList<>();

  private int playerFaceDirection, playerAnimationIndexY;
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
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      //x = event.getX();
      //y = event.getY();
      yMoveDirection = -yMoveDirection;
      if (playerFaceDirection == 0) {
        playerFaceDirection = 1;
      } else {
        playerFaceDirection = 0;
      }
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
    for (int i = 0; i < skeletons.size();i++) {
      canvas.drawBitmap(GameCharacters.SKELETON.getSprite(0,0), skeletons.get(i).x, skeletons.get(i).y, null);
    }
    holder.unlockCanvasAndPost(canvas);
  }

  public void update(double delta) {
    for (PointF skeletonPosition: skeletons) {
      skeletonPosition.y += delta * 300;
      if (skeletonPosition.y > 1980) {
        skeletonPosition.y = 0;
      }
    }
    y += yMoveDirection;
    if (y > 1980) {
      y = 0;
    }
    if (y < 0) {
      y = 1980;
    }
    updateAnimation();
  }
  private void updateAnimation() {
    aniTick++;
    if (aniTick >= aniSpeed) {
      aniTick = 0;
      playerAnimationIndexY++;
      if (playerAnimationIndexY > 4) {
        playerAnimationIndexY = 0;
      }
    }
  }
}
