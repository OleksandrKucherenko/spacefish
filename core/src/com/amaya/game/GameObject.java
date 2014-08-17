package com.amaya.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** Base class for all type of game entities. */
public class GameObject {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Entity bounds */
  public final Rectangle Bounds;
  /** Game object speed. */
  private float mSpeed = 0;
  /** Reference on custom user data. */
  private Object mTag;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** default constructor leave it for inheritors. */
  protected GameObject() {
    this(0, 0, 0, 0);
  }

  public GameObject(final float x, final float y, final float width, final float height) {
    Bounds = new Rectangle(x, y, width, height);
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  public float getSpeed() {
    return mSpeed;
  }

  public void setSpeed(final float speed) {
    mSpeed = speed;
  }

  public Vector2 getPosition() {
    return new Vector2(Bounds.x, Bounds.y);
  }

  public void setPosition(final float x, final float y) {
    Bounds.setPosition(x, y);
  }

  public Object getTag() {
    return mTag;
  }

  public <T extends GameObject> T setTag(final Object tag) {
    mTag = tag;

    return (T) this;
  }
}
