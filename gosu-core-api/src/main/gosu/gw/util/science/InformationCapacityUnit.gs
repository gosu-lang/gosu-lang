package gw.util.science

uses gw.util.Rational
uses java.math.BigInteger

enum InformationCapacityUnit implements IUnit<Rational, InformationCapacity, InformationCapacityUnit> {
  Bit( 1/8, "Bit", "bit" ),
  Nibble( 1/2, "Nibble", "nibble" ),
  Byte( 1, "Byte", "B" ),
  KB( 1000, "Kilobyte", "KB" ),
  KiB( 1024, "Kibibyte", "KiB" ),
  MB( 1000bi.pow( 2 ), "Megabyte", "MB" ),
  MiB( 1024bi.pow( 2 ), "Mebibyte", "MiB" ),
  GB( 1000bi.pow( 3 ), "Gigabyte", "GB" ),
  GiB( 1024bi.pow( 3 ), "Gibibyte", "GiB" ),
  TB( 1000bi.pow( 4 ), "Terabyte", "TB" ),
  TiB( 1024bi.pow( 4 ), "Tebibyte", "TiB" ),
  PB( 1000bi.pow( 5 ), "Petabyte", "TB" ),
  PiB( 1024bi.pow( 5 ), "Pebibyte", "TiB" ),
  EB( 1000bi.pow( 6 ), "Exabyte", "EB" ),
  EiB( 1024bi.pow( 6 ), "Exbibyte", "EiB" ),
  ZB( 1000bi.pow( 7 ), "Zettabyte", "ZB" ),
  ZiB( 1024bi.pow( 7 ), "Zebibyte", "ZiB" ),
  YB( 1000bi.pow( 8 ), "Yottabyte", "YB" ),
  YiB( 1024bi.pow( 8 ), "Yobibyte", "YiB" ),

  var _bytes: Rational as Bytes
  var _name: String
  var _symbol: String

  static property get BASE() : InformationCapacityUnit {
    return Byte
  }

  private construct( bytes: Rational, name: String, symbol: String ) {
    _bytes = bytes
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
    return Bytes * myUnits
  }

  override function toNumber() : Rational {
    return Bytes
  }

  override function from( bytes: InformationCapacity ) : Rational {
    return bytes.toBaseNumber() / Bytes
  }
}
