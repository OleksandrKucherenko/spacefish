package com.amaya.game.entities.behavior;

import com.amaya.game.GameObject;
import com.amaya.game.Spacefish;
import com.amaya.game.entities.modifiers.Command;
import com.amaya.game.entities.modifiers.MoveToCommand;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/** Simplest strategy implements movement from one point to another by line. */
public class NoGravityStrategy implements IStrategy {
  /* [ Interface IStrategy ] =============================================================================================================================== */

  /** {@inheritDoc} */
  @Override
  public void update(final GameObject entity, final List<Command> commands, final float delta) {
    if (Spacefish.Debug.STRATEGY_GRAVITY)
      Gdx.app.log(Spacefish.LOG_TAG, "[strategy] gravity - current: " + entity.getPosition());

    final List<Command> newSet = new ArrayList<Command>();
    MoveToCommand mtc = null;

    if (entity instanceof IOwnTrajectory) {
      mtc = (MoveToCommand) MoveToCommand.moveTo((IOwnTrajectory) entity);
    } else {
      final Vector2 p = entity.getPosition();

      // create vertical line trajectory
      mtc = (MoveToCommand) MoveToCommand.moveTo(
              new Vector2( p.x, Spacefish.Dimensions.VIRTUAL_SCREEN_HEIGHT),
              new Vector2( p.x, 0 ));
    }

    if (null != mtc) {
      if (Spacefish.Debug.STRATEGY_GRAVITY)
        Gdx.app.log(Spacefish.LOG_TAG, "[strategy] gravity - start: " + mtc.getStart() + ", end: " + mtc.getEnd());

      newSet.add(mtc);
    }

    if (null != commands) {
      newSet.addAll(commands);
    }

    StrategiesFactory.LinearMoveByVector.update(entity, newSet, delta);
  }
}
