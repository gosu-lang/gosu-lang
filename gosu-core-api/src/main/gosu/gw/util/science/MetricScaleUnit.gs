package gw.util.science

uses gw.util.Rational
uses java.math.BigInteger
uses java.math.BigDecimal

enum MetricScaleUnit {
  y( 1/1000000000000000000000000, "yocto", "y" ),
  z( 1/1000000000000000000000, "zepto", "z" ),
  a( 1/1000000000000000000, "atto", "a" ),
  fe( 1/1000000000000000, "femto", "f" ), // 'fe', not 'f' because conflicts with number literal float suffix
  p( 1/1000000000000, "pico", "p" ),
  n( 1/1000000000, "nano", "n" ),
  u( 1/1000000, "micro", "u" ),
  m( 1/1000, "milli", "m" ),
  c( 1/100, "centi", "c" ),
  de( 1/10, "deci", "d" ), // 'de', not 'd' because conflicts with number literal float suffix
  da( 10, "Deca", "da" ),
  h( 100, "Hecto", "h" ),
  k( 1000, "Kilo", "k" ),
  Ki( 1024, "Kibi", "Ki" ),
  M( 1000bi.pow( 2 ), "Mega", "M" ),
  Mi( 1024bi.pow( 2 ), "Mebi", "Mi" ),
  G( 1000bi.pow( 3 ), "Giga", "G" ),
  Gi( 1024bi.pow( 3 ), "Gibi", "Gi" ),
  T( 1000bi.pow( 4 ), "Tera", "T" ),
  Ti( 1024bi.pow( 4 ), "Tebi", "Ti" ),
  P( 1000bi.pow( 5 ), "Peta", "P" ),
  Pi( 1024bi.pow( 5 ), "Pebi", "Pi" ),
  E( 1000bi.pow( 6 ), "Exa", "E" ),
  Ei( 1024bi.pow( 6 ), "Exbi", "Ei" ),
  Z( 1000bi.pow( 7 ), "Zetta", "Z" ),
  Zi( 1024bi.pow( 7 ), "Zebi", "Zi" ),
  Y( 1000bi.pow( 8 ), "Yotta", "Y" ),
  Yi( 1024bi.pow( 8 ), "Yobi", "Yi" ),

  var _amount: Rational as Amount
  var _name: String
  var _symbol: String

  private construct( amount: Rational, name: String, symbol: String ) {
    _amount = amount
    _name = name
    _symbol = symbol
  }

  property get UnitName() : String {
    return _name
  }

  property get UnitSymbol() : String {
    return _symbol
  }

  function postfixBind( value: Integer ) : Rational {
    return _amount * value
  }
  function postfixBind( value: Long ) : Rational {
    return _amount * value
  }
  function postfixBind( value: Float ) : Rational {
    return _amount * value
  }
  function postfixBind( value: Double ) : Rational {
    return _amount * value
  }
  function postfixBind( value: BigInteger ) : Rational {
    return _amount * value
  }
  function postfixBind( value: BigDecimal ) : Rational {
    return _amount * value
  }
  function postfixBind( value: Rational ) : Rational {
    return _amount * value
  }
}
