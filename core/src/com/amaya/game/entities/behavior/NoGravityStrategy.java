package com.amaya.game.entities.behavior;

import com.amaya.game.Spacefish;
import com.amaya.game.entities.StrategyObject;
import com.amaya.game.entities.modifiers.CommandsFactory;
import com.amaya.game.entities.modifiers.Mandate;
import com.amaya.game.entities.modifiers.MoveTo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/** Simplest strategy implements movement from one point to another by line. */
public class NoGravityStrategy implements IStrategy {
  /* [ Interface IStrategy ] =============================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void update(final StrategyObject entity, final List<Mandate> mandates, final float gameTime, final float delta) {
    if (Spacefish.Debug.STRATEGY_GRAVITY)
      Gdx.app.log(Spacefish.LOG_TAG, "[strategy] gravity - current: " + entity.getPosition());

    final List<Mandate> newSet = new ArrayList<Mandate>();
    MoveTo mtc = null;

    if (entity instanceof IOwnTrajectory) {
      mtc = (MoveTo) CommandsFactory.moveTo((IOwnTrajectory) entity);
    } else {
      final Vector2 p = entity.getPosition();

      // create vertical line trajectory
      mtc = (MoveTo) CommandsFactory.moveTo(
              new Vector2(p.x, Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT),
              new Vector2(p.x, 0));
    }

    if (null != mtc) {
      if (Spacefish.Debug.STRATEGY_GRAVITY)
        Gdx.app.log(Spacefish.LOG_TAG, "[strategy] gravity - start: " + mtc.getStart() + ", end: " + mtc.getEnd());

      newSet.add(mtc);
    }

    if (null != mandates) {
      newSet.addAll(mandates);
    }

    StrategiesFactory.LinearMoveByVector.update(entity, newSet, gameTime, delta);
  }
}
