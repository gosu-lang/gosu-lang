/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

/**
 */
public enum MemberAccessKind
{
  NORMAL,
  NULL_SAFE,
  EXPANSION,;

  public static MemberAccessKind getForOperator( String strOperator )
  {
    if( strOperator == null )
    {
      return NORMAL;
    }
    if( strOperator.equals( "?." ) )
    {
      return NULL_SAFE;
    }
    if( strOperator.equals( "*.") )
    {
      return EXPANSION;
    }
    return NORMAL;
  }
}
