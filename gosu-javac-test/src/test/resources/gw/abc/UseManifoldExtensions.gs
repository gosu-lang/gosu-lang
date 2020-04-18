package gw.abc

uses java.math.BigDecimal

class UseManifoldExtensions {
  static function useStringExtension() : BigDecimal {
    return "1.234".bd()
  }
}