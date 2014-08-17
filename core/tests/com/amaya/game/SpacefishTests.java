package com.amaya.game;

import com.amaya.game.screens.GameOverScreen;
import com.amaya.game.screens.InfoScreen;
import com.amaya.game.screens.LevelScreen;
import com.amaya.game.screens.MenuScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Spacefish class unit tests. */
@RunWith(MockitoJUnitRunner.class)
public class SpacefishTests {
  /* [ MOCKS ] ============================================================================================================================================= */

  /** Create mock for game resources. */
  @Mock
  protected GameResources mResources;
  @Mock
  protected LibGdxFactory mLibGdxFactory;

  /* [ INJECTED ] ========================================================================================================================================== */

  /** Instance of Game root object. */
  @InjectMocks
  protected Spacefish mGame;
  @InjectMocks
  protected GameResources mResourcesSingleton;
  @InjectMocks
  protected LibGdxFactory mLowLevelSingleton;

  /* [ SETUP / TEAR DOWN ] ================================================================================================================================= */

  @Before
  public void setUp() {
    final BitmapFont bitmapFont = mock(BitmapFont.class);

    // setup Mock for Game resources
    when(mResources.getBackgroundMusic()).thenReturn(mock(Music.class));
    when(mResources.getGameOver()).thenReturn(mock(Sound.class));

    when(mResources.getFont()).thenReturn(bitmapFont);
    when(bitmapFont.getBounds(anyString())).thenReturn(new BitmapFont.TextBounds());

    // Mock Gdx lib
    Gdx.graphics = mock(Graphics.class);
    Gdx.app = mock(Application.class);

    mGame.create();
  }

  @After
  public void tearDown() {
    mGame.dispose();
    mGame = null;
  }

  /* [ TESTS ] ============================================================================================================================================= */

  @Test
  public void test_00_Initialization() {
    // confirm that game resources replaced by mock
    assertThat(mResources, is(GameResources.getInstance()));
    assertThat(mLibGdxFactory, is(LibGdxFactory.getInstance()));

    // confirm that instance created
    Assert.assertNotNull(mGame);

    mGame.create();
  }

  @Test
  public void test_01_Navigation() {
    // after game creation navigation stack should have one screen - 'main menu'
    assertThat(mGame.getNavigationStack().size(), is(1));
    assertThat(mGame.getNavigationStack().get(0), is(instanceOf(MenuScreen.class)));

    mGame.navigateToLevel();
    assertThat(mGame.getNavigationStack().size(), is(2));
    assertThat(mGame.getNavigationStack().get(0), is(instanceOf(MenuScreen.class)));
    assertThat(mGame.getNavigationStack().get(1), is(instanceOf(LevelScreen.class)));

    mGame.navigateToInfo();
    assertThat(mGame.getNavigationStack().size(), is(3));
    assertThat(mGame.getNavigationStack().get(0), is(instanceOf(MenuScreen.class)));
    assertThat(mGame.getNavigationStack().get(1), is(instanceOf(LevelScreen.class)));
    assertThat(mGame.getNavigationStack().get(2), is(instanceOf(InfoScreen.class)));

    mGame.navigateBack();
    assertThat(mGame.getNavigationStack().size(), is(2));
    assertThat(mGame.getNavigationStack().get(0), is(instanceOf(MenuScreen.class)));
    assertThat(mGame.getNavigationStack().get(1), is(instanceOf(LevelScreen.class)));

    // Game over is a special screen. After it showing from stack removed all
    // other screens and only first 'Main Menu'
    mGame.navigateToGameOver();
    assertThat(mGame.getNavigationStack().size(), is(2));
    assertThat(mGame.getNavigationStack().get(0), is(instanceOf(MenuScreen.class)));
    assertThat(mGame.getNavigationStack().get(1), is(instanceOf(GameOverScreen.class)));
  }
}
