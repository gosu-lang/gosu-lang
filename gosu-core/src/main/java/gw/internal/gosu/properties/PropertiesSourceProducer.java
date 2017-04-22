/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.config.BaseService;
import gw.fs.IFile;
import gw.fs.cache.ModulePathCache;
import gw.lang.javac.gen.SrcClass;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.ISourceProducer;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.json.Json;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;
import gw.util.concurrent.LockingLazyVar;
import gw.util.concurrent.LocklessLazyVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PropertiesSourceProducer extends BaseService implements ISourceProducer
{
  public static final String FILE_EXTENSION = "properties";
  private static final String DISPLAY_PROPERTIES = ".display." + FILE_EXTENSION;
  private static final Set<String> EXTENSIONS = Collections.singleton( FILE_EXTENSION );

  private final ITypeLoader _typeLoader;
  private List<PropertySetSource> _sources;
  private final LockingLazyVar<Map<PropertySetSource, TypeNameSet>> _rootTypeNames =
    new LockingLazyVar<Map<PropertySetSource, TypeNameSet>>()
    {
      @Override
      protected Map<PropertySetSource, TypeNameSet> init()
      {
        Map<PropertySetSource, TypeNameSet> result = new HashMap<>();
        for( PropertySetSource source : _sources )
        {
          result.put( source, new TypeNameSet( source.getPropertySetNames() ) );
        }
        return result;
      }
    };
  private LocklessLazyVar<Set<String>> _typeNames =
    LocklessLazyVar.make( () ->  _rootTypeNames.get().values().stream().flatMap( names -> Arrays.stream( names._names ) ).collect( Collectors.toSet() ) );



  public PropertiesSourceProducer( ITypeLoader typeLoader )
  {
    _typeLoader = typeLoader;
    initSources( _typeLoader.getModule() );
  }

  private void initSources( IModule module )
  {
    _sources = Arrays.asList(
      SystemPropertiesPropertySet.SOURCE,
      new PropertiesPropertySet.Source( module )
    );
  }

  @Override
  public boolean handlesFile( IFile file )
  {
    return FILE_EXTENSION.equalsIgnoreCase( file.getExtension() ) &&
           !isDisplayPropertiesFile( file.getName() );
  }

  public static boolean isDisplayPropertiesFile( String fileName )
  {
    return fileName.toLowerCase().endsWith( DISPLAY_PROPERTIES );
  }

  private Map<String, FqnCacheNode<String>> createPropertyTypesForPropertySetWithName( PropertySetSource source, String name )
  {
    Map<String, FqnCacheNode<String>> result = new HashMap<>();
    createPropertyTypesFromPropertyNodeTree( result, buildCache( source.getPropertySet( name ) ) );
    return result;
  }

  private void createPropertyTypesFromPropertyNodeTree( Map<String, FqnCacheNode<String>> resultMap, FqnCacheNode<String> node )
  {
    resultMap.put( node.getFqn(), node );
    //noinspection Convert2streamapi
    for( FqnCacheNode<String> child : node.getChildren() )
    {
      if( !child.isLeaf() )
      {
        createPropertyTypesFromPropertyNodeTree( resultMap, child );
      }
    }
  }

  @Override
  public String[] getTypesForFile( IFile file )
  {
    Map<String, FqnCacheNode<String>> types = new HashMap<>();
    for( PropertySetSource source : _sources )
    {
      PropertySet ps = source.getPropertySetForFile( file );
      if( ps != null )
      {
        createPropertyTypesFromPropertyNodeTree( types, buildCache( ps ) );
      }
    }
    return types.keySet().toArray( new String[types.size()] );
  }

  @Override
  public RefreshKind refreshedFile( IFile file, String[] types, RefreshKind kind )
  {
    _rootTypeNames.clear();
    return kind;
  }

  public static FqnCache<String> buildCache( PropertySet ps )
  {
    FqnCache<String> cache = new FqnCache<>( ps.getName(), true, Json::makeIdentifier );

    for( String key: ps.getKeys() )
    {
      cache.add( key, ps.getValue( key ) );
    }
    return cache;
  }

  /**
   * Set of case insensitive type names with operation to quickly find which type names in the set are possible
   * matches for a full type name. A match is a prefix which is either an exact match or matches up to the
   * package separator (.). So, for example, given the full name one.two.three then one, one.two and one.two.three
   * are all matches.
   */
  static class TypeNameSet
  {

    private final String[] _names;

    public TypeNameSet( Set<String> names )
    {
      _names = names.toArray( new String[names.size()] );
      Arrays.sort( _names, String.CASE_INSENSITIVE_ORDER );
    }

    public List<String> findMatchesFor( String fullName )
    {
      List<String> result = Collections.emptyList();
      int index = getIndexOfLastPossibleMatch( fullName );
      while( index >= 0 && isPrefix( _names[index], fullName ) )
      {
        if( isMatch( _names[index], fullName ) )
        {
          if( result.isEmpty() )
          {
            result = new ArrayList<>();
          }
          result.add( _names[index] );
        }
        index--;
      }
      return result;
    }

    private int getIndexOfLastPossibleMatch( String fullName )
    {
      int index = Arrays.binarySearch( _names, fullName, String.CASE_INSENSITIVE_ORDER );
      if( index < 0 )
      {
        int insertionPoint = -index - 1;
        index = insertionPoint - 1;
      }
      return index;
    }

    private boolean isPrefix( String possiblePrefix, String fullName )
    {
      return fullName.regionMatches( true, 0, possiblePrefix, 0, possiblePrefix.length() );
    }

    private boolean isMatch( String prefix, String fullName )
    {
      return fullName.equalsIgnoreCase( prefix ) || fullName.charAt( prefix.length() ) == '.';
    }
  }

  @Override
  public <T> List<T> getInterface( Class<T> apiInterface )
  {
    if( apiInterface.getName().equals( "editor.plugin.typeloader.ITypeFactory" ) )
    {
      try
      {
        //noinspection unchecked
        return Collections.singletonList( (T)Class.forName( "editor.plugin.typeloader.properties.PropertiesTypeFactory" ).newInstance() );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return super.getInterface( apiInterface );
  }

  @Override
  public ITypeLoader getTypeLoader()
  {
    return _typeLoader;
  }

  public IModule getModule()
  {
    return _typeLoader.getModule();
  }

  @Override
  public SourceKind getSourceKind()
  {
    return SourceKind.Java;
  }

  @Override
  public Set<String> getExtensions()
  {
    return EXTENSIONS;
  }

  @Override
  public boolean isType( String fqn )
  {
    return (findPropertiesFileFor( fqn ) != null || isSystemProperties( fqn ) ) &&
           findNode( fqn, false ) != null;
  }

  private boolean isSystemProperties( String fqn )
  {
    return fqn.equals( "gw.lang.SystemProperties" ) || fqn.startsWith( "gw.lang.SystemProperties." );
  }

  @Override
  public boolean isTopLevelType( String fqn )
  {
    return findNode( fqn, true ) != null;
  }

  /**
   * This method avoids initializing all the properties files etc. so there is minor cost until a properties object is used
   */
  private IFile findPropertiesFileFor( String fqn )
  {
    FqnCache<IFile> propertiesFileCache = ModulePathCache.instance().get( getModule() ).getExtensionCache( FILE_EXTENSION );
    while( true )
    {
      IFile file = propertiesFileCache.get( fqn );
      if( file != null )
      {
        return file;
      }
      int iDot = fqn.lastIndexOf( '.' );
      if( iDot <= 0 )
      {
        return null;
      }
      fqn = fqn.substring( 0, iDot );
    }
  }

  public FqnCacheNode<String> findNode( String fqn, boolean topLevel )
  {
    for( PropertySetSource source : _sources )
    {
      TypeNameSet typeNameSet = _rootTypeNames.get().get( source );
      if( typeNameSet != null &&
          (!topLevel || Arrays.stream( typeNameSet._names ).anyMatch( e -> e.equals( fqn ) )) )
      {
        List<String> possibleMatches = typeNameSet.findMatchesFor( fqn );
        for( String possibleMatch : possibleMatches )
        {
          Map<String, FqnCacheNode<String>> propertySetTypes = createPropertyTypesForPropertySetWithName( source, possibleMatch );
          if( propertySetTypes.containsKey( fqn ) )
          {
            return propertySetTypes.get( fqn );
          }
        }
      }
    }
    return null;
  }

  @Override
  public Collection<String> getAllTypeNames()
  {
    return _typeNames.get();
  }

  public Collection<TypeName> getTypeNames( String namespace )
  {
    return _typeNames.get().stream().filter( fqn -> fqn.startsWith( namespace + '.' ) ).map( fqn -> new TypeName( fqn, _typeLoader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC ) ).collect( Collectors.toSet() );
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    return ClassType.Class;
  }

  @Override
  public String getPackage( String fqn )
  {
    return GosuClassUtil.getPackage( fqn );
  }

  public void invalidate()
  {
    _rootTypeNames.clear();
  }

  @Override
  public IFile findFileForType( String fqn )
  {
    for( PropertySetSource source : _sources )
    {
      String name = fqn;
      do
      {
        IFile file = source.getFile( name );
        if( file != null )
        {
          return file;
        }
        int iDot = name.lastIndexOf( '.' );
        if( iDot > 0 )
        {
          name = name.substring( 0, iDot );
        }
        else
        {
          break;
        }
      } while( true );
    }
    return null;
  }

  public PropertySet findPropertySet( String fqn )
  {
    for( PropertySetSource source : _sources )
    {
      String name = fqn;
      do
      {
        PropertySet ps = source.getPropertySet( name );
        if( ps != null )
        {
          return ps;
        }
        int iDot = name.lastIndexOf( '.' );
        if( iDot > 0 )
        {
          name = name.substring( 0, iDot );
        }
        else
        {
          break;
        }
      } while( true );
    }
    return null;
  }

  @Override
  public void clear()
  {
    _rootTypeNames.clear();
    initSources( _typeLoader.getModule() );
    _typeNames.clear();
  }

  @Override
  public String produce( String fqn )
  {
    SrcClass srcClass = new PropertiesSource( findPropertySet( fqn ), findFileForType( fqn ), fqn ).make();
    StringBuilder sb = srcClass.render( new StringBuilder(), 0 );
    return sb.toString();
  }
}
