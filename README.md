# Spacefish

Small Arcade Game Starter, LibGdx project. 

# Screenshots

![Launcher Icon][1] ![Main Menu][2] 

![Game Screen][3] ![Legend][4]

![Game Over][5]

# Highlights

 - **LibGdx library.** I never use this framework before, so it was a quick learning tasks. This is actually cross-platform and 3D framework based on OpenGL. I start from analysis available on market frameworks, than select two which the most matching requirements and has a good potential.
 - **Classical game design** pattern: Entity, Controller, Renderer.
 - **Android Studio project.** Gradle Build system.
 - **Unit Tests. jUnit, Mockito.** Included two tests for example of skills. Mockito used for isolating tested classes from dependencies.
 - **Android Unit Tests.** Project prepared for Android Unit testing, and TDD/BDD. jUnit, Espresso, Hamcrest.
 - **Code Quality.** in gradle script you can find tasks that allow to execute code quality tools and grab the results. Tools in use: checkstyle, findbugs, cpd, jdepend, pmd, javancss, classycle.
 - **On project spent: ~16 hours.** This is skills demo project, so I do not set goals, like cover all by unit test. Just concentrated on demonstrating skills, tools and patterns.
 - **Patterns:** 
-- Entities, Controller, Renderer, Strategy, etc.
-- Command (Modifications/Commands)
-- Builder (Level builder)
-- Game and UI entities are separated
-- Factory
-- Singleton
-- Navigation Backstack / LIFO
-- State Machine
-- Debug Switches. Set of constants that allow to switch ON/OFF logs of defined modules.
-- etc.
 - **Code style.** I try to follow naming style that defined by LibGdx framework. In use 'Safe coding' Technics: 
final keyword. 
-- @Nullable/@NonNull (defined in artfulbits-sdk library, can be used in code mostly in all places).
-- Avoid use of NULL, replaced by pre-created instances.
-- 90% of the code commented in javadoc standard. Inside the method can be found comments that explain unclear places.
-- Renderer. In several places possible refactoring with elimination of duplicated code. In real life project I will do that of course, but for demo task its not an issue.
 - **Compatibility and Performance.** Game automatically adjust itself to different resolutions and will smoothly run on old devices (tested on HTC Wildfire S). 
-- Game recalculate game field for different resolutions and aspect ratio.
-- LibGdx using JNI and native libraries, that give very good performance  more than 30 FPS even on old devices. But at the same time it has own issues, like battery drain, which of course can be solved by proper implementation.
Game field calculated in VIRTUAL points, which on specific device "projected" on real screen. This is also a design pattern common for games.
 - **Usability, UI/UX, Game Play.** I try to make a fully functioning arcade game for device with touch screen:
tap on game field to set FISH a direction
-- Used different "strategies" for asteroids and FISH, which makes hard to predict behavior and makes in arcade more fun.
-- Added background sound
-- Added BACK button support (usual for Android). Also that solve some "requirements whitespaces"/issue in navigation.

  [1]: https://raw.githubusercontent.com/OleksandrKucherenko/spacefish/master/_documentation/launcher.jpg
  [2]: https://raw.githubusercontent.com/OleksandrKucherenko/spacefish/master/_documentation/main_menu.jpg
  [3]: https://raw.githubusercontent.com/OleksandrKucherenko/spacefish/master/_documentation/game_screen.jpg
  [4]: https://raw.githubusercontent.com/OleksandrKucherenko/spacefish/master/_documentation/legend.jpg
  [5]: https://raw.githubusercontent.com/OleksandrKucherenko/spacefish/master/_documentation/game_over.jpg
