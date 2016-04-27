package gw.util.science
uses gw.util.Rational

abstract class AbstractQuotientUnit<A extends IUnit<Rational, IDimension, A>,
                                    B extends IUnit<Rational, IDimension, B>,
                                    D extends IDimension<D, Rational>,
                                    U extends AbstractQuotientUnit<A, B, D, U>> extends AbstractBinaryUnit<A, B, D, U> {
  construct( leftUnit: A, rightUnit: B ) {
    super( leftUnit, rightUnit )
  }
  
  override property get UnitName() : String {
    return LeftUnit.UnitName + "/" + RightUnit.UnitName
  }

  override property get UnitSymbol() : String {
    return LeftUnit.UnitSymbol + "/" + RightUnit.UnitSymbol
  }
 
  override function toBaseUnits( myUnits: Rational ) : Rational {
    return (LeftUnit.toBaseUnits( 1 ) / RightUnit.toBaseUnits( 1 )) * myUnits
  }  

  override function toNumber() : Rational {
    return LeftUnit.toNumber() / RightUnit.toNumber()
  }
}
