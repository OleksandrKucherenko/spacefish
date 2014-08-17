package com.amaya.game.entities.modifiers;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.Fish;
import com.amaya.game.entities.behavior.IOwnTrajectory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/** Ask fish to move */
public class MoveToCommand extends Command {
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
  protected MoveToCommand(float x, float y, float x1, float y1) {
    super(Fish.Fields.POSITION, 0);

    StartX = x;
    StartY = y;
    EndX = x1;
    EndY = y1;

    if (Spacefish.Debug.MOVE_COMMANDS)
      Gdx.app.log(Spacefish.LOG_TAG, "[move-to] start: " + getStart() + ", end: " + getEnd());
  }

  public static Command moveTo(final Rectangle start, final Vector3 point) {
    return new MoveToCommand(start.x, start.y, point.x, point.y);
  }

  public static Command moveTo(final Vector2 start, final Vector2 point) {
    return new MoveToCommand(start.x, start.y, point.x, point.y);
  }

  public static Command moveTo(final IOwnTrajectory iot) {
    return moveTo(iot.getStart(), iot.getEnd());
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
