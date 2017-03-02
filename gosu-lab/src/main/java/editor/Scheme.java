package editor;

import java.awt.*;

/**
 */
public abstract class Scheme
{
  private static Scheme _active = new LabScheme();
 // private static Scheme _active = new LabDarkScheme();

  public static Scheme active()
  {
    return _active;
  }

  public abstract Color getCodeWindow();

  public abstract Color getCodeWindowText();

  public abstract Color getCodeComment();

  public abstract Color getCodeMultilineComment();

  public abstract Color getCodeStringLiteral();

  public abstract Color getCodeNumberLiteral();

  public abstract Color getCodeKeyword();

  public abstract Color getCodeError();

  public abstract Color getCodeWarning();

  public abstract Color getCodeDeprecated();

  public abstract Color getCodeOperator();

  public abstract Color getCodeTypeLiteral();

  public abstract Color getCodeTypeLiteralNested();

  public abstract Color getControlLight();

  public abstract Color getActiveCaption();

  public abstract Color getXpHighlightColor();

  public abstract Color getActiveCaptionText();

  public abstract Color getSeparator1();
  public abstract Color getSeparator2();

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

  public abstract Color getMenu();
  public abstract Color getMenuText();
  public abstract Color getMenuBorder();

  public abstract Color getScrollbarBorderColor();

  public abstract Color getButtonBorderColor();

  public abstract Color usageReadHighlightColor();
  public abstract Color usageWriteHighlightColor();

  public abstract Color scopeHighlightColor();
}
