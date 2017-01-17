package editor;

import java.nio.file.Path;

/**
 */
public class ExternalFileTree extends FileTree
{
  private String _fqn;


  public ExternalFileTree( Path fileOrDir, String fqn )
  {
    super( fileOrDir, null, LabFrame.instance().getGosuPanel().getExperiment() );
    _fqn = fqn;
  }

  @Override
  public String makeFqn()
  {
    return _fqn;
  }
}
