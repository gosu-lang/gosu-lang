package gw.lang.reflect.gs.gen;

import gw.lang.parser.Keyword;
import gw.lang.reflect.IType;

/**
 */
public class SrcSetProperty extends SrcStatement<SrcSetProperty>
{
  public static final String VALUE_PARAM = "$value";

  private SrcType _type;
  private SrcStatementBlock<SrcSetProperty> _body;

  public SrcSetProperty( String name, Class type )
  {
     this( null );
     name( name );
     type( type );
  }
  public SrcSetProperty( String name, String type )
  {
     this( null );
     name( name );
     type( type );
  }
  public SrcSetProperty( String name, SrcType type )
  {
     this( null );
     name( name );
     type( type );
  }
  public SrcSetProperty( SrcClass srcClass )
  {
     super( srcClass );
  }

  public SrcSetProperty type( SrcType type )
  {
    _type = type;
    return this;
  }
  public SrcSetProperty type( Class type )
  {
    _type = new SrcType( type );
    return this;
  }
  public SrcSetProperty type( IType type )
  {
    _type = new SrcType( type );
    return this;
  }
  public SrcSetProperty type( String type )
  {
    _type = new SrcType( type );
    return this;
  }

  public SrcSetProperty body( SrcStatementBlock<SrcSetProperty> body )
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
    sb.append( Keyword.KW_property.getName() ).append( ' ' ).append( Keyword.KW_set.getName() ).append( ' ' ).append( getSimpleName() ).append( "(" + VALUE_PARAM + ": " );
    _type.render( sb, indent ).append( ")" );
    _body.render( sb, indent );
    return sb;
  }
}
