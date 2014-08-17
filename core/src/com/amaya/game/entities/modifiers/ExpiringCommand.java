package com.amaya.game.entities.modifiers;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.Fish;
import com.badlogic.gdx.Gdx;

/** keep change till the expire happens. */
public class ExpiringCommand extends Command {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** Logging tag. */
  public static final String TAG = Spacefish.LOG_TAG;

	/* [ MEMBERS ] =========================================================================================================================================== */

  /** Command expiring time. game time units. */
  protected final float mExpiredAt;
  /** Game time when command first applied. */
  protected float mApplyTime;
  /** Store old value for recovering needs. */
  protected float mOldValue;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  protected ExpiringCommand(final String name, final float value, final float seconds) {
    super(name, value);

    // convert seconds to millis
    mExpiredAt = seconds;
  }

	/* [ STATIC METHODS ] ==================================================================================================================================== */

  public static ExpiringCommand speed(final float value, final int seconds) {
    return new ExpiringCommand(Fish.Fields.SPEED, value, seconds);
  }

	/* [ OVERRIDES ] ========================================================================================================================================= */

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "{name: " + Name + ", value: " + Value + ", expiredAt: " + mExpiredAt + ", createdAt: " + Timestamp + "}";
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** is Command applied on game entity already or not. */
  public boolean isApplied() {
    return mApplyTime > 0;
  }

  /** is Command expired and should rollback own modifications. */
  public boolean isExpired(final float gameTime) {
    if (Spacefish.Debug.EXPIRED_COMMANDS)
      Gdx.app.log(TAG, "[expired] is expired, apply: " + mApplyTime + ", expire: " + mExpiredAt + ", time: " + gameTime);

    return (mApplyTime + mExpiredAt) < gameTime;
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  /**
   * Calculate modified value based on {@code oldValue}. Operation: Multiply.
   *
   * @return modified value that should be assigned to a specified property/field.
   */
  public float apply(final float gameTime, final float oldValue) {
    mApplyTime = gameTime;
    mOldValue = oldValue;

    if (Spacefish.Debug.EXPIRED_COMMANDS)
      Gdx.app.log(TAG, "[expired] apply: " + mApplyTime + ", expire: " + mExpiredAt + ", time: " + gameTime);

    // TODO: limit minimal value

    return oldValue * Value;
  }

  /**
   * Rollback modifications. In current case were use multiply and divide operations.
   * Type of mathematical operations can be redefined by inheritors.
   */
  public float rollback(final float currentValue) {
    mApplyTime = 0;

    return currentValue / Value;
  }
}
