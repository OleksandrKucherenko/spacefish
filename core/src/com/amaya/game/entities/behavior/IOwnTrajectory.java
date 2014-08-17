package com.amaya.game.entities.behavior;

import com.badlogic.gdx.math.Vector2;

/** Implement interface if you want to adjust trajectory of the entity. */
public interface IOwnTrajectory {
  /** Get defined for entity trajectory start point. */
  Vector2 getStart();

  /** Get defined for entity trajectory end point. */
  Vector2 getEnd();
}
