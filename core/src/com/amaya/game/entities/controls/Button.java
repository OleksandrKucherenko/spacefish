package com.amaya.game.entities.controls;

/** Image that interact with user as button. */
public abstract class Button extends BaseControl {
  /* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public Button(float x, float y, float width, float height, final Object tag) {
    super(x, y, width, height, tag);
  }

	/* [ ABSTRACT ] ========================================================================================================================================== */

  public abstract void click();
}
