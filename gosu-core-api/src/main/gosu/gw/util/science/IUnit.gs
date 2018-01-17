package gw.util.science

interface IUnit<B extends Number & Comparable, 
                D extends IDimension<D, B>, 
                U extends IUnit<B, D, U>> extends IDimension<U, B> {  
  
  property get UnitName() : String
  property get UnitSymbol() : String
  function toBaseUnits( theseUnits: B ) : B
  function from( dim: D ) : B
  
  property get FullName() : String {
    return UnitName
  }

  property get FullSymbol() : String {
    return UnitSymbol 
  }

  // implements IPostfixBinder<Number,D>
  function postfixBind( amount: Number ) : D {
    return new D( amount, this )
  }
    
  override function fromNumber( n: B ) : U {
    return null
  }
  
  override function numberType() : Class<B> {
    return B
  }
  
  override function compareTo( o: U ) : int {
    return toNumber().compareTo( o.toNumber() )
  }
}
