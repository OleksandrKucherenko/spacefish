package com.artfulbits.ui;

import android.os.CountDownTimer;

import com.artfulbits.utils.LogEx;
import com.artfulbits.utils.ValidUtils;

import java.util.WeakHashMap;
import java.util.logging.Logger;

/** CountDown timer that implements callback pattern. */
public final class CountDownWithCallback
        extends CountDownTimer {
  // #region Static Members
  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(CountDownWithCallback.class);

  /**  */
  private final static long THE_END = -1;
  // #endregion

  // #region Members
  /** Collection of callbacks */
  private final WeakHashMap<Callback, Callback> mCallbacks = new WeakHashMap<CountDownWithCallback.Callback, CountDownWithCallback.Callback>();

  private long mMillisUntilFinish;

  private final long mTotal;

  private final long mInterval;

  // #endregion

  // #region Constructor
  public CountDownWithCallback(long total, long interval, Callback callback) {
    super(total, interval);

    mTotal = total;
    mInterval = interval;

    if (null != callback)
      mCallbacks.put(callback, callback);
  }

  /**
   * Copy constructor that reveal countdown from it last position.
   *
   * @param copy instance to copy.
   */
  public CountDownWithCallback(final CountDownWithCallback copy) {
    super((THE_END == copy.mMillisUntilFinish) ? copy.mTotal : copy.mMillisUntilFinish, copy.mInterval);

    mInterval = copy.mInterval;
    mTotal = copy.mTotal;

    if (copy.mCallbacks.size() > 0) {
      for (Callback call : copy.mCallbacks.keySet()) {
        mCallbacks.put(call, call);
      }
    }
  }

  // #endregion

  // #region Public API

  /**
   * Register callback.
   *
   * @param callback instance of callback.
   * @return this.
   */
  public CountDownWithCallback register(final Callback callback) {
    ValidUtils.isNull(callback, "Callback instance required.");

    synchronized (mCallbacks) {
      mCallbacks.put(callback, callback);
    }

    return this;
  }

  /**
   * Unregister callback.
   *
   * @param callback instance of callback.
   * @return this.
   */
  public CountDownWithCallback unregister(final Callback callback) {
    ValidUtils.isNull(callback, "Callback instance required.");

    synchronized (mCallbacks) {
      mCallbacks.remove(callback);
    }

    return this;
  }

  // #endregion

  // #region Overrides

  /** {@inheritDoc} */
  @Override
  public void onTick(long until) {
    mMillisUntilFinish = until;

    _log.finest("Countdown: " + until);

    for (Callback call : mCallbacks.keySet()) {
      try {
        call.onTick(until);
      } catch (Throwable ignored) {
        _log.warning("Callback throws exception. " + LogEx.dump(ignored));
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void onFinish() {
    mMillisUntilFinish = THE_END;

    for (Callback call : mCallbacks.keySet()) {
      try {
        call.onFinish();
      } catch (Throwable ignored) {
        _log.warning("Callback throws exception. " + LogEx.dump(ignored));
      }
    }
  }
  // #endregion

  // #region Nested declarations

  /** Callback interface for count-down timer. */
  public interface Callback {
    /** @see CountDownTimer#onTick(long) */
    public void onTick(long until);

    /** @see CountDownTimer#onFinish() */
    public void onFinish();
  }
  // #endregion
}