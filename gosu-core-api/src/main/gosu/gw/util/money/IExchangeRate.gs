package gw.util.money
uses gw.util.Rational

interface IExchangeRate {
  function get( rateType: RateType ) : Rational
}