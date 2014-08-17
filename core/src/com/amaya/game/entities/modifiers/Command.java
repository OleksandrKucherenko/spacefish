package com.amaya.game.entities.modifiers;

import java.util.Collection;
import java.util.Iterator;

import static com.amaya.game.entities.Fish.Fields.HEALTH;
import static com.amaya.game.entities.Fish.Fields.POINTS;

/** Base class. Property change command or event raising command. */
public class Command {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** field or event name. */
  public final String Name;
  /** field or event value delta/change step. */
  public final float Value;
  /** Time of command creation. */
  public final float Timestamp = System.currentTimeMillis();

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Hidden constructor. use static methods for instance creation.
   *
   * @param name the name of the field or event
   * @param value the value change delta
   */
  protected Command(final String name, final float value) {
    Name = name;
    Value = value;
  }

  /**
   * Compose Life command.
   *
   * @param value the value
   * @return the command
   */
  public static Command life(final float value) {
    return new Command(HEALTH, value);
  }

  /**
   * Compose Points command.
   *
   * @param value the value
   * @return the command
   */
  public static Command points(final float value) {
    return new Command(POINTS, value);
  }

  /**
   * Compose Event command.
   *
   * @param name the name
   * @return the command
   */
  public static Command event(final String name) {
    return new EventCommand(name);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{name: " + Name + ", value: " + Value + "}";
  }

  /* [ STATIC METHODS ] ==================================================================================================================================== */

  /** find in commands collection first item of defined type. */
  public static <T extends Command> T findFirst(final Collection<Command> commands, final Class<T> clazz) {
    final Iterator<Command> iterator = commands.iterator();

    while (iterator.hasNext()) {
      final Command cmd = iterator.next();

      if (clazz.isAssignableFrom(cmd.getClass())) {
        return (T) cmd;
      }
    }

    return null;
  }
}
