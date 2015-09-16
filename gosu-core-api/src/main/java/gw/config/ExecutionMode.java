package gw.config;

import java.lang.management.ManagementFactory;

public enum ExecutionMode {
  // support refresh at RUNTIME only if VM is in debug mode
  RUNTIME( ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains( "jdwp" ) ),
  IDE( true ),
  COMPILER( false );

  private static ExecutionMode _mode;

  private boolean _bSupportsRefresh;

  ExecutionMode( boolean bSupportsRefresh ) {
    _bSupportsRefresh = checkCommandLineOverride( bSupportsRefresh );
  }

  private boolean checkCommandLineOverride( boolean bDefault )
  {
    String refreshProperty = System.getProperty( "gosu.refresh" );
    if( refreshProperty != null ) {
      return Boolean.valueOf( refreshProperty );
    }
    return bDefault;
  }

  /**
   * @return true if the type system supports type redefinition for a given mode.
   */
  public boolean isRefreshSupportEnabled() {
    return _bSupportsRefresh;
  }

  public static ExecutionMode get() {
    return _mode == null ? (_mode = CommonServices.getPlatformHelper().getExecutionMode()) : _mode;
  }
  public static void clear() {
    _mode = null;
  }

  public static boolean isRuntime() {
    return get() == RUNTIME;
  }

  public static boolean isIDE() {
    return get() == IDE;
  }

  public static boolean isCompiler() {
    return get() == COMPILER;
  }
}
