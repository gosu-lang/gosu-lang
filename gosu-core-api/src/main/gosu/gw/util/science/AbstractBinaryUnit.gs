package gw.util.science

uses gw.util.Rational
uses gw.util.concurrent.Cache

abstract class AbstractBinaryUnit<A extends IUnit<Rational, IDimension, A>,
                                  B extends IUnit<Rational, IDimension, B>,
                                  D extends AbstractMeasure<AbstractBinaryUnit, D>,
                                  U extends AbstractBinaryUnit<A, B, D, U>> implements IUnit<Rational, D, U> {
  final var _leftUnit: A
  final var _rightUnit: B
  final var _factor : Rational as Factor
  final var _name: String as UnitName
  final var _symbol: String as UnitSymbol

  protected construct( leftUnit: A, rightUnit: B, factor: Rational = null, name: String = null, symbol: String = null ) {
    _leftUnit = leftUnit
    _rightUnit = rightUnit
    _factor = factor ?: 1
    _name = name
    _symbol = symbol
  }
  
  protected property get LeftUnit() : A {
    return _leftUnit
  }
  protected property get RightUnit() : B {
    return _rightUnit  
  }
  
  override function from( r: D ) : Rational {
    return r.toBaseNumber() / toBaseUnits( 1 )
  }   

  override function toString() : String {
    return UnitName
  }

  override function hashCode() : int {
    return 31*(31*_leftUnit.hashCode() + _rightUnit.hashCode()) + _factor.hashCode()
  }
  
  override function equals( obj: Object ) : boolean {
    if( obj.Class != Class ) {
      return false
    }
    
    var that = obj as AbstractBinaryUnit
    
    return _leftUnit == that.LeftUnit
           && _rightUnit == that.RightUnit
           && _factor == that._factor
           // note we don't want name here since we look up based on just left & right units 
           // i.e., we don't want to have to name units all the time, just look them up by left & right units
           // && _name == that._name
  }
}
