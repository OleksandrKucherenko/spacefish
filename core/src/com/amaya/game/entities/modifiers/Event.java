package com.amaya.game.entities.modifiers;

/** Represent special event. */
public class Event extends Mandate {
  /* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Instantiates a new Command.
   *
   * @param name the name of the event
   */
  protected Event(final String name) {
    super(name);
  }

	/* [ OVERRIDES ] ========================================================================================================================================= */

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{event: " + Name + "}";
  }
}
