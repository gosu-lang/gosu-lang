package gw.util.money
uses gw.util.Rational
uses gw.util.science.Time

class ExchangeRate implements IExchangeRate {
  final var _mid: Rational
  final var _ask: Rational
  final var _bid: Rational
  
  construct( mid: Rational, ask: Rational, bid: Rational ) {
    _mid = mid
    _ask = ask
    _bid = bid
  }
  
  override function get( rateType: RateType ) : Rational {
    switch( rateType ) {
      case Mid:  
        return _mid
      case Ask:
        return _ask
      case Bid:
        return _bid
    }
  }
}