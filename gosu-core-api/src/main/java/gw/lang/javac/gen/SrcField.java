package gw.lang.javac.gen;

import java.lang.reflect.Modifier;

/**
 */
public class SrcField extends SrcAnnotated<SrcField>
{
  private SrcType _type;
  private SrcExpression _initializer;

  public SrcField( SrcClass srcClass )
  {
    super( srcClass );
  }

  public SrcField type( SrcType type )
  {
    _type = type;
    return this;
  }
  public SrcField type( Class type )
  {
    _type = new SrcType( type );
    return this;
  }

  public SrcField initializer( SrcExpression expr )
  {
    _initializer = expr;
    return this;
  }

  @Override
  public StringBuilder render( StringBuilder sb, int indent )
  {
    renderAnnotations( sb, indent, false );
    indent( sb, indent );
    renderModifiers( sb, false, 0 );
    _type.render( sb, 0 ).append( ' ' ).append( getSimpleName() ).append( " = " );
    _initializer.render( sb, 0 ).append( ";\n" );
    return sb;
  }
}
