package gw.lang.parser.resources;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import gw.lang.reflect.Modifier;
import gw.test.TestClass;

public class ResTest extends TestClass
{
  public void testAllResourceKeysHaveStrings() throws IllegalAccessException
  {
    Map<Object, Object> strings = new HashMap<>();
    for( int i = 0; i < Strings.contents.length; i++ )
    {
      strings.put( Strings.contents[i][0], Strings.contents[i][1] );
    }
    Field[] fields = Res.class.getFields();
    for( Field field : fields )
    {
      if( Modifier.isStatic( field.getModifiers() ) && field.getType() == ResourceKey.class )
      {
        ResourceKey key = (ResourceKey)field.get( null );
        assertTrue( "Did not find default string in Strings.java for resource key: " + key.getKey(), strings.containsKey( key.getKey() ) );
      }
    }
  }
}
