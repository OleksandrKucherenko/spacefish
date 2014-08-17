package com.artfulbits.io;

import com.artfulbits.utils.CleanupUtils;
import com.artfulbits.utils.LogEx;
import com.artfulbits.utils.StringUtils;
import com.artfulbits.utils.ValidUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.logging.Logger;

/** Collection of utility methods that helps make file IO operations faster and safer. */
public class FileUtils {
    /* [ CONSTANTS ] ======================================================================================================================================= */

  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(FileUtils.class);

  /** Name of the digest algorithm - MD5. */
  public final static String MD5 = "MD5";
  /** Size of hash calculation buffer. Default: 4Kb. */
  private final static int BUFFER_SIZE = 4 * 1024;
  /** Default size of read/write operation buffer. Default: 16Kb. */
  private final static int BUFFER_READ_WRITE_SIZE = 4 * BUFFER_SIZE;

  /** Set of known to us measurement units. */
  private final static String[] Units = new String[]{
          "B",
          "KiB",
          "MiB",
          "GiB",
          "TiB"};

	/* [ CONSTRUCTORS ] ==================================================================================================================================== */

  /** Hidden constructor. */
  private FileUtils() {
    throw new AssertionError();
  }

	/* [ STATIC METHODS ] ================================================================================================================================== */

  public static FileInputStream safeInput(final File file) {
    ValidUtils.isNull(file, "File instance required.");

    FileInputStream input = null;

    if (exists(file, 0, -1)) {
      try {
        input = new FileInputStream(file);
      } catch (final FileNotFoundException ignored) {
        _log.warning(LogEx.dump(ignored));
      }
    }

    return input;
  }

  public static BufferedInputStream safeBufferedInput(final File file) {
    final InputStream fis = safeInput(file);
    BufferedInputStream bis = null;

    if (null != fis) {
      bis = new BufferedInputStream(fis, BUFFER_READ_WRITE_SIZE);
    }

    return bis;
  }

  public static FileOutputStream safeOutput(final File file) {
    ValidUtils.isNull(file, "File instance required.");

    FileOutputStream fos = null;

    try {
      fos = new FileOutputStream(file);
    } catch (FileNotFoundException ignored) {
      _log.warning(LogEx.dump(ignored));
    }

    return fos;
  }

  public static BufferedOutputStream safeBufferedOutput(final File file) {
    final OutputStream fos = safeOutput(file);
    BufferedOutputStream bos = null;

    if (null != fos) {
      bos = new BufferedOutputStream(fos, BUFFER_READ_WRITE_SIZE);
    }

    return bos;
  }

  public static BufferedReader safeReader(final File file) {
    ValidUtils.isNull(file, "File instance required.");

    BufferedReader input = null;

    if (file.exists()) {
      try {
        input = new BufferedReader(new FileReader(file), BUFFER_READ_WRITE_SIZE);
      } catch (FileNotFoundException ignored) {
        _log.warning(LogEx.dump(ignored));
      }
    }

    return input;
  }

  /**
   * Copy source file to destination. In case of success return true.
   *
   * @param src source file.
   * @param dst destination file.
   * @return True - success, otherwise False.
   */
  public static boolean safeCopy(final File src, final File dst) {
    try {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dst);

      copy(in, out);

      return true;
    } catch (Throwable ignored) {
      _log.warning(LogEx.dump(ignored));
    }

    return false;
  }

  /**
   * Copy input stream to output without exceptions raising.
   *
   * @param in instance of input stream.
   * @param out instance of output stream.
   * @return True - success, otherwise False.
   */
  public static boolean safeCopy(final InputStream in, final OutputStream out) {
    ValidUtils.isNull(in, "InputStream instance required.");
    ValidUtils.isNull(out, "OutputStream instance required.");

    try {
      copy(in, out);

      return true;
    } catch (Throwable ignored) {
      _log.warning(LogEx.dump(ignored));
    }

    return false;
  }

  /**
   * Extract input stream content to byte array.
   *
   * @param is input stream instance.
   * @return extracted bytes.
   */
  public static byte[] toBytes(final InputStream is) {
    ValidUtils.isNull(is, "InputStream instance required.");

    final ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_READ_WRITE_SIZE);

    try {
      copy(is, baos);
    } catch (final IOException ignored) {
      _log.warning(LogEx.dump(ignored));
    }

    return baos.toByteArray();
  }

  /**
   * Extract from input stream content into string.
   *
   * @param is input stream instance.
   * @return extracted string.
   */
  public static String toString(final InputStream is) {
    ValidUtils.isNull(is, "InputStream instance required.");

    StringWriter writer = new StringWriter(BUFFER_READ_WRITE_SIZE);
    InputStreamReader isr = new InputStreamReader(is);

    try {
      copy(isr, writer);
    } catch (final IOException ignored) {
      _log.warning(LogEx.dump(ignored));
    }

    CleanupUtils.destroy(isr);

    return writer.toString();
  }

  /**
   * Prepare file storage for data. Create folders if needed. Drop existing file, if found one.
   *
   * @param file instance of file.
   * @param doDrop True - if drop of file required, otherwise False.
   * @return True - success, otherwise False.
   */
  public static boolean prepareStorage(final File file, boolean doDrop) {
    final String dir = file.getParent();

    if (file.exists()) {
      if (doDrop) {
        return file.delete();
      }

      return true;
    }

    if (new File(dir).exists()) {
      return true;
    }

    if (new File(dir).mkdirs()) {
      return true;
    }

    return false;
  }

  /**
   * Check that file exists and it length in range of expected.
   *
   * @param file reference on file.
   * @param moreThan -1 - if check should be skipped, otherwise size of file more which it should
   * be.
   * @param lessThan -1 - if check should be skipped, otherwise size of file less which it should
   * be.
   * @return True - if file match criteria, otherwise False.
   */
  public static boolean exists(final File file, long moreThan, long lessThan) {
    ValidUtils.isNull(file, "File instance required.");

    boolean exist = file.exists();
    boolean less = (lessThan < 0 ? true : (file.length() < lessThan));
    boolean more = (moreThan < 0 ? true : (file.length() > moreThan));

    return (exist && less && more);
  }

  /**
   * Copy source file to destination.
   *
   * @param src Source file instance.
   * @param dst Destination file instance.
   * @throws IOException read/write errors.
   */
  public static void copy(final File src, final File dst) throws IOException {
    ValidUtils.isNull(src, "Source file instance required.");
    ValidUtils.isNull(dst, "Destination file instance required.");

    InputStream in = new FileInputStream(src);
    OutputStream out = new FileOutputStream(dst);

    copy(in, out);
  }

  /**
   * Copy input stream to output.
   *
   * @param in instance of input stream.
   * @param out instance of output stream.
   * @throws IOException read/write operation failure.
   */
  public static void copy(final InputStream in, final OutputStream out) throws IOException {
    ValidUtils.isNull(in, "InputStream instance required.");
    ValidUtils.isNull(out, "OutputStream instance required.");

    // Transfer bytes from in to out
    final byte[] buf = new byte[BUFFER_READ_WRITE_SIZE];
    int len;

    while ((len = in.read(buf)) > 0) {
      out.write(buf, 0, len);
    }

    in.close();
    out.close();
  }

  /**
   * Copy all from reader to writer.
   *
   * @param in instance of reader.
   * @param out instance of writer.
   * @throws IOException read/write operation failure.
   */
  public static void copy(final Reader in, final Writer out) throws IOException {
    char[] buffer = new char[BUFFER_READ_WRITE_SIZE];
    int n = 0;

    while (-1 != (n = in.read(buffer))) {
      out.write(buffer, 0, n);
    }
  }

  /**
   * Copy source file or directory to the specified destination folder.
   *
   * @param sourceFileOrDir file path to source file or dir.
   * @param destinationDir destination directory.
   * @return -1 - in case of error, otherwise quantity of copied files.
   */
  public static int copyToDir(final String sourceFileOrDir, final String destinationDir) {
    int quantity = 0;

    final File source = new File(sourceFileOrDir);
    final File destination = new File(destinationDir);

    // first create a destination directories
    if (!destination.canWrite() || (!destination.exists() && !destination.mkdirs())) {
      return -1;
    }

    // copy one file to destination directory
    if (source.isFile()) {
      final File copy = new File(destinationDir, source.getName());

      quantity = (safeCopy(source, copy)) ? -1 : 1;
    }
    // source is a directory and we are copying all files from it
    else if (source.isDirectory()) {
      final String files[] = source.list();

      for (int i = 0, len = files.length; i < len; i++) {
        final File sourceNew = new File(sourceFileOrDir, files[i]);
        final String destNew = (sourceNew.isDirectory()) ? destinationDir + File.separator + files[i]
                : destinationDir;

        int result = copyToDir(sourceNew.getAbsolutePath(), destNew);

        if (result > 0) {
          quantity += result;
        }
      }
    }

    return quantity;
  }

  /**
   * Find all files from specified directory recursively.
   *
   * @param dir root directory.
   * @return Collection of found files.
   */
  public static ArrayList<File> recursiveFind(final File dir) {
    ValidUtils.isNull(dir, "Directory file instance required");

    return recursiveFind(new File[]{dir}, new ArrayList<File>());
  }

  /**
   * Find all files recursively in array of files.
   *
   * @param files array of roots for search.
   * @param searchResults collection of results.
   * @return Collection of found files.
   */
  public static ArrayList<File> recursiveFind(final File[] files, final ArrayList<File> searchResults) {
    int i = 0;

    if (null != files) {
      while (i != files.length) {
        final File subFile = files[i];

        if (subFile.isFile()) {
          searchResults.add(subFile);
        } else if (subFile.isDirectory()) {
          final File subFiles[] = subFile.listFiles();

          recursiveFind(subFiles, searchResults);
        }

        i++;
      }
    }

    return searchResults;
  }

  /**
   * Calculate MD5 checksum for file.
   *
   * @param filename file to check
   * @return calculated hash.
   *
   * @throws Exception read/write exception.
   */
  public static byte[] createChecksum(final File filename) throws Exception {
    final InputStream fis = new BufferedInputStream(new FileInputStream(filename), BUFFER_READ_WRITE_SIZE);
    final byte[] checkSum = createChecksum(fis);
    fis.close();

    return checkSum;
  }

  /**
   * Calculate MD5 checksum for input stream.
   *
   * @param fis instance of input stream.
   * @return calculated hash.
   *
   * @throws Exception read/write exception.
   */
  public static byte[] createChecksum(final InputStream fis) throws Exception {
    final byte[] buffer = new byte[BUFFER_SIZE];
    final MessageDigest complete = MessageDigest.getInstance(MD5);
    int numRead;

    do {
      if ((numRead = fis.read(buffer)) > 0) {
        complete.update(buffer, 0, numRead);
      }
    }
    while (numRead != -1);

    return complete.digest();
  }

  /**
   * Calculate file MD5 hash checksum and return it as hex string.
   *
   * @param input input as a string value.
   * @return calculated checksum hex string.
   *
   * @throws Exception something wrong with MD5 digest access.
   */
  public static String md5(final String input) throws Exception {
    final byte[] buffer = input.getBytes(StringUtils.UTF8);
    final MessageDigest complete = MessageDigest.getInstance(MD5);
    complete.update(buffer);

    return toHex(complete.digest());
  }

  /**
   * Calculate file MD5 hash checksum and return it as hex string.
   *
   * @param file file to check
   * @return calculated checksum hex string.
   *
   * @throws Exception something wrong with file access.
   */
  public static String md5(final File file) throws Exception {
    final byte[] b = createChecksum(file);

    return toHex(b);
  }

  /**
   * Calculate file MD5 hash checksum and return it as hex string.
   *
   * @param is Input stream
   * @return calculated checksum hex string.
   *
   * @throws Exception something wrong with file access.
   */
  public static String md5(final InputStream is) throws Exception {
    final byte[] b = createChecksum(is);

    return toHex(b);
  }

  /**
   * Convert byte array to hex string.
   *
   * @param b data to convert.
   * @return Result of convert.
   */
  public static String toHex(final byte[] b) {
    final StringBuilder result = new StringBuilder(256);

    for (int i = 0; i < b.length; i++) {
      result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
    }

    return result.toString();
  }

  /**
   * return text representation of file size.
   *
   * @param f file which size to format.
   * @return formatted file size.
   */
  public static String fileSize(final File f) {
    ValidUtils.isNull(f, "File instance required.");

    final long length = f.length();
    final int digitGroups = (int) (Math.log10(length) / Math.log10(1024));
    final double value = (double) length / Math.pow(1024, digitGroups);

    // normalize unit index
    final int index = Math.max(0, Math.min(digitGroups, Units.length - 1));

    return new java.text.DecimalFormat("#,##0.# ").format(value) + Units[index];
  }
}
