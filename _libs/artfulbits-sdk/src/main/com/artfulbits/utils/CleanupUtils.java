package com.artfulbits.utils;

import android.database.Cursor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * Cleanup Utilities used for simplifying cleanup logic in methods, hide try/catches and etc.
 *
 * @author Oleksandr Kucherenko
 */
public final class CleanupUtils {
  // #region Static Members
  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(CleanupUtils.class);

  // #endregion

  // #region Public methods

  /**
   * Close random access file gracefully.
   *
   * @param raf instance of the random access file.
   */
  public static void destroy(final RandomAccessFile raf) {
    if (null != raf) {
      try {
        raf.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Close input stream gracefully.
   *
   * @param is instance of the input stream.
   */
  public static void destroy(final InputStream is) {
    if (null != is) {
      try {
        is.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Close output stream gracefully.
   *
   * @param os instance of the output stream.
   */
  public static void destroy(final OutputStream os) {
    if (null != os) {
      try {
        os.flush();
        os.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy NIO selector instance.
   *
   * @param selector selector instance.
   */
  public static void destroy(final Selector selector) {
    if (null != selector) {
      try {
        selector.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy thread instance.
   *
   * @param runnable instance of thread.
   */
  public static void destroy(final Thread runnable) {
    if (null != runnable) {
      if (runnable.isAlive()) {
        runnable.interrupt();
      }
    }
  }

  /**
   * Destroy Socket Channel.
   *
   * @param channel channel instance.
   */
  public static void destroy(final SocketChannel channel) {
    if (null != channel) {
      try {
        channel.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy Datagram Channel instance.
   *
   * @param channel instance of channel.
   */
  public static void destroy(final DatagramChannel channel) {
    if (null != channel) {
      try {
        channel.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy Server Socket Channel.
   *
   * @param channel instance of channel.
   */
  public static void destroy(final ServerSocketChannel channel) {
    if (null != channel) {
      try {
        channel.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy reader instance.
   *
   * @param reader instance of the reader.
   */
  public static void destroy(final Reader reader) {
    if (null != reader) {
      try {
        reader.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy writer instance.
   *
   * @param writer reference on instance.
   */
  public static void destroy(final Writer writer) {
    if (null != writer) {
      try {
        writer.flush();
        writer.close();
      } catch (IOException ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy file writer instance.
   *
   * @param writer reference on instance.
   */
  public static void destroy(final PrintWriter writer) {
    if (null != writer) {
      try {
        writer.flush();
        writer.close();
      } catch (Throwable ignored) {
        _log.info(ignored.getMessage());
      }
    }
  }

  /**
   * Destroy DB cursor.
   *
   * @param cursor instance for destroying.
   */
  public static void destroy(final Cursor cursor) {
    if (null != cursor) {
      cursor.close();
    }
  }

  /**
   * Destroy URL connection instance.
   *
   * @param connection connection instance.
   */
  public static void destroy(final HttpURLConnection connection) {
    if (null != connection) {
      connection.disconnect();
    }
  }

  // #endregion

  // #region Hidden constructor

  /** Hidden constructor. */
  private CleanupUtils() {
    throw new AssertionError();
  }
  // #endregion

}
