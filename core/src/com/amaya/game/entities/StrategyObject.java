package com.amaya.game.entities;

import com.amaya.game.entities.behavior.IStrategy;

/** */
public class StrategyObject extends GameObject {
  /** Game object speed. */
  private float mSpeed = 0;
  /** Movements strategy/behavior. */
  private IStrategy mStrategy;

  public StrategyObject(final float x, final float y, final float width, final float height) {
    super(x, y, width, height);
  }

  /* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  public float getSpeed() {
    return getSpeed(0.0f);
  }

  public float getSpeed(final float gameTime) {
    return mSpeed;
  }

  protected void setSpeed(final float speed) {
    mSpeed = speed;
  }

  public IStrategy getStrategy() {
    return mStrategy;
  }

  protected void setStrategy(final IStrategy strategy) {
    mStrategy = strategy;
  }
}
