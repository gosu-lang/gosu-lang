package gw.util.science
uses java.math.BigDecimal

enum ChargeUnit implements IUnit<BigDecimal, Charge, ChargeUnit> {
  Coulomb( 1bd, "Coulomb", "C" ),
  Elementary( 1.6021766208e-19, "Elementary", "e" ),
  
  var _coulombs: BigDecimal as Coulombs
  var _name: String
  var _symbol: String
  
  private construct( coulombs: BigDecimal, name: String, symbol: String ) {
    _coulombs = coulombs
    _name = name
    _symbol = symbol
  }
  
  override property get UnitName() : String {
    return _name 
  }
 
   override property get UnitSymbol() : String {
    return _symbol
  }
 
  override function toBaseUnits( myUnits: BigDecimal ) : BigDecimal {
    return Coulombs * myUnits
  }
  
  override function toNumber() : BigDecimal {
    return Coulombs
  }
    
  override function from( len: Charge ) : BigDecimal {
    return len.toNumber() / Coulombs
  }
  
  function divide( time: TimeUnit ) : CurrentUnit {
    return new( this, time )
  }
}
