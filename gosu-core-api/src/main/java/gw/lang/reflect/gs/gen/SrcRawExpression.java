package gw.lang.reflect.gs.gen;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 */
public class SrcRawExpression extends SrcExpression<SrcRawExpression>
{
  private String _text;

  public SrcRawExpression( String text )
  {
    _text = text;
  }

  public SrcRawExpression( Class type, Object value )
  {
    this( TypeSystem.get( type ), value );
  }
  public SrcRawExpression( IType type, Object value )
  {
    _text = makeCompileTimeConstantValue( type, value );
  }

  public StringBuilder render( StringBuilder sb, int indent )
  {
    return render( sb, indent, false );
  }

  public StringBuilder render( StringBuilder sb, int indent, boolean sameLine )
  {
    indent( sb, indent );
    sb.append( _text );
    return sb;
  }
}
