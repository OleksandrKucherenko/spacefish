/**  */
package com.artfulbits.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artfulbits.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Class which simplify common android development tasks.
 *
 * @version 1.2.0.0
 */
public final class Use {
    /* [ CONSTANTS ] ======================================================================================================================================= */

  /** android.os.Build.VERSION_CODES.HONEYCOMB */
  public static final int HONEYCOMB = 11;
  /** android.os.Build.VERSION_CODES.JELLY_BEAN */
  public static final int JELLY_BEAN = 16;

  /** Our own class Logger instance. */
  private final static Logger _log = LogEx.getLogger(Use.class);

	/* [ STATIC MEMBERS ] ================================================================================================================================== */

  /** Static handler for processing messages in case if MAIN thread handler is not reachable. */
  private static Handler sHandler;

	/* [ CONSTRUCTORS ] ==================================================================================================================================== */

  /** Hidden constructor. */
  private Use() {
    throw new AssertionError();
  }

	/* [ STATIC METHODS ] ================================================================================================================================== */

  /**
   * Extract assets resource as input stream or return NULL.
   *
   * @param context instance of application context
   * @param fileName assets resource file name
   * @return opened input stream or NULL
   */
  public static final InputStream safeAssets(final Context context, final String fileName) {
    ValidUtils.isNull(context, "Required instance of the context.");
    ValidUtils.isEmpty(fileName, "Required assets resource file name.");

    InputStream is = null;

    try {
      is = context.getAssets().open(fileName);
    } catch (Throwable ignored) {
      _log.warning("Cannot extract asset with defined by user name. Exception: " + ignored.getMessage());
    }

    return is;
  }

  /**
   * Extracts application of specified type from context instance.
   *
   * @param context instance of context.
   * @return Extracted application instance or null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends Application> T annex(final Context context) {
    ValidUtils.isNull(context, "Required instance of the context.");

    if (context instanceof Application) {
      return (T) context;
    }

    Context c2 = context.getApplicationContext();

    if (c2 instanceof Application) {
      return (T) c2;
    }

    return null;
  }

  /**
   * Extracts application of specified type from context instance.
   *
   * @param context instance of context.
   * @return Extracted application instance or null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends Application> WeakReference<T> weak(final Context context) {
    ValidUtils.isNull(context, "Required instance of the context.");

    if (context instanceof Application) {
      return new WeakReference<T>((T) context);
    }

    Context c2 = context.getApplicationContext();

    if (c2 instanceof Application) {
      return new WeakReference<T>((T) c2);
    }

    return null;
  }

  /**
   * Find View and cast it to specified type by id.
   *
   * @param root activity instance
   * @param id view identifier
   * @return extracted instance of view
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T id(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    return (T) root.findViewById(id);
  }

  /**
   * Find View and cast it to specified type by id.
   *
   * @param root view instance
   * @param id view identifier
   * @return extracted instance of view
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T id(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    return (T) root.findViewById(id);
  }

  /**
   * Find View and assign to it click listener in one call.
   *
   * @param root view instance
   * @param id view identifier
   * @param listener click listener instance
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T click(final Activity root, final int id, final OnClickListener listener) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setOnClickListener(listener);
    }

    return (T) view;
  }

  /**
   * Find View and assign to it click listener in one call.
   *
   * @param root view instance
   * @param id view identifier
   * @param listener click listener instance
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T click(final View root, final int id, final OnClickListener listener) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setOnClickListener(listener);
    }

    return (T) view;
  }

  /**
   * Find View and assign to it checked state listener in one call.
   *
   * @param root view instance
   * @param id view identifier
   * @param listener click listener instance
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends CompoundButton> T onCheck(final Activity root, final int id,
                                                           final OnCheckedChangeListener listener) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    CompoundButton view = (CompoundButton) root.findViewById(id);

    if (null != view) {
      view.setOnCheckedChangeListener(listener);
    }

    return (T) view;
  }

  /**
   * Find View and assign to it checked state listener in one call.
   *
   * @param root view instance
   * @param id view identifier
   * @param listener click listener instance
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends CompoundButton> T onCheck(final View root, final int id,
                                                           final OnCheckedChangeListener listener) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    CompoundButton view = (CompoundButton) root.findViewById(id);

    if (null != view) {
      view.setOnCheckedChangeListener(listener);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it state to readonly.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T readonly(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setClickable(false);
      view.setFocusable(false);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it state to readonly.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T readonly(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setClickable(false);
      view.setFocusable(false);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it state to read/write.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T writable(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setClickable(true);
      view.setFocusable(true);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it state to read/write.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T writable(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setClickable(true);
      view.setFocusable(true);
    }

    return (T) view;
  }

  /**
   * Find View in layout to disabled state.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T disable(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setEnabled(false);
    }

    return (T) view;
  }

  /**
   * Find View in layout to disabled state.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T disable(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setEnabled(false);
    }

    return (T) view;
  }

  /**
   * Find View in layout to enabled state.
   *
   * @param root activity instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T enable(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setEnabled(true);
    }

    return (T) view;
  }

  /**
   * Find View in layout to enabled state.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T enable(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setEnabled(true);
    }

    return (T) view;
  }

  /**
   * Based on Enabled state update view state to ENABLED or DISABLED.
   *
   * @param root view instance
   * @param id view identifier
   * @param enabled
   * @return found view, otherwise null.
   */
  public static final <T extends View> T availability(final View root, final int id, boolean enabled) {
    if (enabled) {
      return enable(root, id);
    }

    return disable(root, id);
  }

  /**
   * Based on Enabled state update view state to ENABLED or DISABLED.
   *
   * @param root activity instance
   * @param id view identifier
   * @param enabled
   * @return found view, otherwise null.
   */
  public static final <T extends View> T availability(final Activity root, final int id, boolean enabled) {
    if (enabled) {
      return enable(root, id);
    }

    return disable(root, id);
  }

  /**
   * Find View in layout and set it visibility to VISIBLE.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T show(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    final View view = (View) root.findViewById(id);
    ValidUtils.isNull(view, "Cannot find view with id: " + id + ".");

    if (null != view) {
      view.setVisibility(View.VISIBLE);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it visibility to VISIBLE.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T show(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setVisibility(View.VISIBLE);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it visibility to GONE.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T gone(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setVisibility(View.GONE);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it visibility to GONE.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T gone(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setVisibility(View.GONE);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it visibility to INVISIBLE.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T hide(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setVisibility(View.INVISIBLE);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it visibility to INVISIBLE.
   *
   * @param root view instance
   * @param id view identifier
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T hide(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setVisibility(View.INVISIBLE);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it visibility to provided value.
   *
   * @param root view instance
   * @param id view identifier
   * @param visibility defined by user visibility level
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T visibility(final Activity root, final int id, final int visibility) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setVisibility(visibility);
    }

    return (T) view;
  }

  /**
   * Find View in layout and set it visibility to provided value.
   *
   * @param root view instance
   * @param id view identifier
   * @param visibility defined by user visibility level
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T visibility(final View root, final int id, final int visibility) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setVisibility(visibility);
    }

    return (T) view;
  }

  /**
   * Extract text and item instance from view by id.
   *
   * @param root activity instance
   * @param id view unique identifier.
   * @return extracted values.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> ValueView<String, T> text(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    TextView view = (TextView) root.findViewById(id);
    String value = null;

    if (null != view) {
      value = view.getText().toString();
    }

    return new ValueView<String, T>(value, (T) view);
  }

  /**
   * Find View and set it Text.
   *
   * @param root view instance
   * @param id view identifier
   * @param text text resource id
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T text(final Activity root, final int id, final int text) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setText(text);
    }

    return (T) view;
  }

  /**
   * Find View and set it Text.
   *
   * @param root view instance
   * @param id view identifier
   * @param text text resource id
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T text(final View root, final int id, final int text) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setText(text);
    }

    return (T) view;
  }

  /**
   * Find View and set it Text.
   *
   * @param root view instance
   * @param id view identifier
   * @param text text to set
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T text(final Activity root, final int id, final String text) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setText(text);
    }

    return (T) view;
  }

  /**
   * Find View and set it Text.
   *
   * @param root view instance
   * @param id view identifier
   * @param text text to set
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T text(final View root, final int id, final String text) {
    ValidUtils.isNull(root, "Required instance of the activity/view.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setText(text);
    }

    return (T) view;
  }

  /**
   * Find View and set it hint message.
   *
   * @param root view instance
   * @param id view identifier
   * @param hint text resource id
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T hint(final Activity root, final int id, final int hint) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setHint(hint);
    }

    return (T) view;
  }

  /**
   * Find View and set it hint message.
   *
   * @param root view instance
   * @param id view identifier
   * @param hint hint resource id
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T hint(final View root, final int id, final int hint) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setHint(hint);
    }

    return (T) view;
  }

  /**
   * Find View and set it hint message.
   *
   * @param root view instance
   * @param id view identifier
   * @param hint hint to set
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T hint(final Activity root, final int id, final String hint) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setHint(hint);
    }

    return (T) view;
  }

  /**
   * Find View and set it hint message.
   *
   * @param root view instance
   * @param id view identifier
   * @param hint hint to set
   * @return found view, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends TextView> T hint(final View root, final int id, final String hint) {
    ValidUtils.isNull(root, "Required instance of the activity/view.");

    TextView view = (TextView) root.findViewById(id);

    if (null != view) {
      view.setHint(hint);
    }

    return (T) view;
  }

  /**
   * Find View and assign to it click listener in one call.
   *
   * @param root activity instance
   * @param id view identifier
   * @param background drawable identifier
   * @return found view, otherwise null.
   */
  @SuppressLint("NewApi")
  @SuppressWarnings({
          "unchecked",
          "deprecation"})
  public static final <T extends View> T background(final Activity root, final int id, final int background) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      final Drawable d = root.getResources().getDrawable(background);

      if (android.os.Build.VERSION.SDK_INT < JELLY_BEAN) {
        view.setBackgroundDrawable(d);
      } else {
        view.setBackground(d);
      }
    }

    return (T) view;
  }

  /**
   * Find View and assign to it click listener in one call.
   *
   * @param root view instance
   * @param id view identifier
   * @param background drawable identifier
   * @return found view, otherwise null.
   */
  @SuppressLint("NewApi")
  @SuppressWarnings({
          "unchecked",
          "deprecation"})
  public static final <T extends View> T background(final View root, final int id, final int background) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      final Drawable d = root.getResources().getDrawable(background);

      if (android.os.Build.VERSION.SDK_INT < JELLY_BEAN) {
        view.setBackgroundDrawable(d);
      } else {
        view.setBackground(d);
      }
    }

    return (T) view;
  }

  /**
   * Find view and change it color matrix to gray-scale mode. Emulate effect of none enabled button.
   *
   * @param root parent instance
   * @param id unique identifier of the view
   * @return found view instance.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends ImageView> T gray(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    ImageView view = (ImageView) root.findViewById(id);

    if (null != view) {
      final ColorMatrix matrix = new ColorMatrix();
      matrix.setSaturation(0);
      final ColorMatrixColorFilter gray = new ColorMatrixColorFilter(matrix);

      view.setColorFilter(gray);
    }

    return (T) view;
  }

  /**
   * Find view and change it color matrix to gray-scale mode. Emulate effect of none enabled button.
   *
   * @param root parent instance
   * @param id unique identifier of the view
   * @return found view instance.
   */
  public static final <T extends ImageView> T gray(final Fragment root, final int id) {
    return gray(root.getActivity(), id);
  }

  /**
   * Find view and change it color matrix to gray-scale mode. Emulate effect of none enabled button.
   *
   * @param root parent instance
   * @param id unique identifier of the view
   * @return found view instance.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends ImageView> T gray(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    ImageView view = (ImageView) root.findViewById(id);

    if (null != view) {
      final ColorMatrix matrix = new ColorMatrix();
      matrix.setSaturation(0);
      final ColorMatrixColorFilter gray = new ColorMatrixColorFilter(matrix);

      view.setColorFilter(gray);
    }

    return (T) view;
  }

  /**
   * Find view and clear it color matrix.
   *
   * @param root parent instance
   * @param id unique identifier of the view
   * @return found view instance.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends ImageView> T fullcolor(final Activity root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    ImageView view = (ImageView) root.findViewById(id);

    if (null != view) {
      view.clearColorFilter();
    }

    return (T) view;
  }

  /**
   * Find view and clear it color matrix.
   *
   * @param root parent instance
   * @param id unique identifier of the view
   * @return found view instance.
   */
  public static final <T extends ImageView> T fullcolor(final Fragment root, final int id) {
    return fullcolor(root.getActivity(), id);
  }

  /**
   * Find view and clear it color matrix.
   *
   * @param root parent instance
   * @param id unique identifier of the view
   * @return found view instance.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends ImageView> T fullcolor(final View root, final int id) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    ImageView view = (ImageView) root.findViewById(id);

    if (null != view) {
      view.clearColorFilter();
    }

    return (T) view;
  }

  /**
   * Find view and set it background color.
   *
   * @param root root of search.
   * @param id unique identifier of view.
   * @param backColor background color. Supported formats are: #RRGGBB #AARRGGBB 'red', etc.
   * @return instance of changed view.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T backgroundcolor(final Activity root, final int id, final String backColor) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setBackgroundColor(Color.parseColor(backColor));
    }

    return (T) view;
  }

  /**
   * Find view and set it background color.
   *
   * @param root root of search.
   * @param id unique identifier of view.
   * @param backColor background color. Supported formats are: #RRGGBB #AARRGGBB 'red', etc.
   * @return instance of changed view.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T backgroundcolor(final View root, final int id, final String backColor) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    View view = (View) root.findViewById(id);

    if (null != view) {
      view.setBackgroundColor(Color.parseColor(backColor));
    }

    return (T) view;
  }

  /**
   * Find View and assign to it click listener in one call.
   *
   * @param root activity instance
   * @param id view identifier
   * @param value value to set.
   * @return found view, otherwise null.
   */
  @SuppressWarnings({"unchecked"})
  public static final <T extends CompoundButton> T checked(final Activity root, final int id, final boolean value) {
    ValidUtils.isNull(root, "Required instance of the activity.");

    CompoundButton view = (CompoundButton) root.findViewById(id);

    if (null != view) {
      view.setChecked(value);
    }

    return (T) view;
  }

  /**
   * Find View and assign to it click listener in one call.
   *
   * @param root view instance
   * @param id view identifier
   * @param value value to set.
   * @return found view, otherwise null.
   */
  @SuppressWarnings({"unchecked"})
  public static final <T extends CompoundButton> T checked(final View root, final int id, final boolean value) {
    ValidUtils.isNull(root, "Required instance of the view.");

    CompoundButton view = (CompoundButton) root.findViewById(id);

    if (null != view) {
      view.setChecked(value);
    }

    return (T) view;
  }

  /**
   * Get root view of the current fragment.
   *
   * @param activity fragment instance.
   * @return found root view.
   */
  public static final <T extends Activity> View root(T activity) {
    ValidUtils.isNull(activity, "Activity instance required.");

    return activity.getWindow().getDecorView().findViewById(android.R.id.content);
  }

  /**
   * Get root view of the current fragment.
   *
   * @param fragment fragment instance.
   * @return found root view.
   */
  public static final <T extends Fragment> View root(T fragment) {
    ValidUtils.isNull(fragment, "Fragment instance required.");
    ValidUtils.isNull(fragment.getActivity(), "Fragment should be a part of Activity.");

    return fragment.getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
  }

  /**
   * Try to find tag with specified id in current view or it parents hierarchy.
   *
   * @param v start point of views hierarchy
   * @param tagId unique tag identifier
   * @return found tag instance or null.
   */
  @SuppressWarnings("unchecked")
  public static <T> T findTagValue(final View v, int tagId) {
    if (null == v) {
      return null;
    }

    Object tag = v.getTag(tagId);

    if (null != tag) {
      return (T) tag;
    }

    ViewParent vp = v.getParent();

    while (null == tag && null != vp) {
      if (vp instanceof View) {
        tag = ((View) vp).getTag(tagId);
      }

      vp = vp.getParent();
    }

    if (null != tag) {
      return (T) tag;
    }

    return null;
  }

  /**
   * Try to find tag with specified id in current view or it parents hierarchy. Method return view
   * that contains tag.
   *
   * @param v start point of views hierarchy
   * @param tagId unique tag identifier
   * @return found tag instance or null.
   */
  @SuppressWarnings("unchecked")
  public static <T extends View> T findTag(final View v, int tagId) {
    if (null == v) {
      return null;
    }

    Object tag = v.getTag(tagId);

    if (null != tag) {
      return (T) v;
    }

    ViewParent vp = v.getParent();
    View vFound = null;

    while (null == tag && null != vp) {
      if (vp instanceof View) {
        vFound = (View) vp;
        tag = vFound.getTag(tagId);
      }

      vp = vp.getParent();
    }

    if (null != tag) {
      return (T) vFound;
    }

    return null;
  }

  /**
   * Try to find in views hierarchy parent with corresponding id.
   *
   * @param v start point of view hierarchy.
   * @param id unique identifier of parent view in scope.
   * @return found view instance or null.
   */
  public static View findParent(final View v, final int id) {
    if (null == v) {
      return null;
    }

    if (v.getId() == id) {
      return v;
    }

    ViewParent vp = v.getParent();

    while (null != vp) {
      if (vp instanceof View) {
        if (((View) vp).getId() == id) {
          return (View) vp;
        }
      }

      vp = vp.getParent();
    }

    return null;
  }

  @SuppressWarnings({
          "unchecked",
          "deprecation"})
  public static final <T extends Preference> T pref(final PreferenceActivity pa, final String key) {
    return (T) pa.findPreference(key);
  }

  /**
   * Extract instance of system service with specified data type and service name.
   *
   * @param view fragment instance, which request service.
   * @param serviceName service name.
   * @return casted to specific class type service.
   */
  @TargetApi(HONEYCOMB)
  @SuppressWarnings("unchecked")
  public static final <T extends Object> T service(final Fragment view, final String serviceName) {
    return (T) view.getActivity().getSystemService(serviceName);
  }

  /**
   * Extract instance of system service with specified data type and service name.
   *
   * @param view fragment instance, which request service.
   * @param serviceName service name.
   * @return casted to specific class type service.
   */
  @TargetApi(HONEYCOMB)
  @SuppressWarnings("unchecked")
  public static final <T extends Object> T service(final android.app.Fragment view, final String serviceName) {
    return (T) view.getActivity().getSystemService(serviceName);
  }

  /**
   * Extract instance of system service with specified data type and service name.
   *
   * @param context application context.
   * @param serviceName service name.
   * @return casted to specific class type service.
   */
  @SuppressWarnings("unchecked")
  public static final <T extends Object> T service(final Context context, final String serviceName) {
    return (T) context.getSystemService(serviceName);
  }

  /**
   * Find view and assign to it adapter.
   *
   * @param v start point of view hierarchy.
   * @param id unique identifier of parent view in scope.
   * @param adapter adapter instance or null.
   * @return found adapter
   */
  public static final <W extends View, T extends Adapter> W adapter(final View v, final int id, T adapter) {
    final AdapterView<T> aview = Use.id(v, id);

    if (null != aview) {
      aview.setAdapter(adapter);
    }

    return (W) aview;
  }

  // #endregion

  // #region Helpers, Strings, Arrays

  /**
   * Join multiple objects into one string with defined separator. Null values are skipped.
   *
   * @param separator separator string.
   * @param objects objects to join.
   * @return result string.
   */
  public static final String join(final String separator, Object... objects) {
    final StringBuilder sb = new StringBuilder(1024);
    String delimiter = "";

    for (int i = 0, len = objects.length; i < len; i++) {
      final Object param = objects[i];

      if (null != param) {
        sb.append(delimiter);
        sb.append((param instanceof CharSequence) ? (CharSequence) param : param.toString());
        delimiter = separator;
      }
    }

    return sb.toString();
  }

  /**
   * Search in array for specified value.
   *
   * @param array array of items
   * @param v item to search
   * @return true - found, otherwise false.
   */
  public static <T> boolean contains(final T[] array, final T v) {
    for (final T e : array) {
      if (e == v || v != null && v.equals(e)) {
        return true;
      }
    }

    return false;
  }

  /**
   * create unique set of items from array.
   *
   * @param array array to convert.
   * @return Compose set with unique values.
   */
  public static <T> Set<T> unique(final T[] array) {
    return new HashSet<T>(Arrays.asList(array));
  }

  /**
   * Split long strings into set of smaller.
   *
   * @param text string to split.
   * @param sliceSize slice size.
   * @return strings collection.
   */
  public static List<String> split(final String text, final int sliceSize) {
    final List<String> textList = new ArrayList<String>();

    String aux;
    int left = -1, right = 0;
    int charsLeft = text.length();

    while (charsLeft != 0) {
      left = right;

      if (charsLeft >= sliceSize) {
        right += sliceSize;
        charsLeft -= sliceSize;
      } else {
        right = text.length();
        aux = text.substring(left, right);
        charsLeft = 0;
      }

      aux = text.substring(left, right);
      textList.add(aux);
    }

    return textList;
  }

  /**
   * Extract resource string.
   *
   * @param context application context.
   * @param id unique identifier of the string.
   * @return extracted string.
   */
  public static String string(final Context context, final int id) {
    ValidUtils.isNull(context, "Required instance of the context.");

    final Resources r = context.getResources();
    final String fString = r.getString(id);

    return fString;
  }

  /**
   * Extract resource color.
   *
   * @param context application context.
   * @param id unique identifier of the string.
   * @return extracted string.
   */
  public static int color(final Context context, final int id) {
    ValidUtils.isNull(context, "Required instance of the context.");

    final Resources r = context.getResources();
    final int _color = r.getColor(id);

    return _color;
  }

  /**
   * Post delayed execution with 100 millis delay.
   *
   * @param f parent instance.
   * @param r runnable to execute.
   */
  public static void postDelayed(final Fragment f, final Runnable r) {
    if (null != f && null != r) {
      postDelayed(f.getActivity(), r);
    }
  }

  /**
   * Post delayed execution with 100 millis delay.
   *
   * @param f parent instance.
   * @param r runnable to execute.
   */
  public static void postDelayed(final Activity f, final Runnable r) {
    final long DELAY = 100;

    postDelayed(f, r, DELAY);
  }

  /**
   * Post delayed execution with 100 millis delay.
   *
   * @param f parent instance.
   * @param r runnable to execute.
   */
  public static void postDelayed(final Activity f, final Runnable r, final long delay) {
    if (null != f && null != r) {
      // check that we are not in orientation change process when handler is not available
      // and logic can partly returns NULLs
      if (null != f.getWindow() &&
              null != f.getWindow().getDecorView() &&
              null != f.getWindow().getDecorView().getHandler()) {
        final Handler handler = f.getWindow().getDecorView().getHandler();

        handler.postDelayed(r, delay);
      } else {
        _log.warning("Post delayed call is not set. Main handler is not reachable.");

        if (null == sHandler) {
          sHandler = new Handler();
        }

        sHandler.postDelayed(r, delay);
      }
    }
  }

  /**
   * Create LONG stay toast with message from resource string.
   *
   * @param context application context
   * @param stringId resource string identifier
   * @return created toast.
   */
  public static Toast toast(final Context context, final int stringId) {
    final String msg = context.getString(stringId);

    return toast(context, msg);
  }

  /**
   * Create LONG stay toast with message from resource string.
   *
   * @param context application context
   * @param msg message to show
   * @return created toast.
   */
  public static Toast toast(final Context context, final String msg) {
    ValidUtils.isNull(context, "expected valid context instance.");

    if (null == context)
      return null;

    final Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
    toast.show();
    return toast;
  }

  /**
   * Show not implemented message to the user.
   *
   * @param context any available context.
   */
  public static <T extends Context> void notImplemented(T context) {
    Toast.makeText(context, "Not implemented!", Toast.LENGTH_LONG).show();
  }

  /**
   * Show not implemented message to the user.
   *
   * @param context any available context.
   */
  public static <T extends Fragment> void notImplemented(T context) {
    notImplemented(context.getActivity());
  }

  public static int statusbarHeight(final Context context) {
    int heightStatusBar = 0;
    final Resources resources = context.getResources();
    final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");

    if (resourceId > 0) {
      heightStatusBar = resources.getDimensionPixelSize(resourceId);
    }

    return heightStatusBar;
  }

  public static float dp2pixel(final Context context, int idDimens) {
    final Resources r = context.getResources();
    final float pixels = r.getDimension(idDimens);
    return pixels;
  }

  public static float dp2pixel(final Context context, float dip) {
    final Resources r = context.getResources();
    final float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
    return pixels;
  }

  public static float pixel2dp(final Context context, int idDimens) {
    final Resources r = context.getResources();
    final float pixels = r.getDimension(idDimens);
    final float dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels, r.getDisplayMetrics());

    return dip;
  }

  public static float pixel2dp(final Context context, float pixels) {
    final Resources r = context.getResources();
    final float dip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels, r.getDisplayMetrics());

    return dip;
  }

  /**
   * This method converts dp unit to equivalent pixels, depending on device density.
   *
   * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
   * @param context Context to get resources and device specific display metrics
   * @return A float value to represent px equivalent to dp depending on device density
   */
  public static float convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return px;
  }

  /**
   * This method converts device specific pixels to density independent pixels.
   *
   * @param px A value in px (pixels) unit. Which we need to convert into db
   * @param context Context to get resources and device specific display metrics
   * @return A float value to represent dp equivalent to px value
   */
  public static float convertPixelsToDp(float px, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = px / (metrics.densityDpi / 160f);
    return dp;
  }

  /**
   * I added a generic return type to reduce the casting noise in client code
   *
   * @param view instance of view to check
   * @param id unique identifier of the the view.
   * @return found view instance.
   */
  @SuppressWarnings("unchecked")
  public static <T extends View> T getholded(final View view, final int id) {
    SparseArray<View> viewHolder = (SparseArray<View>) view.getTag(R.id.tag_view_holder);

    if (null == viewHolder) {
      view.setTag(R.id.tag_view_holder, viewHolder = new SparseArray<View>());
    }

    View childView = viewHolder.get(id);

    if (null == childView) {
      viewHolder.put(id, childView = view.findViewById(id));
    }

    return (T) childView;
  }

  /**
   * Try to detect view type by previously stored tag assigned by call of
   * {@link Use#tagType(View, String)}. Tag searched in provided view or it parent hierarchy.
   * Comparison of names done in case-insensitive mode.
   *
   * @param view view to check.
   * @param name type name
   * @return True - types are identical, otherwise - not found type info or they are different.
   */
  public static boolean isType(final View view, final String name) {
    boolean checkPassed = false;

    final View root = Use.findTag(view, R.id.tag_view_type);

    if (null != root) {
      final String tag = String.valueOf(root.getTag(R.id.tag_view_type));

      checkPassed = tag.equalsIgnoreCase(name);
    }

    return checkPassed;
  }

  /**
   * Extract view type name from binded tag, if not found in provided view method will search in
   * parent hierarchy. See - {@link Use#tagType(View, String)}.
   *
   * @param view view instance.
   * @return extracted value, otherwise null.
   */
  public static String tagType(final View view) {
    final View root = Use.findTag(view, R.id.tag_view_type);

    if (null != root) {
      return String.valueOf(root.getTag(R.id.tag_view_type));
    }

    return null;
  }

  /**
   * Store in tags view name for conversion adapter operations. See -
   * {@link Use#isType(View, String)}.
   *
   * @param view view to update.
   * @param name name to store.
   */
  public static void tagType(final View view, final String name) {
    view.setTag(R.id.tag_view_type, name);
  }

  /**
   * Extract instance of object from binded tag, if not found in provided view method will search in
   * parent hierarchy. See - {@link Use#tagInstance(View, Object)}.
   *
   * @param view view instance.
   * @return extracted value, otherwise null.
   */
  @SuppressWarnings("unchecked")
  public static <T> T tagInstance(final View view) {
    final View root = Use.findTag(view, R.id.tag_item_instance);

    if (null != root) {
      return (T) root.getTag(R.id.tag_item_instance);
    }

    return null;
  }

  /**
   * Store in tags view instance of binded object for conversion adapter operations.
   *
   * @param view view to update.
   * @param value instance to store.
   */
  public static void tagInstance(final View view, final Object value) {
    view.setTag(R.id.tag_item_instance, value);
  }

	/* [ NESTED DECLARATIONS ] ============================================================================================================================= */

  /**
   * Generic data class for extracting value and View that is contains instance.
   *
   * @param <K> extracted value.
   * @param <T> extracted instance of view.
   */
  public static class ValueView<K, T extends View> {
    /** Extracted value. */
    public final K value;

    /** View instance. */
    public final T view;

    public ValueView(final K one, final T two) {
      value = one;
      view = two;
    }
  }
}
