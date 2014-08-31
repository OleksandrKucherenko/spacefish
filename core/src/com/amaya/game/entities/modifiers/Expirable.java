package com.amaya.game.entities.modifiers;

/** keep change till the expire happens. */
public class Expirable extends Modifier {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Command expiring time. game time units. */
  protected final float mExpiredAt;
  /** Game time when command first applied. */
  protected float mApplyTime;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  protected Expirable(final String name, final float value, final float seconds) {
    super(name, value);

    // convert seconds to millis
    mExpiredAt = seconds;
  }

  /* [ OVERRIDES ] ========================================================================================================================================= */

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{name: " + Name +
            ", value: " + Value +
            ", expiredAt: " + mExpiredAt +
            ", createdAt: " + Timestamp + "}";
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** is Command expired and should rollback own modifications. */
  public boolean isExpired(final float gameTime) {
    return (mApplyTime + mExpiredAt) < gameTime;
  }

  public float getExpiredAt() {
    return mExpiredAt;
  }

  public float getApplyTime() {
    return mApplyTime;
  }
}
