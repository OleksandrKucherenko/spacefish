package com.amaya.game.entities;

import com.amaya.game.GameObject;
import com.amaya.game.entities.behavior.IStrategy;
import com.amaya.game.entities.behavior.StrategiesFactory;

import static com.amaya.game.Spacefish.Dimensions.FISH_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.FISH_WIDTH;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH;

/** Fish abstraction. represent user entity. */
public class Fish extends GameObject {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Fish health. */
  public int Health = 1;
  /** Collected points. */
  public int Points = 0;
  /** Movements strategy/behavior. */
  public final IStrategy Behavior = StrategiesFactory.MomentumMoveByVector;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** default constructor. */
  public Fish() {
    super((VIRTUAL_SCREEN_WIDTH - FISH_WIDTH) / 2, (VIRTUAL_SCREEN_HEIGHT - FISH_HEIGHT) / 2, FISH_WIDTH, FISH_HEIGHT);
    setSpeed(1.0f);
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  /** Reset entity to initial state. */
  public void reset() {
    Points = 0;
    Health = 1;
    setSpeed(1.0f);
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  /**
   * Syntax hack. reduce the number of 'static final' repeats during declarations,
   * in addition defines domain of the constants.
   */
  public interface Fields {
    /** speed of game object */
    String SPEED = "Speed";
    /** health */
    String HEALTH = "Health";
    /** score points */
    String POINTS = "Points";
    /** position */
    String POSITION = "Position";
  }
}
