package editor;

import manifold.api.fs.IDirectory;
import gw.lang.reflect.IType;
import manifold.api.host.RefreshKind;
import gw.lang.reflect.TypeLoaderBase;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class TypeInPackageTypeLoader extends TypeLoaderBase
{
  private static final Map<IModule, TypeInPackageTypeLoader> INSTANCE_BY_MODULE = new HashMap<IModule, TypeInPackageTypeLoader>();
  private final Map _mapTypeByName;

  public static TypeInPackageTypeLoader instance()
  {
    IModule module = TypeSystem.getCurrentModule();
    TypeInPackageTypeLoader instance = INSTANCE_BY_MODULE.get( module );
    if( instance == null )
    {
      INSTANCE_BY_MODULE.put( module, instance = new TypeInPackageTypeLoader() );
    }
    return instance;
  }

  private TypeInPackageTypeLoader()
  {
    _mapTypeByName = new ConcurrentHashMap();
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
