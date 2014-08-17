package com.amaya.game.entities.controls;

/** Button that has several states. */
public abstract class ToggleButton extends Button {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Reference on custom user object. */
  private Object mTag2;
  /** true - return primary tag, otherwise return second tag. */
  private boolean mTagSwitcher = true;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public ToggleButton(final float x, final float y, final float width, final float height, final Object tag, final Object tag2) {
    super(x, y, width, height, tag);

    mTag2 = tag2;
  }

	/* [ ABSTRACT ] ========================================================================================================================================== */

  /** Toggle button state switched. */
  public abstract void toggle();

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** Is toggle button in 'original' state. */
  public boolean isOriginal() {
    return mTagSwitcher;
  }

  /** {@inheritDoc} */
  @Override
  public Object getTag() {
    if (mTagSwitcher) {
      return super.getTag();
    }

    return mTag2;
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void click() {
    mTagSwitcher = !mTagSwitcher;

    toggle();
  }
}
