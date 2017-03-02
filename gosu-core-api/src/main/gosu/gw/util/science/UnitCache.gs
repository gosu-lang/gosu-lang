package gw.util.science
uses gw.util.Rational
uses gw.util.concurrent.Cache

class UnitCache<U extends AbstractBinaryUnit> extends Cache<U, U> {
  construct() {
    super( "Unit Cache", 1000, \ unit -> unit ) 
  }
}