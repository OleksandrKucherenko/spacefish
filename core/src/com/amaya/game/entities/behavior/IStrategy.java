package com.amaya.game.entities.behavior;

import com.amaya.game.entities.StrategyObject;
import com.amaya.game.entities.modifiers.Mandate;

import java.util.List;

/** Behavior strategy of the Entity in time. */
public abstract interface IStrategy {
  /** Update game object position according to list of command and delta time (in seconds). */
  public abstract void update(final StrategyObject entity, final List<Mandate> mandates, final float gameTime, final float delta);
}
