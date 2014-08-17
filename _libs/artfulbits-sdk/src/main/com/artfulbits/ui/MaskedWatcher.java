package com.artfulbits.ui;

import android.text.Editable;
import android.text.TextWatcher;

import java.text.ParseException;

/**
 * Mask editor. Watcher that keeps user input following specified mask.<br/>
 * <br/>
 * Reference:<br/>
 * <a href="http://horribileru.blogspot.com/2011/12/textedit.html">Masked editor</a>
 */
public class MaskedWatcher
        implements TextWatcher {
  // #region Members
  private String mMask;

  private String mResult = "";

  // #endregion

  // #region Constructors
  public MaskedWatcher(String mask) {
    mMask = mask;
  }

  // #endregion

  // #region Overrides

  /** {@inheritDoc} */
  @Override
  public void afterTextChanged(Editable s) {
    String mask = mMask;
    String value = s.toString();

    if (value.equals(mResult))
      return;

    try {
      // prepare the formatter
      MaskedFormatter formatter = new MaskedFormatter(mask);
      formatter.setValueContainsLiteralCharacters(false);
      formatter.setPlaceholderCharacter((char) 1);

      // get a string with applied mask and placeholder chars
      value = formatter.valueToString(value);

      try {
        // find first placeholder
        value = value.substring(0, value.indexOf((char) 1));

        // process a mask char
        if (value.charAt(value.length() - 1) == mask.charAt(value.length() - 1)) {
          value = value.substring(0, value.length() - 1);
        }
      } catch (Exception e) {
      }

      mResult = value;

      s.replace(0, s.length(), value);
    } catch (ParseException e) {
      // the entered value does not match a mask
      int offset = e.getErrorOffset();
      value = removeCharAt(value, offset);
      s.replace(0, s.length(), value);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    // do nothing
  }

  /** {@inheritDoc} */
  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    // do nothing
  }

  // #endregion

  // #region Public methods
  public static String removeCharAt(final String s, int pos) {
    final StringBuffer buffer = new StringBuffer(s.length() - 1);
    buffer.append(s.substring(0, pos)).append(s.substring(pos + 1));

    return buffer.toString();
  }
  // #endregion
}