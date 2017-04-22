package gw.lang.javac.gen;

import gw.lang.reflect.IType;
import java.lang.reflect.Modifier;

/**
 */
public class SrcMethod extends SrcStatement<SrcMethod>
{
  private SrcType _returns;
  private SrcStatementBlock<SrcMethod> _body;

  public SrcMethod( SrcClass srcClass )
  {
     super( srcClass );
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
    renderModifiers( sb, false, Modifier.PUBLIC );
    _returns.render( sb, indent ).append( ' ' ).append( getSimpleName() ).append( renderParameters( sb ) );
    _body.render( sb, indent );
    return sb;
  }
}
