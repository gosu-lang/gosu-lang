package gw.util.money
uses java.net.URL
uses java.math.BigDecimal
uses gw.util.science.Time
uses gw.util.science.UnitConstants

/**
 * Override and access the currency rate service via the ExchangeRatesService property
 */
class CurrencyExchange {
  static final var INSTANCE: CurrencyExchange = new CurrencyExchange()
  
  static function instance() : CurrencyExchange {
    return INSTANCE  
  }
  
  var _exchangeRatesService: IExchangeRatesService
  
  private construct() {
  }
  
  property get ExchangeRatesService() : IExchangeRatesService {
    _exchangeRatesService = _exchangeRatesService?:new DefaultExchangeRatesService()
    return _exchangeRatesService
  }
  property set ExchangeRatesService( service: IExchangeRatesService ) {
    _exchangeRatesService = service  
  }
}