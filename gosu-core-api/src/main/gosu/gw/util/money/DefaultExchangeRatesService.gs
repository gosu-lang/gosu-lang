package gw.util.money
uses java.net.URL
uses java.math.BigDecimal
uses gw.util.science.Time
uses gw.util.science.UnitConstants
uses gw.lang.reflect.json.DefaultParser_Big

/**
 * This default implementation uses the Yahoo Finance API the get the table of exchange rates via:
 * https://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote
 */
class DefaultExchangeRatesService implements IExchangeRatesService, UnitConstants  
{  
  var _frequency: Time as Frequency = 1 hr
  var _timestamp: Time
  var _rateTables: Map<String, Map<String, BigDecimal>>
  var _exchangeRatesService: IExchangeRatesService
  
  override property get ExchangeRatesTables() : Map<String, Map<String, BigDecimal>> {
    if( _timestamp != null && (Time.Now - _timestamp) < Frequency ) {
      return _rateTables
    }
    _timestamp = Time.Now
    var rateTables = new HashMap<String, Map<String, BigDecimal>>()
    var exch = getUsdMap()
    rateTables.put( "USD", exch )
    exch.eachKeyAndValue( \ code, rate -> {
      rateTables.put( code, makeMap( "USD", code, rate, exch ) )
    } )
    _rateTables = rateTables
    return _rateTables
  }

  private function makeMap( base: String, code: String, rate: BigDecimal, exch: Map<String, BigDecimal> ) : Map<String, BigDecimal> {
    var map = new  HashMap<String, BigDecimal>()
    exch.eachKeyAndValue( \ k, v -> {
      if( k != code ) {
        map[k] = v/rate
      }
    } )
    map[base] = 1/rate
    return map
  }

  private function getUsdMap() : Map<String, BigDecimal> {
    var ratesUrl = new URL( "https://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json" )
    var json = ratesUrl.JsonContent
    var usdMap = new HashMap<String, BigDecimal>()
    for( x in json.list.resources ) {
      var symbol_x =  x.resource.fields.symbol
      var name = symbol_x.substring( 0, 3 )
      var price = new BigDecimal( x.resource.fields.price )
      usdMap.put( name, price )
    }    
    return usdMap
  } 
}