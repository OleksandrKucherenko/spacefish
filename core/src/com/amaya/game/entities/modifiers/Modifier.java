package com.amaya.game.entities.modifiers;

/** Modify property value. */
public class Modifier extends Mandate {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** field or event value delta/change step. */
  public final float Value;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Hidden constructor. use static methods for instance creation.
   *
   * @param name the name of the field or event
   * @param value the value change delta
   */
  protected Modifier(final String name, final float value) {
    super(name);

    this.Value = value;
  }

	/* [ OVERRIDES ] ========================================================================================================================================= */

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{name: " + Name +
            ", value: " + Value + "}";
  }
}
