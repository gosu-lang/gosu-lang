package editor;

import gw.config.AbstractPlatformHelper;
import gw.config.ExecutionMode;
import gw.lang.reflect.module.IModule;

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
  public void refresh( IModule module )
  {
  }
}
