package com.amaya.game.entities.behavior;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.StrategyObject;
import com.amaya.game.entities.modifiers.Mandate;
import com.amaya.game.entities.modifiers.MoveTo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/** Momentum strategy implement pattern when entity got initial momentum which in time loose the velocity. */
public class MomentumStrategy implements IStrategy {
  /* [ Interface IStrategy ] =============================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void update(final StrategyObject entity, final List<Mandate> mandates, final float gameTime, final float delta) {
    final float speed = entity.getSpeed(gameTime);
    final MoveTo cmd = Mandate.findFirst(mandates, MoveTo.class);

    if (null != cmd) {
      final Vector2 start = entity.getPosition();
      final Vector2 end = cmd.getEnd();
      final float velocity = speed;

      if (Spacefish.Debug.STRATEGY_MOMENTUM)
        Gdx.app.log(Spacefish.LOG_TAG, "[strategy] momentum - start: " + cmd.getStart() + ", end: " + end + ", speed: " + speed);

      // NOTE: emulate momentum by decreasing a step: ( end.x - start.x ), more close we are
      // to the object less change step we have
      final float newX = start.x + (end.x - start.x) * velocity * delta;
      final float newY = start.y + (end.y - start.y) * velocity * delta;

      // recalculate entity position in space
      entity.setPosition(newX, newY);

      if (Spacefish.Debug.STRATEGY_MOMENTUM)
        Gdx.app.log(Spacefish.LOG_TAG, "[strategy] momentum - new: " + entity.getPosition());
    }
  }
}
