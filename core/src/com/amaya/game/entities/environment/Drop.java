package com.amaya.game.entities.environment;

import com.amaya.game.GameObject;
import com.amaya.game.Spacefish;
import com.amaya.game.entities.behavior.IOwnTrajectory;
import com.amaya.game.entities.behavior.IStrategy;
import com.amaya.game.entities.behavior.StrategiesFactory;
import com.amaya.game.entities.modifiers.Command;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/** Abstract dropped item on the game field. */
public abstract class Drop
        extends GameObject
        implements IOwnTrajectory {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** Logging tag. */
  public static final String TAG = Spacefish.LOG_TAG;
  /** Minimalistic speed of drop entity. */
  public static final float MIN_SPEED = 0.1f;
  /** Max allowed speed of drop entity. */
  public static final float MAX_SPEED = 0.9f;
  /** Dummy offset for game object, outside the game screen. */
  public static final float DUMMY_OFFSET = -1000.0f;

	/* [ MEMBERS ] =========================================================================================================================================== */

  /** Drop object behavior on game field. */
  public final IStrategy Behavior;
  /** Command/Modifier applied on collision. */
  public final Command Modifier;
  /** Start point of the trajectory */
  protected final Vector2 mStart = new Vector2(0, 0);
  /** End point of the trajectory */
  protected final Vector2 mEnd = new Vector2(0, 0);

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** Create game field entity that we can 'drop' on field with custom Modifier and custom Strategy. */
  protected Drop(final Command modifier, final IStrategy behavior) {
    Behavior = (null == behavior) ? StrategiesFactory.WaterGravity : behavior;
    Modifier = modifier;

    // speed of each object is different
    setSpeed(MIN_SPEED + Spacefish.randomFloat(MAX_SPEED));
  }

  /** Create game field entity that we can 'drop' on field with custom Modifier. */
  protected Drop(final Command modifier) {
    this(modifier, null);
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** Get defined for entity trajectory start point. */
  public Vector2 getStart() {
    return mStart;
  }

  /** Get defined for entity trajectory end point. */
  public Vector2 getEnd() {
    return mEnd;
  }

  /** Randomize trajectory but start from defined xOffset. */
  public <T extends Drop> T randomizeTrajectory(final float xOffset) {
    // define start and end points
    mStart.set(xOffset, Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT);
    mEnd.set(Spacefish.randomInt(Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH), 0);

    // update current position
    setPosition(mStart.x, mStart.y);

    if (Spacefish.Debug.MOVE_TRAJECTORY)
      Gdx.app.log(TAG, "Trajectory - start: " + mStart + ", end: " + mEnd);

    return (T) this;
  }
}
