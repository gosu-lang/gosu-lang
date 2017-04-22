/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.gs.ISourceProducer;
import gw.lang.reflect.module.IModule;

import gw.util.concurrent.LocklessLazyVar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SimpleTypeLoader extends TypeLoaderBase
{
  private LocklessLazyVar<Set<ISourceProducer>> _javaSourceProducers;
  private LocklessLazyVar<Set<ISourceProducer>> _gosuSourceProducers;

  protected SimpleTypeLoader( IModule module )
  {
    super( module );
  }

  public abstract Set<String> getExtensions();

  @Override
  public boolean handlesFile( IFile file )
  {
    boolean handlesFile = getExtensions().contains( file.getExtension() );
    if( !handlesFile )
    {
      for( ISourceProducer sp : getJavaSourceProducers() )
      {
        if( sp.handlesFile( file ) )
        {
          handlesFile = true;
          break;
        }
      }
      if( !handlesFile )
      {
        for( ISourceProducer sp : getGosuSourceProducers() )
        {
          if( sp.handlesFile( file ) )
          {
            handlesFile = true;
            break;
          }
        }
      }
    }
    return handlesFile;
  }

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
    for( ISourceProducer sp : getJavaSourceProducers() )
    {
      Arrays.stream( sp.getTypesForFile( file ) ).forEach( result::add );
    }
    for( ISourceProducer sp : getGosuSourceProducers() )
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

  public Set<ISourceProducer> getJavaSourceProducers()
  {
    return _javaSourceProducers == null ? Collections.emptySet() : _javaSourceProducers.get();
  }

  public Set<ISourceProducer> getGosuSourceProducers()
  {
    return _gosuSourceProducers == null ? Collections.emptySet() : _gosuSourceProducers.get();
  }

  public void setSourceProducers( Set<String> sourceProducers )
  {
    _javaSourceProducers = LocklessLazyVar.make( () ->
                                                 {
                                                   Set<ISourceProducer> set = sourceProducers.stream()
                                                     .map( fqn ->
                                                           {
                                                             try
                                                             {
                                                               Class<?> cls = Class.forName( fqn );
                                                               return (ISourceProducer)cls.newInstance();
                                                             }
                                                             catch( Exception ex )
                                                             {
                                                               throw new RuntimeException( ex );
                                                             }
                                                           } )
                                                     .filter( sp -> sp.getSourceKind() == ISourceProducer.SourceKind.Java )
                                                     .collect( Collectors.toSet() );
                                                   addBuiltInSourceProducers( set );
                                                   return set;
                                                 } );
    _gosuSourceProducers = LocklessLazyVar.make( () ->
                                                   sourceProducers.stream()
                                                     .map( fqn ->
                                                           {
                                                             try
                                                             {
                                                               Class<?> cls = Class.forName( fqn );
                                                               return (ISourceProducer)cls.newInstance();
                                                             }
                                                             catch( Exception ex )
                                                             {
                                                               throw new RuntimeException( ex );
                                                             }
                                                           } )
                                                     .filter( sp -> sp.getSourceKind() == ISourceProducer.SourceKind.Gosu )
                                                     .collect( Collectors.toSet() ) );
  }

  protected abstract void addBuiltInSourceProducers( Set<ISourceProducer> set );
}
