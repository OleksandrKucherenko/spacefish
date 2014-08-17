package com.amaya.game;

import com.amaya.game.entities.Fish;
import com.amaya.game.entities.Level;
import com.amaya.game.screens.BaseScreen;
import com.amaya.game.screens.GameOverScreen;
import com.amaya.game.screens.InfoScreen;
import com.amaya.game.screens.LevelScreen;
import com.amaya.game.screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import java.util.LinkedList;
import java.util.Random;

/**
 * Class responsible for persistence of the current game state.
 *
 * @see <a href="https://github.com/libgdx/libgdx-demo-superjumper">Sample</a>
 */
public class Spacefish extends Game {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** Logs Tag. */
  public static final String LOG_TAG = "spacefish";

	/* [ STATIC MEMBERS ] ==================================================================================================================================== */

  /** Random numbers generator. */
  private final static Random sRandom = new Random(System.currentTimeMillis());

	/* [ MEMBERS ] =========================================================================================================================================== */

  /** Reference on shape renderer that we using for debugging bounds. */
  private ShapeRenderer mDebugShapes;
  /** Drawing batching. */
  private SpriteBatch mBatch;
  /** Create entity of the Fish/user. */
  private final Fish mFish = new Fish();
  /** UI navigation backstack. */
  private final LinkedList<BaseScreen> mUiStack = new LinkedList<BaseScreen>();

	/* [ STATIC METHODS ] ==================================================================================================================================== */

  public static int randomInt(final int max) {
    return sRandom.nextInt(max);
  }

  public static float randomFloat(final float max) {
    return sRandom.nextFloat() * max;
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  public SpriteBatch getBatch() {
    return mBatch;
  }

  public ShapeRenderer getDebugShapes() {
    return mDebugShapes;
  }

  public Fish getFish() {
    return mFish;
  }

  protected InfoScreen getInfo() {
    return new InfoScreen(this);
  }

  protected LevelScreen getLevel() {
    // use builder pattern for level configuration building.
    final Level level = new Level.Builder()
            .setTotalTime(45)
            .randomize()
            .build();

    return new LevelScreen(this, level);
  }

  protected MenuScreen getMenu() {
    return new MenuScreen(this);
  }

  protected GameOverScreen getGameOver() {
    return new GameOverScreen(this);
  }

	/* [ Interface ApplicationListener ] ===================================================================================================================== */

  @Override
  public void create() {
    mBatch = LibGdxFactory.getInstance().newSpriteBatch();
    mDebugShapes = LibGdxFactory.getInstance().newShapeRenderer();

    GameResources.getInstance().load();

    navigateToMenu();
  }

  @Override
  public void dispose() {
    GameResources.getInstance().dispose();
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  public LinkedList<BaseScreen> getNavigationStack() {
    return mUiStack;
  }

  public void navigateBack() {
    if (Debug.UI_NAVIGATION)
      Gdx.app.log(LOG_TAG, "[navigate] to previous screen");

    // in back stack should stay at least one Screen
    if (mUiStack.size() > 1) {
      mUiStack.removeLast();
      setScreen(mUiStack.getLast());

      if (Debug.UI_NAVIGATION)
        Gdx.app.log(LOG_TAG, "[navigate] to previous screen: " + mUiStack.getLast().getClass().getSimpleName());
    }
  }

  public void navigateToInfo() {
    if (Debug.UI_NAVIGATION)
      Gdx.app.log(LOG_TAG, "[navigate] to 'Info'");

    mUiStack.add(getInfo());
    setScreen(mUiStack.getLast());
  }

  public void navigateToLevel() {
    if (Debug.UI_NAVIGATION)
      Gdx.app.log(LOG_TAG, "[navigate] to 'Game Level'");

    mUiStack.add(getLevel());
    setScreen(mUiStack.getLast());
  }

  public void navigateToMenu() {
    if (Debug.UI_NAVIGATION)
      Gdx.app.log(LOG_TAG, "[navigate] to 'Main Menu'");

    mUiStack.add(getMenu());
    setScreen(mUiStack.getLast());
  }

  public void navigateToGameOver() {
    if (Debug.UI_NAVIGATION)
      Gdx.app.log(LOG_TAG, "[navigate] to 'Game Over'");

    // recreate navigation stack. From Game over screen we can return only to 'main menu'
    mUiStack.clear();
    mUiStack.add(getMenu());
    mUiStack.add(getGameOver());

    setScreen(mUiStack.getLast());
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  /** Dimensions used for game design. */
  public interface Dimensions {
    /** Width. Portrait game in virtual resolution: 320x480. */
    int VIRTUAL_SCREEN_WIDTH = 320;
    /** Height. Portrait game in virtual resolution: 320x480. */
    int VIRTUAL_SCREEN_HEIGHT = 480;
    /** Aspect ration: <code>0.6666f</code>. Portrait game in virtual resolution: 320x480. */
    float VIRTUAL_ASPECT_RATIO = (float) VIRTUAL_SCREEN_WIDTH / (float) VIRTUAL_SCREEN_HEIGHT;
    /** white space between several Menu Items. */
    float DELIMITER = 20.0f;
    /** smallest whitespace. */
    float SPACE = 4.0f;
    /** Width of the icon used for image buttons. */
    float ICON_WIDTH = 32.0f;
    /** Height of the icon used for image buttons. */
    float ICON_HEIGHT = 32.0f;
    /** Button Controls minimal padding. */
    float ICON_PADDING = 16.0f;
    /** fish entity width. */
    float FISH_WIDTH = 64.0f;
    /** fish entity height. */
    float FISH_HEIGHT = 32.0f;
    /** Calculations accuracy. */
    float ACCURACY = 0.55f;
  }

  /** Debug switches, allow to disable or enable logs in specific module. */
  public interface Debug {
    /** dump strategy calculations. */
    boolean STRATEGY_MOMENTUM = false;
    /** dump strategy calculations. */
    boolean STRATEGY_LINEAR = false;
    /** dump strategy calculations. */
    boolean STRATEGY_GRAVITY = false;
    /** dump fish trajectory. */
    boolean FISH_BEHAVIOR = false;
    /** dump alien trajectory. */
    boolean ALIEN_BEHAVIOR = false;
    /** dump asteroid trajectory. */
    boolean ASTEROID_BEHAVIOR = false;
    /** dump level randomized numbers. */
    boolean LEVEL_NUMBERS = false;
    /** dump ui bounds. */
    boolean UI_BOUNDS = false;
    /** dump ui screens navigation. */
    boolean UI_NAVIGATION = false;
    /** dump level update strategy results. */
    boolean LEVEL_DROPS = false;
    /** dump ui clicks. */
    boolean UI_CLICKS = false;
    /** dump MoveToCommands creation. */
    boolean MOVE_COMMANDS = false;
    /** dump Move trajectory creation. */
    boolean MOVE_TRAJECTORY = false;
    /** dump run environment */
    boolean ENVIRONMENT = false;
    /** dump level state changes. */
    boolean LEVEL_STATE = false;
    /** dump any fish collisions. */
    boolean FISH_COLLISIONS = false;
    /** dump all expired commands processing. */
    boolean EXPIRED_COMMANDS = true;
  }
}
