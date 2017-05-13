/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import manifold.api.fs.IDirectory;
import manifold.api.fs.IFile;
import gw.lang.GosuShop;
import gw.lang.reflect.gs.ISourceFileHandle;
import manifold.api.host.RefreshKind;
import manifold.api.sourceprod.ISourceProducer;
import gw.lang.reflect.gs.SourceProducerSourceFileHandle;
import gw.lang.reflect.module.IModule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class SimpleTypeLoader extends TypeLoaderBase
{
  protected SimpleTypeLoader( IModule module )
  {
    super( module );
  }

  public abstract Set<String> getExtensions();

  @Override
  public String[] getTypesForFile( IFile file )
  {
    List<String> result = new ArrayList<>();
    List<IDirectory> sourcePath = getModule().getSourcePath();
    for( IDirectory src : sourcePath )
    {
      if( file.isDescendantOf( src ) )
      {
        String fqn = src.relativePath( file );
        fqn = fqn.substring( 0, fqn.lastIndexOf( '.' ) ).replace( '/', '.' );
        result.add( fqn );
      }
    }
    for( ISourceProducer sp : getSourceProducers() )
    {
      Arrays.stream( sp.getTypesForFile( file ) ).forEach( result::add );
    }
    return result.toArray( new String[result.size()] );
  }

  @Override
  public RefreshKind refreshedFile( IFile file, String[] types, RefreshKind kind )
  {
    return kind;
  }

  @Override
  public boolean handlesDirectory( IDirectory dir )
  {
    List<IDirectory> sourcePath = getModule().getSourcePath();
    for( IDirectory src : sourcePath )
    {
      if( dir.isDescendantOf( src ) )
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getNamespaceForDirectory( IDirectory dir )
  {
    List<IDirectory> sourcePath = getModule().getSourcePath();
    for( IDirectory src : sourcePath )
    {
      if( dir.isDescendantOf( src ) )
      {
        return src.relativePath( dir ).replace( '/', '.' );
      }
    }
    return null;
  }

  public abstract void setSourceProducers( Set<String> sourceProducers );
  public abstract Set<ISourceProducer> getSourceProducers();

  protected ISourceFileHandle loadFromSourceProducer( String fqn, Collection<ISourceProducer> sourceProducers )
  {
    for( ISourceProducer sp : sourceProducers )
    {
      if( sp.isType( fqn ) )
      {
        if( sp.isTopLevelType( fqn ) )
        {
          return new SourceProducerSourceFileHandle( fqn, sp );
        }
        else
        {
          int iLastDot = fqn.lastIndexOf( '.' );
          String enclosingClass = fqn.substring( 0, iLastDot );
          String simpleName = fqn.substring( iLastDot + 1 );
          return GosuShop.createInnerClassSourceFileHandle( sp.getClassType( fqn ), enclosingClass, simpleName, false );
        }
      }
    }
    return null;
  }

  @Override
  public void refreshedNamespace( String namespace, IDirectory dir, RefreshKind kind )
  {
    //noinspection unchecked
    Set<CharSequence> namespaces = (Set<CharSequence>)getAllNamespaces();
    if( namespaces != null )
    {
      if( kind == RefreshKind.CREATION )
      {
        namespaces.add( namespace );
      }
      else if( kind == RefreshKind.DELETION )
      {
        namespaces.remove( namespace );
      }
    }
  }

  protected abstract void addBuiltInSourceProducers( Set<ISourceProducer> set );
}
