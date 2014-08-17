package com.amaya.game.entities.controls;

import com.amaya.game.Spacefish;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** Menu Item. */
public abstract class MenuItem extends BaseControl {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** menu item text to display. */
  private final String mText;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Instantiates a new Menu item.
   *
   * @param text the text
   */
  public MenuItem(final String text) {
    this(text, null);
  }

  /**
   * Instantiates a new Menu item.
   *
   * @param text the text
   * @param tag the tag
   */
  public MenuItem(final String text, final Object tag) {
    super(0, 0, 0, 0, tag);
    mText = text;
  }

	/* [ ABSTRACT ] ========================================================================================================================================== */

  /** Process click on the menu item. */
  public abstract void click();

	/* [ OVERRIDES ] ========================================================================================================================================= */

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "menu: " + mText + ", rect: " + Bounds.toString();
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** return Layout y-coordinate for text displaying for coordinate system started from bottom left.  @return the y */
  public float getY() {
    return Bounds.y + Bounds.height;
  }

  /** Gets menu item text. */
  public String getText() {
    return mText;
  }

  /**
   * Sets size, update bounds.
   *
   * @param size the size
   */
  public void setSize(final BitmapFont.TextBounds size) {
    Bounds.setSize(size.width, size.height);
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  /**
   * Center x. Place the menu item text in the horizontal center.
   *
   * @param yPosition the y position
   * @return the calculate position.
   */
  public int centerX(final float yPosition) {
    Bounds.setPosition((Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH - Bounds.width) / 2, yPosition);

    return (int) (Bounds.height + Spacefish.Dimensions.DELIMITER);
  }
}
