package editor;

import gw.fs.IDirectory;
import gw.lang.reflect.IType;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.TypeLoaderBase;
import gw.lang.reflect.TypeSystem;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class TypeInPackageTypeLoader extends TypeLoaderBase
{
  private static TypeInPackageTypeLoader INSTANCE;

  private int _moduleId = -1;
  private final Map _mapTypeByName;

  public static TypeInPackageTypeLoader instance()
  {
    if( INSTANCE == null )
    {
      INSTANCE = new TypeInPackageTypeLoader();
    }
    else
    {
      // a lazy way to handle an environment such as Gosu Lab where the module can change (a new project opens)

      int moduleId = System.identityHashCode( TypeSystem.getModule() );
      if( INSTANCE._moduleId != moduleId )
      {
        INSTANCE = new TypeInPackageTypeLoader();
      }
    }

    return INSTANCE;
  }

  private TypeInPackageTypeLoader()
  {
    _mapTypeByName = new ConcurrentHashMap();
    _moduleId = System.identityHashCode( TypeSystem.getModule() );
  }

  public IType getType( String fullyQualifiedName )
  {
    return get( fullyQualifiedName );
  }

  @Override
  public Set<? extends CharSequence> getAllNamespaces()
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public IType getIntrinsicTypeByFullName( String fullyQualifiedName ) throws ClassNotFoundException
  {
    return get( fullyQualifiedName );
  }

  public List<String> getHandledPrefixes()
  {
    return Collections.emptyList();
  }

  @Override
  public boolean handlesNonPrefixLoads()
  {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void refreshedNamespace( String s, IDirectory iDirectory, RefreshKind refreshKind )
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean hasNamespace( String s )
  {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Set<String> computeTypeNames()
  {
    return Collections.emptySet();
  }

  private TypeInPackageType get( String strPackage )
  {
    TypeInPackageType type = (TypeInPackageType)_mapTypeByName.get( strPackage );

    if( type == null )
    {
      synchronized( _mapTypeByName )
      {
        type = (TypeInPackageType)_mapTypeByName.get( strPackage );
        if( type == null )
        {
          type = define( strPackage );
        }
      }
    }
    return type;
  }

  private TypeInPackageType define( String strPackage )
  {
    TypeInPackageType type = new TypeInPackageType( strPackage );
    _mapTypeByName.put( strPackage, type );
    return type;
  }
}
