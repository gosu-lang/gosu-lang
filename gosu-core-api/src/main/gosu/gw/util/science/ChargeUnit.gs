package gw.util.science
uses gw.util.Rational

enum ChargeUnit implements IUnit<Rational, Charge, ChargeUnit> {
  Coulomb( 1, "Coulomb", "C" ),
  Elementary( 1.6021766208e-19, "Elementary", "e" ),
  
  var _coulombs: Rational as Coulombs
  var _name: String
  var _symbol: String

  static property get BASE() : ChargeUnit {
    return Coulomb
  }

  private construct( coulombs: Rational, name: String, symbol: String ) {
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
 
  override function toBaseUnits( myUnits: Rational ) : Rational {
    return Coulombs * myUnits
  }
  
  override function toNumber() : Rational {
    return Coulombs
  }
    
  override function from( len: Charge ) : Rational {
    return len.toBaseNumber() / Coulombs
  }
  
  function divide( time: TimeUnit ) : CurrentUnit {
    return CurrentUnit.get( this, time )
  }

  function divide( i: CurrentUnit ) : TimeUnit {
    return i.TimeUnit
  }

  function divide( p: PotentialUnit ) : CapacitanceUnit {
    return CapacitanceUnit.get( this, p )
  }
  function divide( cu: CapacitanceUnit ) : PotentialUnit {
    return cu.PotentialUnit
  }
}
