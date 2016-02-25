package editor;

import editor.util.IProgressCallback;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.ITypeLoaderStack;
import gw.util.concurrent.LocklessLazyVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class TypeNameCache implements ITypeLoaderListener
{
  private LocklessLazyVar<ITypeLoaderStack> _moduleTypeLoader =
    new LocklessLazyVar<ITypeLoaderStack>()
    {
      protected ITypeLoaderStack init()
      {
        ITypeLoaderStack moduleLoader = TypeSystem.getGlobalModule().getModuleTypeLoader();
        TypeSystem.addTypeLoaderListenerAsWeakRef( TypeNameCache.this );
        return moduleLoader;
      }
    };

  private volatile Set<String> _allTypeNames;
  private Set<String> _allNamespaces;
  private Map<String, List<String>> _relativeTypeNameToFullyQualifiedTypeNames;
  private volatile boolean _caching;


  public TypeNameCache()
  {
  }

  public Set<String> getAllTypeNames( IProgressCallback progress )
  {
    return getAllTypeNamesCache( progress );
  }

  public List<String> getFullyQualifiedClassNameFromRelativeName( String relativeClassName )
  {
    return getRelativeTypeNamesCache().get( relativeClassName );
  }

  public Set<String> getAllNamespaces()
  {
    getAllTypeNamesCache( null );
    return _allNamespaces;
  }

  @Override
  public void refreshedTypes( RefreshRequest request )
  {
    switch( request.kind )
    {
      case CREATION:
      case DELETION:
        clearCaches();
        break;
    }
  }

  @Override
  public void refreshed()
  {
    clearCaches();
  }

  private Set<String> getAllTypeNamesCache( IProgressCallback progress )
  {
    if( _allTypeNames != null )
    {
      return _allTypeNames;
    }

    TypeSystem.lock();
    try
    {
      if( _allTypeNames != null )
      {
        return _allTypeNames;
      }
      _caching = true;
      Set<String> names = new HashSet<>();
      List<ITypeLoader> loaders = _moduleTypeLoader.get().getTypeLoaderStack();

      if( progress != null )
      {
        progress.setLength( loaders.size() );
      }

      // Walk the list backward so we resolve type dependencies in the correct order
      for (int i = loaders.size() -1; i >= 0; i--)
      {
        if( progress != null )
        {
          progress.updateProgress( progress.getProgress()+1, "ProgressFeedback.Loading_type_names" );
        }

        ITypeLoader loader = loaders.get( i );
        for( CharSequence typeName : loader.getAllTypeNames() )
        {
          names.add( (String)typeName );
          addRelativeToFullTypeMapping( (String)typeName );
        }
      }
      if( _relativeTypeNameToFullyQualifiedTypeNames != null )
      {
        for( List<String> fullNames : _relativeTypeNameToFullyQualifiedTypeNames.values() )
        {
          ((ArrayList<String>)fullNames).trimToSize();
        }
      }
      _allTypeNames = names;
      return _allTypeNames;
    }
    finally
    {
      TypeSystem.unlock();
      _caching = false;
    }
  }

  private Map<String, List<String>> getRelativeTypeNamesCache()
  {
    getAllTypeNamesCache( null );
    return _relativeTypeNameToFullyQualifiedTypeNames;
  }

  private void addRelativeToFullTypeMapping( String fullyQualifiedTypeName )
  {
    if( _relativeTypeNameToFullyQualifiedTypeNames == null )
    {
      _relativeTypeNameToFullyQualifiedTypeNames = new HashMap<>();
    }

    if( _allNamespaces ==  null )
    {
      _allNamespaces = new HashSet<>();
    }

    int index = lastIndexOf( fullyQualifiedTypeName, '.' );
    String relativeName = (String)fullyQualifiedTypeName.subSequence( index + 1, fullyQualifiedTypeName.length() );
    if( index > 0 )
    {
      String namespace = (String)fullyQualifiedTypeName.subSequence( 0, index );
      _allNamespaces.add( namespace  );
    }
    List<String> fullNames = _relativeTypeNameToFullyQualifiedTypeNames.get( relativeName );
    if( fullNames == null )
    {
      fullNames = new ArrayList<>( 1 );
      _relativeTypeNameToFullyQualifiedTypeNames.put( relativeName, fullNames );
    }
    fullNames.add( fullyQualifiedTypeName );
  }

  private int lastIndexOf( String fullyQualifiedTypeName, char c )
  {
    int index = fullyQualifiedTypeName.length();
    boolean found = false;
    while( (index > 0) && !found )
    {
      found = fullyQualifiedTypeName.charAt( --index ) == c;
    }
    if( !found )
    {
      index = -1;
    }
    return index;
  }


  public void clearCaches()
  {
    if( _caching )
    {
      return;
    }
    TypeSystem.lock();
    try
    {
      if( _caching )
      {
        return;
      }
      _allTypeNames = null;
      _allNamespaces = null;
      _relativeTypeNameToFullyQualifiedTypeNames = null;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }
}
