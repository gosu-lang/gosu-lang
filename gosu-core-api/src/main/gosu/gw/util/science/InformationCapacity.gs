package gw.util.science

uses gw.util.Rational

final class InformationCapacity extends AbstractMeasure<InformationCapacityUnit, InformationCapacity> {
  construct( value: Rational, unit: InformationCapacityUnit, displayUnit: InformationCapacityUnit ) {
    super( value, unit, displayUnit, InformationCapacityUnit.BaseUnit )
  }
  construct( value: Rational, unit: InformationCapacityUnit ) {
    this( value, unit, unit )
  }
}
