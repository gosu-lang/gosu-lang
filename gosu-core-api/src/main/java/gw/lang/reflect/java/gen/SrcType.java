package gw.lang.reflect.java.gen;

import gw.lang.reflect.IType;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class SrcType extends SrcElement
{
  private final String _type;
  private List<SrcType> _typeParams = new ArrayList<>();
  private boolean _diamond;

  public SrcType( Class type )
  {
    _type = type.getName();
  }
  public SrcType( IType type )
  {
    _type = type.getName();
  }
  public SrcType( String type )
  {
    _type = type;
  }

  public SrcType addTypeParam( SrcType srcType )
  {
    _typeParams.add( srcType );
    return this;
  }
  public SrcType addTypeParam( Class type )
  {
    SrcType srcType = new SrcType( type );
    _typeParams.add( srcType );
    return this;
  }
  public SrcType addTypeParam( IType type )
  {
    SrcType srcType = new SrcType( type );
    _typeParams.add( srcType );
    return this;
  }
  public SrcType addTypeParam( String type )
  {
    SrcType srcType = new SrcType( type );
    _typeParams.add( srcType );
    return this;
  }

  public SrcType diamond()
  {
    _diamond = true;
    return this;
  }

  @Override
  public StringBuilder render( StringBuilder sb, int indent )
  {
    String type = _type.startsWith( "java.lang." ) ? _type.substring( "java.lang.".length() ) : _type;
    sb.append( type );
    if( _typeParams.size() > 0 || _diamond )
    {
      sb.append( '<' );
      for( SrcType param: _typeParams )
      {
        param.render( sb, 0 );
      }
      sb.append( '>' );
    }
    return sb;
  }
}
