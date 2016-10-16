package editor;

import java.awt.*;

/**
 */
public class LabScheme extends Scheme
{
  private static final Color CONTROL_LIGHT = Color.white; //UIManager.getColor( "controlLtHighlight" );
  /* colors */            
  public static final Color TOOLTIP_BACKGROUND = new Color( 255, 255, 225 ); //  UIManager.getColor( "info" );
  private static final Color TOOLTIP_TEXT = Color.black; //  UIManager.getColor( "infoText" );
  private static final Color WINDOW = new Color( 252, 252, 252 );
  private static final Color WINDOW_TEXT = Color.black;
  private static final Color WINDOW_BORDER = new Color( 100, 100, 100 );
  private static final Color TEXT_HIGHLIGHT = new Color( 51, 153, 255 );
  private static final Color TEXT_HIGHLIGHT_TEXT = Color.white;
  private static final Color TEXT_TEXT = Color.black;
  private static final Color XP_BORDER_COLOR = new Color( 49, 106, 197 );
  private static final Color XP_HIGHLIGHT_TOGGLE_COLOR = new Color( 225, 230, 232 );
  private static final Color XP_HIGHLIGHT_SELECTED_COLOR = new Color( 152, 179, 219 );
  private static final Color ACTIVE_CAPTION = new Color( 210, 235, 251 );
  private static final Color XP_HIGHLIGHT_COLOR = ACTIVE_CAPTION;//new Color( 190, 205, 224 );
  private static final Color ACTIVE_CAPTION_TEXT = Color.black;

  private static final Color USAGE_READ_HIGHLIGHT_COLOR = new Color( 246, 232, 194 );
  private static final Color USAGE_WRITE_HIGHLIGHT_COLOR = new Color( 255, 201, 247 );
  private static final Color SCOPE_HIGHLIGHT_COLOR = new Color( 200, 210, 255 );

  private static final Color CONTROL = new Color( 240, 240, 240 ); //UIManager.getColor( "control" );
  private static final Color CONTROL_DARKSHADOW = new Color( 105, 105, 105 ); // UIManager.getColor( "controlDkShadow" );
  private static final Color CONTROL_HIGHLIGHT = Color.white; //UIManager.getColor( "controlHighlight" );
  private static final Color CONTROL_LIGHT_SHADOW = new Color( 200, 200, 200 );
  private static final Color CONTROL_SHADOW = new Color( 160, 160, 160 ); //EditorUtilities.CONTROL_SHADOW;
  private static final Color CONTROL_TEXT = Color.black; //UIManager.getColor( "controlText" );

  private static final Color SEPARATOR_1 = CONTROL;
  private static final Color SEPARATOR_2 = CONTROL_LIGHT_SHADOW;

  private static final Color COLOR_BREAKPOINT = new Color( 255, 0, 0, 50 );
  private static final Color COLOR_EXECPOINT = new Color( 0, 255, 0, 50 );
  private static final Color COLOR_FRAMEPOINT = new Color( 128, 128, 128, 50 );

  private static final Color COLOR_ERROR = new Color( 226, 83, 70 );
  private static final Color COLOR_ERROR_SHADOW = new Color( 193, 44, 36 );
  private static final Color COLOR_WARNING = new Color( 255, 240, 0 );
  private static final Color COLOR_WARNING_SHADOW = new Color( 234, 190, 0 );

  
  // Code editor
  private static final Color CODE_WINDOW = WINDOW;
  private static final Color CODE_WINDOW_TEXT = WINDOW_TEXT;
  private static final Color CODE_COMMENT = Color.gray;
  private static final Color CODE_MULTILINE_COMMENT = CODE_COMMENT;
  private static final Color CODE_STRING_LITERAL = new Color( 0, 128, 0 );
  private static final Color CODE_NUMBER_LITERAL = Color.blue;
  private static final Color CODE_KEYWORDS = new Color( 0, 0, 128 );
  private static final Color CODE_ERROR = new Color( 164, 0, 0 );
  private static final Color CODE_WARNING = Color.gray;
  private static final Color CODE_DEPRECATED = CODE_WINDOW_TEXT;
  private static final Color CODE_OPERATOR = CODE_WINDOW_TEXT;
  private static final Color CODE_TYPE_LITERAL = new Color( 0, 75, 0 );
  private static final Color CODE_TYPE_LITERAL_NESTED = CODE_TYPE_LITERAL;

  @Override
  public Color getCodeWindow()
  {
    return CODE_WINDOW;
  }

  @Override
  public Color getCodeWindowText()
  {
    return CODE_WINDOW_TEXT;
  }

  @Override
  public Color getCodeComment()
  {
    return CODE_COMMENT;
  }

  @Override
  public Color getCodeMultilineComment()
  {
    return CODE_MULTILINE_COMMENT;
  }

  @Override
  public Color getCodeStringLiteral()
  {
    return CODE_STRING_LITERAL;
  }

  @Override
  public Color getCodeNumberLiteral()
  {
    return CODE_NUMBER_LITERAL;
  }

  @Override
  public Color getCodeKeyword()
  {
    return CODE_KEYWORDS;
  }

  @Override
  public Color getCodeError()
  {
    return CODE_ERROR;
  }

  @Override
  public Color getCodeWarning()
  {
    return CODE_WARNING;
  }

  @Override
  public Color getCodeDeprecated()
  {
    return CODE_DEPRECATED;
  }

  @Override
  public Color getCodeOperator()
  {
    return CODE_OPERATOR;
  }

  @Override
  public Color getCodeTypeLiteral()
  {
    return CODE_TYPE_LITERAL;
  }

  @Override
  public Color getCodeTypeLiteralNested()
  {
    return CODE_TYPE_LITERAL_NESTED;
  }


  @Override
  public Color getControlLight()
  {
    return CONTROL_LIGHT;
  }

  @Override
  public Color getActiveCaption()
  {
    return ACTIVE_CAPTION;
  }

  @Override
  public Color getXpHighlightColor()
  {
    return XP_HIGHLIGHT_COLOR;
  }

  @Override
  public Color getActiveCaptionText()
  {
    return ACTIVE_CAPTION_TEXT;
  }

  @Override
  public Color getSeparator1()
  {
    return SEPARATOR_1;
  }

  @Override
  public Color getSeparator2()
  {
    return SEPARATOR_2;
  }

  @Override
  public Color getControl()
  {
    return CONTROL;
  }

  @Override
  public Color getControlDarkshadow()
  {
    return CONTROL_DARKSHADOW;
  }

  @Override
  public Color getControlHighlight()
  {
    return CONTROL_HIGHLIGHT;
  }

  @Override
  public Color getControlLigthShadow()
  {
    return CONTROL_LIGHT_SHADOW;
  }

  @Override
  public Color getControlShadow()
  {
    return CONTROL_SHADOW;
  }

  @Override
  public Color getControlText()
  {
    return CONTROL_TEXT;
  }

  @Override
  public Color getTooltipBackground()
  {
    return TOOLTIP_BACKGROUND;
  }

  @Override
  public Color getTooltipText()
  {
    return TOOLTIP_TEXT;
  }

  @Override
  public Color getWindow()
  {
    return WINDOW;
  }

  @Override
  public Color getWindowText()
  {
    return WINDOW_TEXT;
  }

  @Override
  public Color getWindowBorder()
  {
    return WINDOW_BORDER;
  }

  @Override
  public Color getTextHighlight()
  {
    return TEXT_HIGHLIGHT;
  }

  @Override
  public Color getTextHighlightText()
  {
    return TEXT_HIGHLIGHT_TEXT;
  }

  @Override
  public Color getTextText()
  {
    return TEXT_TEXT;
  }

  @Override
  public Color getXpBorderColor()
  {
    return XP_BORDER_COLOR;
  }

  @Override
  public Color getXpHighlightToggleColor()
  {
    return XP_HIGHLIGHT_TOGGLE_COLOR;
  }

  @Override
  public Color getXpHighlightSelectedColor()
  {
    return XP_HIGHLIGHT_SELECTED_COLOR;
  }

  @Override
  public Color getBreakpointColor()
  {
    return COLOR_BREAKPOINT;
  }

  @Override
  public Color getExecBreakpoint()
  {
    return COLOR_EXECPOINT;
  }

  @Override
  public Color getFrameBreakpoint()
  {
    return COLOR_FRAMEPOINT;
  }

  @Override
  public Color getColorError()
  {
    return COLOR_ERROR;
  }

  @Override
  public Color getColorErrorShadow()
  {
    return COLOR_ERROR_SHADOW;
  }

  @Override
  public Color getColorWarning()
  {
    return COLOR_WARNING;
  }

  @Override
  public Color getColorWarningShadow()
  {
    return COLOR_WARNING_SHADOW;
  }

  @Override
  public Color getMenu()
  {
    return CONTROL;
  }

  @Override
  public Color getMenuText()
  {
    return CONTROL_TEXT;
  }

  @Override
  public Color getMenuBorder()
  {
    return CONTROL_SHADOW;
  }

  @Override
  public Color getScrollbarBorderColor()
  {
    return getControlLigthShadow();
  }

  @Override
  public Color getButtonBorderColor()
  {
    return getControlDarkshadow();
  }

  @Override
  public Color usageReadHighlightColor()
  {
    return USAGE_READ_HIGHLIGHT_COLOR;
  }

  @Override
  public Color usageWriteHighlightColor()
  {
    return USAGE_WRITE_HIGHLIGHT_COLOR;
  }

  @Override
  public Color scopeHighlightColor()
  {
    return SCOPE_HIGHLIGHT_COLOR;
  }
}
