package gw.fs.cache;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.json.Json;
import gw.lang.reflect.module.IModule;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 */
public class PathCache extends FqnCache<IFile>
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
      if( sourceEntry.exists() )
      {
        addFilesInDir( "", sourceEntry, filesByExtension );
      }
    }
    _filesByExtension = filesByExtension;
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

  @Override
  protected void updateReverseMap( FqnCacheNode<IFile> node, IFile prev )
  {
    IFile newValue = node.getUserData();
    if( newValue == null )
    {
      _reverseMap.remove( prev, node.getFqn() );
    }
    else
    {
      _reverseMap.put( newValue, node.getFqn() );
    }
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
        add( fqn, file );
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

  @Override
  public void clear()
  {
    super.clear();
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
              add( fqn, request.file );
              addToExtension( fqn, request.file, _filesByExtension );
            } );
          break;
        }

        case DELETION:
        {
          Arrays.stream( request.types ).forEach(
            fqn -> {
              remove( fqn );
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
