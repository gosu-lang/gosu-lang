package gw.fs.cache;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.json.Json;
import gw.lang.reflect.module.IModule;
import gw.util.Extensions;
import gw.util.cache.FqnCache;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 */
public class PathCache
{
  @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
  private CacheClearer _clearer;
  private IModule _module;
  private final Supplier<Collection<IDirectory>> _pathSupplier;
  private final Runnable _clearHandler;
  private Map<IFile, String> _reverseMap;
  private Map<String, FqnCache<IFile>> _filesByExtension;

  public PathCache( IModule module, Supplier<Collection<IDirectory>> pathSupplier, Runnable clearHandler )
  {
    _module = module;
    _pathSupplier = pathSupplier;
    _clearHandler = clearHandler;
    _reverseMap = new ConcurrentHashMap<>();
    init();
    TypeSystem.addTypeLoaderListenerAsWeakRef( _clearer = new CacheClearer() );
  }

  private void init()
  {
    Map<String, FqnCache<IFile>> filesByExtension = new ConcurrentHashMap<>();
    for( IDirectory sourceEntry : _pathSupplier.get() )
    {
      if( hasSourceFiles( sourceEntry ) )
      {
        addFilesInDir( "", sourceEntry, filesByExtension );
      }
    }
    _filesByExtension = filesByExtension;
  }

  /**
   * Avoid including dependency jar files that are not meant to be scanned for source files
   */
  private boolean hasSourceFiles( IDirectory root )
  {
    if( !root.exists() )
    {
      return false;
    }

//    if( root.toString().contains( File.separator + "gosu-" ) )
//    {
//      return false;
//    }

    return !Extensions.containsManifest( root ) ||
           !Extensions.getExtensions( root, Extensions.CONTAINS_SOURCES ).isEmpty() ||
           // Weblogic packages all WEB-INF/classes content into this JAR
           // http://middlewaremagic.com/weblogic/?p=408
           // http://www.coderanch.com/t/69641/BEA-Weblogic/wl-cls-gen-jar-coming
           // So we need to always treat it as containing sources
           root.getName().equals( "_wl_cls_gen.jar" );
  }

  public Set<IFile> findFiles( String fqn )
  {
    Set<IFile> result = Collections.emptySet();
    for( String ext: _filesByExtension.keySet() )
    {
      IFile file = _filesByExtension.get( ext ).get( fqn );
      if( file != null )
      {
        if( result.isEmpty() )
        {
          result = new HashSet<>( 2 );
        }
        result.add( file );
      }
    }
    return result;
  }

  public FqnCache<IFile> getExtensionCache( String extension )
  {
    FqnCache<IFile> extCache = _filesByExtension.get( extension.toLowerCase() );
    if( extCache == null )
    {
      _filesByExtension.put( extension, extCache = new FqnCache<>() );
    }
    return extCache;
  }

  public String getFqnForFile( IFile file )
  {
    return _reverseMap.get( file );
  }

  private void addFilesInDir( String relativePath, IDirectory dir, Map<String, FqnCache<IFile>> filesByExtension )
  {
    if( !CommonServices.getPlatformHelper().isPathIgnored( relativePath ) )
    {
      for( IFile file : dir.listFiles() )
      {
        String simpleName = file.getName();
        int iDot = simpleName.lastIndexOf( '.' );
        if( iDot > 0 )
        {
          simpleName = simpleName.substring( 0, iDot );
        }
        String fqn = appendResourceNameToPath( relativePath, simpleName );
        addToExtension( fqn, file, filesByExtension );
      }
      for( IDirectory subdir : dir.listDirs() )
      {
        String fqn = appendResourceNameToPath( relativePath, subdir.getName() );
        addFilesInDir( fqn, subdir, filesByExtension );
      }
    }
  }

  private void addToExtension( String fqn, IFile file, Map<String, FqnCache<IFile>> filesByExtension )
  {
    String ext = file.getExtension().toLowerCase();
    FqnCache<IFile> cache = filesByExtension.get( ext );
    if( cache == null )
    {
      filesByExtension.put( ext, cache = new FqnCache<>() );
    }
    cache.add( fqn, file );
  }
  private void removeFromExtension( String fqn, IFile file, Map<String, FqnCache<IFile>> filesByExtension )
  {
    String ext = file.getExtension().toLowerCase();
    FqnCache<IFile> cache = filesByExtension.get( ext );
    if( cache != null )
    {
      cache.remove( fqn );
    }
  }

  private static String appendResourceNameToPath( String relativePath, String resourceName )
  {
    String path;
    if( relativePath.length() > 0 )
    {
      path = relativePath + '.' + Json.makeIdentifier( resourceName );
    }
    else
    {
      path = resourceName;
    }
    return path;
  }

  public void clear()
  {
    _filesByExtension.clear();
    _reverseMap = new ConcurrentHashMap<>();
  }

  private class CacheClearer extends AbstractTypeSystemListener
  {
    @Override
    public void refreshed()
    {
      clear();
      _clearHandler.run();
    }

    @Override
    public void refreshedTypes( RefreshRequest request )
    {
      IModule refreshModule = request.module;
      if( refreshModule != null && refreshModule != _module )
      {
        return;
      }

      switch( request.kind )
      {
        case CREATION:
        {
          Arrays.stream( request.types ).forEach(
            fqn -> {
              _reverseMap.put( request.file, fqn );
              addToExtension( fqn, request.file, _filesByExtension );
            } );
          break;
        }

        case DELETION:
        {
          Arrays.stream( request.types ).forEach(
            fqn -> {
              _reverseMap.remove( request.file );
              removeFromExtension( fqn, request.file, _filesByExtension );
            } );
          break;
        }

        case MODIFICATION:
          break;
      }
    }

  }
}
