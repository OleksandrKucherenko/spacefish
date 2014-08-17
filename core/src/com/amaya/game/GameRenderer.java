package com.amaya.game;

import com.amaya.game.entities.environment.Alien;
import com.amaya.game.entities.environment.Asteroid;
import com.amaya.game.entities.modifiers.Command;
import com.amaya.game.entities.modifiers.MoveToCommand;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.amaya.game.Spacefish.Dimensions.ICON_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.ICON_WIDTH;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH;

/** Class responsible for rendering of the Entities states. */
public class GameRenderer {
  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Reference on controller. */
  private final GameController mController;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public GameRenderer(final GameController controller) {
    mController = controller;
  }

	/* [ GET/SET METHODS ] =================================================================================================================================== */

  /** Reference on controller. */
  public GameController getController() {
    return mController;
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  private void drawAliens(final SpriteBatch batch) {
    for (Alien al : getController().getCurrentLevel().Aliens) {
      final Rectangle rc = al.Bounds;

      batch.draw(GameResources.getInstance().getAlien(al), rc.x, rc.y,
              ICON_WIDTH, ICON_HEIGHT);

      if (Spacefish.Debug.ALIEN_BEHAVIOR) {
        final MoveToCommand mtc = (MoveToCommand) MoveToCommand.moveTo(al);

        ShapeRenderer sr = getController().getGame().getDebugShapes();
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(Color.GREEN);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(mtc.StartX, mtc.StartY, mtc.EndX, mtc.EndY);
        sr.circle(rc.x, rc.y, ICON_WIDTH / 4);
        sr.end();
      }

      if (Spacefish.Debug.UI_BOUNDS) {
        batch.draw(GameResources.getInstance().getDebugBounds(), rc.x, rc.y, rc.width, rc.height);
      }
    }
  }

  private void drawAsteroids(final SpriteBatch batch) {
    for (Asteroid as : getController().getCurrentLevel().Asteroids) {
      final Rectangle rc = as.Bounds;
      batch.draw(GameResources.getInstance().getAsteroid(as), rc.x, rc.y, ICON_WIDTH, ICON_HEIGHT);

      if (Spacefish.Debug.ASTEROID_BEHAVIOR) {
        final MoveToCommand mtc = (MoveToCommand) MoveToCommand.moveTo(as);

        ShapeRenderer sr = getController().getGame().getDebugShapes();
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(Color.BLUE);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(mtc.StartX, mtc.StartY, mtc.EndX, mtc.EndY);
        sr.circle(rc.x, rc.y, ICON_WIDTH / 4);
        sr.end();
      }

      if (Spacefish.Debug.UI_BOUNDS) {
        batch.draw(GameResources.getInstance().getDebugBounds(), rc.x, rc.y, rc.width, rc.height);
      }
    }
  }

  private void drawFish(final SpriteBatch batch) {
    final Rectangle rc = getController().getGame().getFish().Bounds;
    batch.draw(GameResources.getInstance().getFish(), rc.x, rc.y, rc.width, rc.height);

    // DEBUG logic
    if (Spacefish.Debug.FISH_BEHAVIOR) {
      final MoveToCommand mtc = Command.findFirst(getController().getCommands(), MoveToCommand.class);

      if (null != mtc) {
        ShapeRenderer sr = getController().getGame().getDebugShapes();
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(Color.RED);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(mtc.StartX, mtc.StartY, mtc.EndX, mtc.EndY);
        sr.circle(rc.x, rc.y, ICON_WIDTH / 4);
        sr.end();
      }
    }

    if (Spacefish.Debug.UI_BOUNDS) {
      batch.draw(GameResources.getInstance().getDebugBounds(), rc.x, rc.y, rc.width, rc.height);
    }
  }

  private void drawState(final SpriteBatch batch) {
    if (GameController.KnownStates.PAUSED == getController().getState()) {
      final BitmapFont font = GameResources.getInstance().getFont();
      final BitmapFont.TextBounds bounds = font.getBounds(Texts.PAUSED);

      font.draw(batch, Texts.PAUSED, (VIRTUAL_SCREEN_WIDTH - bounds.width) / 2,
              (VIRTUAL_SCREEN_HEIGHT - bounds.height) / 2);
    }
  }

  public void render(final SpriteBatch batch) {
    drawAsteroids(batch);
    drawAliens(batch);
    drawFish(batch);
    drawState(batch);
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  private interface Texts {
    String PAUSED = "Game Paused";
  }
}
