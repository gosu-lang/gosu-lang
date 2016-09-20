package editor;

import java.awt.*;

/**
 */
public abstract class Scheme
{
  private static Scheme _active = new LabScheme();

  public static Scheme active()
  {
    return _active;
  }

  abstract Color breakpointColor();
  abstract Color getExecBreakpoint();

}
