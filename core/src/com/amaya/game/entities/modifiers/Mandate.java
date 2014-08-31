package com.amaya.game.entities.modifiers;

import java.util.Collection;
import java.util.Iterator;

/** Base class. Change mandate/request. */
public abstract class Mandate {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** field or event name. */
  public final String Name;
  /** Time of command creation. */
  public final float Timestamp = System.currentTimeMillis();

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Hidden constructor. use static methods for instance creation.
   *
   * @param name the name of the field or event
   */
  protected Mandate(final String name) {
    Name = name;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{name: " + Name + "}";
  }

  /* [ STATIC METHODS ] ==================================================================================================================================== */

  /** find in commands collection first item of defined type. */
  public static <T extends Mandate> T findFirst(final Collection<Mandate> mandates, final Class<T> clazz) {
    final Iterator<Mandate> iterator = mandates.iterator();

    while (iterator.hasNext()) {
      final Mandate cmd = iterator.next();

      if (clazz.isAssignableFrom(cmd.getClass())) {
        return (T) cmd;
      }
    }

    return null;
  }
}
