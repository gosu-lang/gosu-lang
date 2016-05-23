package gw.util.money

uses java.util.Currency

enhancement CoreCurrencyEnhancement : Currency
{
  static property get BASE() : Currency {
    try {
      return Currency.getInstance( Locale.Default ) 
    }
    catch( e: Exception ) {
      // if the locale's currency is not supported default to USD
      return Currency.getInstance( "USD" ) 
    }
  }

  function postfixBind( value: Number ) : Money {
    return new Money( value, this )
  }
}