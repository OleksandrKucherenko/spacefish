package com.amaya.game.entities.behavior;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.StrategyObject;
import com.amaya.game.entities.modifiers.Mandate;
import com.amaya.game.entities.modifiers.MoveTo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/** movement from one point to another with constant speed. */
public class LinearStrategy implements IStrategy {
  /* [ Interface IStrategy ] =============================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void update(final StrategyObject entity, final List<Mandate> mandates, final float gameTime, final float delta) {
    final float speed = entity.getSpeed(gameTime);
    final MoveTo cmd = Mandate.findFirst(mandates, MoveTo.class);

    if (null != cmd) {
      final Vector2 current = entity.getPosition();
      final Vector2 start = cmd.getStart();
      final Vector2 end = cmd.getEnd();
      final float velocity = speed;

      if (Spacefish.Debug.STRATEGY_LINEAR)
        Gdx.app.log(Spacefish.LOG_TAG, "[strategy] linear - start: " + cmd.getStart() + ", end: " + end + ", speed: " + speed);

      // NOTE: emulate momentum by decreasing a step: ( end.x - start.x ), more close we are
      // to the object less change step we have
      final float newX = current.x + (end.x - start.x) * velocity * delta;
      final float newY = current.y + (end.y - start.y) * velocity * delta;

      // recalculate entity position in space
      entity.setPosition(newX, newY);

      if (Spacefish.Debug.STRATEGY_LINEAR)
        Gdx.app.log(Spacefish.LOG_TAG, "[strategy] linear - new: " + entity.getPosition());
    }
  }
}
