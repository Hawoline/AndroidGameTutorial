package ru.hawoline.androidgametutorial;

import android.util.Log;

public class GameLoop implements Runnable {
  private Thread thread;
  private GamePanel gamePanel;

  public GameLoop(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    thread = new Thread(this);
  }
  @Override public void run() {
    long lastFpsCheck = System.currentTimeMillis();
    int fps = 0;

    long lastDelta = System.nanoTime();
    long nanoSecond = 1_000_000_000;

    while (true) {
      long nowDelta = System.nanoTime();
      double timeSinceLastDelta = nowDelta - lastDelta;
      double delta = timeSinceLastDelta / nanoSecond;
      gamePanel.update(delta);
      gamePanel.render();
      lastDelta = nowDelta;

      fps++;

      long now = System.currentTimeMillis();
      if (now - lastFpsCheck >= 1000) {
        //System.out.println("FPS: " + fps);
        fps = 0;
        lastFpsCheck += 1000;
      }
    }
  }

  public void startGameLoop() {
    thread.start();
  }
}
