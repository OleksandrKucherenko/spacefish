package com.amaya.game.entities.controls;

import com.amaya.game.entities.GameObject;

/** Base class for UI controls. Introduce functionality of Tag into Game object. */
public class BaseControl extends GameObject {
  /* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /* package */ BaseControl(float x, float y, float width, float height, final Object tag) {
    super(x, y, width, height);
    setTag(tag);
  }
}
