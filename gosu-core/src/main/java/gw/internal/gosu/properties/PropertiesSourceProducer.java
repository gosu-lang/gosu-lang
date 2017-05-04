/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.lang.reflect.java.gen.SrcClass;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.JavaSourceProducer;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;
import java.util.Collections;
import java.util.Set;

public class PropertiesSourceProducer extends JavaSourceProducer<Model>
{
  public static final Set<String> FILE_EXTENSION = Collections.singleton( "properties" );

  public PropertiesSourceProducer( ITypeLoader typeLoader )
  {
    super( typeLoader, FILE_EXTENSION, Model::new,
           "editor.plugin.typeloader.properties.PropertiesTypeFactory",
           SystemProperties.make() );
  }

  @Override
  protected boolean isInnerType( String topLevel, String relativeInner )
  {
    Model model = getModel( topLevel );
    FqnCache<String> cache = model == null ? null : model.getCache();
    if( cache == null )
    {
      return false;
    }
    FqnCacheNode<String> node = cache.getNode( relativeInner );
    return node != null && !node.isLeaf();
  }

  @Override
  protected String produce( String topLevelFqn, Model model )
  {
    SrcClass srcClass = new PropertiesCodeGen( model.getCache(), findFileForType( topLevelFqn ), topLevelFqn ).make();
    StringBuilder sb = srcClass.render( new StringBuilder(), 0 );
    return sb.toString();
  }
}
