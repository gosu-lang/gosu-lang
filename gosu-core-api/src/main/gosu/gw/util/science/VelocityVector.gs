package gw.util.science
uses gw.util.Rational

final class VelocityVector extends Vector<Velocity, VelocityUnit, VelocityVector> {
  construct( value : Velocity, angle: Angle ) {
    super( value, angle )
  }
}
