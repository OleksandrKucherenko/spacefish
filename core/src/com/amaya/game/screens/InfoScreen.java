package com.amaya.game.screens;

import com.amaya.game.GameResources;
import com.amaya.game.Spacefish;
import com.amaya.game.entities.environment.Alien;
import com.amaya.game.entities.environment.Asteroid;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.amaya.game.Spacefish.Dimensions.DELIMITER;
import static com.amaya.game.Spacefish.Dimensions.ICON_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.ICON_PADDING;
import static com.amaya.game.Spacefish.Dimensions.ICON_WIDTH;
import static com.amaya.game.Spacefish.Dimensions.SPACE;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH;

/** Legend of object. */
public class InfoScreen extends BaseScreen {
  /* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public InfoScreen(final Spacefish game) {
    super(game);
  }

	/* [ Interface Screen ] ================================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void render(final float delta) {
    super.render(delta);

    final SpriteBatch batch = getGame().getBatch();
    batch.begin();

    // add good background
    drawBackground(batch);

    final BitmapFont font = GameResources.getInstance().getFont();
    font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    batch.enableBlending();

    // legend, horizontal-center of the screen
    final BitmapFont.TextBounds bounds = font.getBounds(Legend.LEGEND);
    float yPosition = VIRTUAL_SCREEN_HEIGHT - DELIMITER;
    font.draw(batch, Legend.LEGEND, (VIRTUAL_SCREEN_WIDTH - bounds.width) / 2, yPosition);

    yPosition -= bounds.height;
    yPosition -= DELIMITER * 2;

    final Alien[] aliens = new Alien[]{Alien.GREEN, Alien.YELLOW, Alien.ORANGE};
    final String[] texts = new String[]{Legend.POINTS_FIVE, Legend.POINTS_TEN, Legend.POINTS_THIRTY};

    // TODO: good candidate for refactoring, duplicated code!

    int i = 0;
    for (Alien al : aliens) {
      batch.draw(GameResources.getInstance().getAlien(al), ICON_PADDING,
              yPosition - ICON_HEIGHT + SPACE,
              ICON_WIDTH, ICON_HEIGHT);

      float xPosition = ICON_PADDING * 3 + ICON_WIDTH;
      font.draw(batch, texts[i], xPosition, yPosition);

      i++;
      yPosition -= Math.max(font.getLineHeight(), ICON_HEIGHT);
      yPosition -= DELIMITER;
    }

    yPosition -= DELIMITER;
    final Asteroid[] asteroids = new Asteroid[]{Asteroid.SOUND, Asteroid.HALF_SPEED, Asteroid.DEATH};
    final String[] texts2 = new String[]{Legend.ANNOYING_SOUND, Legend.MOVES_SLOWER, Legend.INSTANT_DEATH};

    i = 0;
    for (Asteroid as : asteroids) {
      batch.draw(GameResources.getInstance().getAsteroid(as), ICON_PADDING,
              yPosition - ICON_HEIGHT + SPACE,
              ICON_WIDTH, ICON_HEIGHT);

      float xPosition = ICON_PADDING * 3 + ICON_WIDTH;
      font.draw(batch, texts2[i], xPosition, yPosition);

      i++;
      yPosition -= Math.max(font.getLineHeight(), ICON_HEIGHT);
      yPosition -= DELIMITER;
    }

    batch.end();
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

  private interface Legend {
    String LEGEND = "Legend";
    String POINTS_FIVE = "5 points";
    String POINTS_TEN = "10 points";
    String POINTS_THIRTY = "30 points";

    String ANNOYING_SOUND = "Noise";
    String MOVES_SLOWER = "Speed x0.5";
    String INSTANT_DEATH = "Death";
  }
}
