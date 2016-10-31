package editor;

import java.io.File;

/**
 */
public class ExternalFileTree extends FileTree
{
  private String _fqn;


  public ExternalFileTree( File fileOrDir, String fqn )
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
