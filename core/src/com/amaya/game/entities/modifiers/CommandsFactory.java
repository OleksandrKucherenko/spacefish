package com.amaya.game.entities.modifiers;

import com.amaya.game.entities.Fish;
import com.amaya.game.entities.behavior.IOwnTrajectory;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.amaya.game.entities.Fish.Fields.HEALTH;
import static com.amaya.game.entities.Fish.Fields.POINTS;

/** */
public final class CommandsFactory {
  /* [ STATIC METHODS ] ==================================================================================================================================== */

  /**
   * Compose Life command.
   *
   * @param value the value
   * @return the command
   */
  public static Mandate life(final float value) {
    return new Modifier(HEALTH, value);
  }

  /**
   * Compose Points command.
   *
   * @param value the value
   * @return the command
   */
  public static Mandate points(final float value) {
    return new Modifier(POINTS, value);
  }

  /**
   * Compose Event command.
   *
   * @param name the name
   * @return the command
   */
  public static Mandate event(final String name) {
    return new Event(name);
  }

  public static Mandate speed(final float value, final int seconds) {
    return new Expirable(Fish.Fields.SPEED, value, seconds);
  }

  public static Mandate moveTo(final Rectangle start, final Vector3 point) {
    return new MoveTo(start.x, start.y, point.x, point.y);
  }

  public static Mandate moveTo(final Vector2 start, final Vector2 point) {
    return new MoveTo(start.x, start.y, point.x, point.y);
  }

  public static Mandate moveTo(final IOwnTrajectory iot) {
    return moveTo(iot.getStart(), iot.getEnd());
  }
}
