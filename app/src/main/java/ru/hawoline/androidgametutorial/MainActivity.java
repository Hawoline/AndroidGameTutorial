package ru.hawoline.androidgametutorial;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
  private static Context gameContext;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gameContext = this;
    setContentView(new GamePanel(this));
  }

  public static Context getGameContext() {
    return gameContext;
  }
}