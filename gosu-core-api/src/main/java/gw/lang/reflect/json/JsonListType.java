package gw.lang.reflect.json;

import java.util.HashMap;
import java.util.Map;

/**
 */
class JsonListType implements IJsonParentType
{
  private IJsonType _componentType;
  private IJsonParentType _parent;
  private Map<String, IJsonParentType> _innerTypes;

  JsonListType( IJsonParentType parent )
  {
    _parent = parent;
    _innerTypes = new HashMap<>();
  }

  public String getName()
  {
    return "List<" + _componentType.getName() + ">";
  }

  @Override
  public IJsonParentType getParent()
  {
    return _parent;
  }

  public void addChild( String name, IJsonParentType type )
  {
    _innerTypes.put( name, type );
  }
  public IJsonParentType findChild( String name )
  {
    return _innerTypes.get( name );
  }

  public IJsonType getComponentType()
  {
    return _componentType;
  }
  public void setComponentType( IJsonType compType )
  {
    if( _componentType != null && _componentType != compType )
    {
      throw new IllegalStateException( "Component type already set to: " + _componentType.getName() + ", which is not the same as: " + compType.getName() );
    }
    _componentType = compType;
  }

  public IJsonType merge( JsonListType other )
  {
    JsonListType mergedType = new JsonListType( getParent() );

    if( !getComponentType().equals( other.getComponentType() ) )
    {
      IJsonType componentType = Json.mergeTypes( getComponentType(), other.getComponentType() );
      if( componentType != null )
      {
        mergedType.setComponentType( componentType );
      }
      else
      {
        return null;
      }
    }

    for( Map.Entry<String, IJsonParentType> e: _innerTypes.entrySet() )
    {
      String name = e.getKey();
      IJsonType innerType = other.findChild( name );
      if( innerType != null )
      {
        innerType = Json.mergeTypes( e.getValue(), innerType );
      }
      else
      {
        innerType = e.getValue();
      }

      if( innerType != null )
      {
        mergedType.addChild( name, (IJsonParentType)innerType );
      }
      else
      {
        return null;
      }
    }

    return mergedType;
  }

  public void render( StringBuilder sb, int indent, boolean mutable )
  {
    for( IJsonParentType child: _innerTypes.values() )
    {
      child.render( sb, indent, mutable );
    }
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    JsonListType that = (JsonListType)o;

    if( !_componentType.equals( that._componentType ) )
    {
      return false;
    }
    if( !_innerTypes.equals( that._innerTypes ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = _componentType.hashCode();
    result = 31 * result + _innerTypes.hashCode();
    return result;
  }
}
