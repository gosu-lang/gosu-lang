package gw.util.money
uses java.math.BigDecimal

interface IExchangeRate {
  function get( rateType: RateType ) : BigDecimal
}