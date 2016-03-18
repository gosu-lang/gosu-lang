package gw.util.science
uses java.math.BigDecimal

abstract class AbstractProductUnit<A extends IUnit<BigDecimal, IDimension, A>, 
                                   B extends IUnit<BigDecimal, IDimension, B>, 
                                   D extends IDimension<D, BigDecimal>, 
                                   U extends AbstractProductUnit<A, B, D, U>> extends AbstractBinaryUnit<A, B, D, U> {
  construct( leftUnit: A, rightUnit: B ) {
    super( leftUnit, rightUnit )
  }
  
  override property get UnitName() : String {
    return LeftUnit.UnitName + " " + RightUnit.UnitName
  }

  override property get UnitSymbol() : String {
    return LeftUnit.UnitSymbol + "\u22C5" + RightUnit.UnitSymbol
  }
 
  override function toBaseUnits( myUnits: BigDecimal ) : BigDecimal {
    return (LeftUnit.toBaseUnits( 1 ) * RightUnit.toBaseUnits( 1 )) * myUnits
  } 
  
  override function toNumber() : BigDecimal {
    return LeftUnit.toNumber() * RightUnit.toNumber()
  }
}
