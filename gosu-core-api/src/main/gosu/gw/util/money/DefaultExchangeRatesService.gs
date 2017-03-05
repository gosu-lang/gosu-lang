package gw.util.money
uses java.net.URL
uses gw.util.Rational
uses gw.util.science.Time
uses java.util.concurrent.ConcurrentHashMap
uses gw.util.science.UnitConstants#min
uses java.util.Currency
uses gw.util.money.ExchangeRate
uses gw.util.money.RateType

/**
 * This default implementation uses the Yahoo Finance API to get the table of exchange rates.
 * Yahoo updates the rates continuously.  This service refreshes by the minute on demand; 
 * no rate is ever more than one minute old.
 */
class DefaultExchangeRatesService implements IExchangeRatesService  
{  
  final var _rateTables: Map<Currency, ExpiringRateTable>
  var _frequency: Time as Frequency = 1 min
  var _exchangeRatesService: IExchangeRatesService
  
  construct() {
    _rateTables = new ConcurrentHashMap<Currency, ExpiringRateTable>()
  }
  
  override function getExchangeRatesTable( currency: Currency ) : Map<Currency, ExchangeRate> {
    var rateTable = _rateTables.get( currency )
    if( rateTable != null && (Time.Now - rateTable.Timestamp) < Frequency ) {
      return rateTable 
    }
    rateTable = getRateTableFromTheInterwebz( currency )
    _rateTables.put( currency, rateTable )
    return rateTable
  }
  
  private function getRateTableFromTheInterwebz( currency: Currency ) : ExpiringRateTable {
    var fail: Throwable = null
    for( 0..4 ) {
      try {
        return getRateTable( currency )
      }
      catch( e ) {
        // try to recover from interwebz
        Thread.sleep( 5000 )
        fail = e
        continue
      }
    }    
    throw fail
  }

  private function getRateTable( currency: Currency ) : ExpiringRateTable {
    var args = new Dynamic()
    args.q = "select * from yahoo.finance.xchange where pair in ( ${getCurrencies( currency )} )"
    args.format = "json"
    args.diagnostics = "true"
    args.env = "store://datatables.org/alltableswithkeys"
    args.callback = ""
    var ratesUrl = URL.makeUrl( "https://query.yahooapis.com/v1/public/yql", args )
    var json = ratesUrl.JsonContent
    var rateTable = new ExpiringRateTable()
    for( r in json.query.results.rate ) {
      var id =  r.id
      var name = id.substring( 3 )
      var currencyOfName : Currency = null
      try {
        currencyOfName = Currency.getInstance( name )
      }
      catch( e: IllegalArgumentException ) {
        // So far Java doesn't support currencies for: ECS, XCP, CNH
        //print( "Failed to get currency for: " + name )
      }
      if( currencyOfName != null ) {
        try {
          var mid = Rational.get( r.Rate )
          var ask = bigDecimalDefault( name, Ask, r.Ask, mid )
          var bid = bigDecimalDefault( name, Bid, r.Bid, mid )
          rateTable.put( Currency.getInstance( name ), new ExchangeRate( mid, ask, bid ) )
        }
        catch( nfe: NumberFormatException ) {
          // sometimes the data is garbage
        }
      }
    }    
    return rateTable
  }
  
  private function bigDecimalDefault( name: String, rateType: RateType, value: String, def: Rational ) : Rational {
    var result : Rational
    if( value != "N/A" ) {
      result = Rational.get( value )
    }
    else {
      // for N/A rates use the mid rate, common for precious metals other than gold
      result = def 
    }
    return result
  }
  
  private function getCurrencies( currency: Currency ) : String {
    var ratesUrl = new URL( "https://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json" )
    var json = ratesUrl.JsonContent
    var currencies = new StringBuilder()
    for( x in json.list.resources ) {
      var symbol_x =  x.resource.fields.symbol
      var name = symbol_x.substring( 0, 3 )
      currencies.append( currencies.length() > 0 ? ", '" : "'" ).append( currency ).append( name ).append( "'" )
    }
    return currencies.toString()
  }
}