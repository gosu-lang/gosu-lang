package gw.util.science
uses gw.util.Rational

abstract class AbstractQuotientUnit<A extends IUnit<Rational, IDimension, A>,
                                    B extends IUnit<Rational, IDimension, B>,
                                    D extends IDimension<D, Rational>,
                                    U extends AbstractQuotientUnit<A, B, D, U>> extends AbstractBinaryUnit<A, B, D, U> {
  protected construct( leftUnit: A, rightUnit: B, factor: Rational = null, name: String = null, symbol: String = null ) {
    super( leftUnit, rightUnit, factor, name, symbol )
  }

  override property get UnitName() : String {
    return super.UnitName == null
           ? LeftUnit.UnitName + "/" + RightUnit.UnitName
           : super.UnitName
  }

  override property get UnitSymbol() : String {
    return super.UnitSymbol == null
           ? LeftUnit.UnitSymbol + "/" + RightUnit.UnitSymbol
           : super.UnitSymbol
  }

  override property get FullName() : String {
    return LeftUnit.FullName + "/" + RightUnit.FullName
  }

  override property get FullSymbol() : String {
    return LeftUnit.FullSymbol + "/" + RightUnit.FullSymbol
  }

  override function toBaseUnits( myUnits: Rational ) : Rational {
    return (LeftUnit.toBaseUnits( 1 ) / RightUnit.toBaseUnits( 1 )) * myUnits * Factor
  }  

  override function toNumber() : Rational {
    return LeftUnit.toNumber() / RightUnit.toNumber()
  }
    
  function multiply( a: B ) : A {
    return LeftUnit  
  }
}
