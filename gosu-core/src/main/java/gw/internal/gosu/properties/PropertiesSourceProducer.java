/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.fs.IFile;
import gw.lang.javac.gen.SrcClass;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.JavaSourceProducer;
import gw.lang.reflect.json.Json;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesSourceProducer extends JavaSourceProducer<FqnCache<String>>
{
  public static final String FILE_EXTENSION = "properties";

  public PropertiesSourceProducer( ITypeLoader typeLoader )
  {
    super( typeLoader, FILE_EXTENSION, PropertiesSourceProducer::buildModel,
           "editor.plugin.typeloader.properties.PropertiesTypeFactory",
           SystemProperties.make() );
  }

  private static FqnCache<String> buildModel( String fqn, IFile file )
  {
    try( InputStream propertiesStream = file.openInputStream() )
    {
      Properties properties = new Properties();
      properties.load( propertiesStream );

      FqnCache<String> cache = new FqnCache<>( fqn, true, Json::makeIdentifier );

      for( String key: properties.stringPropertyNames() )
      {
        cache.add( key, properties.getProperty( key ) );
      }
      return cache;
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  protected boolean isInnerType( String topLevel, String relativeInner )
  {
    FqnCache<String> model = getModel( topLevel );
    if( model == null )
    {
      return false;
    }
    FqnCacheNode<String> node = model.getNode( relativeInner );
    return node != null && !node.isLeaf();
  }

  @Override
  protected String produce( String topLevelFqn, FqnCache<String> model )
  {
    SrcClass srcClass = new PropertiesCodeGen( model, findFileForType( topLevelFqn ), topLevelFqn ).make();
    StringBuilder sb = srcClass.render( new StringBuilder(), 0 );
    return sb.toString();
  }
}
