package gw.lang.reflect.json;

/**
 */
public interface IJsonParentType extends IJsonType
{
  void addChild( String name, IJsonParentType child );
  IJsonParentType findChild( String name );
  void render( StringBuilder sb, int indent );
}
