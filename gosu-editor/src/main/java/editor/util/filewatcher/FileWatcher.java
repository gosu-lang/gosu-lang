package editor.util.filewatcher;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.lang.UnstableAPI;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.util.GosuObjectUtil;
import gw.util.Pair;

import javax.swing.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@UnstableAPI
public class FileWatcher
{

  private static final FileWatcher INSTANCE = new FileWatcher();
  private static boolean _SCAN_ENTIRE = true;

  private final Map<URI, Pair<Long, Long>> _currentState = new HashMap<URI, Pair<Long, Long>>();
  private WeakHashMap<IFileChangeListener, Object> _listeners = new WeakHashMap<IFileChangeListener, Object>();
  private boolean _watcherThreadStarted;
  private volatile boolean _checkNow;

  public static FileWatcher instance()
  {
    return INSTANCE;
  }

  private FileWatcher()
  {
  }

  public void setScanEntire( boolean scanEntire )
  {
    _SCAN_ENTIRE = scanEntire;
  }

  public void scanForChangesAndNotify()
  {
    maybeStartWatcherThread();
    synchronized( FileWatcher.this )
    {
      _checkNow = true;
      FileWatcher.this.notifyAll();
    }
  }

  private void maybeStartWatcherThread()
  {
    synchronized( FileWatcher.this )
    {
      if( _watcherThreadStarted )
      {
        return;
      }
      _watcherThreadStarted = true;
    }
    Thread watcherThread = new Thread( "File Watcher Thread" )
    {
      public void run()
      {
        boolean firstRun = true;
        try
        {
          MAINLOOP:
          //noinspection InfiniteLoopStatement
          while( true )
          {
            synchronized( FileWatcher.this )
            {
              while( !_checkNow )
              {
                FileWatcher.this.wait();
              }
              _checkNow = false;
            }
            Map<URI, FileChangeInfo> changedFiles = new HashMap<URI, FileChangeInfo>();
            //noinspection LoopStatementThatDoesntLoop
            while( true )
            {
              try
              {
                Thread.sleep( 1000 );
              }
              catch( InterruptedException e )
              {
                break;
              }
              Set<URI> allFiles = new HashSet<URI>();
              synchronized( _currentState )
              {
                scanForChanges( changedFiles, allFiles );
                if( firstRun )
                {
                  firstRun = false;
                  continue MAINLOOP;
                }
                Iterator<URI> iter = _currentState.keySet().iterator();
                while( iter.hasNext() )
                {
                  URI uri = iter.next();
                  if( !allFiles.contains( uri ) )
                  {
                    // file was deleted
                    iter.remove();
                    changedFiles.put( uri, new FileChangeInfo( FileChangeType.DELETED, null, null ) );
                  }
                }
              }
              if( !changedFiles.isEmpty() )
              {
//                scanForChangesAndNotify(); // run algorithm again
//                for ( Map.Entry<URI, FileChangeInfo> changedFile : changedFiles.entrySet() ) {
//                  System.out.println( "*** " + changedFile.getValue().getFileChangeType() + ": " + changedFile.getKey() );
//                }
                CommonServices.getFileSystem().clearAllCaches();
                TypeSystem.lock();
                try
                {
                  for( IFileChangeListener listener : _listeners.keySet() )
                  {
                    listener.filesChanged( changedFiles );
                    listener.fileScanComplete();
                  }
                }
                finally
                {
                  TypeSystem.unlock();
                }
              }
              break;
            }
            for( IFileChangeListener listener : _listeners.keySet() )
            {
              listener.fileScanComplete();
            }
          }
        }
        catch( Throwable throwable )
        {
          throwable.printStackTrace();
        }
      }
    };
    watcherThread.setDaemon( true );
    watcherThread.start();
  }


  private void scanForChanges( Map<URI, FileChangeInfo> changedFiles, Set<URI> allFiles )
  {
    boolean debug = CommonServices.getEntityAccess().getLogger().isDebugEnabled();
    long start = debug ? System.nanoTime() : 0;

    for( IModule module : TypeSystem.getExecutionEnvironment().getModules() )
    {
      scanForChanges( module, changedFiles, allFiles );
    }

    if( debug )
    {
      long end = System.nanoTime();
      long delta = (end - start + 500000) / 1000000;
      CommonServices.getEntityAccess().getLogger().debug( "FileWatcher.scanForChanges() scan took " + delta + "ms" );
    }
  }

  private void scanForChanges( IModule module, Map<URI, FileChangeInfo> changedFiles, Set<URI> allFiles )
  {
    WatcherHandler handler = new WatcherHandler( changedFiles, allFiles );
//## todo: need to implement utilities to replace module.getResourceAccess().getRoots() etc.
//    if(_SCAN_ENTIRE) {
//      for ( IDirectory root : module.getResourceAccess().getRoots() ) {
//        scanForChanges(root, handler);
//      }
//    } else {
//      // Optimization for Studio.  Should encompass all areas containing reloadable files that could be modified.
//      for ( IDirectory root : module.getResourceAccess().getSourceEntries() ) {
//        scanForChanges(root, handler);
//      }
//
//      for ( IDirectory root : module.getResourceAccess().getRoots() ) {
//        if ( root.isJavaFile() ) {
//          scanForChanges(root.dir("config"), handler);
//        }
//      }
//    }
  }

  private void scanForChanges( IDirectory root, IFileHandler handler )
  {
    if( root == null )
    {
      return;
    }

    if( !root.isJavaFile() )
    {
      // TODO: store checksum and contents of containing jar file
      return;
    }

    // the root "directory" of a jar is actually considered a java file, whereas it's subdirectories are not
    File dir = root.toJavaFile();
    if( dir.isDirectory() )
    {
      String path = dir.getAbsolutePath();
      if( handler.filter( path ) )
      {
        if( NativeFileSupport.isEnabled() )
        {
          try
          {
            NativeFileSupport.nativeFindFiles( path, handler );
          }
          catch( Throwable t )
          {
            NativeFileSupport.disable();
            CommonServices.getEntityAccess().getLogger().info( "Unexpected exception during native execution.  " +
                                                               "NativeFileSupport disabled for session.", t );
            // TODO Better to detect remaining work and finish it in pure java impl but this will work.
            scanForChangesImpl( path, handler );
          }
        }
        else
        {
          scanForChangesImpl( path, handler );
        }
      }
    }
  }

  private void scanForChangesImpl( String parentPath, IFileHandler handler )
  {
    for( File file : new File( parentPath ).listFiles() )
    {
      String childPath = file.getAbsolutePath();
      if( handler.filter( childPath ) )
      {
        boolean isDir = file.isDirectory();
        if( isDir )
        {
          scanForChangesImpl( childPath, handler );
        }
        handler.process( childPath, isDir, file.lastModified(), file.length() );
      }
    }
  }

  private class WatcherHandler implements IFileHandler
  {

    private Map<URI, FileChangeInfo> _changedFiles;
    private Set<URI> _allFiles;

    public WatcherHandler( Map<URI, FileChangeInfo> changedFiles, Set<URI> allFiles )
    {
      _changedFiles = changedFiles;
      _allFiles = allFiles;
    }

    @Override
    public boolean filter( String path )
    {
      return true;
    }

    @Override
    public void process( String path, boolean isDir, long timestamp, long length )
    {
      File file = new File( path );
      if( isDir )
      {
        // TODO does anything need to happen here?
      }
      else
      {
        // do not use File.toURI() here -- too slow.
        URI uri = toURI( file, isDir );
        _allFiles.add( uri );
        Pair<Long, Long> currentStateForFile = _currentState.get( uri );
        Pair<Long, Long> newState = new Pair<Long, Long>( length, timestamp );
        if( !GosuObjectUtil.equals( currentStateForFile, newState ) )
        {
          _currentState.put( uri, newState );
          _changedFiles.put( uri, new FileChangeInfo( currentStateForFile == null ? FileChangeType.ADDED : FileChangeType.CHANGED, newState.getFirst(), newState.getSecond() ) );
        }
      }
    }
  }

  private URI toURI( File file, boolean isDir )
  {
    try
    {
      File f = file.getAbsoluteFile();
      String sp = slashify( f.getPath(), isDir );
      if( sp.startsWith( "//" ) )
      {
        sp = "//" + sp;
      }
      return new URI( "file", null, sp, null );
    }
    catch( URISyntaxException x )
    {
      throw new Error( x );    // Can't happen
    }
  }

  private static String slashify( String path, boolean isDirectory )
  {
    String p = path;
    if( File.separatorChar != '/' )
    {
      p = p.replace( File.separatorChar, '/' );
    }
    if( !p.startsWith( "/" ) )
    {
      p = "/" + p;
    }
    if( !p.endsWith( "/" ) && isDirectory )
    {
      p = p + "/";
    }
    return p;
  }

  // Do not call this from inside WatcherHandler.
  private Pair<Long, Long> getFileState( URI uri )
  {
    File file = new File( uri );
    if( file.exists() )
    {
      long currentLength = file.length();
      long modificationTime = file.lastModified();
      return new Pair<Long, Long>( currentLength, modificationTime );
    }
    else
    {
      return new Pair<Long, Long>( null, null );
    }
  }

  public void addFileChangeListenerAsWeakRef( IFileChangeListener listener )
  {
    TypeSystem.lock();
    try
    {
      _listeners.put( listener, null );
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public void removeFileChangeListener( IFileChangeListener listener )
  {
    TypeSystem.lock();
    try
    {
      _listeners.remove( listener );
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public void markAsCurrent( File file )
  {
    synchronized( _currentState )
    {
      URI fileUri = file.getAbsoluteFile().toURI();
      Pair<Long, Long> fileState = getFileState( fileUri );
      if( fileState.getFirst() == null )
      {
        _currentState.remove( fileUri );
      }
      else
      {
        _currentState.put( fileUri, fileState );
      }
    }
  }

  public void markAsCurrent( File... files )
  {
    markAsCurrent( Arrays.asList( files ) );
  }

  public void markAsCurrent( final List<File> files )
  {
    if( SwingUtilities.isEventDispatchThread() )
    {
      final Map<URI, Pair<Long, Long>> fileStates = collectFileStates( files );
      Thread bgmarker = new Thread( new Runnable()
      {
        public void run()
        {
          markCurrent( fileStates );
        }
      } );
      bgmarker.setDaemon( true );
      bgmarker.start();
    }
    else
    {
      markCurrent( collectFileStates( files ) );
    }
  }

  private void markCurrent( Map<URI, Pair<Long, Long>> fileStates )
  {
    synchronized( _currentState )
    {
      for( Map.Entry<URI, Pair<Long, Long>> fileEntry : fileStates.entrySet() )
      {
        URI fileUri = fileEntry.getKey();
        Pair<Long, Long> fileState = fileEntry.getValue();
        if( fileState.getFirst() == null )
        {
          _currentState.remove( fileUri );
        }
        else
        {
          _currentState.put( fileUri, fileState );
        }
      }
    }
  }

  private Map<URI, Pair<Long, Long>> collectFileStates( List<File> files )
  {
    if( files != null && files.size() > 0 )
    {
      Map<URI, Pair<Long, Long>> fileStates = new HashMap<URI, Pair<Long, Long>>( files.size() * 2 );
      for( File file : files )
      {
        URI fileUri = file.getAbsoluteFile().toURI();
        fileStates.put( fileUri, getFileState( fileUri ) );
      }
      return fileStates;
    }
    return Collections.emptyMap();
  }
}
