/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public enum GlobalScope
{
  EXECUTION( "execution" ),
  REQUEST( "request" ),
  SESSION( "session" ),
  APPLICATION( "application" );

  private String _strScope;

  private GlobalScope( String strScope )
  {
    _strScope = strScope;
  }

  public String toString()
  {
    return _strScope;
  }

  public static GlobalScope getScope( String code ) {
    for( GlobalScope globalScope : GlobalScope.values() )
    {
      //noinspection StringEquality
      if( globalScope._strScope == code )
      {
        return globalScope;
      }
    }
    throw new IllegalArgumentException( "Cannot resolve scope with " +
                                        "name " + code );
  }

}
