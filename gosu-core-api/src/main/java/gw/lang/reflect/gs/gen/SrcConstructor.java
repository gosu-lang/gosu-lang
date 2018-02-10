package gw.lang.reflect.gs.gen;

import gw.lang.parser.Keyword;
import java.lang.reflect.Modifier;

/**
 */
public class SrcConstructor extends SrcStatement<SrcConstructor>
{
  private SrcStatementBlock<SrcConstructor> _body;

  public SrcConstructor()
  {
    super();
  }
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
    sb.append( Keyword.KW_construct.getName() ).append( renderParameters( sb ) );
    _body.render( sb, indent );
    return sb;
  }
}
