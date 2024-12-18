package ru.hawoline.androidgametutorial.inputs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import ru.hawoline.androidgametutorial.GamePanel;

public class TouchEvents {
  private float xCenter = 540;
  private float yCenter = 1300;
  private float radius = 100;
  private GamePanel gamePanel;
  private Paint circlePaint, yellowPaint;
  private boolean touchDown;
  private float xTouch;
  private float yTouch;

  public TouchEvents(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    circlePaint = new Paint();
    circlePaint.setColor(Color.RED);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setStrokeWidth(5);
    yellowPaint = new Paint();
    yellowPaint.setColor(Color.YELLOW);
  }

  public void draw(Canvas canvas) {
    canvas.drawCircle(xCenter, yCenter, radius, circlePaint);
    if (touchDown) {
      canvas.drawLine(xCenter, yCenter, xTouch, yTouch, yellowPaint);
      canvas.drawLine(xCenter, yCenter, xTouch, yCenter, yellowPaint);
      canvas.drawLine(xTouch, yTouch, xTouch, yCenter, yellowPaint);
    }
  }

  public boolean touchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        float x = event.getX();
        float y = event.getY();

        float a = Math.abs(x - xCenter);
        float b = Math.abs(y - yCenter);
        float hypotenuse = (float) Math.hypot(a, b);
        if (hypotenuse < radius) {
          touchDown = true;
          xTouch = x;
          yTouch = y;
        }
        break;
      case MotionEvent.ACTION_MOVE:
        if (touchDown) {
          xTouch = event.getX();
          yTouch = event.getY();

          float xDiff = xTouch - xCenter;
          float yDiff = yTouch - yCenter;
          gamePanel.setPlayerMoveTrue(new PointF(xDiff, yDiff));
        }
        break;
      case MotionEvent.ACTION_UP:
        touchDown = false;
        gamePanel.setPlayerMoveFalse();
        break;
    }
    return true;
  }


}
