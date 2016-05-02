package gw.util.science
uses gw.util.Rational
uses DimensionlessConstants#pi
uses MetricScaleUnit#*

enum SolidAngleUnit implements IUnit<Rational, SolidAngle, SolidAngleUnit> {
  Steradian( 1, "Steradian", "sr" ),

  var _sr: Rational as Sterads
  var _name: String
  var _symbol: String

  static property get BASE() : SolidAngleUnit {
    return Steradian
  }

  private construct( sr: Rational, name: String, symbol: String ) {
    _sr = sr
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
    return Sterads * myUnits
  }

  override function toNumber() : Rational {
    return Sterads
  }

  override function from( len: SolidAngle ) : Rational {
    return len.toNumber() / Sterads
  }

}
