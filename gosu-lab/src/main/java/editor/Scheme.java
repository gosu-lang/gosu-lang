package editor;

import editor.settings.AppearanceSettings;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 */
public abstract class Scheme
{
  public static final Map<String, Class<? extends Scheme>> SCHEMES_BY_NAME = new HashMap<>();
  static
  {
    SCHEMES_BY_NAME.put( LabScheme.NAME, LabScheme.class );
    SCHEMES_BY_NAME.put( LabDarkScheme.NAME, LabDarkScheme.class );
  }

  private static Scheme _active = null;

  public static Scheme active()
  {
    if( _active == null )
    {
      try
      {
        _active = SCHEMES_BY_NAME.get( AppearanceSettings.getTheme() ).newInstance();
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return _active;
  }

  public abstract boolean isDark();

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
  public abstract Color usageReadHighlightShadowColor();
  public abstract Color usageWriteHighlightColor();
  public abstract Color usageWriteHighlightShadowColor();

  public abstract Color scopeHighlightColor();

  public abstract Color getFieldBorderColor();

  public abstract Color debugVarRedText();
  public abstract Color debugVarGreenText();
  public abstract Color debugVarBlueText();
}
