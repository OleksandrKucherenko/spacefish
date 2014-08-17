package com.amaya.game;

import com.amaya.game.entities.environment.Alien;
import com.amaya.game.entities.environment.Asteroid;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** class responsible for loading game resources: fonts, textures, sounds and etc.
 * <p>
 *   Note: class is not final due to Mockito restrictions. Mockito cannot mock/spy final classes.
 * </p> */
public /* final */ class GameResources {
  /* [ STATIC MEMBERS ] ==================================================================================================================================== */

  private static GameResources INSTANCE = new GameResources();

  /* [ MEMBERS ] =========================================================================================================================================== */

  /** true - already initialized, otherwise false. */
  private boolean mIsInitialized;

  private BitmapFont mFont;

  private Music mBackMusic;
  private Sound mHitSound;
  private Sound mGameOver;

  private Texture mBackground;
  private Texture mPlay;
  private Texture mPause;
  private Texture mInfo;
  private Texture mBack;
  private Texture mAsteroidBeep;
  private Texture mAsteroidDeath;
  private Texture mAsteroidSpeed;
  private Texture mAlienGreen;
  private Texture mAlienYellow;
  private Texture mAlienOrange;
  private Texture mFish;
  private Texture mDebugBounds;

	/* [ CONSTRUCTORS ] ====================================================================================================================================== */

  /** hidden constructor. */
  private GameResources() {
    // do nothing
  }

  /** hidden constructor. Used by Mockito for setting the mock object. */
  private GameResources( final GameResources mock ){
    INSTANCE = mock;
  }

  public static GameResources getInstance() {
    return INSTANCE;
  }

	/* [ METHODS ] =========================================================================================================================================== */

  public void load() {
    // protection from second time call
    if (mIsInitialized)
      return;

    mIsInitialized = true;

    // background looping sound
    mBackMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_sound.mp3"));
    mBackMusic.setLooping(true);
    mBackMusic.setVolume(0.5f);

    // annoying sound
    mHitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/annoying_sound.mp3"));
    mGameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/game_over.mp3"));

    // arcade style font
    mFont = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);

    // load background picture, portrait orientation
    mBackground = new Texture(Gdx.files.internal("backgrounds/space.jpg"));

    // load controls
    mPlay = new Texture(Gdx.files.internal("controls/play.png"));
    mPause = new Texture(Gdx.files.internal("controls/pause.png"));
    mInfo = new Texture(Gdx.files.internal("controls/info.png"));
    mBack = new Texture(Gdx.files.internal("controls/back.png"));

    // load asteroids
    mAsteroidSpeed = new Texture(Gdx.files.internal("entities/asteroid_speed.png"));
    mAsteroidBeep = new Texture(Gdx.files.internal("entities/asteroid_beep.png"));
    mAsteroidDeath = new Texture(Gdx.files.internal("entities/asteroid_death.png"));

    // load aliens
    mAlienGreen = new Texture(Gdx.files.internal("entities/alien_five_green.png"));
    mAlienYellow = new Texture(Gdx.files.internal("entities/alien_ten_yellow.png"));
    mAlienOrange = new Texture(Gdx.files.internal("entities/alien_thirty_orange.png"));

    // load fish
    mFish = new Texture(Gdx.files.internal("entities/fish.png"));

    // debug bounds
    mDebugBounds = new Texture(Gdx.files.internal("debug_bounds.png"));
  }

  public void dispose() {
    if (!mIsInitialized)
      return;

    mBackMusic.dispose();
    mHitSound.dispose();
    mGameOver.dispose();

    mFont.dispose();
    mBackground.dispose();

    mPlay.dispose();
    mInfo.dispose();
    mPause.dispose();
    mBack.dispose();

    mAsteroidDeath.dispose();
    mAsteroidSpeed.dispose();
    mAsteroidBeep.dispose();

    mAlienYellow.dispose();
    mAlienGreen.dispose();
    mAlienOrange.dispose();

    mFish.dispose();
    mDebugBounds.dispose();

    mIsInitialized = false;
  }

	/* [ GET/SET METHODS ] =================================================================================================================================== */

  public Texture getBackground() {
    return mBackground;
  }

  public Music getBackgroundMusic() {
    return mBackMusic;
  }

  public Sound getHitSound() {
    return mHitSound;
  }

  public Sound getGameOver() {
    return mGameOver;
  }

  public BitmapFont getFont() {
    return mFont;
  }

  public Texture getPause() {
    return mPause;
  }

  public Texture getPlay() {
    return mPlay;
  }

  public Texture getInfo() {
    return mInfo;
  }

  public Texture getBack() {
    return mBack;
  }

  public Texture getAsteroid(final Asteroid as) {

    if (Asteroid.KnownAsteroids.DEATH == as.getTag()) {
      return mAsteroidDeath;
    } else if (Asteroid.KnownAsteroids.SPEED == as.getTag()) {
      return mAsteroidSpeed;
    }

    return mAsteroidBeep;
  }

  public Texture getAlien(final Alien al) {

    if (Alien.KnownAliens.ORANGE == al.getTag()) {
      return mAlienOrange;
    } else if (Alien.KnownAliens.YELLOW == al.getTag()) {
      return mAlienYellow;
    }

    return mAlienGreen;
  }

  public Texture getFish() {
    return mFish;
  }

  public Texture getDebugBounds() {
    return mDebugBounds;
  }
}
