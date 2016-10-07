package editor;


/**
 */
public interface IFileWatcherListener
{
  void fireCreate( String dir, String file );
  void fireDelete( String dir, String file );
  void fireModify( String dir, String file );

  void setLastModified();
}
