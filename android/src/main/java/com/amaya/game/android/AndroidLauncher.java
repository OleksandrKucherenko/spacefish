package com.amaya.game.android;

import android.os.Bundle;

import com.amaya.game.Spacefish;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

    initialize(new Spacefish(), config);
  }
}
