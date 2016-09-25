package editor;

import java.awt.*;

/**
 */
public class LabScheme extends Scheme
{
  static final Color COLOR_BREAKPOINT = new Color( 255, 0, 0, 50 );
  static final Color COLOR_EXECPOINT = new Color( 0, 255, 0, 50 );
  static final Color COLOR_FRAMEPOINT = new Color( 128, 128, 128, 50 );

  @Override
  Color breakpointColor()
  {
    return COLOR_BREAKPOINT;
  }

  @Override
  Color getExecBreakpoint()
  {
    return COLOR_EXECPOINT;
  }

  @Override
  Color getFrameBreakpoint()
  {
    return COLOR_FRAMEPOINT;
  }
}
