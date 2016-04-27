package gw.util.money
uses gw.util.Rational
uses java.net.URL
uses gw.util.science.Time

/**
 * A user-definable service for foreign currency exchange rates
 * @see CurrencyExchange#ExchangeRatesService
 */
interface IExchangeRatesService {
  /**
   * @param The currency for the resulting exchange rate table
   * @return The currency exchange rates table for the specified currency
   */
  function getExchangeRatesTable( currency: Currency ) : Map<Currency, IExchangeRate>
  
  /**
   * The frequency at which this service refreshes rate tables with newer data
   */
  property get Frequency() : Time
  property set Frequency( value: Time )
}