package gw.util.science

uses gw.util.Rational

abstract class AbstractBinaryUnit<A extends IUnit<Rational, IDimension, A>,
                                    B extends IUnit<Rational, IDimension, B>,
                                    D extends IDimension<D, Rational>,
                                    U extends AbstractBinaryUnit<A, B, D, U> > implements IUnit<Rational, D, U> {
  var _leftUnit: A
  var _rightUnit: B
  
  construct( leftUnit: A, rightUnit: B ) {
    _leftUnit = leftUnit
    _rightUnit = rightUnit
  }
  
  protected property get LeftUnit() : A {
    return _leftUnit
  }
  protected property get RightUnit() : B {
    return _rightUnit  
  }
  
  override function from( r: D ) : Rational {
    return r.toNumber() / toBaseUnits( 1 )
  }   
  
  override function hashCode() : int {
    return 31 * LeftUnit.hashCode() + RightUnit.hashCode()
  }
  
  override function equals( obj: Object ) : boolean {
    if( obj.Class != Class ) {
      return false
    }
    
    var that = obj as AbstractBinaryUnit
    
    return LeftUnit.equals( that.LeftUnit ) && 
           RightUnit.equals( that.RightUnit )
  }
}
