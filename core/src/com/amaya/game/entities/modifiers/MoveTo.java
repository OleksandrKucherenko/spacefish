package com.amaya.game.entities.modifiers;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.Fish;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/** Ask fish to move */
public class MoveTo extends Mandate {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** start point x-coordinate */
  public final float StartX;
  /** start point y-coordinate */
  public final float StartY;
  /** end point x-coordinate */
  public final float EndX;
  /** end point y-coordinate */
  public final float EndY;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** hidden constructor. Use static methods for getting instance. */
  protected MoveTo(float x, float y, float x1, float y1) {
    super(Fish.Fields.POSITION);

    StartX = x;
    StartY = y;
    EndX = x1;
    EndY = y1;

    if (Spacefish.Debug.MOVE_COMMANDS)
      Gdx.app.log(Spacefish.LOG_TAG, "[move-to] start: " + getStart() + ", end: " + getEnd());
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{name: " + Name +
            ", start: [" + StartX + ", " + StartY + "]" +
            ", end: [" + EndX + ", " + EndY + "]}";
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** Get end point as Vector2. */
  public Vector2 getEnd() {
    return new Vector2(EndX, EndY);
  }

  /** Get start point as Vector2. */
  public Vector2 getStart() {
    return new Vector2(StartX, StartY);
  }
}
