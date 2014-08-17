package com.amaya.game.entities;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.environment.Alien;
import com.amaya.game.entities.environment.Asteroid;
import com.amaya.game.entities.environment.Drop;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

/** Describe the Level behavior. */
public class Level {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** Logging tag. */
  public static final String TAG = Spacefish.LOG_TAG;
  /** Minimalistic score of the level. */
  public static final int MINIMUM_LEVEL_POINTS = Alien.GREEN.getPoints() + Alien.ORANGE.getPoints() + Alien.YELLOW.getPoints();
  /** cheapest alien. */
  public static final int MINIMUM_ALIEN_POINTS = Alien.GREEN.getPoints();

	/* [ MEMBERS ] =========================================================================================================================================== */

  /** Level total time. */
  private final float mTotalTime;
  /** Level total drops. */
  private final int mTotalDrops;
  /** Initial proportions of Asteroids. */
  private final int[] mAsteroids;
  /** Initial proportions of Aliens. */
  private final int[] mAliens;

	/* [ RUNTIME ] =========================================================================================================================================== */

  /** Visible asteroids. */
  public final List<Asteroid> Asteroids = new ArrayList<Asteroid>();
  /** Visible aliens. */
  public final List<Alien> Aliens = new ArrayList<Alien>();
  /** Quantity of totally done drops. */
  private int mAlreadyDropped;
  /** Quantity of available drops of each type. */
  private final List<Integer> mAvailable = new ArrayList<Integer>();
  /** Level status - State machine. */
  private KnownStates mState = KnownStates.DONE;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /**
   * Construct level with set of important configuration parameters.
   *
   * @param time level total time in seconds.
   * @param asteroids amount of asteroids.
   * @param aliens amount of aliens.
   */
  private Level(final float time, final int[] asteroids, final int[] aliens) {
    // sum 'entities'
    int total = 0;
    for (int i : asteroids) total += i;
    for (int i : aliens) total += i;

    // convert second to millis
    mTotalTime = toMillis(time);
    mTotalDrops = total;
    mAsteroids = asteroids;
    mAliens = aliens;

    reset();
  }

	/* [ API METHODS ] ======================================================================================================================================= */

  /** Reset level runtime state to the initial one. */
  public void reset() {
    mState = KnownStates.RUNNING;
    mAlreadyDropped = 0;
    mAvailable.clear();
    Asteroids.clear();
    Aliens.clear();

    for (int i : mAsteroids) {
      mAvailable.add(i);
    }

    for (int j : mAliens) {
      mAvailable.add(j);
    }
  }

  /**
   * Based on delta and current time from level beginning calculate collection of objects to show.
   *
   * @param gameTime - total game time in seconds
   * @param delta - time between calls in seconds
   * @return Newly created objects. All those objects are accessible over {@link #Asteroids} and {@link #Aliens} collections.
   */
  public List<Drop> update(final float gameTime, final float delta) {
    final List<Drop> results = new ArrayList<Drop>();

    // game pass own total time
    if (KnownStates.DONE == mState) {
      return results;
    }

    // update current State of the level
    if (toMillis(gameTime) > mTotalTime) {
      mState = KnownStates.DONE;

      if (Spacefish.Debug.LEVEL_STATE)
        Gdx.app.log(TAG, "Level State changed to: " + mState);
    }

    // quantity of drops per millis
    final double ratio = (double) mTotalDrops / mTotalTime;
    final double amount = Math.min(mTotalDrops, ratio * toMillis(gameTime));
    final int toDrop = (int) (amount - mAlreadyDropped);

    // generate required quantity of entities
    for (int i = 0; i < toDrop; i++) {
      int index = Spacefish.randomInt(mAsteroids.length + mAliens.length);

      // resolve empty slots
      if (0 == mAvailable.get(index)) {
        index = findNearest(index);
      }

      if (0 <= index) {
        // reduce the value
        mAvailable.set(index, mAvailable.get(index) - 1);
        results.add(newDropInstance(index));
      }
    }

    mAlreadyDropped += toDrop;

    // update main collections
    for (Drop drop : results) {
      if (drop instanceof Alien) {
        Aliens.add((Alien) drop);
      } else if (drop instanceof Asteroid) {
        Asteroids.add((Asteroid) drop);
      }
    }

    if (Spacefish.Debug.LEVEL_DROPS && 0 < results.size())
      Gdx.app.log(TAG, "[level] new drops: " + results.size());

    return results;
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** get current state of the game level. {@link KnownStates} */
  public KnownStates getState() {
    return mState;
  }

	/* [ STATIC METHODS ] ==================================================================================================================================== */

  /** generate random asteroids configuration for level. */
  private static int[] randomAsteroid(final int total) {
    final int withSound = Spacefish.randomInt(total / 2); // up to 50%
    final int withDeath = Spacefish.randomInt((int) (total * 0.4)); // up to 40%

    return asteroids(withSound, total - withSound - withDeath, withDeath);
  }

  /** pack properly asteroids configuration into array. */
  private static int[] asteroids(final int withSound, final int withSpeed, final int withDeath) {
    if (Spacefish.Debug.LEVEL_NUMBERS)
      Gdx.app.log(TAG, "[level] asteroids - green: " + withSound + ", yellow: " + withSpeed + ", orange: " + withDeath);

    return new int[]{withSound, withSpeed, withDeath};
  }

  /** generate random aliens configuration for level. */
  private static int[] randomAliens(final int totalScore) {
    int green = 0, yellow = 0, orange = 0;
    int total = Math.max(totalScore, Level.MINIMUM_LEVEL_POINTS);
    int countdown = 1 + total / MINIMUM_ALIEN_POINTS;

    // NOTE: a little dummy algorithm of fill. We just need something simple to randomize levels.

    while (total > 0 && countdown > 0) {
      switch (Spacefish.randomInt(3)) {
        case 2:
          if (total - Alien.ORANGE.getPoints() > 0) {
            orange++;
            total -= Alien.ORANGE.getPoints();
            break;
          }
          // goto to next SWITCH/CASE, fall to cheaper alien

        case 1:
          if (total - Alien.YELLOW.getPoints() > 0) {
            yellow++;
            total -= Alien.YELLOW.getPoints();
            break;
          }
          // goto to next SWITCH/CASE, fall to cheaper alien

        default:
          if (total - Alien.GREEN.getPoints() > 0) {
            green++;
            total -= Alien.GREEN.getPoints();
          }
          break;
      }

      // potential infinite loop breaker
      countdown--;
    }

    return aliens(green, yellow, orange);
  }

  /** pack properly aliens configuration into array. */
  private static int[] aliens(final int green, final int yellow, final int orange) {
    if (Spacefish.Debug.LEVEL_NUMBERS)
      Gdx.app.log(TAG, "[level] aliens - green: " + green + ", yellow: " + yellow + ", orange: " + orange);

    return new int[]{green, yellow, orange};
  }

  /** Utility. convert seconds to millis. */
  private static float toMillis(final float seconds) {
    return seconds * 1000 /* millis */;
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  private int findNearest(int index) {
    boolean found = false;
    int oldIndex = index;

    // failure, all drops of specified type are already in game
    for (; index < mAvailable.size(); index++) {
      if ((found = (0 < mAvailable.get(index)))) {
        break;
      }
    }

    if (!found) {
      index = oldIndex;

      for (; index >= 0; index--) {
        if (0 < mAvailable.get(index)) {
          break;
        }
      }
    }

    return index;
  }

  /** based on configuration index/position create new instance of game entity. */
  private Drop newDropInstance(final int index) {
    final int xLimit = (int) (Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH - Spacefish.Dimensions.ICON_WIDTH - Spacefish.Dimensions.SPACE);
    float xOffset = Spacefish.Dimensions.SPACE + Spacefish.randomInt(xLimit);

    // TODO: refactor 'magic numbers'

    switch (index) {
      case 0:
        return Asteroid.sound(xOffset);
      case 1:
        return Asteroid.speed(xOffset);
      case 2:
        return Asteroid.death(xOffset);

      case 3:
        return Alien.green(xOffset);
      case 4:
        return Alien.yellow(xOffset);
      default:
        return Alien.orange(xOffset);
    }
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  /** Level known states: Running and Done. */
  public static enum KnownStates {
    RUNNING,
    DONE
  }

  /** Level builder. 'builder' pattern. */
  public static class Builder {
    private int[] mAsteroids = new int[Asteroid.KnownAsteroids.values().length];
    private int[] mAliens = new int[Alien.KnownAliens.values().length];
    private float mTotalTime;

    public Builder setAliensRaw(int[] aliens) {
      System.arraycopy(aliens, 0, mAliens, 0, mAliens.length);
      return this;
    }

    public Builder setAsteroidsRaw(int[] asteroids) {
      System.arraycopy(asteroids, 0, mAsteroids, 0, mAsteroids.length);
      return this;
    }

    public Builder setTotalTime(final float time) {
      mTotalTime = time;
      return this;
    }

    public Builder randomize() {
      setAsteroidsRaw(randomAsteroid((int) mTotalTime));
      setAliensRaw(randomAliens(MINIMUM_LEVEL_POINTS * (int) mTotalTime / 4));
      return this;
    }

    public Level build() {
      return new Level(mTotalTime, mAsteroids, mAliens);
    }
  }
}
