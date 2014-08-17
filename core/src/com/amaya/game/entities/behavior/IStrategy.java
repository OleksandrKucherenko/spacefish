package com.amaya.game.entities.behavior;

import com.amaya.game.GameObject;
import com.amaya.game.entities.modifiers.Command;

import java.util.List;

/** Behavior strategy of the Entity in time. */
public abstract interface IStrategy {
  /** Update game object position according to list of command and delta time (in seconds). */
  public abstract void update(final GameObject entity, final List<Command> commands, final float delta);
}
