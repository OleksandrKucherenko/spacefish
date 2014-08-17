package com.amaya.game.entities.behavior;

/**
 * Class that responsible for instantiation of Strategies.
 *
 * @see <a href="http://gamedev.stackexchange.com/questions/23430/get-points-on-a-line-between-two-points">Get Point on a line between two points</a>
 */
public final class StrategiesFactory {
  /** instance of NoGravity strategy. */
  public static final IStrategy WaterGravity = new NoGravityStrategy();
  /** instance of Momentum/Impulse strategy. */
  public static final IStrategy MomentumMoveByVector = new MomentumStrategy();
  /** instance of Linear move strategy. */
  public static final IStrategy LinearMoveByVector = new LinearStrategy();
}
