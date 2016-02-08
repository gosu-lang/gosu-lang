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

  public void render( StringBuilder sb, int indent )
  {
    for( IJsonParentType child: _innerTypes.values() )
    {
      child.render( sb, indent );
    }
  }
}
