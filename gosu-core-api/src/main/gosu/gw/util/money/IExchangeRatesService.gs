package gw.util.money
uses java.math.BigDecimal
uses java.net.URL
uses gw.util.science.Time

/**
 * A user-definable service for foreign currency exchange rates
 * @see CurrencyExchange#ExchangeRatesService
 */
interface IExchangeRatesService {
  /**
   * @return A mapping of currency to mapping of currency to exchange rate
   */
  property get ExchangeRatesTables() : Map<String, Map<String, BigDecimal>>
  
  /**
   * The frequency at which this service refreshes rate tables with newer data
   */
  property get Frequency() : Time
  property set Frequency( value: Time )
}