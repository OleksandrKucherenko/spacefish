package com.amaya.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** Class designed for giving ability to easy mock all low level LibGdx objects. */
public class LibGdxFactory {

  /** Singleton instance. */
  private static LibGdxFactory INSTANCE = new LibGdxFactory();

  /** Hidden constructor. */
  private LibGdxFactory() {
    // do nothing
  }

  /** private constructor, allows Mockito to replace instance by Mock object. */
  private LibGdxFactory(final LibGdxFactory mock) {
    INSTANCE = mock;
  }

  /** Get instance. */
  public static LibGdxFactory getInstance() {
    return INSTANCE;
  }

  /* [ UNIT TESTING/MOCKS ] ================================================================================================================================ */

  public SpriteBatch newSpriteBatch() {
    return new SpriteBatch();
  }

  public ShapeRenderer newShapeRenderer() {
    return new ShapeRenderer();
  }

  public OrthographicCamera newOrthographicCamera() {
    final OrthographicCamera mUiCamera = new OrthographicCamera(
            Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH,
            Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT);

    mUiCamera.position.set(
            Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH / 2,
            Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT / 2, 0);

    return mUiCamera;
  }
}
