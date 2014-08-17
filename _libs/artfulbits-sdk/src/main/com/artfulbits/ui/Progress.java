package com.artfulbits.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.artfulbits.R;
import com.artfulbits.utils.LogEx;
import com.artfulbits.utils.Use;
import com.artfulbits.utils.ValidUtils;

import java.util.Locale;
import java.util.logging.Logger;

public class Progress
        implements Handler.Callback, CountDownWithCallback.Callback, View.OnClickListener {
    /* [ CONSTANTS ] ======================================================================================================================================= */

  /** Unique message ID. Request timer/countdown update. */
  private final static int MSG_UPDATE_TIMER = -1;
  /** Request progress view to hide. */
  private final static int MSG_HIDE = -2;
  /** Request progress to display a message. */
  private final static int MSG_UPDATE_TEXT = -3;
  /** Rotation listening. 30 degrees is a threshold for rotation. */
  private final static int ANGLE_THRESHOLD = 30;
  /** 7.5 seconds is a default UX timeout for web requests. */
  public final static long DEFAULT_UX_TIMEOUT = 7500;
  /** User cancel action */
  public final static int STATE_CANCEL = -100;
  /** Action not processed in specified time. */
  public final static int STATE_TIMEOUT = -101;
  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(Progress.class);

	/* [ MEMBERS ] ========================================================================================================================================= */

  public final PopupWindow popup;

  public final View progress;

  public final TextView countdown;

  public final TextView messages;

  public final View parent;

  public final int x;

  public final int y;

  public final int width;

  public final int height;

  public final int frame;

  public final boolean isPortrait;

  /** Reference on application context. */
  private final Context mContext;
  /** messages handler. */
  private final Handler mHandler = new Handler(this);
  /** Count down timer. */
  private final CountDownWithCallback mTimer;
  /** Reference on callback interface. */
  private final Callback mCallback;
  /** Listen screen orientation changes and if needed recreate progress screen. */
  private final OrientationCallback mOrientation;

	/* [ CONSTRUCTORS ] ==================================================================================================================================== */

  /**
   * Copy constructor.
   *
   * @param v reference on new parent
   * @param source progress window to inherit.
   */
  protected Progress(final View v, final Progress source) {
    final Context context = mContext = source.mContext;
    parent = v;

    // reuse elements of source - progress view
    progress = source.progress;
    countdown = source.countdown;
    messages = source.messages;

    source.mOrientation.disable();

    // reuse elements of source - timers, callbacks, orientation listener
    mTimer = new CountDownWithCallback(source.mTimer);
    mOrientation = source.mOrientation.setParent(this);
    mCallback = source.mCallback;

    // update progress view
    progress.setOnClickListener(this);
    progress.setTag(R.id.tag_popup_holder, this);

    // get orientation mode
    isPortrait = (Configuration.ORIENTATION_PORTRAIT == context.getResources().getConfiguration().orientation);

    // get screen metrics
    final WindowManager wm = Use.service(context, Context.WINDOW_SERVICE);
    final DisplayMetrics metrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(metrics);

    // x,y positions on screen
    final int[] location = new int[]{v.getLeft(), v.getTop()};
    v.getLocationOnScreen(location);

    // calculate bounds
    frame = (int) Use.dp2pixel(context, R.dimen.frame_normal);
    x = location[0] + frame;
    y = location[1] + frame;
    width = Math.min(v.getWidth() - frame * 2, metrics.widthPixels - x - frame);
    height = Math.min(v.getHeight() - frame * 2, metrics.heightPixels - y - frame);

    // create popup window
    popup = new PopupWindow(progress, width, height);

    // start listen orientation change
    mOrientation.enable();
  }

  /**
   * Construct progress window.
   *
   * @param context application context.
   * @param v parent view reference.
   * @param total timeout total in millis.
   * @param callback user interaction listener.
   */
  public Progress(final Context context, final View v, final long total, final Callback callback) {
    final LayoutInflater inflater = LayoutInflater.from(context);

    mContext = context;
    mCallback = callback;
    parent = v;

    // create progress view and get references on it controls
    progress = inflater.inflate(R.layout.elm_progress_inidicator, null);
    countdown = Use.id(progress, R.id.txt_countdown);
    messages = Use.id(progress, R.id.txt_message);
    progress.setOnClickListener(this);
    progress.setTag(R.id.tag_popup_holder, this);

    // get orientation mode
    isPortrait = (Configuration.ORIENTATION_PORTRAIT == context.getResources().getConfiguration().orientation);

    // get screen metrics
    final WindowManager wm = Use.service(context, Context.WINDOW_SERVICE);
    final DisplayMetrics metrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(metrics);

    // x,y positions on screen
    final int[] location = new int[]{v.getLeft(), v.getTop()};
    v.getLocationOnScreen(location);

    // calculate bounds
    frame = (int) Use.dp2pixel(context, R.dimen.frame_normal);
    x = location[0] + frame;
    y = location[1] + frame;
    width = Math.min(v.getWidth() - frame * 2, metrics.widthPixels - x - frame);
    height = Math.min(v.getHeight() - frame * 2, metrics.heightPixels - y - frame);

    // create popup window
    popup = new PopupWindow(progress, width, height);

    mTimer = new CountDownWithCallback(total, 100, this);

    // listen orientation change
    mOrientation = new OrientationCallback(this);
    mOrientation.enable();
  }

	/* [ STATIC METHODS ] ================================================================================================================================== */

  /**
   * Create and show progress popup window with 7.5 seconds timeout.
   *
   * @param context application context
   * @param v parent view which location and size will be used for popup adjusting
   * @param callback callback on user actions listening
   * @return instance of the Progress window
   */
  public static Progress show(final Context context, final View v, final Callback callback) {
    final Progress holder = new Progress(context, v, DEFAULT_UX_TIMEOUT, callback);

    return holder.show().start();
  }

  /**
   * Recreate progress popup window with reuse of existing progress.
   *
   * @param v new parent instance.
   * @param prev instance on resources to reuse.
   * @return newly created progress window.
   */
  public static Progress recreate(final View v, final Progress prev) {
    ValidUtils.isNull(v, "Parent instance required.");
    ValidUtils.isNull(prev, "Instance of prviously created progress instance needed.");

    return new Progress(v, prev);
  }

  /**
   * Detect are we in landscape mode.
   *
   * @param degree orientation event listener value.
   * @return True - landscape, otherwise False.
   */
  public static boolean isLandscape(int degree) {
    return (degree >= (90 - ANGLE_THRESHOLD) && degree <= (90 + ANGLE_THRESHOLD));
  }

  /**
   * Detect are we in portrait mode.
   *
   * @param degree orientation event listener value.
   * @return True - portrait, otherwise False.
   */
  public static boolean isPortrait(int degree) {
    return ((degree >= (360 - ANGLE_THRESHOLD) && degree <= 360) || (degree >= 0 && degree <= ANGLE_THRESHOLD));
  }

	/* [ Interface Callback ] ============================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public final boolean handleMessage(final Message msg) {
    if (MSG_UPDATE_TIMER == msg.what) {
      final float until = (float) msg.arg1 / 1000;
      final String text = String.format(Locale.US, "%.1fs", until);

      countdown.setText(text);

      return true;
    } else if (MSG_HIDE == msg.what) {
      if (popup.isShowing()) {
        popup.dismiss();
      }

      return true;
    } else if (MSG_UPDATE_TEXT == msg.what) {
      if (msg.arg1 != 0) {
        messages.setText(msg.arg1);
      } else if (msg.obj instanceof CharSequence) {
        messages.setText((CharSequence) msg.obj);
      }

      return true;
    }

    return false;
  }

  /** {@inheritDoc} */
  @Override
  public final void onTick(long until) {
    mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_TIMER, (int) until, -1));
  }

  /** {@inheritDoc} */
  @Override
  public final void onFinish() {
    mHandler.sendMessage(mHandler.obtainMessage(MSG_HIDE));

    if (null != mCallback) {
      mCallback.onFinish(this, STATE_TIMEOUT);
    }
  }

	/* [ Interface OnClickListener ] ======================================================================================================================= */

  /** {@inheritDoc} */
  @Override
  public final void onClick(final View v) {
    if (R.id.rl_progress_indicator == v.getId()) {
      if (null != mCallback) {
        mCallback.onFinish(this, STATE_CANCEL);
      }
    }
  }

	/* [ IMPLEMENTATION & HELPERS ] ======================================================================================================================== */

  /**
   * Dismiss progress dialog.
   *
   * @return this instance, for chained calls.
   */
  public Progress dismiss() {
    mTimer.cancel();

    mHandler.sendMessage(mHandler.obtainMessage(MSG_HIDE));

    return this;
  }

  /**
   * Raised on detecting orientation change.
   *
   * @param portrait True - rotated to portrait, otherwise to landscape.
   */
  protected void onOrientationChanged(boolean portrait) {
    try {
      popup.dismiss();
      mTimer.cancel();
    } catch (Throwable ignore) {
    }
  }

  /**
   * Show custom message on progress view.
   *
   * @param message the message to show
   * @return this progress, for chained calls.
   */
  public Progress report(final String message) {
    mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_TEXT, -1, -1, message));

    return this;
  }

  /**
   * Show custom message by it ID on progress view.
   *
   * @param idMessage the id of message to show
   * @return this progress, for cahined calls.
   */
  public Progress report(final int idMessage) {
    mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_TEXT, idMessage, -1));

    return this;
  }

  /**
   * Show progress view based on done configuration.
   *
   * @return this progress, for chained calls.
   */
  public Progress show() {
    popup.showAtLocation(this.parent, Gravity.NO_GRAVITY, width, height);
    popup.update(x, y, width, height, true);

    return this;
  }

  /**
   * Start countdown timer and progress displaying.
   *
   * @return this progress, for chained calls.
   */
  public Progress start() {
    mTimer.start();

    return this;
  }

	/* [ NESTED DECLARATIONS ] ============================================================================================================================= */

  /** Orientation changed event listener. */
  private static class OrientationCallback
          extends OrientationEventListener {
    /** Reference on listener. */
    private Progress mParent;

    /**
     * Create listener for orientation change.
     *
     * @param parent listener instance.
     */
    public OrientationCallback(final Progress parent) {
      super(parent.mContext, SensorManager.SENSOR_DELAY_UI);

      mParent = parent;
    }

    /**
     * Assign new parent for callback.
     *
     * @param progress parent instance.
     */
    public OrientationCallback setParent(final Progress progress) {
      ValidUtils.isNull(progress, "Progress instance required");

      synchronized (this) {
        mParent = progress;
      }

      return this;
    }

    /** {@inheritDoc} */
    @Override
    public void onOrientationChanged(int orientation) {
      // NOTE: 180 degree rotation is not detected

      boolean portrait = isPortrait(orientation);

      _log.finest("Orientation degree: " + orientation);

      synchronized (this) {
        if (mParent.isPortrait != portrait) {
          mParent.onOrientationChanged(portrait);
        }
      }
    }
  }

  /** Callback interface for Progress popup window. */
  public interface Callback {
    /**
     * @param caller reference on caller.
     * @param state possible states: {@link com.artfulbits.ui.Progress#STATE_TIMEOUT},
     * {@link com.artfulbits.ui.Progress#STATE_CANCEL}
     */
    public void onFinish(final Progress caller, int state);
  }
}
