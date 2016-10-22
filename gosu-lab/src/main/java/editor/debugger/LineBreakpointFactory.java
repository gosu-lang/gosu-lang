package editor.debugger;

import javax.swing.*;

/**
 */
public class LineBreakpointFactory implements IBreakpointFactory
{
  private static final LineBreakpointFactory INSTANCE = new LineBreakpointFactory();

  public static LineBreakpointFactory instance()
  {
    return INSTANCE;
  }

  @Override
  public String getName()
  {
    return "Line Breakpoints";
  }

  @Override
  public Icon getIcon()
  {
    return null;
  }
}
