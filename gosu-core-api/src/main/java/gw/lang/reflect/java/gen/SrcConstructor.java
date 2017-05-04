package gw.lang.reflect.java.gen;

import java.lang.reflect.Modifier;

/**
 */
public class SrcConstructor extends SrcStatement<SrcConstructor>
{
  private SrcStatementBlock<SrcConstructor> _body;

  public SrcConstructor( SrcClass owner )
  {
    super( owner );
  }

  public SrcConstructor body( SrcStatementBlock<SrcConstructor> body ) {
    _body = body;
    return this;
  }

  @Override
  public StringBuilder render( StringBuilder sb, int indent )
  {
    renderAnnotations( sb, indent, false );
    indent( sb, indent );
    renderModifiers( sb, false, Modifier.PUBLIC );
    sb.append( getOwner().getSimpleName() ).append( renderParameters( sb ) );
    _body.render( sb, indent );
    return sb;
  }
}
