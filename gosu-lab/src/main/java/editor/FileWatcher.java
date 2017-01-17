package editor;

import editor.util.Experiment;
import gw.util.PathUtil;
import gw.util.concurrent.ConcurrentWeakValueHashMap;

import java.awt.*;
import java.io.File;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 */
public class FileWatcher implements Runnable
{
  private static FileWatcher FILE_WATCHER = null;

  private WatchService _watcher;
  private Experiment _experiment;
  private ConcurrentWeakValueHashMap<String, IFileWatcherListener> _listeners;
  private Map<WatchKey,String> _keyToPath;
  private boolean _disposed;

  public static FileWatcher instance( Experiment experiment )
  {
    if( FILE_WATCHER != null && FILE_WATCHER._experiment != experiment )
    {
      FILE_WATCHER.dispose();
    }
    if( FILE_WATCHER == null )
    {
      FILE_WATCHER = new FileWatcher( experiment );
    }

    return FILE_WATCHER;
  }

  private FileWatcher( Experiment experiment )
  {
    _experiment = experiment;
    _listeners = new ConcurrentWeakValueHashMap<>();
    _keyToPath = new ConcurrentHashMap<>();
    Path path = PathUtil.create( experiment.getSourcePath().get( 0 ) );
    try
    {
      _watcher = path.getFileSystem().newWatchService();
      Executors.newSingleThreadExecutor().execute( this );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public void register( FileTree fileTree )
  {
    if( !fileTree.isDirectory() )
    {
      throw new IllegalStateException( "Expecting a directory" );
    }

    try
    {
      Path fileOrDir = fileTree.getFileOrDir();
      WatchKey key = fileOrDir.register( _watcher,
                                         StandardWatchEventKinds.ENTRY_CREATE,
                                         StandardWatchEventKinds.ENTRY_DELETE,
                                         StandardWatchEventKinds.ENTRY_MODIFY );
      _keyToPath.put( key, PathUtil.getAbsolutePathName( fileOrDir ) );
      addListener( fileTree );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public void unregister( FileTree fileTree )
  {
    if( !fileTree.isDirectory() )
    {
      throw new IllegalStateException( "Expecting a directory" );
    }

    removeListener( fileTree );
    for( WatchKey key: _keyToPath.keySet() )
    {
      String path = _keyToPath.get( key );
      if( path.equals( PathUtil.getAbsolutePathName( fileTree.getFileOrDir() ) ) )
      {
        _keyToPath.remove( key );
      }
    }
  }

  private void dispose()
  {
    try
    {
      _disposed = true;
      _watcher.close();
      FILE_WATCHER = null;
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public void run()
  {
    try
    {
      for( WatchKey key = _watcher.take(); !_disposed && key != null; key = _watcher.take() )
      {
        for( WatchEvent event: key.pollEvents() )
        {
          String dirPath = _keyToPath.get( key );
          if( event.kind() == StandardWatchEventKinds.ENTRY_CREATE )
          {
            EventQueue.invokeLater( () ->
             fireCreate( dirPath, (Path)event.context() ) );
          }
          else if( event.kind() == StandardWatchEventKinds.ENTRY_DELETE )
          {
            EventQueue.invokeLater( () ->
              fireDelete( dirPath, (Path)event.context() ) );
          }
          else if( event.kind() == StandardWatchEventKinds.ENTRY_MODIFY )
          {
            EventQueue.invokeLater( () -> 
              fireModify( dirPath, (Path)event.context() ) );
          }
        }
        key.reset();
      }
    }
    catch( ClosedWatchServiceException cw )
    {
      if( !_disposed )
      {
        throw new IllegalStateException( "Unexpected close file watcher, not disposed" );
      }
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private void addListener( FileTree fileTree )
  {
    _listeners.put( PathUtil.getAbsolutePathName( fileTree.getFileOrDir() ), fileTree );
  }
  private void removeListener( FileTree fileTree )
  {
    _listeners.remove( PathUtil.getAbsolutePathName( fileTree.getFileOrDir() ) );
  }

  private void fireDelete( String dirPath, Path fileName )
  {
    String dir = new File( dirPath ).getAbsolutePath();
    IFileWatcherListener listener = _listeners.get( dir );
    listener.fireDelete( dir, fileName.toString() );

    //System.out.println( "Delete: " + dirPath + " : " + fileName );
  }

  private void fireCreate( String dirPath, Path fileName )
  {
    String dir = new File( dirPath ).getAbsolutePath();
    IFileWatcherListener listener = _listeners.get( dir );
    listener.fireCreate( dir, fileName.toString() );

    //System.out.println( "Create: " + dirPath + " : " + fileName );
  }
  
  private void fireModify( String dirPath, Path fileName )
  {
    String dir = new File( dirPath ).getAbsolutePath();
    IFileWatcherListener listener = _listeners.get( dir );
    listener.fireModify( dir, fileName.toString() );

    //System.out.println( "Modify: " + dirPath + " : " + fileName );
  }
}
