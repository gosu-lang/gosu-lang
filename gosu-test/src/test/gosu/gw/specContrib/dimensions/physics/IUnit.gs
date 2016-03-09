package gw.specContrib.dimensions.physics
uses java.math.BigDecimal

interface IUnit<B extends Number, D extends IDimension<D, B>, U extends IUnit<B, D, U>> extends IDimension<U, B>, UnitConstants {
  property get UnitName() : String
  property get UnitSymbol() : String
  property get BaseUnitFactor() : B

  function multiply( amount: Number ) : D {
    return new D( amount, this )
  }
    
  override function toNumber() : B {
    return BaseUnitFactor
  }
  
  override function fromNumber( n: B ) : U {
    return null
  }
  
  override function numberType() : Class<B> {
    return B
  }
  
  override function compareTo( o: U ) : int {
    return UnitName.compareTo( o.UnitName )
  }
  
  function equals( u: U ) : boolean {
    return UnitName.equals( u.UnitName ) && 
    BaseUnitFactor.equals( u.BaseUnitFactor )
  }
}