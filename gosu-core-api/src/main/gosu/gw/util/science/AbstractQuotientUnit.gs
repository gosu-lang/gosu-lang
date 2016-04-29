package gw.util.science
uses gw.util.Rational

abstract class AbstractQuotientUnit<A extends IUnit<Rational, IDimension, A>,
                                    B extends IUnit<Rational, IDimension, B>,
                                    D extends IDimension<D, Rational>,
                                    U extends AbstractQuotientUnit<A, B, D, U>> extends AbstractBinaryUnit<A, B, D, U> {
  protected construct( leftUnit: A, rightUnit: B, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( leftUnit, rightUnit, factor,
           name != null ? name : leftUnit.UnitName + "/" + rightUnit.UnitName,
           symbol != null ? symbol : leftUnit.UnitSymbol + "/" + rightUnit.UnitSymbol )
  }
  
  override function toBaseUnits( myUnits: Rational ) : Rational {
    return (LeftUnit.toBaseUnits( 1 ) / RightUnit.toBaseUnits( 1 )) * myUnits * Factor
  }  

  override function toNumber() : Rational {
    return LeftUnit.toNumber() / RightUnit.toNumber()
  }
}
