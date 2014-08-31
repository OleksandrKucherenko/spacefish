package com.amaya.game.screens;

import com.amaya.game.GameResources;
import com.amaya.game.Spacefish;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.amaya.game.Spacefish.Dimensions.DELIMITER;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH;

/** */
public class GameOverScreen extends BaseScreen {
  /* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public GameOverScreen(final Spacefish game) {
    super(game);
  }

	/* [ Interface Screen ] ================================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void render(final float delta) {
    super.render(delta);

    final BitmapFont font = GameResources.getInstance().getFont();
    font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    final SpriteBatch batch = getGame().getBatch();
    batch.begin();

    // add good background
    drawBackground(batch);

    batch.enableBlending();

    // 'Game Over'
    float yPosition = VIRTUAL_SCREEN_HEIGHT - DELIMITER;
    final BitmapFont.TextBounds boundsOver = font.getBounds(Texts.GAME_OVER);
    font.draw(batch, Texts.GAME_OVER, (VIRTUAL_SCREEN_WIDTH - boundsOver.width) / 2, yPosition);

    // 'Total Score'
    yPosition -= (boundsOver.height + DELIMITER * 3);
    final BitmapFont.TextBounds boundsScores = font.getBounds(Texts.TOTAL_SCORE);
    font.draw(batch, Texts.TOTAL_SCORE, (VIRTUAL_SCREEN_WIDTH - boundsScores.width) / 2, yPosition);

    // '0'
    yPosition -= (boundsScores.height + DELIMITER * 3);
    final String points = String.valueOf(getGame().getFish().getPoints());
    final BitmapFont.TextBounds boundsPoints = font.getBounds(points);
    font.draw(batch, points, (VIRTUAL_SCREEN_WIDTH - boundsPoints.width) / 2, yPosition);

    batch.end();
  }

  /** {@inheritDoc} */
  @Override
  public void show() {
    super.show();

    // stop playing the game background music
    GameResources.getInstance().getBackgroundMusic().stop();
    GameResources.getInstance().getGameOver().play();
  }

  /** {@inheritDoc} */
  @Override
  public void hide() {
    super.hide();

    // stop playing the game background music
    GameResources.getInstance().getBackgroundMusic().stop();
    GameResources.getInstance().getGameOver().stop();
  }

  /** {@inheritDoc} */
  @Override
  public void resize(final int width, final int height) {
    super.resize(width, height);
  }

  /** {@inheritDoc} */
  @Override
  public boolean touched() {
    getGame().navigateBack();
    return true;
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  /** Syntax hack. The same approach as in - {@link com.amaya.game.screens.MenuScreen.Menu} */
  private interface Texts {
    String GAME_OVER = "Game Over";
    String TOTAL_SCORE = "Total Score";
  }
}
