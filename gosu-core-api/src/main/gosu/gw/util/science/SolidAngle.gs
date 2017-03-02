package gw.util.science

uses gw.util.Rational

final class SolidAngle extends AbstractMeasure<SolidAngleUnit, SolidAngle> {
  construct( value: Rational, unit: SolidAngleUnit, displayUnit: SolidAngleUnit ) {
    super( value, unit, displayUnit, SolidAngleUnit.BASE )
  }
  construct( value: Rational, unit: SolidAngleUnit ) {
    this( value, unit, unit )
  }

}
