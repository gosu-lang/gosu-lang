package gw.util.money
uses gw.util.Rational
uses gw.util.money.RateType

interface IExchangeRate {
  function get( rateType: RateType ) : Rational
}