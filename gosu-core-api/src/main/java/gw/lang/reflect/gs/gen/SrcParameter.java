package gw.lang.reflect.gs.gen;

import gw.lang.reflect.IType;

/**
 */
public class SrcParameter extends SrcAnnotated<SrcParameter>
{
  private SrcType _type;

  public SrcParameter( String name )
  {
    name( name );
  }
  public SrcParameter( String name, IType type )
  {
    name( name );
    type( type );
  }
  public SrcParameter( String name, Class type )
  {
    name( name );
    type( type );
  }
  public SrcParameter( String name, String type )
  {
    name( name );
    type( type );
  }
  public SrcParameter( String name, SrcType type )
  {
    name( name );
    type( type );
  }

  public SrcParameter type( SrcType type )
  {
    _type = type;
    return this;
  }
  public SrcParameter type( Class type )
  {
    _type = new SrcType( type );
    return this;
  }
  public SrcParameter type( IType type )
  {
    _type = new SrcType( type );
    return this;
  }
  public SrcParameter type( String type )
  {
    _type = new SrcType( type );
    return this;
  }

  @Override
  public StringBuilder render( StringBuilder sb, int indent )
  {
    renderAnnotations( sb, 0, true );
    renderModifiers( sb, false, 0 );
    sb.append( getSimpleName() ).append( ": " );
    _type.render( sb, 0 );
    return sb;
  }
}
