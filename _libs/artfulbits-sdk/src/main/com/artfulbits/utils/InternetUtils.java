package com.artfulbits.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;

import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.logging.Logger;

/**
 * Class implements periodic check for Internet connectivity on device. It is possible to use it as
 * broadcast receiver on CONNECTIVITY_CHANGE and WIFI_STATE_CHANGED.<br/>
 * For proper class use please do configuration steps:<br/>
 * <br/>
 * STEP #1 (CRITICAL) - set permissions in AndroidManifest.xml:
 * <p/>
 * <pre>
 * &lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;
 * </pre>
 * <p/>
 * STEP #2 (OPTIONAL) - configure broadcast receiver in AndroidManifest.xml:
 * <p/>
 * <pre>
 * &lt;receiver android:name="com.artfulbits.utils.InternetUtils" &gt;
 *  &lt;intent-filter&gt;
 *    &lt;action android:name="android.net.conn.CONNECTIVITY_CHANGE" /&gt;
 *    &lt;action android:name="android.net.wifi.WIFI_STATE_CHANGED" /&gt;
 *  &lt;/intent-filter&gt;
 * &lt;/receiver&gt;
 * </pre>
 * <p/>
 * STEP #3 (CRITICAL) - place one line of code:
 * <p/>
 * <pre>
 * InternetUtils.install( context, period, listener );
 * </pre>
 * <p/>
 * Best place for call is {@link android.app.Application#onCreate()} class or inside main Activity.
 */
public class InternetUtils
        extends BroadcastReceiver {
    /* [ CONSTANTS ] ======================================================================================================================================= */

  /** Our specific action for broadcasting. */
  public static final String ACTION_PERIODIC_CHECK_BROADCAST = "com.artfulbits.utils.InternetUtils.PERIODIC_CHECK";

  /** Send this constant if you want to disable periodic checks. */
  public static final long NO_PERIODIC_CHECK = -1;

  /** Major instance that do processing of all broadcasts */
  private static final InternetUtils Instance = new InternetUtils();

  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(InternetUtils.class);

  /** unique broadcast identifier. */
  private static final int UniqueRequestCode = -100;

  /** Maximum allowed delay in periodic checks in millis. */
  private static final int MaxSchedulePeriod = 24 * 60 * 60 * 1000;

  /** Collection of attached listeners. */
  private static final WeakHashMap<IConnectionListener, IConnectionListener> sListeners = new WeakHashMap<InternetUtils.IConnectionListener,
          InternetUtils.IConnectionListener>();

  /** Known connection status. */
  public enum InternetStatus {
    /** No Internet connection. */
    NO_CONNECTION,
    /** We are in process of status check. */
    CHECKING_CONNECTION,
    /** Internet connected. */
    CONNECTED,
    /** Class not properly attached to the project. */
    BAD_CONFIG
  }

	/* [ STATIC MEMBERS ] ================================================================================================================================== */

  private static boolean sIsInstalled;

  private static boolean sHasPeriodicCheck;

  private static boolean sInstalledReceiver;

  private static InternetStatus sCurrentState = InternetStatus.NO_CONNECTION;

	/* [ STATIC METHODS ] ================================================================================================================================== */

  /**
   * Install Internet connection checker.
   *
   * @param context application context.
   */
  public static void install(final Context context) {
    install(context, NO_PERIODIC_CHECK, null);
  }

  /**
   * Install Internet connection checker with specified period.
   *
   * @param context application context
   * @param period period of checks in millis, use NO_PERIODIC_CHECK if no needs in periodic checks.
   */
  public static void install(final Context context, final long period) {
    install(context, period, null);
  }

  /**
   * Install Internet connection checker with specified listener. No periodic checks.
   *
   * @param context application context
   * @param listener listener instance.
   */
  public static void install(final Context context, final IConnectionListener listener) {
    install(context, NO_PERIODIC_CHECK, listener);
  }

  /**
   * Install Internet connection checker with specified period and listener.
   *
   * @param context application context
   * @param period period of checks in millis, use NO_PERIODIC_CHECK if no needs in periodic checks.
   * @param listener listener instance.
   */
  public synchronized static void install(final Context context, final long period, final IConnectionListener listener) {
    ValidUtils.isNull(context, "Context instance required.");
    ValidUtils.isRange((int) period, (int) NO_PERIODIC_CHECK, MaxSchedulePeriod,
            "period can not be larger 24 hours or be less NO_PERIOD_CHECK");

    if (!sIsInstalled) {
      // get current status
      forceCheck(context);

      // periodic check
      cancelPeriodicCheck(context);
      setPeriodicCheck(context, period);

      // register receiver
      registerReceiver(context);

      // attach listener
      register(listener);

      sIsInstalled = !(sCurrentState == InternetStatus.BAD_CONFIG && period == NO_PERIODIC_CHECK);

      _log.info(sIsInstalled ? "Connect: installed internet connection checker." : "Connect: installation failed.");
    }
  }

  /**
   * Un-install utility class. Free system resources.
   *
   * @param context application context.
   */
  public synchronized static void uninstall(final Context context) {
    ValidUtils.isNull(context, "Context instance required.");

    if (sIsInstalled) {
      cancelPeriodicCheck(context);
      unregisterReceiver(context);

      sIsInstalled = false;
    }
  }

  /**
   * Force Internet connection check.
   *
   * @param context application context.
   */
  public static void forceCheck(final Context context) {
    ValidUtils.isNull(context, "Context instance required.");

    InternetStatus status = InternetStatus.BAD_CONFIG;

    final int security = context.checkCallingOrSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);

    if (PackageManager.PERMISSION_GRANTED == security) {
      final ConnectivityManager cm = Use.service(context, Context.CONNECTIVITY_SERVICE);

      // grab status from WiFi, mobile and active connection
      final NetworkInfo niWiFi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      final NetworkInfo niMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      final NetworkInfo niActive = cm.getActiveNetworkInfo();

      status = (null != niWiFi && niWiFi.isConnected()) ? InternetStatus.CONNECTED : InternetStatus.NO_CONNECTION;
      _log.info("Connect: WiFi status - " + status);

      if (InternetStatus.NO_CONNECTION == status) {
        status = (null != niMobile && niMobile.isConnected()) ? InternetStatus.CONNECTED
                : InternetStatus.NO_CONNECTION;

        _log.info("Connect: Mobile status - " + status);
      }

      if (InternetStatus.NO_CONNECTION == status) {
        status = (null != niActive && niActive.isConnected()) ? InternetStatus.CONNECTED
                : InternetStatus.NO_CONNECTION;
        _log.info("Connect: Active status - " + status);
      }
    }

    updateStatus(status);
  }

  /**
   * Attach listener instance. We keep only weak references on listeners, so it is no actual need to
   * call 'unregister'.
   *
   * @param listener listener instance.
   */
  public static void register(final IConnectionListener listener) {
    if (null != listener) {
      sListeners.put(listener, listener);
    }
  }

  /**
   * Detach listener instance.
   *
   * @param listener listener instance.
   */
  public static void unregister(final IConnectionListener listener) {
    if (null != listener) {
      sListeners.remove(listener);
    }
  }

  /**
   * Get current Internet connection state.
   *
   * @return last known connection state.
   */
  public static InternetStatus getCurrentState() {
    return sCurrentState;
  }

  /**
   * Is Internet connection checker installed.
   *
   * @return True - installed, otherwise False.
   */
  public static boolean isInstalled() {
    return sIsInstalled;
  }

  /**
   * Is Internet connection checker installed with periodic checks.
   *
   * @return True - periodic check set, otherwise false.
   */
  public static boolean hasPeriodicCheck() {
    return sHasPeriodicCheck;
  }

	/* [ IMPLEMENTATION & HELPERS ] ======================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void onReceive(final Context context, final Intent intent) {
    // redirect calls from another instances to our main one
    if (!Instance.equals(this)) {
      Instance.onReceive(context, intent);
      return;
    }

    final String action = intent.getAction();

    _log.info("Connect: Received broadcast Action: " + action);

    if (ACTION_PERIODIC_CHECK_BROADCAST.equals(action) ||
            ConnectivityManager.CONNECTIVITY_ACTION.equals(action) ||
            WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
      updateStatus(InternetStatus.CHECKING_CONNECTION);

      forceCheck(context);
    }
  }

  /**
   * Update Internet connection status and notify all listeners about changes.
   *
   * @param status
   */
  private static void updateStatus(final InternetStatus status) {
    final InternetStatus old = sCurrentState;
    sCurrentState = status;

    // do not do any callbacks if new status repeats previous status
    if (old == status) {
      return;
    }

    // report into LOG only if states are different
    _log.info("Connect: current " + status + ", old " + old);

    final Iterator<IConnectionListener> iterator = sListeners.keySet().iterator();

    while (iterator.hasNext()) {
      final IConnectionListener listener = iterator.next();

      try {
        listener.onStateChanged(old, status);
      } catch (Throwable ignored) {
        _log.warning("Connect: user listener raise exception. Exception: " + ignored.getMessage());
        _log.warning("Connect: exception stack: " + Log.getStackTraceString(ignored));
      }
    }
  }

  /**
   * Schedule periodic check.
   *
   * @param context application context.
   * @param period defined by user period.
   */
  private static void setPeriodicCheck(final Context context, long period) {
    // no periodic checks
    if (NO_PERIODIC_CHECK == period) {
      _log.warning("Connect: no periodic checks.");
      return;
    }

    final AlarmManager am = Use.service(context, Context.ALARM_SERVICE);
    final long current = SystemClock.elapsedRealtime();

    final Intent intent = new Intent(ACTION_PERIODIC_CHECK_BROADCAST);
    final PendingIntent pi = PendingIntent.getBroadcast(context, UniqueRequestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, current + period, period, pi);

    sHasPeriodicCheck = true;

    _log.info("Connect: check internet connection with period: " + period);
  }

  /**
   * Register broadcast receiver in system.
   *
   * @param context application context.
   */
  private synchronized static void registerReceiver(final Context context) {
    if (!sInstalledReceiver) {
      final IntentFilter filter = new IntentFilter();
      filter.addAction(ACTION_PERIODIC_CHECK_BROADCAST);
      filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
      filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

      context.registerReceiver(Instance, filter);

      sInstalledReceiver = true;
    }
  }

  /**
   * Unregister broadcast receiver in system.
   *
   * @param context application context.
   */
  private synchronized static void unregisterReceiver(final Context context) {
    if (sInstalledReceiver) {
      context.unregisterReceiver(Instance);
      sInstalledReceiver = false;
    }
  }

  /**
   * Cancel scheduling of periodic check.
   *
   * @param context application context.
   */
  private static void cancelPeriodicCheck(final Context context) {
    final AlarmManager am = Use.service(context, Context.ALARM_SERVICE);
    final Intent intent = new Intent(ACTION_PERIODIC_CHECK_BROADCAST);
    final PendingIntent pi = PendingIntent.getBroadcast(context, UniqueRequestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    am.cancel(pi);

    sHasPeriodicCheck = false;
  }

	/* [ NESTED DECLARATIONS ] ============================================================================================================================= */

  /** Callback interface for everyone who wants to listen internet connection state changes. */
  public interface IConnectionListener {
    /**
     * Internet connection state changed.
     *
     * @param old previous state
     * @param new$ new state
     */
    void onStateChanged(final InternetStatus old, final InternetStatus new$);
  }
}