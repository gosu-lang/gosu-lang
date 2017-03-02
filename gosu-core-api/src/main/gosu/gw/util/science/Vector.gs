package gw.util.science
uses gw.util.Rational
uses gw.lang.reflect.interval.ISequenceable
uses DimensionlessConstants#pi

abstract class Vector<T extends AbstractMeasure<U, T>, 
                      U extends IUnit<Rational, T, U>,
                      V extends Vector<T, U, V>> implements IDimension<V, Rational> {
  final var _magnitude: T as Magnitude
  final var _angle: Angle as Angle

  construct( magnitude: T, angle: Angle ) {
    _magnitude = magnitude
    _angle = angle
  } 
  
  property get X() : T {
     return Magnitude.fromBaseNumber( Magnitude.toBaseNumber() * RationalTrig.instance().cos( Angle.to( Radian ) ) )
  }
  
  property get Y() : T {
    return Magnitude.fromBaseNumber( Magnitude.toBaseNumber() * RationalTrig.instance().sin( Angle.to( Radian ) ) ) 
  }
  
  function add( v: V ) : V {
    var x = X.toBaseNumber() + v.X.toBaseNumber()
    var y = Y.toBaseNumber() + v.Y.toBaseNumber()
    var angle = RationalTrig.instance().atan( y/x )
    var mag = (x*x + y*y).sqrt()
    if( x < 0 ) {
      if( y < 0 ) {
        angle -= pi
      }
      else {
        angle += pi
      }
    }
    return new V( Magnitude.fromBaseNumber( mag ), new Angle( angle, AngleUnit.Radian, _angle.Unit ) )
  }
  
  function subtract( v: V ) : V {
    return add( -v )
  }
 
  function multiply( v: V ) : Rational  {
    var x = X.toBaseNumber() * v.X.toBaseNumber()
    var y = Y.toBaseNumber() * v.Y.toBaseNumber()
    return x + y 
  }

  function divide( v: V ) {
    throw new UnsupportedOperationException()
  }
  
  function copy( lu: U, au: AngleUnit ) : V {
    return new V( new T( Magnitude.toBaseNumber(), Magnitude.BaseUnit, lu ),
                  new Angle( Angle.toBaseNumber(), Angle.BaseUnit, au ) )
  }
  
  override function fromNumber( p0: Rational ) : V {
    return new V( new T( p0, Magnitude.Unit ), Angle )
  }

  function fromBaseNumber( p0: Rational ) : V {
    return new V( new T( p0, Magnitude.BaseUnit, Magnitude.Unit ), Angle )
  }

  override function numberType() : java.lang.Class<Rational> {
    return Rational
  }

  override function toNumber() : Rational {
    return Magnitude.toNumber()
  }

  override function toBaseNumber() : Rational {
    return Magnitude.toBaseNumber()
  }

  function to( lu: U, au: AngleUnit ) : V {
    return copy( lu, au )
  }
  
  override function toString() : String {
    return Magnitude + " " + Angle
  }
  
  override function hashCode() : int {
    return 31 * Magnitude.hashCode() + Angle.hashCode() 
  }

  override function equals( o: Object ) : boolean {
    if( typeof o != typeof this ) {
      return false
    }
    var that = o as Vector<T, U, V>
    return Magnitude == that.Magnitude && Angle == that.Angle
  }
  
  override function compareTo( o: V ) : int {
    return Magnitude.compareTo( o.Magnitude )
  }
}
