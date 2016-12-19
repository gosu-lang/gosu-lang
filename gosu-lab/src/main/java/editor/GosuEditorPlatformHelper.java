package editor;

import editor.util.Experiment;
import gw.config.AbstractPlatformHelper;
import gw.config.ExecutionMode;
import gw.lang.reflect.module.IModule;
import java.io.File;

/**
 */
public class GosuEditorPlatformHelper extends AbstractPlatformHelper
{
  @Override
  public ExecutionMode getExecutionMode()
  {
    return ExecutionMode.IDE;
  }

  @Override
  public boolean shouldCacheTypeNames()
  {
    return false;
  }

  @Override
  public File getIndexFile( String id )
  {
    final File indexPath = LabFrame.getIndexDir();
    File dir = new File( indexPath, "gosutypenames" );
    if( !dir.exists() )
    {
      //noinspection ResultOfMethodCallIgnored
      dir.mkdir();
    }
    Experiment experiment = LabFrame.instance().getGosuPanel().getExperiment();
    String projectLocationId = Integer.toHexString( experiment.getOrMakeExperimentFile().getAbsolutePath().hashCode() );
    String projectId = experiment.getName().replace( ' ', '_' ) + "$" + projectLocationId;
    return new File( dir, projectId + "$" + id + "$index.txt" );
  }

  @Override
  public void refresh( IModule module )
  {
  }
}
