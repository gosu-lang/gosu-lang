package gw.util.science
uses java.math.BigDecimal

enum LengthUnit implements IUnit<BigDecimal, Length, LengthUnit> {
  Micro( .000001bd, "Micrometre", "µm" ),
  Milli( .001bd, "Millimeter", "mm" ),
  Meter( 1bd, "Meter", "m" ),
  Kilometer( 1000bd, "Kilometer", "km" ),
  Inch( 0.0254bd, "Inch", "in" ),
  Foot( 12*0.0254bd, "Foot", "ft" ),
  Yard( 3*12*0.0254bd, "Yard", "yd" ),
  Mile( 1609.344bd, "Mile", "mi" )  
  
  var _meters: BigDecimal as Meters
  var _name: String
  var _symbol: String
  
  static property get BaseUnit() : LengthUnit {
    return Meter
  }
  
  private construct( meters: BigDecimal, name: String, symbol: String ) {
    _meters = meters
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
    return Meters * myUnits
  }

  override function toNumber() : BigDecimal {
    return Meters
  }
    
  override function from( len: Length ) : BigDecimal {
    return len.toNumber() / Meters
  }
 
  function postfixBind( f: ForceUnit ) : WorkUnit {
    return multiply( f )
  }

  function divide( t: TimeUnit ) : VelocityUnit {
    return new VelocityUnit( this, t )
  }
   
  function divide( v: VelocityUnit ) : TimeUnit {
    return v.TimeUnit
  }
  
  function multiply( len: LengthUnit ) : AreaUnit {
    return new AreaUnit( this, len )
  }
  
  function multiply( area: AreaUnit ) : VolumeUnit {
    return new VolumeUnit( area, this )
  }  
  
  function multiply( f: ForceUnit ) : WorkUnit {
    return new( f, this )
  }  
}
