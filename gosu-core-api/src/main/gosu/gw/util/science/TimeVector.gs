package gw.util.science
uses gw.util.Rational

final class TimeVector extends Vector<Time, TimeUnit, TimeVector> {
  construct( value : Time, angle: Angle ) {
    super( value, angle )
  }
}
