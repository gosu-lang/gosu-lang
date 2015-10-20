package gw.lang.parser.expressions;

import gw.lang.parser.Keyword;

/**
 */
public enum Variance
{
  COVARIANT( Keyword.KW_out.getName() ),
  CONTRAVARIANT( Keyword.KW_in.getName() ),
  INVARIANT( Keyword.KW_in.getName() + "/" + Keyword.KW_out.getName() ),
  DEFAULT( "default" );

  private final String _desc;

  Variance( String desc )
  {
    _desc = desc;
  }

  public String getDesc()
  {
    return _desc;
  }
}
