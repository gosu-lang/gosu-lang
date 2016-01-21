package editor.util.filewatcher;

public interface IFileHandler
{

  public boolean filter( String path );

  public void process( String path, boolean isDir, long timestamp, long length );

}
