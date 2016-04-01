package gw.util.money
uses java.math.BigDecimal
uses gw.util.science.Time

class ExchangeRate implements IExchangeRate {
  final var _mid: BigDecimal
  final var _ask: BigDecimal
  final var _bid: BigDecimal
  
  construct( mid: BigDecimal, ask: BigDecimal, bid: BigDecimal ) {
    _mid = mid
    _ask = ask
    _bid = bid
  }
  
  override function get( rateType: RateType ) : BigDecimal {
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