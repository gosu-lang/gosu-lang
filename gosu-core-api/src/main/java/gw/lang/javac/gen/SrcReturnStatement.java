package gw.lang.javac.gen;

import gw.lang.reflect.TypeSystem;

/**
 */
public class SrcReturnStatement extends SrcStatement<SrcReturnStatement>
{
  private SrcExpression _expr;

  public SrcReturnStatement( Class type, Object returnValue )
  {
    _expr = new SrcRawExpression( TypeSystem.get( type ), returnValue );
  }

  public SrcReturnStatement( SrcExpression expr )
  {
    _expr = expr;
  }

  @Override
  public StringBuilder render( StringBuilder sb, int indent )
  {
    indent( sb, indent );
    sb.append( "return" );
    if( _expr != null )
    {
      sb.append( ' ' ).append( _expr );
    }
    sb.append( ";\n" );
    return sb;
  }
}
