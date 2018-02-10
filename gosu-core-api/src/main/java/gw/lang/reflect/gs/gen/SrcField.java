package gw.lang.reflect.gs.gen;

import gw.lang.parser.Keyword;

/**
 */
public class SrcField extends SrcAnnotated<SrcField>
{
  private SrcType _type;
  private SrcExpression _initializer;

  public SrcField( String name, Class type )
  {
    super();
    name( name );
    type( type );
  }
  public SrcField( String name, String type )
  {
    super();
    name( name );
    type( type );
  }
  public SrcField( String name, SrcType type )
  {
    super();
    name( name );
    type( type );
  }
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
  public SrcField type( String type )
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
    sb.append( Keyword.KW_var.getName() ).append( ' ' ).append( getSimpleName() ).append( ": " );
    _type.render( sb, 0 );
    if( _initializer != null )
    {
      _initializer.render( sb, 0 ).append( "\n" );
    }
    else
    {
      sb.append( "\n" );
    }
    return sb;
  }
}
