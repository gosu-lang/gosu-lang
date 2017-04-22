package gw.lang.javac.gen;

/**
 */
public class SrcRawStatement extends SrcStatement<SrcRawStatement>
{
  private String _text;

  public SrcRawStatement( SrcStatementBlock owner )
  {
    super( owner );
  }

  public SrcRawStatement rawText( String text )
  {
    _text = text;
    return this;
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
