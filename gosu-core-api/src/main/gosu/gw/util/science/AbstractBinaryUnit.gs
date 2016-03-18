package gw.util.science

uses java.math.BigDecimal

abstract class AbstractBinaryUnit<A extends IUnit<BigDecimal, IDimension, A>,
                                    B extends IUnit<BigDecimal, IDimension, B>, 
                                    D extends IDimension<D, BigDecimal>, 
                                    U extends AbstractBinaryUnit<A, B, D, U> > implements IUnit<BigDecimal, D, U> {
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
  
  override function from( r: D ) : BigDecimal {
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
