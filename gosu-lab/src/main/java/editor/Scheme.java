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

  public abstract Color getControlLight();

  public abstract Color getActiveCaption();

  public abstract Color getXpHighlightColor();

  public abstract Color getActiveCaptionText();

  public abstract Color getControl();

  public abstract Color getControlDarkshadow();

  public abstract Color getControlHighlight();

  public abstract Color getControlLigthShadow();

  public abstract Color getControlShadow();

  public abstract Color getControlText();

  public abstract Color getTooltipBackground();

  public abstract Color getTooltipText();

  public abstract Color getWindow();

  public abstract Color getWindowText();

  public abstract Color getWindowBorder();

  public abstract Color getTextHighlight();

  public abstract Color getTextHighlightText();

  public abstract Color getTextText();

  public abstract Color getXpBorderColor();

  public abstract Color getXpHighlightToggleColor();

  public abstract Color getXpHighlightSelectedColor();

  public abstract Color getBreakpointColor();
  public abstract Color getExecBreakpoint();
  public abstract Color getFrameBreakpoint();

  public abstract Color getColorError();
  public abstract Color getColorErrorShadow();
  public abstract Color getColorWarning();
  public abstract Color getColorWarningShadow();
}
