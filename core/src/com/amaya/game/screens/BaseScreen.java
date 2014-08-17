package com.amaya.game.screens;

import com.amaya.game.GameResources;
import com.amaya.game.LibGdxFactory;
import com.amaya.game.Spacefish;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_ASPECT_RATIO;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH;

/** Base class for all screens. Provides access to the instance of Game. */
public class BaseScreen extends ScreenAdapter {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** Logging tag. */
  public static final String TAG = Spacefish.LOG_TAG;

	/* [ MEMBERS ] =========================================================================================================================================== */

  /** 2D rendering camera. */
  protected final OrthographicCamera mUiCamera;
  /** Reference on the game instance. */
  protected final Spacefish mGame;
  /** Calculated view port for keep correct aspect ratio on all screens. */
  protected Rectangle mViewport;
  /** Scale factor of the screen. */
  protected float mScale = 1f;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public BaseScreen(final Spacefish game) {
    mGame = game;

    mUiCamera = LibGdxFactory.getInstance().newOrthographicCamera();
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  public Spacefish getGame() {
    return mGame;
  }

	/* [ Interface Screen ] ================================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void resize(final int width, final int height) {
    super.resize(width, height);

    // calculate new viewport
    final float aspectRatio = (float) width / (float) height;
    float scale = 1.0f;
    Vector2 crop = new Vector2(0f, 0f);

    if (aspectRatio > VIRTUAL_ASPECT_RATIO) {
      scale = (float) height / (float) VIRTUAL_SCREEN_HEIGHT;
      crop.x = (width - VIRTUAL_SCREEN_WIDTH * scale) / 2f;
    } else if (aspectRatio < VIRTUAL_ASPECT_RATIO) {
      scale = (float) width / (float) VIRTUAL_SCREEN_WIDTH;
      crop.y = (height - VIRTUAL_SCREEN_HEIGHT * scale) / 2f;
    } else {
      scale = (float) width / (float) VIRTUAL_SCREEN_WIDTH;
    }

    mScale = scale;
    final float w = (float) VIRTUAL_SCREEN_WIDTH * scale;
    final float h = (float) VIRTUAL_SCREEN_HEIGHT * scale;
    mViewport = new Rectangle(crop.x, crop.y, w, h);

    if (Spacefish.Debug.ENVIRONMENT)
      Gdx.app.log(TAG, "aspect ration: " + aspectRatio);
  }

  /** {@inheritDoc} */
  @Override
  public void render(final float delta) {
    super.render(delta);

    // process user input
    if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
      touched();
    }

    // cleanup the screen
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // adjust aspect ratio
    Gdx.gl.glViewport((int) mViewport.x, (int) mViewport.y, (int) mViewport.width, (int) mViewport.height);

    // say that our rendering should be scaled to current screen resolution
    mUiCamera.update();

    final SpriteBatch batch = getGame().getBatch();
    batch.setProjectionMatrix(mUiCamera.combined);
  }

  /**
   * Touched event processing.
   *
   * @return true - event processed, otherwise false.
   */
  public boolean touched() {
    if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
      getGame().navigateBack();
      return true;
    }

    return false;
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  protected void drawBackground(final SpriteBatch batch) {
    // draw background, z-order: 0
    batch.disableBlending();
    batch.draw(GameResources.getInstance().getBackground(), 0, 0, VIRTUAL_SCREEN_WIDTH, VIRTUAL_SCREEN_HEIGHT);
  }

  public void debugRect(final Rectangle rc) {
    if (Spacefish.Debug.UI_BOUNDS) {
      getGame().getBatch().draw(GameResources.getInstance().getDebugBounds(),
              rc.x, rc.y, rc.width, rc.height);
    }
  }

  /** Convert last touch position to world coordinates. */
  public Vector3 unprojectTouch() {
    final Vector3 touch = new Vector3();
    touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);

    if (Spacefish.Debug.UI_CLICKS)
      Gdx.app.log(TAG, "[click] with HW coordinates: " + touch + ", viewport: " + mViewport);

    if (mViewport.contains(touch.x, touch.y)) {
//      // shift point on cropped area, HW screen has y-axis from top to down
//      touch.set(touch.x - mViewport.x, touch.y + mViewport.y, 0);
//
//      if (Spacefish.Debug.UI_CLICKS)
//        Gdx.app.log(TAG, "[click] shifted: " + touch);

      mUiCamera.unproject(touch, mViewport.x, mViewport.y, mViewport.width, mViewport.height);
    } else {
      touch.set(-1, -1, -1);
    }

    return touch;
  }
}
