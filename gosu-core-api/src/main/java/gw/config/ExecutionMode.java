package gw.config;

public enum ExecutionMode {
  RUNTIME,
  IDE,
  COMPILER;

  public static boolean isRuntime() {
    return CommonServices.getPlatformHelper().getExecutionMode() == RUNTIME;
  }

  public static boolean isIDE() {
    return CommonServices.getPlatformHelper().getExecutionMode() == IDE;
  }

  public static boolean isCompiler() {
    return CommonServices.getPlatformHelper().getExecutionMode() == COMPILER;
  }
}
