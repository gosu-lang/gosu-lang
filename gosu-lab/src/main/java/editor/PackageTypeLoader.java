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
public class PackageTypeLoader extends TypeLoaderBase
{
  private static final Map<IModule, PackageTypeLoader> INSTANCE_BY_MODULE = new HashMap<IModule, PackageTypeLoader>();
  private final Map<CharSequence, PackageType> _mapTypeByName;

  public static PackageTypeLoader instance()
  {
    IModule module = TypeSystem.getCurrentModule();
    PackageTypeLoader instance = INSTANCE_BY_MODULE.get( module );
    if( instance == null )
    {
      INSTANCE_BY_MODULE.put( module, instance = new PackageTypeLoader() );
    }
    return instance;
  }

  private PackageTypeLoader()
  {
    _mapTypeByName = new ConcurrentHashMap<CharSequence, PackageType>();
  }

  @Override
  public PackageType getType( String fullyQualifiedName )
  {
    return get( fullyQualifiedName );
  }

  @Override
  public boolean hasNamespace( String s )
  {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Set<String> computeTypeNames()
  {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public IType getIntrinsicTypeByFullName( String fullyQualifiedName, boolean bSkipVerify ) throws ClassNotFoundException
  {
    return get( fullyQualifiedName, bSkipVerify );
  }

  @Override
  public Set<? extends CharSequence> getAllNamespaces()
  {
    return Collections.emptySet();
  }

  private void clearCaches()
  {
    _mapTypeByName.clear();
  }

  @Override
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
    clearCaches();
  }

  private PackageType get( String strPackage )
  {
    return get( strPackage, false );
  }

  private PackageType get( String strPackage, boolean bSkipVerify )
  {
    PackageType type = _mapTypeByName.get( strPackage );

    if( type == null )
    {
      synchronized( _mapTypeByName )
      {
        type = _mapTypeByName.get( strPackage );
        if( type == null )
        {
          type = define( strPackage, bSkipVerify );
        }
      }
    }
    return type;
  }

  private PackageType define( String strPackage, boolean bSkipVerify )
  {
    if( bSkipVerify || packageExists( strPackage ) )
    {
      PackageType type = new PackageType( strPackage );
      _mapTypeByName.put( strPackage, type );
      return type;
    }
    return null;
  }

  private boolean packageExists( String strName )
  {
    String strNamePlusDot = strName + '.';
    for( CharSequence cs : TypeSystem.getAllTypeNames() )
    {
      String strType = cs.toString();
      if( strType.startsWith( strNamePlusDot ) )
      {
        return true;
      }
    }
    return false;
  }

}
