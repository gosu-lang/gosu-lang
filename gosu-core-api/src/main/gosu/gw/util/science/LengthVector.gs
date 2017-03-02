package gw.util.science
uses gw.util.Rational

final class LengthVector extends Vector<Length, LengthUnit, LengthVector> {
  construct( value : Length, angle: Angle ) {
    super( value, angle )
  }
}
