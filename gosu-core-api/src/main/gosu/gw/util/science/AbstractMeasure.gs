package gw.util.science

uses java.lang.Class
uses gw.util.Rational
uses gw.lang.reflect.interval.ISequenceable

abstract class AbstractMeasure<U extends IUnit<Rational, IDimension, U>, T extends AbstractMeasure<U, T>> implements IDimension<T, Rational>, ISequenceable<T, Rational, U> {
  final var _value: Rational
  final var _dipslayUnit: U as Unit
  final var _baseUnit: U as BaseUnit

  construct( value : Rational, unit: U, displayUnit: U, baseUnit: U ) {
    _value = unit.toBaseUnits( value )
    _dipslayUnit = displayUnit
    _baseUnit = baseUnit
  }

  function copy( unit: U ) : T {
    return new T( toNumber(), BaseUnit, unit ) 
  }

  override function fromNumber( p0: Rational ) : T {
    return new T( p0, BaseUnit, Unit )
  }
  
  override function numberType() : java.lang.Class<Rational> {
    return Rational
  }

  /**
   * Always stored in Base units
   */
  override function toNumber() : Rational {
    return _value
  }

  function to( unit: U ) : T {
    return copy( unit )
  }
  
  function toNumber( unit: U ) : Rational {
    return unit.from( this )
  }
  
  override function toString() : String {
    return toNumber( Unit ).toBigDecimal().stripTrailingZeros().toPlainString() + " " + Unit.UnitSymbol
  }
  
  override function hashCode() : int {
    return 31 * _value.intValue() + _baseUnit.hashCode()
  }

  override function equals( o: Object ) : boolean {
    if( typeof o != typeof this ) {
      return false
    }
    var that = o as AbstractMeasure<U,T>
    return _baseUnit == that._baseUnit && _value == that._value
  }
  
  override function compareTo( o: T ) : int {
    var n = o.toNumber()
    return _value.compareTo( n )
  }
  
  function add( r: T ) : T {
    return new T( _value + r._value, BaseUnit, Unit )
  }
  function subtract( r: T ) : T {
    return new T( _value - r._value, BaseUnit, Unit )
  }
  function divide( r: T ) : Rational {
    return _value / r._value
  }
  function modulo( r: T ) : Rational {
    return _value % r._value
  }
    
  /**
   *  Implementation of ISequenceable
   */  
  override function nextInSequence( step: Rational, unit: U ) : T {
    step = step ?: Rational.ONE
    unit = unit ?: Unit
    return fromNumber( toNumber() + (unit.toBaseUnits( step ) - unit.toBaseUnits( 0 )) )
  }
  override function nextNthInSequence( step: Rational, unit: U, iIndex: int ) : T {
    step = step ?: Rational.ONE
    unit = unit ?: Unit
    return fromNumber( toNumber() + (unit.toBaseUnits( step ) - unit.toBaseUnits( 0 ))*iIndex )
  }
  override function previousInSequence( step: Rational, unit: U ) : T {
    step = step ?: Rational.ONE
    unit = unit ?: Unit
    return fromNumber( toNumber() - (unit.toBaseUnits( step ) - unit.toBaseUnits( 0 )) )
  }
  override function previousNthInSequence( step: Rational, unit: U, iIndex : int ) : T {
    step = step ?: Rational.ONE
    unit = unit ?: Unit
    return fromNumber( toNumber() - (unit.toBaseUnits( step ) - unit.toBaseUnits( 0 ))*iIndex )
  }  
}
