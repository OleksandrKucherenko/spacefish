package com.amaya.game;

import com.amaya.game.entities.Fish;
import com.amaya.game.entities.Level;
import com.amaya.game.entities.environment.Drop;
import com.amaya.game.entities.modifiers.CommandsFactory;
import com.amaya.game.entities.modifiers.Event;
import com.amaya.game.entities.modifiers.Expirable;
import com.amaya.game.entities.modifiers.Mandate;
import com.amaya.game.entities.modifiers.Modifier;
import com.amaya.game.entities.modifiers.MoveTo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import static com.amaya.game.Spacefish.Dimensions;

/** Runtime state of the game. */
public class GameController {
  /* [ CONSTANTS ] ========================================================================================================================================= */

  /** Logging tag. */
  public static final String TAG = Spacefish.LOG_TAG;

	/* [ MEMBERS ] =========================================================================================================================================== */

  /** Reference on current level. */
  private final Level mCurrentLevel;
  /** Reference on current game. */
  private final Spacefish mGame;
  /** Stack of commands for execution. */
  private final List<Mandate> mMandates = new ArrayList<Mandate>();
  /** Accumulated game time. */
  private float mGameTime = 0;
  /** Current state of the game. */
  private KnownStates mState = KnownStates.PLAY;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  public GameController(final Spacefish game, final Level level) {
    mGame = game;
    mCurrentLevel = level;

    getGame().getFish().reset();
  }

	/* [ PUBLIC API ] ======================================================================================================================================== */

  public void resize(final int width, final int height) {
    // do nothing
  }

  public boolean touched(final Vector3 point) {
    // ignore all calls if we are not in PLAY mode
    if (KnownStates.PLAY != mState)
      return false;

    // drop old "move-to' command if exist one
    final MoveTo mtc = Mandate.findFirst(getMandates(), MoveTo.class);

    if (null != mtc) {
      getMandates().remove(mtc);
    }

    getMandates().add(CommandsFactory.moveTo(getGame().getFish().Bounds, point));

    return true;
  }

  public void update(final float delta) {
    // ignore all calls if we are not in PLAY mode
    if (KnownStates.PLAY != mState)
      return;

    mGameTime = getGameTime() + delta;

    processCommands(delta);
    processLevel(delta);
    processCollisions();
  }

	/* [ GETTER / SETTER METHODS ] =========================================================================================================================== */

  /** Reference on current game. */
  public Spacefish getGame() {
    return mGame;
  }

  /** Reference on current level. */
  public Level getCurrentLevel() {
    return mCurrentLevel;
  }

  /** Stack of commands for execution. */
  public List<Mandate> getMandates() {
    return mMandates;
  }

  /** Accumulated game time. */
  public float getGameTime() {
    return mGameTime;
  }

  public KnownStates getState() {
    return mState;
  }

  public void setState(final KnownStates state) {
    mState = state;
  }

  /* [ IMPLEMENTATION & HELPERS ] ========================================================================================================================== */

  private void processCommands(final float delta) {
    final List<Mandate> toDelete = new ArrayList<Mandate>();

    for (Mandate cmd : getMandates()) {
      if (cmd instanceof MoveTo) {
        if (processMoveTo((MoveTo) cmd, delta)) {
          toDelete.add(cmd);
        }
      } else if (cmd instanceof Expirable) {
        if (processExpiring((Expirable) cmd, delta)) {
          toDelete.add(cmd);
        }
      } else if (cmd instanceof Event) {
        if (processEvent((Event) cmd, delta)) {
          toDelete.add(cmd);
        }
      } else if (cmd instanceof Modifier) {
        if (processOther((Modifier) cmd, delta)) {
          toDelete.add(cmd);
        }
      }
    }

    // do cleanup after applying the commands
    for (Mandate cmd : toDelete) {
      getMandates().remove(cmd);
    }
  }

  private boolean processEvent(final Event event, final float delta) {
    if (Events.ANNOYING_SOUND.equals(event.Name)) {
      GameResources.getInstance().getHitSound().play();

      return true;
    }

    return false;
  }

  private boolean processExpiring(final Expirable cmd, final float delta) {

    if (Fish.Fields.SPEED.equals(cmd.Name)) {
      getGame().getFish().addModifier(cmd);
      return true;
    }

    return false;
  }

  private boolean processMoveTo(final MoveTo mtc, final float delta) {
    // game entity
    final Fish fish = getGame().getFish();

    // modifiers/commands
    final List<Mandate> mandates = new ArrayList<Mandate>();
    mandates.add(mtc);

    // apply movement algorithm
    fish.getStrategy().update(fish, mandates, getGameTime(), delta);

    // try to detect is our behavior processing finished or not
    final Vector2 current = fish.getPosition();
    final Vector2 last = mtc.getEnd();

    if (Math.abs(current.x - last.x) <= Dimensions.ACCURACY) {
      if (Math.abs(current.y - last.y) <= Dimensions.ACCURACY) {

        if (Spacefish.Debug.MOVE_COMMANDS)
          Gdx.app.log(TAG, "[move-to] processing finished.");

        // command processed, remove it
        return true;
      }
    }

    // keep command in stack
    return false;
  }

  private boolean processOther(final Modifier cmd, final float delta) {
    final Fish fish = getGame().getFish().addModifier(cmd);

    // end of the game
    if (fish.getHealth() <= 0) {
      setState(KnownStates.GAME_OVER);
      getGame().navigateToGameOver();
    }

    return true;
  }

  private void processLevel(final float delta) {
    final Level.KnownStates oldState = getCurrentLevel().getState();

    // get new objects for displaying
    getCurrentLevel().update(getGameTime(), delta);
    final Level.KnownStates newState = getCurrentLevel().getState();

    processLevelDrops(getCurrentLevel().Aliens, delta);
    processLevelDrops(getCurrentLevel().Asteroids, delta);

    // level finished
    if (newState != oldState && Level.KnownStates.DONE == newState) {
      mState = KnownStates.GAME_OVER;
      getGame().navigateToGameOver();
    }
  }

  private void processLevelDrops(final List<? extends Drop> list, final float delta) {
    // calculate new positions
    for (Drop drop : list) {
      drop.getStrategy().update(drop, null, getGameTime(), delta);
    }

    // do cleanup
    for (int i = list.size() - 1; i >= 0; i--) {
      final Drop drop = list.get(i);

      if (drop.getPosition().y < Dimensions.ACCURACY) {
        list.remove(i);
      }
    }
  }

  private void processCollisions() {
    final Rectangle rcFish = getGame().getFish().Bounds;

    processCollisions(rcFish, getCurrentLevel().Aliens);
    processCollisions(rcFish, getCurrentLevel().Asteroids);
  }

  private void processCollisions(final Rectangle rc, final List<? extends Drop> list) {
    for (int i = list.size() - 1; i >= 0; i--) {
      final Drop drop = list.get(i);

      if (drop.Bounds.overlaps(rc)) {
        if (Spacefish.Debug.FISH_COLLISIONS) {
          Gdx.app.log(TAG, "[collision] with: " + drop.Bounds + ", entity: " + drop.getClass().getSimpleName());
          Gdx.app.log(TAG, "[collision] modifier: " + drop.getModifier());
        }

        getMandates().add(drop.getModifier());
        list.remove(i);
      }
    }
  }

	/* [ NESTED DECLARATIONS ] =============================================================================================================================== */

  public interface Events {
    String PREFIX = "Event: ";
    String ANNOYING_SOUND = PREFIX + "AnnoyingSound";
  }

  public static enum KnownStates {
    PLAY,
    PAUSED,
    GAME_OVER;
  }
}
