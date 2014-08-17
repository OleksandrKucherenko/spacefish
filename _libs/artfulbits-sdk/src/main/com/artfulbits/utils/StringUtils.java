package com.artfulbits.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

/** Common string routine. */
public final class StringUtils {
    /* [ CONSTANTS ] ======================================================================================================================================= */

  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(StringUtils.class);

  /** Default strings encoding. */
  public final static String UTF8 = "UTF-8";

	/* [ CONSTRUCTORS ] ==================================================================================================================================== */

  /** Hidden constructor. */
  private StringUtils() {
    throw new AssertionError();
  }

	/* [ STATIC METHODS ] ================================================================================================================================== */

  /**
   * Convert string to utf 8 bytes.
   *
   * @param value the value to convert
   * @return the bytes in UTF8 encoding.
   */
  public static byte[] toUtf8Bytes(final String value) {
    ValidUtils.isEmpty(value, "Expected not null value.");

    // try to avoid NULL values, better to return empty array
    byte[] buffer = new byte[]{};

    if (!TextUtils.isEmpty(value)) {
      try {
        buffer = value.getBytes(StringUtils.UTF8);
      } catch (final UnsupportedEncodingException e) {
        _log.severe(LogEx.dump(e));
      }
    }

    return buffer;
  }
}
