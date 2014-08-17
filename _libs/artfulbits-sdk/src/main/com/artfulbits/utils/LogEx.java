package com.artfulbits.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Helper class that simplify LOGs creation. Class provide helper methods for simplifying LOGs
 * formatting.
 *
 * @author Oleksandr Kucherenko
 */
public final class LogEx {
    /* [ CONSTANTS ] ======================================================================================================================================= */

  /** android.os.Build.VERSION_CODES.GINGERBREAD */
  private final static int GINGERBREAD = 9;

  /** Logger.GLOBAL_LOGGER_NAME */
  private static final String GLOBAL_LOGGER_NAME = "global";

  /** Default representer of NULL value. */
  private static final String Null = "<NULL>";

  /** Default logger name based on API. */
  private final static String LOGGER_NAME = getGlobalLogger();

  /** Root logger instance. Always exists. */
  private final static Logger GlobalLogger = LogManager.getLogManager().getLogger(LOGGER_NAME);

	/* [ STATIC METHODS ] ================================================================================================================================== */

  /**
   * Local timezone date time formatter.
   *
   * @return New instance of simple date time formatter.
   */
  private static final SimpleDateFormat LocalTzDateTimeFormat() {
    return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
  }

	/* [ CONSTRUCTORS ] ==================================================================================================================================== */

  /** Hidden constructor. */
  private LogEx() {
    throw new AssertionError();
  }

  /**
   * Create custom logger if default does not exists.
   *
   * @param className class domain name.
   * @return instance of custom logger or null.
   */
  private static Logger initialize(final String className) {
    // TODO: replace by custom initialization if needed

    return null;
  }

  /**
   * Helper method that returns Logger based on class type information.
   *
   * @param classInfo class information.
   * @return instance of Logger, in case if it not found for specified class returned Root logger
   * instance.
   */
  public static Logger getLogger(Class<?> classInfo) {
    return getLogger(classInfo.getName());
  }

  /**
   * Get logger based on class name.
   *
   * @param className
   * @return instance of Logger, in case if it not found for specified class returned Root logger
   * instance.
   */
  public static Logger getLogger(final String className) {
    Logger logger = LogManager.getLogManager().getLogger(className);

    if (null == logger) {
      logger = initialize(className);
    }

    if (null != logger) {
      return logger;
    }

    return GlobalLogger;
  }

  /**
   * Convert object to string if possible, otherwise return &lt;NULL&gt;.
   *
   * @param item instance to convert
   * @return result string.
   */
  public static String safeString(final Object item) {
    return (null != item) ? item.toString() : Null;
  }

  /**
   * Convert ticks into date time string with local timezone.
   *
   * @param ticks ticks to convert.
   * @return Formatted date, otherwise &lt;NULL&gt;.
   */
  public static String safeDate(final Long ticks) {
    String result = Null;

    if (null != ticks) {
      final Date date = new Date(ticks);

      result = LocalTzDateTimeFormat().format(date);
    }

    return result;
  }

  /**
   * Get global logger name.
   *
   * @return depends on API level return logger name.
   */
  private static String getGlobalLogger() {
    if (GINGERBREAD >= android.os.Build.VERSION.SDK_INT) {
      return GLOBAL_LOGGER_NAME; /* Logger.GLOBAL_LOGGER_NAME; */
    }

    return "";
  }

  /**
   * Dump exception details to string.
   *
   * @param ex exception to process.
   * @return Created dump of exception info.
   */
  public static String dump(final Throwable ex) {
    final StringBuilder sb = new StringBuilder(8192);

    sb.append("Exception: " + ex.getMessage()).append(", ");
    sb.append("Stack: " + Log.getStackTraceString(ex));

    return sb.toString();
  }

  /**
   * Get string representation of object.
   *
   * @param value instance to convert
   * @return result string.
   */
  public static String toString(Object value) {
    return ((null != value) ? value.toString() : Null);
  }
}
