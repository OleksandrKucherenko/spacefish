package com.amaya.game.entities;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.behavior.StrategiesFactory;
import com.amaya.game.entities.modifiers.Expirable;
import com.amaya.game.entities.modifiers.Mandate;
import com.amaya.game.entities.modifiers.Modifier;
import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amaya.game.Spacefish.Dimensions.FISH_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.FISH_WIDTH;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT;
import static com.amaya.game.Spacefish.Dimensions.VIRTUAL_SCREEN_WIDTH;

/** Fish abstraction. represent user entity. */
public class Fish extends StrategyObject {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** Logging tag. */
  public static final String TAG = Spacefish.LOG_TAG;

  /* [ MEMBERS ] =========================================================================================================================================== */

  /** Fish health. */
  private int mHealth = 1;
  /** Collected points. */
  private int mPoints = 0;
  /** Stack of modifiers for game object properties. */
  private final Map<String, List<Modifier>> mModifiers = new HashMap<String, List<Modifier>>();

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** default constructor. */
  public Fish() {
    super((VIRTUAL_SCREEN_WIDTH - FISH_WIDTH) / 2, (VIRTUAL_SCREEN_HEIGHT - FISH_HEIGHT) / 2, FISH_WIDTH, FISH_HEIGHT);
    setSpeed(1.0f);
    setStrategy(StrategiesFactory.MomentumMoveByVector);
  }

	/* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  public int getHealth() {
    int health = this.mHealth;

    if (mModifiers.containsKey(Fields.HEALTH)) {
      for (Modifier cmd : mModifiers.get(Fields.HEALTH)) {
        health += cmd.Value;
      }
    }

    return health;
  }

  public int getPoints() {
    int points = mPoints;

    if (mModifiers.containsKey(Fields.POINTS)) {
      for (Modifier cmd : mModifiers.get(Fields.POINTS)) {
        points += cmd.Value;
      }
    }

    return points;
  }

  @Override
  public float getSpeed(final float gameTime) {
    float speed = super.getSpeed();
    final List<Mandate> toDelete = new ArrayList<Mandate>();
    final List<Modifier> speedMandates = mModifiers.get(Fields.SPEED);

    if (null != speedMandates) {
      for (Modifier cmd : speedMandates) {
        final Expirable ec = (Expirable) cmd;

        if (ec.isExpired(gameTime)) {
          if (Spacefish.Debug.EXPIRED_COMMANDS) {
            Gdx.app.log(TAG, "[expired] is expired, apply: " + ec.getApplyTime() +
                    ", expire: " + ec.getExpiredAt() + ", time: " + gameTime);
          }

          toDelete.add(ec);
        } else {
          speed *= cmd.Value;
        }
      }

      // do cleanup after applying the commands
      for (Mandate cmd : toDelete) {
        speedMandates.remove(cmd);
      }
    }

    return speed;
  }

  /** Reset entity to initial state. */
  public void reset() {
    mPoints = 0;
    mHealth = 1;
    setSpeed(1.0f);
  }

  public Fish addModifier(final Modifier cmd) {
    // create array for modifiers
    if (!mModifiers.containsKey(cmd.Name)) {
      mModifiers.put(cmd.Name, new ArrayList<Modifier>());
    }

    mModifiers.get(cmd.Name).add(cmd);

    return this;
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  /**
   * Syntax hack. reduce the number of 'static final' repeats during declarations,
   * in addition defines domain of the constants.
   */
  public interface Fields {
    /** speed of game object */
    String SPEED = "Speed";
    /** health */
    String HEALTH = "Health";
    /** score points */
    String POINTS = "Points";
    /** position */
    String POSITION = "Position";
  }
}
