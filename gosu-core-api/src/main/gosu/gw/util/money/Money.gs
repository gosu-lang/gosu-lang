package gw.util.money

uses gw.util.Rational
uses java.text.NumberFormat

/**
 * Money represents an immutable amount of one or more currencies.  An instance of Money may be 
 * an operand in any Gosu arithmetic expression.  You can add, subtract, and divide Money of
 * different currencies.  You can exchange Money involving any number of currencies for another Money 
 * involving any number of currencies. Money also works with Gosu Binding Expressions where currency 
 * codes can be used as direct labels on number literals and simple expressions e.g., 35 USD is the 
 * same as new Money( 35, USD )
 */
final class Money implements IDimension<Money, Rational> {
  final var _amount: Map<Currency, Rational> as Amount

  construct( value : Rational, currency: Currency ) {
    _amount = {currency -> value}
  }
  
  internal construct( amount: Map<Currency, Rational> ) {
    _amount = amount
  }

  property get SingleValue() : Rational {
    if( Amount.size() == 1 ) {
      return Amount.Values.first()
    }  
    throw new RuntimeException( "Multiple currency amount" )
  }
  property get SingleUnit() : Currency {
    if( Amount.size() == 1 ) {
      return Amount.Keys.first()
    }  
    throw new RuntimeException( "Multiple currency amount" )
  }
  
  override function fromNumber( value: Rational ) : Money {
    if( Amount.size() == 1 ) {
      return new Money( value, SingleUnit )
    }    
    throw new RuntimeException( "Multiple currency amount" )
  }

  
  override function numberType() : java.lang.Class<Rational> {
    return Rational
  }

  /**
   * Always stored in Base units
   */
  override function toNumber() : Rational {
    return SingleValue
  }

  override function toString() : String {
    return toString( Locale.getDefault() )
  }
  function toString( locale: Locale ) : String {
    var format = NumberFormat.getInstance( locale )
    if( Amount.size() == 1 ) { 
      format.MaximumFractionDigits = SingleUnit.DefaultFractionDigits
      format.GroupingUsed = true
      return format.format( SingleValue ) + " " + SingleUnit.CurrencyCode
    }
    var sb = new StringBuilder()
    Amount.eachKeyAndValue( \ k,v -> { 
      format.MaximumFractionDigits = k.DefaultFractionDigits
      format.GroupingUsed = true
      sb.append( format.format( v ) ).append( " " ).append( k.CurrencyCode ).append( "\n" )
    } )
    return sb.toString()
  }

  override function hashCode() : int {
    return _amount.hashCode()
  }
  override function equals( o: Object ) : boolean {
    return o typeis Money && _amount == o._amount  
  }
  
  override function compareTo( o: Money ) : int {
    if( _amount.size() == 1 && o._amount.size() == 1 && SingleUnit == o.SingleUnit ) {
      return SingleValue.compareTo( o.SingleValue ) 
    }
    return exchange( Currency.BASE ).compareTo( o.exchange( Currency.BASE ) )
  }

  /**
   * Exchange this Money for another with the specified single currency and rate type
   */
  function exchange( currency: Currency, rateType: RateType = Mid ) : Money {
    var rateTable = CurrencyExchange.instance().ExchangeRatesService.getExchangeRatesTable( currency )
    var total = 0r
    Amount.eachKeyAndValue( \ k, v -> {
      if( k == currency ) {
        total += v
      }
      else {
        total += v / rateTable[k].get( rateType )
      }
    } )
    return new Money( total, currency )
  }
 
  /**
   * Exchange this Money for a target Money with multiple currencies maintaining the value of this Money 
   * as a proportional distribution of multiple currencies of the target Money, using the given rate type
   */
  function weightedExchange( to: Money, rateType: RateType = Mid ) : Money {
    var baseTable = CurrencyExchange.instance().ExchangeRatesService.getExchangeRatesTable( Currency.BASE )
    var totalTo = to.exchange( Currency.BASE )
    var totalFrom = exchange( Currency.BASE )
    var result = new HashMap<Currency, Rational>()
    to.Amount.eachKeyAndValue( \ k, v -> {
      if( k == Currency.BASE ) {
        result.put( k, v/totalTo.SingleValue * totalFrom.SingleValue )
      }
      else {
        var rateTable = CurrencyExchange.instance().ExchangeRatesService.getExchangeRatesTable( k )
        result.put( k, v/baseTable[k].get( rateType )/totalTo.SingleValue * totalFrom.SingleValue / rateTable[Currency.BASE].get( rateType )  )
      }
    } )
    return new Money( result )
  }
  
  function add( money: Money ) : Money {
    var sum = new HashMap<Currency, Rational>( _amount )
    for( entrySet in money._amount.entrySet() ) {
       var value = sum[entrySet.Key]
       value = value == null ? entrySet.Value : value + entrySet.Value
       sum[entrySet.Key] = value
     }
     return new Money( sum )
  }
  
  function subtract( money: Money ) : Money {
    var sum = new HashMap<Currency, Rational>( _amount )
    for( entrySet in money._amount.entrySet() ) {
       var value = sum[entrySet.Key]
       value = value == null ? entrySet.Value : value - entrySet.Value
       sum[entrySet.Key] = value
     }
     return new Money( sum )
  }  
  
  function multiply( value: Rational ) : Money {
    var product = new HashMap<Currency, Rational>()
    for( entrySet in _amount.entrySet() ) {
       var result = entrySet.Value * value
       product[entrySet.Key] = result
     }
     return new Money( product )
  }
  
  function divide( value: Rational ) : Money {
    var quotient = new HashMap<Currency, Rational>()
    for( entrySet in _amount.entrySet() ) {
       var result = entrySet.Value / value
       quotient[entrySet.Key] = result
     }
     return new Money( quotient )
  }
  function divide( money: Money ) : Rational {
    if( _amount.size() == 1 && money._amount.size() == 1 && SingleUnit == money.SingleUnit ) {
      return SingleValue / money.SingleValue
    }
    return exchange( Currency.BASE ) / money.exchange( Currency.BASE )
  }

  function modulo( value: Rational ) : Money {
    var mod = new HashMap<Currency, Rational>()
    for( entrySet in _amount.entrySet() ) {
       var result = entrySet.Value % value
       mod[entrySet.Key] = result
     }
     return new Money( mod )
  }
  function modulo( money: Money ) : Rational {
    if( _amount.size() == 1 && money._amount.size() == 1 && SingleUnit == money.SingleUnit ) {
      return SingleValue % money.SingleValue
    }
    return exchange( Currency.BASE ) % money.exchange( Currency.BASE )
  }

  
  function negate() : Money {
    var negation = new HashMap<Currency, Rational>()
    for( entrySet in _amount.entrySet() ) {
       negation[entrySet.Key] = -entrySet.Value
     }
     return new Money( negation )
  }
}

