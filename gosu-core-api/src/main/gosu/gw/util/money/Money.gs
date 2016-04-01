package gw.util.money

uses java.math.BigDecimal 
uses java.text.NumberFormat

/**
 * Money represents an immutable amount of one or more currencies.  An instance of Money 
 * can be an operand in any Gosu arithmetic expression.  You can add or subtract Money of
 * different currencies and exchange Money for another currency or basket of currencies.
 * Money also works with Gosu Binding Expressions where currency codes can be used as direct
 * labels on number literals and simple expressions e.g., 35 USD is the same as new Money( 35, USD )
 */
final class Money implements IDimension<Money, BigDecimal> {
  final var _amount: Map<Currency, BigDecimal> as Amount

  construct( value : BigDecimal, currency: Currency ) {
    _amount = {currency -> value}
  }
  
  internal construct( amount: Map<Currency, BigDecimal> ) {
    _amount = amount
  }

  property get SingleValue() : BigDecimal {
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
  
  override function fromNumber( value: BigDecimal ) : Money {
    if( Amount.size() == 1 ) {
      return new Money( value, SingleUnit )
    }    
    throw new RuntimeException( "Multiple currency amount" )
  }

  
  override function numberType() : java.lang.Class<BigDecimal> {
    return BigDecimal
  }

  /**
   * Always stored in Base units
   */
  override function toNumber() : BigDecimal {
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
    var total = 0bd
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
    var result = new HashMap<Currency, BigDecimal>()
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
    var sum = new HashMap<Currency, BigDecimal>( _amount )
    for( entrySet in money._amount.entrySet() ) {
       var value = sum[entrySet.Key]
       value = value == null ? entrySet.Value : value + entrySet.Value
       sum[entrySet.Key] = value
     }
     return new Money( sum )
  }
  
  function subtract( money: Money ) : Money {
    var sum = new HashMap<Currency, BigDecimal>( _amount )
    for( entrySet in money._amount.entrySet() ) {
       var value = sum[entrySet.Key]
       value = value == null ? entrySet.Value : value - entrySet.Value
       sum[entrySet.Key] = value
     }
     return new Money( sum )
  }  
  
  function multiply( value: BigDecimal ) : Money {
    var product = new HashMap<Currency, BigDecimal>()
    for( entrySet in _amount.entrySet() ) {
       var result = entrySet.Value * value
       product[entrySet.Key] = result
     }
     return new Money( product )
  }
  
  function divide( value: BigDecimal ) : Money {
    var quotient = new HashMap<Currency, BigDecimal>()
    for( entrySet in _amount.entrySet() ) {
       var result = entrySet.Value / value
       quotient[entrySet.Key] = result
     }
     return new Money( quotient )
  }

  function modulo( value: BigDecimal ) : Money {
    var mod = new HashMap<Currency, BigDecimal>()
    for( entrySet in _amount.entrySet() ) {
       var result = entrySet.Value % value
       mod[entrySet.Key] = result
     }
     return new Money( mod )
  }
  
  function negate() : Money {
    var negation = new HashMap<Currency, BigDecimal>()
    for( entrySet in _amount.entrySet() ) {
       negation[entrySet.Key] = -entrySet.Value
     }
     return new Money( negation )
  }
}

