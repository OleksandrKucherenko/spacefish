package com.artfulbits.utils;

import android.os.Looper;

import java.util.Collection;
import java.util.logging.Logger;

/**
 * Utilities that helps in validating input values for methods.
 *
 * @author Oleksandr Kucherenko
 */
public final class ValidUtils {
    /* [ CONSTANTS ] ======================================================================================================================================= */

  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(ValidUtils.class);

	/* [ CONSTRUCTORS ] ==================================================================================================================================== */

  /** Hidden constructor. */
  private ValidUtils() {
    throw new AssertionError();
  }

	/* [ STATIC METHODS ] ================================================================================================================================== */

  /**
   * Check value and if it equal to zero writes error message to LOG.
   *
   * @param value value to check
   * @param msg error message
   */
  public static void isNull(final Object value, final String msg) {
    if (null == value) {
      _log.warning(msg);
    }
  }

  /**
   * Check value and if it less then specified value place into LOG error message.
   *
   * @param value value to check
   * @param min minimal allowed value
   * @param msg error message
   */
  public static void isLess(final int value, final int min, final String msg) {
    if (value < min) {
      _log.warning(msg);
    }
  }

  /**
   * Check value and if it less then specified value place into LOG error message.
   *
   * @param value value to check
   * @param min minimal allowed value
   * @param msg error message
   */
  public static void isLess(final long value, final long min, final String msg) {
    if (value < min) {
      _log.warning(msg);
    }
  }

  /**
   * Check value and if it equals then specified value place into LOG error message.
   *
   * @param value value to check
   * @param equal allowed value
   * @param msg error message
   */
  public static void isEquals(final int value, final int equal, final String msg) {
    if (value == equal) {
      _log.warning(msg);
    }
  }

  /**
   * Check value and if it less then specified value place into LOG error message.
   *
   * @param value value to check
   * @param min minimal allowed value
   * @param msg error message
   */
  public static void isLessOrEqual(final double value, final double min, final String msg) {
    if (value <= min) {
      _log.warning(msg);
    }
  }

  /**
   * Check value and if it greater then specified value place into LOG error message.
   *
   * @param value value to check
   * @param max maximum allowed value
   * @param msg error message
   */
  public static void isGreater(final int value, final int max, final String msg) {
    if (value > max) {
      _log.warning(msg);
    }
  }

  /**
   * Check value and if it is not in range then log error message.
   *
   * @param value value to check
   * @param min minimal allowed value
   * @param max maximum allowed value
   * @param msg error message
   */
  public static void isRange(final int value, final int min, final int max, final String msg) {
    if (value < min || value > max) {
      _log.warning(msg);
    }
  }

  /**
   * Check condition and if it's TRUE then place error message to LOG.
   *
   * @param bCondition condition to check
   * @param msg error message
   * @return repeat condition chain checks
   */
  public static boolean isWrong(final boolean bCondition, final String msg) {
    if (bCondition) {
      _log.warning(msg);
    }

    return bCondition;
  }

  /**
   * Check that string is not empty.
   *
   * @param value string to check.
   * @param msg error message
   */
  public static void isEmpty(final String value, final String msg) {
    if (null == value || 0 == value.length()) {
      _log.warning(msg);
    }
  }

  /**
   * Check that collection is not empty.
   *
   * @param value collection to check.
   * @param msg error message
   */
  public static void isEmpty(final Collection<?> value, final String msg) {
    if (null == value || 0 == value.size()) {
      _log.warning(msg);
    }
  }

  /**
   * Helper methods. Normalize value to range limits.
   *
   * @param value value for normalization.
   * @param min minimal allowed value.
   * @param max maximum allowed value.
   * @return safe value for using in range indexing.
   */
  public static int safeRange(final int value, final int min, final int max) {
    int result = Math.max(min, Math.min(value, max));

    if (result != value) {
      _log.warning("Value normalization was used. New: " +
              result +
              "Original: " +
              value +
              " Range(" +
              min +
              "," +
              max +
              ")");
    }

    return result;
  }

  /**
   * If current thread is ui thread, than show the message.
   *
   * @param msg error message
   */
  public static void isUiThread(final String msg) {
    final boolean isUiThread = Looper.getMainLooper().getThread() == Thread.currentThread();

    if (isUiThread) {
      _log.warning(msg);
    }
  }
}
