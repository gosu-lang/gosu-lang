package gw.lang.reflect.gs.gen;

import gw.lang.parser.Keyword;
import gw.lang.reflect.IType;

/**
 */
public class SrcGetProperty extends SrcStatement<SrcGetProperty>
{
  private SrcType _type;
  private SrcStatementBlock<SrcGetProperty> _body;

  public SrcGetProperty( String name, Class type )
  {
     this( null );
     name( name );
     type( type );
  }
  public SrcGetProperty( String name, String type )
  {
     this( null );
     name( name );
     type( type );
  }
  public SrcGetProperty( String name, SrcType type )
  {
     this( null );
     name( name );
     type( type );
  }
  public SrcGetProperty( SrcClass srcClass )
  {
     super( srcClass );
  }

  public SrcGetProperty type( SrcType returns )
  {
    _type = returns;
    return this;
  }
  public SrcGetProperty type( Class returns )
  {
    _type = new SrcType( returns );
    return this;
  }
  public SrcGetProperty type( IType returns )
  {
    _type = new SrcType( returns );
    return this;
  }
  public SrcGetProperty type( String returns )
  {
    _type = new SrcType( returns );
    return this;
  }

  public SrcGetProperty body( SrcStatementBlock<SrcGetProperty> body )
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
    sb.append( Keyword.KW_property.getName() ).append( ' ' ).append( Keyword.KW_get.getName() ).append( ' ' ).append( getSimpleName() ).append( "() : " );
    _type.render( sb, indent );
    _body.render( sb, indent );
    return sb;
  }
}
