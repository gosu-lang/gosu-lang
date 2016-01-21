package editor.util.filewatcher;

public class FileChangeInfo
{

  private final FileChangeType _fileChangeType;
  private final Long _lastModified;
  private final Long _fileLength;

  public FileChangeInfo( FileChangeType fileChangeType, Long lastModified, Long fileLength )
  {
    _fileChangeType = fileChangeType;
    _lastModified = lastModified;
    _fileLength = fileLength;
  }

  public FileChangeType getFileChangeType()
  {
    return _fileChangeType;
  }

  public Long getLastModified()
  {
    return _lastModified;
  }

  public Long getFileLength()
  {
    return _fileLength;
  }

}
