package editor.debugger;

import javax.swing.*;

/**
 */
public class ExceptionBreakpointFactory implements IBreakpointFactory
{
  private static final ExceptionBreakpointFactory INSTANCE = new ExceptionBreakpointFactory();

  public static ExceptionBreakpointFactory instance()
  {
    return INSTANCE;
  }

  @Override
  public String getName()
  {
    return "Exception Breakpoints";
  }

  @Override
  public Icon getIcon()
  {
    return null;
  }
}
