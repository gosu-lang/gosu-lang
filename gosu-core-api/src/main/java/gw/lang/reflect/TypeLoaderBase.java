/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.BaseService;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.gs.TypeName;
import gw.util.GosuClassUtil;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class TypeLoaderBase extends BaseService implements ITypeLoader
{
  protected Set<String> _typeNames;

  protected TypeLoaderBase()
  {
  }

  @Override
  public boolean isCaseSensitive()
  {
    return false;
  }

  @Override
  public boolean handlesFile( IFile file )
  {
    return false;
  }

  @Override
  public boolean handlesDirectory( IDirectory dir )
  {
    return false;
  }

  @Override
  public String getNamespaceForDirectory( IDirectory dir )
  {
    return null;
  }

  @Override
  public String[] getTypesForFile( IFile file )
  {
    return NO_TYPES;
  }

  @Override
  public RefreshKind refreshedFile( IFile file, String[] types, RefreshKind kind )
  {
    return kind;
  }

  @Override
  public URL getResource( String name )
  {
    return null;
  }

  @Override
  public final void refreshedTypes( RefreshRequest request )
  {
    clearTypeNames();

    refreshedTypesImpl( request );
  }

  protected void refreshedTypesImpl( RefreshRequest request )
  {
    // for subclasses to do their refresh work
  }

  @Override
  public final void refreshed()
  {
    clearTypeNames();
    refreshedImpl();
  }

  protected void clearTypeNames()
  {
    _typeNames = null;
  }

  protected void refreshedImpl()
  {
    // for subclasses to do their refresh work
  }

  public String toString()
  {
    return this.getClass().getSimpleName();
  }

  @Override
  public Set<TypeName> getTypeNames( String namespace )
  {
    if( hasNamespace( namespace ) )
    {
      return TypeLoaderBase.getTypeNames( namespace, this );
    }
    else
    {
      return Collections.emptySet();
    }
  }

  public static Set<TypeName> getTypeNames( String parentNamespace, ITypeLoader loader )
  {
    Set<TypeName> typeNames = new HashSet<>();
    for( CharSequence typeNameCS: loader.getAllTypeNames() )
    {
      String typeName = typeNameCS.toString();
      String packageName = GosuClassUtil.getPackage( typeName );
      if( packageName.equals( parentNamespace ) )
      {
        typeNames.add( new TypeName( typeName, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC ) );
      }
    }
    for( CharSequence namespaceCs: loader.getAllNamespaces() )
    {
      String namespace = namespaceCs.toString();
      String containingPackageName = GosuClassUtil.getPackage( namespace );
      if( containingPackageName.equals( parentNamespace ) )
      {
        typeNames.add( new TypeName( GosuClassUtil.getNameNoPackage( namespace ), TypeName.Kind.NAMESPACE, TypeName.Visibility.PUBLIC ) );
      }
    }
    return typeNames;
  }

  @Override
  public boolean showTypeNamesInIDE()
  {
    return true;
  }

  @Override
  public void shutdown()
  {
  }

  @Override
  public final Set<String> getAllTypeNames()
  {
    if( _typeNames == null )
    {
      _typeNames = new HashSet<>( computeTypeNames() );
    }
    return _typeNames;
  }
}
