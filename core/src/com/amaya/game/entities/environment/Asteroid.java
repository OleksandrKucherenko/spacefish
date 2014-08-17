package com.amaya.game.entities.environment;

import com.amaya.game.GameController;
import com.amaya.game.entities.modifiers.Command;
import com.amaya.game.entities.modifiers.ExpiringCommand;

/** Describe asteroid type. */
public class Asteroid extends Drop {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** dummy instance for operations 'without NULL'. */
  public final static Asteroid SOUND = sound(DUMMY_OFFSET);
  /** dummy instance for operations 'without NULL'. */
  public final static Asteroid HALF_SPEED = speed(DUMMY_OFFSET);
  /** dummy instance for operations 'without NULL'. */
  public final static Asteroid DEATH = death(DUMMY_OFFSET);

  /** Known types of Asteroids. */
  public static enum KnownAsteroids {
    SOUND,
    SPEED,
    DEATH
  }

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** hidden constructor. Use static methods for instance getting. */
  protected Asteroid(final Command modifier) {
    super(modifier);
  }

  /** new instance of SOUND asteroid with randomized trajectory. */
  public static Asteroid sound(final float xOffset) {
    return new Asteroid(Command.event(GameController.Events.ANNOYING_SOUND))
            .randomizeTrajectory(xOffset)
            .setTag(KnownAsteroids.SOUND);
  }

  /** new instance of SPEED reducing asteroid with randomized trajectory. */
  public static Asteroid speed(final float xOffset) {
    return new Asteroid(ExpiringCommand.speed(0.5f, 2))
            .randomizeTrajectory(xOffset)
            .setTag(KnownAsteroids.SPEED);
  }

  /** new instance of DEATH asteroid with randomized trajectory. */
  public static Asteroid death(final float xOffset) {
    return new Asteroid(Command.life(-1))
            .randomizeTrajectory(xOffset)
            .setTag(KnownAsteroids.DEATH);
  }
}
