package com.amaya.game.screens;

import com.amaya.game.GameController;
import com.amaya.game.GameRenderer;
import com.amaya.game.GameResources;
import com.amaya.game.Spacefish;
import com.amaya.game.entities.Fish;
import com.amaya.game.entities.Level;
import com.amaya.game.entities.controls.Button;
import com.amaya.game.entities.controls.ToggleButton;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import static com.amaya.game.Spacefish.Dimensions;

/** show specific game level. Game field. */
public class LevelScreen extends BaseScreen {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Reference on game renderer. */
  private final GameRenderer mRenderer;
  /** Reference on game controller. */
  private final GameController mController;
  /** List of UI elements. */
  private final List<Button> mButtons = new ArrayList<Button>();

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public LevelScreen(final Spacefish game, final Level level) {
    super(game);

    initializeButtons();

    mController = new GameController(game, level);
    mRenderer = new GameRenderer(mController);
  }

  private void initializeButtons() {
    mButtons.add(new Button(Dimensions.ICON_PADDING, Dimensions.VIRTUAL_SCREEN_HEIGHT - Dimensions.ICON_PADDING - Dimensions.ICON_HEIGHT,
            Dimensions.ICON_WIDTH, Dimensions.ICON_HEIGHT, Actions.BACK) {
      @Override
      public void click() {
        getGame().navigateBack();
      }
    });

    mButtons.add(new ToggleButton(Dimensions.ICON_PADDING, Dimensions.ICON_PADDING,
            Dimensions.ICON_WIDTH, Dimensions.ICON_HEIGHT, Actions.PAUSE, Actions.PLAY) {
      @Override
      public void toggle() {
        final GameController.KnownStates state = mController.getState();

        mController.setState(GameController.KnownStates.PAUSED == state ?
                GameController.KnownStates.PLAY : GameController.KnownStates.PAUSED);
      }
    });

    mButtons.add(new Button(Dimensions.VIRTUAL_SCREEN_WIDTH - Dimensions.ICON_PADDING - Dimensions.ICON_WIDTH, Dimensions.ICON_PADDING,
            Dimensions.ICON_WIDTH, Dimensions.ICON_HEIGHT, Actions.INFO) {
      @Override
      public void click() {
        // during navigation to Info screen, game is paused. emulate pause click
        final GameController.KnownStates state = mController.getState();
        if (GameController.KnownStates.PAUSED != state) {
          mButtons.get(1).click();
        }

        getGame().navigateToInfo();
      }
    });
  }

	/* [ STATIC METHODS ] ==================================================================================================================================== */

  public static Texture resolve(final Button btn) {
    if (LevelScreen.Actions.BACK.equals(btn.getTag()))
      return GameResources.getInstance().getBack();

    if (LevelScreen.Actions.INFO.equals(btn.getTag()))
      return GameResources.getInstance().getInfo();

    if (LevelScreen.Actions.PAUSE.equals(btn.getTag()))
      return GameResources.getInstance().getPause();

    return GameResources.getInstance().getPlay();
  }

	/* [ Interface Screen ] ================================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void render(final float delta) {
    // do call for super for initial setup of GL graphics
    super.render(delta);

    // recalculate layout of the entities
    mController.update(delta);

    // do updates in batch
    final SpriteBatch batch = getGame().getBatch();
    batch.begin();

    drawBackground(batch);
    drawScores(batch);
    drawControls(batch);

    mRenderer.render(batch);

    batch.end();
  }

  /** {@inheritDoc} */
  @Override
  public void show() {
    super.show();

    // start background music playing
    GameResources.getInstance().getGameOver().stop();
    GameResources.getInstance().getBackgroundMusic().play();
  }

  /** {@inheritDoc} */
  @Override
  public void hide() {
    super.hide();

    // start background music playing
    GameResources.getInstance().getGameOver().stop();
    GameResources.getInstance().getBackgroundMusic().stop();
  }

  /** {@inheritDoc} */
  @Override
  public boolean touched() {
    boolean processed;

    // give super class chance to process BACK button
    if( !( processed = super.touched() ) ) {
      final Vector3 touch = unprojectTouch();

      if (Spacefish.Debug.UI_CLICKS)
        Gdx.app.log(TAG, "[click] with coordinates: " + touch);

      // first try UI controls
      for (Button btn : mButtons) {
        if ((processed = btn.Bounds.contains(touch.x, touch.y))) {
          if (Spacefish.Debug.UI_CLICKS)
            Gdx.app.log(TAG, "[click] button click.");

          btn.click();
          break;
        }
      }

      // give game controller chance to process event
      if (!processed) {
        if (Spacefish.Debug.UI_CLICKS)
          Gdx.app.log(TAG, "[click] delegated to controller.");

        // redirect touch to Game engine
        processed = mController.touched(unprojectTouch());
      }
    }

    return processed;
  }

  /** {@inheritDoc} */
  @Override
  public void resize(final int width, final int height) {
    super.resize(width, height);

    // say controller to update all layouts
    mController.resize(width, height);
    mController.update(Gdx.graphics.getDeltaTime());
  }

  /** {@inheritDoc} */
  @Override
  public void pause() {
    super.pause();

    mController.setState(GameController.KnownStates.PAUSED);
  }

  /** {@inheritDoc} */
  @Override
  public void resume() {
    super.resume();
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  private void drawControls(final SpriteBatch batch) {
    for (Button btn : mButtons) {
      batch.draw(resolve(btn), btn.Bounds.x, btn.Bounds.y, btn.Bounds.width, btn.Bounds.height);

      debugRect( btn.Bounds );
    }
  }

  private void drawScores(final SpriteBatch batch) {
    final BitmapFont font = GameResources.getInstance().getFont();

    batch.enableBlending();
    font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    final Fish fish = mController.getGame().getFish();

    final String points = String.valueOf(fish.Points);
    final BitmapFont.TextBounds bounds = font.getBounds(points);

    final float x = Dimensions.VIRTUAL_SCREEN_WIDTH - Dimensions.ICON_PADDING - bounds.width;
    final float y = Dimensions.VIRTUAL_SCREEN_HEIGHT - bounds.height;
    font.draw(batch, points, x, y);

    final String speed = "x" + fish.getSpeed();
    final BitmapFont.TextBounds bounds1 = font.getBounds(speed);
    font.draw(batch, speed, x - bounds1.width - Dimensions.ICON_PADDING, y);
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  /**
   * Syntax hack.  Define constants inside the Actions interface domain, which makes them easy to read in code.
   * In addition syntax hack allows to avoid 'static final' repeating.
   */
  private interface Actions {
    /** Pause button. */
    String PAUSE = "pause";
    /** Info/Legend button. */
    String INFO = "info";
    /** Play button. */
    String PLAY = "play";
    /** Back to main menu button. */
    String BACK = "back";
  }
}
