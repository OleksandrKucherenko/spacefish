package com.artfulbits.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/** The helper that shows/hides header during a process of scroll. */
public class ScrollListenerHelper
        implements OnScrollListener, Handler.Callback {
  // #region Constants 
  private final int MSG_SCROLL = -1;

  /** The time (ms) after which header will be shown * */
  private final int DELAY_SHOW_HEADER = 2000;
  // #endregion 

  // #region Members
  /** Header that will be to shown/hidden. * */
  private View mHeader;

  /** Current state * */
  private int mCurrentState = 0;

  /** Handler for showing a header together with waiting * */
  private final Handler mHandler = new Handler(this);
  // #endregion 

  // #region Constructor 

  /**
   * main constructor.
   *
   * @param context application context.
   * @param idView header unique identifier.
   */
  public ScrollListenerHelper(Context context, int idView) {
    mHeader = ((Activity) context).findViewById(idView);
  }
  // #endregion 

  // #region Overrides 

  /** {@inheritDoc} */
  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {
    mCurrentState = scrollState;

    if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {
      mHeader.setVisibility(View.GONE);
    } else if (SCROLL_STATE_FLING == scrollState) {
    } else if (SCROLL_STATE_IDLE == scrollState) {
      Message msg = mHandler.obtainMessage(MSG_SCROLL);

      mHandler.sendMessageDelayed(msg, DELAY_SHOW_HEADER);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean handleMessage(Message msg) {
    if (MSG_SCROLL == msg.what) {
      if (SCROLL_STATE_TOUCH_SCROLL != mCurrentState) {
        mHeader.setVisibility(View.VISIBLE);
        return true;
      }
    }

    return false;
  }

  /** {@inheritDoc} */
  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
  }
  // #endregion 
}
