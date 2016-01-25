package editor;

import editor.util.Project;
import gw.util.concurrent.ConcurrentWeakValueHashMap;

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
  private Project _project;
  private ConcurrentWeakValueHashMap<String, IFileWatcherListener> _listeners;
  private Map<WatchKey,String> _keyToPath;
  private boolean _disposed;

  public static FileWatcher instance( Project project )
  {
    if( FILE_WATCHER != null && FILE_WATCHER._project != project )
    {
      FILE_WATCHER.dispose();
    }
    if( FILE_WATCHER == null )
    {
      FILE_WATCHER = new FileWatcher( project );
    }

    return FILE_WATCHER;
  }

  private FileWatcher( Project project )
  {
    _project = project;
    _listeners = new ConcurrentWeakValueHashMap<>();
    _keyToPath = new ConcurrentHashMap<>();
    Path path = new File( project.getSourcePath().get( 0 ) ).toPath();
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
      File fileOrDir = fileTree.getFileOrDir();
      WatchKey key = fileOrDir.toPath().register( _watcher,
                                                  StandardWatchEventKinds.ENTRY_CREATE,
                                                  StandardWatchEventKinds.ENTRY_DELETE );
      _keyToPath.put( key, fileOrDir.getAbsolutePath() );
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
      if( path.equals( fileTree.getFileOrDir().getAbsolutePath() ) )
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
            fireCreate( dirPath, ((Path)event.context()).getFileName() );
          }
          else if( event.kind() == StandardWatchEventKinds.ENTRY_DELETE )
          {
            fireDelete( dirPath, ((Path)event.context()).getFileName() );
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
    _listeners.put( fileTree.getFileOrDir().getAbsolutePath(), fileTree );
  }
  private void removeListener( FileTree fileTree )
  {
    _listeners.remove( fileTree.getFileOrDir().getAbsolutePath() );
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
}
