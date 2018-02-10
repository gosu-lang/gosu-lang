package gw.lang.reflect.gs.gen;

import gw.lang.parser.Keyword;
import gw.lang.reflect.IType;

/**
 */
public class SrcMethod extends SrcStatement<SrcMethod>
{
  private SrcType _returns;
  private SrcStatementBlock<SrcMethod> _body;

  public SrcMethod()
  {
     this( null );
  }
  public SrcMethod( SrcClass srcClass )
  {
     super( srcClass );
     _returns = SrcType.VOID;
  }

  public SrcMethod returns( SrcType returns )
  {
    _returns = returns;
    return this;
  }
  public SrcMethod returns( Class returns )
  {
    _returns = new SrcType( returns );
    return this;
  }
  public SrcMethod returns( IType returns )
  {
    _returns = new SrcType( returns );
    return this;
  }
  public SrcMethod returns( String returns )
  {
    _returns = new SrcType( returns );
    return this;
  }

  public SrcMethod body( SrcStatementBlock<SrcMethod> body )
  {
    _body = body;
    return this;
  }

  @Override
  public StringBuilder render( StringBuilder sb, int indent )
  {
    renderAnnotations( sb, indent, false );
    indent( sb, indent );
    renderModifiers( sb, false, 0 );
    sb.append( Keyword.KW_function.getName() ).append( ' ' ).append( getSimpleName() ).append( renderParameters( sb ) );
    if( _returns != null && !_returns.getType().equals( "void" ) )
    {
      sb.append( " : " );
      _returns.render( sb, indent );
    }
    _body.render( sb, indent );
    return sb;
  }
}
