package com.amaya.game.screens;

import com.amaya.game.GameResources;
import com.amaya.game.Spacefish;
import com.amaya.game.entities.controls.MenuItem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import static com.amaya.game.Spacefish.Dimensions;

/** */
public class MenuScreen extends BaseScreen {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Collection of menu items. Initialized in static constructor of the class. */
  private final List<MenuItem> mMenuItems = new ArrayList<MenuItem>();

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Instantiates a new Menu screen.
   *
   * @param game the game instance.
   */
  public MenuScreen(final Spacefish game) {
    super(game);

    initializeMainMenu();
  }

  private void initializeMainMenu() {
    // initialize menu items
    mMenuItems.add(new MenuItem(Menu.START) {
      @Override
      public void click() {
        // start the game!
        getGame().navigateToLevel();
      }
    });

    mMenuItems.add(new MenuItem(Menu.EXIT) {
      @Override
      public void click() {
        System.exit(0);
      }
    });
  }

	/* [ Interface Screen ] ================================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void render(final float delta) {
    super.render(delta);

    // do rendering in BATCH
    final SpriteBatch batch = getGame().getBatch();
    batch.begin();

    drawBackground(batch);
    drawMenuItems(batch);

    batch.end();
  }

  /** {@inheritDoc} */
  @Override
  public void resize(final int width, final int height) {
    super.resize(width, height);

    final BitmapFont font = GameResources.getInstance().getFont();

    // calculate TEXT bounds, and total occupied space
    int totalHeight = 0;

    for (final MenuItem menu : mMenuItems) {
      menu.setSize(font.getBounds(menu.getText()));
      totalHeight += menu.Bounds.height;
    }

    // calculate on screen position for menu items
    final float delimitersSpace = Dimensions.DELIMITER * (mMenuItems.size() - 1);
    float yPosition = Dimensions.VIRTUAL_SCREEN_HEIGHT - (Dimensions.VIRTUAL_SCREEN_HEIGHT - totalHeight - delimitersSpace) / 2;
    for (int i = 0, len = mMenuItems.size(); i < len; i++) {
      yPosition -= mMenuItems.get(i).centerX(yPosition);

      Gdx.app.log(TAG, mMenuItems.get(i).toString());
    }
  }

  /** {@inheritDoc} */
  @Override
  public void show() {
    super.show();

    // stop playing the game background music
    GameResources.getInstance().getBackgroundMusic().stop();
    GameResources.getInstance().getGameOver().stop();
  }

  /** {@inheritDoc} */
  @Override
  public boolean touched() {
    // convert coordinates
    final Vector3 touch = unprojectTouch();

    if (Spacefish.Debug.UI_CLICKS)
      Gdx.app.log(TAG, "[click] with coordinates: " + touch);

    for (MenuItem menu : mMenuItems) {
      if (menu.Bounds.contains(touch.x, touch.y)) {
        menu.click();
        return true;
      }
    }

    return false;
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  /** Draw the menu. */
  protected void drawMenuItems(final SpriteBatch batch) {
    // draw menu items, z-order: 1
    final BitmapFont font = GameResources.getInstance().getFont();

    batch.enableBlending();
    font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    for (int i = 0, len = mMenuItems.size(); i < len; i++) {
      final MenuItem menu = mMenuItems.get(i);

      font.draw(batch, menu.getText(), menu.Bounds.x, menu.getY());

      debugRect(menu.Bounds);
    }
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  /**
   * Syntax hack. Define constants inside the Menu interface domain, which makes them easy to read in code.
   * In addition syntax hack allows to avoid 'static final' repeating.
   */
  private interface Menu {
    /** Start. */
    String START = "START";
    /** Exit. */
    String EXIT = "EXIT";
  }

}
