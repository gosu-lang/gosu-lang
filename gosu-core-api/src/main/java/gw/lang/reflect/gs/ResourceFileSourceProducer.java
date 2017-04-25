package gw.lang.reflect.gs;

import gw.config.BaseService;
import gw.fs.IFile;
import gw.fs.cache.ModulePathCache;
import gw.lang.reflect.AbstractTypeSystemListener;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;
import gw.util.cache.FqnCache;
import gw.util.concurrent.LocklessLazyVar;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A base class for a source producer that is based on a resource file of a specific extension.
 *
 * @param <M> The model you derive backing production of source code.
 */
public abstract class ResourceFileSourceProducer<M> extends BaseService implements ISourceProducer
{
  private final ITypeLoader _typeLoader;
  private final String _extension;
  private final LocklessLazyVar<FqnCache<LocklessLazyVar<M>>> _fqnToModel;
  private final String _typeFactoryFqn;
  private final BiFunction<String, IFile, M> _modelMapper;
  @SuppressWarnings("all")
  private final CacheClearer _cacheClearer;

  /**
   * @param typeLoader The typeloader passed into the ISourceProvider implementation constructor
   * @param extension The extension of the resource file this source producer handles
   * @param modelMapper A function to provide a model given a qualified name and resource file
   */
  public ResourceFileSourceProducer( ITypeLoader typeLoader, String extension, BiFunction<String, IFile, M> modelMapper )
  {
    this( typeLoader, extension, modelMapper, null, null );
  }
  /**
   * @param typeLoader The typeloader passed into the ISourceProvider implementation constructor
   * @param extension The extension of the resource file this source producer handles
   * @param modelMapper A function to provide a model given a qualified name and resource file
   * @param typeFactoryFqn For Gosu Lab.  Optional.
   * @param peripheralTypes A map of name-to-model peripheral to the main map of name-to-model, possibly including types that are not file-based. Optional.
   */
  public ResourceFileSourceProducer( ITypeLoader typeLoader, String extension, BiFunction<String, IFile, M> modelMapper,
                                     String typeFactoryFqn, Map<String, LocklessLazyVar<M>> peripheralTypes )
  {
    _typeLoader = typeLoader;
    _extension = extension;
    _typeFactoryFqn = typeFactoryFqn;
    _modelMapper = modelMapper;
    _fqnToModel = LocklessLazyVar.make( () -> {
      FqnCache<LocklessLazyVar<M>> cache = new FqnCache<>();
      FqnCache<IFile> fileCache = ModulePathCache.instance().get( getModule() ).getExtensionCache( _extension );
      fileCache.getFqns().forEach( fqn -> cache.add( fqn, LocklessLazyVar.make( () -> _modelMapper.apply( fqn, fileCache.get( fqn ) ) ) ) );
      if( peripheralTypes != null )
      {
        cache.addAll( peripheralTypes );
      }
      return cache;
    } );
    TypeSystem.addTypeLoaderListenerAsWeakRef( _cacheClearer = new CacheClearer() );
  }

  /**
   * @param topLevelFqn Qualified name of top-level type
   * @param relativeInner Top-level relative name of inner class
   * @return true if relativeInner is an inner class of topLevel
   */
  protected abstract boolean isInnerType( String topLevelFqn, String relativeInner );

  /**
   * Generate Java source code for the named model.
   * @param topLevelFqn The qualified name of the top-level Java type to produce.
   * @param model The model your source code provider uses to generate the source.
   * @return The source code for the specified top-level Java type.
   */
  protected abstract String produce( String topLevelFqn, M model );

  protected M getModel( String topLevel )
  {
    LocklessLazyVar<M> lazyModel = _fqnToModel.get().get( topLevel );
    return lazyModel == null ? null : lazyModel.get();
  }

  @Override
  public boolean handlesFile( IFile file )
  {
    return _extension.equalsIgnoreCase( file.getExtension() );
  }

  @Override
  public String[] getTypesForFile( IFile file )
  {
    if( !file.getExtension().equalsIgnoreCase( _extension ) )
    {
      return new String[0];
    }

    String fqn = ModulePathCache.instance().get( getModule() ).getFqnForFile( file );
    return fqn != null ? new String[]{fqn} : new String[0];
  }

  public IModule getModule()
  {
    return _typeLoader.getModule();
  }

  @Override
  public RefreshKind refreshedFile( IFile file, String[] types, RefreshKind kind )
  {
    _fqnToModel.clear();
    return kind;
  }

  @Override
  public ITypeLoader getTypeLoader()
  {
    return _typeLoader;
  }

  @Override
  public SourceKind getSourceKind()
  {
    return SourceKind.Java;
  }

  @Override
  public Set<String> getExtensions()
  {
    return Collections.singleton( _extension );
  }

  @Override
  public boolean isType( String fqn )
  {
    String topLevel = findTopLevelFqn( fqn );
    if( topLevel == null )
    {
      return false;
    }

    if( topLevel.equals( fqn ) )
    {
      return true;
    }

    return isInnerType( topLevel, fqn.substring( topLevel.length()+1 ) );
  }

  /**
   * This method avoids initializing all the properties files etc. so there is minor cost until a properties object is used
   */
  protected String findTopLevelFqn( String fqn )
  {
    while( true )
    {
      LocklessLazyVar<M> lazyModel = _fqnToModel.get().get( fqn );
      if( lazyModel != null )
      {
        return fqn;
      }
      int iDot = fqn.lastIndexOf( '.' );
      if( iDot <= 0 )
      {
        return null;
      }
      fqn = fqn.substring( 0, iDot );
    }
  }

  @Override
  public boolean isTopLevelType( String fqn )
  {
    return _fqnToModel.get().get( fqn ) != null;
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    return ClassType.JavaClass;
  }

  @Override
  public String getPackage( String fqn )
  {
    String topLevel = findTopLevelFqn( fqn );
    return GosuClassUtil.getPackage( topLevel );
  }

  @Override
  public String produce( String fqn )
  {
    String topLevel = findTopLevelFqn( fqn );
    LocklessLazyVar<M> lazyModel = _fqnToModel.get().get( topLevel );

    String source = produce( topLevel, lazyModel.get() );

    // Now remove the model since we don't need it anymore
    lazyModel.clear();

    return source;
  }

  @Override
  public Collection<String> getAllTypeNames()
  {
    return _fqnToModel.get().getFqns();
  }

  @Override
  public Collection<TypeName> getTypeNames( String namespace )
  {
    return getAllTypeNames().stream().filter( fqn -> fqn.startsWith( namespace + '.' ) ).map( fqn -> new TypeName( fqn, _typeLoader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC ) ).collect( Collectors.toSet() );
  }

  @Override
  public IFile findFileForType( String fqn )
  {
    String topLevel = findTopLevelFqn( fqn );
    if( topLevel == null )
    {
      return null;
    }

    FqnCache<IFile> fileCache = ModulePathCache.instance().get( getModule() ).getExtensionCache( _extension );
    return fileCache.get( topLevel );
  }

  @Override
  public void clear()
  {
    _fqnToModel.clear();
  }

  @Override
  public <T> List<T> getInterface( Class<T> apiInterface )
  {
    if( _fqnToModel != null && apiInterface.getName().equals( "editor.plugin.typeloader.ITypeFactory" ) )
    {
      try
      {
        //noinspection unchecked
        return Collections.singletonList( (T)Class.forName( _typeFactoryFqn ).newInstance() );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return super.getInterface( apiInterface );
  }

  private class CacheClearer extends AbstractTypeSystemListener
  {
    @Override
    public void refreshed()
    {
      clear();
    }

    @Override
    public void refreshedTypes( RefreshRequest request )
    {
      IModule refreshModule = request.module;
      if( refreshModule != null && refreshModule != getModule() )
      {
        return;
      }

      switch( request.kind )
      {
        case MODIFICATION:
          Arrays.stream( request.types ).forEach(
            fqn -> {
              LocklessLazyVar<M> lazyModel = _fqnToModel.get().get( fqn );
              if( lazyModel != null )
              {
                lazyModel.clear();
              }
            } );
          break;

        case CREATION:
        {
          Arrays.stream( request.types ).forEach(
            fqn -> {
              FqnCache<IFile> fileCache = ModulePathCache.instance().get( getModule() ).getExtensionCache( _extension );
              _fqnToModel.get().add( fqn, LocklessLazyVar.make( () -> _modelMapper.apply( fqn, fileCache.get( fqn ) ) ) );
            } );
          break;
        }

        case DELETION:
        {
          Arrays.stream( request.types ).forEach(
            fqn -> {
              _fqnToModel.get().remove( fqn );
            } );
          break;
        }
      }
    }

  }

}
