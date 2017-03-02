package editor;

import editor.util.Experiment;

import java.io.File;

/**
 */
public class ExternalFileTree extends FileTree
{
  private String _fqn;


  public ExternalFileTree( File fileOrDir, String fqn )
  {
    super( fileOrDir, null, RunMe.getEditorFrame().getGosuPanel().getExperiment() );
    _fqn = fqn;
  }

  @Override
  protected String makeFqn()
  {
    return _fqn;
  }
}
