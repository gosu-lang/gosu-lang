package editor.util.filewatcher;

import java.net.URI;
import java.util.Map;

public interface IFileChangeListener
{

  void filesChanged( Map<URI, FileChangeInfo> changedFiles );

  void fileScanComplete();

}
