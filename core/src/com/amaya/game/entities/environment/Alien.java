package com.amaya.game.entities.environment;

import com.amaya.game.entities.modifiers.Command;

/** */
public class Alien extends Drop {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** dummy instance for operations 'without NULL'. */
  public static final Alien GREEN = green(DUMMY_OFFSET);
  /** dummy instance for operations 'without NULL'. */
  public static final Alien YELLOW = yellow(DUMMY_OFFSET);
  /** dummy instance for operations 'without NULL'. */
  public static final Alien ORANGE = orange(DUMMY_OFFSET);

  /** enum of known alien types. */
  public static enum KnownAliens {
    GREEN,
    YELLOW,
    ORANGE
  }

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** hidden constructor. Use static methods for instance creation. */
  protected Alien(final Command modifier) {
    super(modifier);
  }

  /** new instance of Green alien with randomized trajectory. */
  public static Alien green(final float xOffset) {
    return new Alien(Command.points(5))
            .randomizeTrajectory(xOffset)
            .setTag(KnownAliens.GREEN);
  }

  /** new instance of Yellow alien with randomized trajectory. */
  public static Alien yellow(final float xOffset) {
    return new Alien(Command.points(10))
            .randomizeTrajectory(xOffset)
            .setTag(KnownAliens.YELLOW);
  }

  /** new instance of Orange alien with randomized trajectory. */
  public static Alien orange(final float xOffset) {
    return new Alien(Command.points(30))
            .randomizeTrajectory(xOffset)
            .setTag(KnownAliens.ORANGE);
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** Get alien cost in points. */
  public int getPoints() {
    return (int) Modifier.Value;
  }
}
