package com.amaya.game.entities.modifiers;

/** Represent special event. */
public class EventCommand extends Command {
	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Instantiates a new Command.
   *
   * @param name the name of the event
   */
  protected EventCommand(final String name) {
    super(name, 0.0f);
  }

	/* [ OVERRIDES ] ========================================================================================================================================= */	

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{event: " + Name + "}";
  }
}
