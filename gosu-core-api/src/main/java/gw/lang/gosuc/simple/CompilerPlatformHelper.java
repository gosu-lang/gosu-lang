package gw.lang.gosuc.simple;

import gw.config.AbstractPlatformHelper;
import gw.config.ExecutionMode;
import gw.lang.reflect.module.IModule;

public class CompilerPlatformHelper extends AbstractPlatformHelper {

  @Override
  public ExecutionMode getExecutionMode() {
    return ExecutionMode.COMPILER;
  }

  @Override
  public boolean shouldCacheTypeNames() {
    return false;
  }

  @Override
  public void refresh(IModule module) {
  }
}
