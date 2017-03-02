package gw.util.money

uses gw.util.science.Time
uses gw.util.money.ExchangeRate
uses java.util.Currency

class ExpiringRateTable extends HashMap<Currency, ExchangeRate> {
  final var _timestamp: Time as Timestamp
  
  construct() {
    _timestamp = Time.Now
  }
}