/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.EditorUtilities;
import editor.util.Pair;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.exceptions.ParseWarningForDeprecatedMember;
import gw.util.GosuObjectUtil;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabExpander;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.WrappedPlainView;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of styles used to render gosu source.
 */
public class GosuStyleContext extends StyleContext implements ViewFactory
{
  public static final int KEY_WORD = -99;
  public static final int PARSE_ERROR = -98;
  public static final int DEPRECATED = -97;
  public static final int PARSE_WARNING = -96;
  public static final int TYPE_LITERAL = -95;
  public static final int NESTED_TYPE_LITERAL = -94;
  public static final int METHOD_CALL = -93;
  public static final int USES = -92;
  public static final int DEFAULT = -91;
  public static final int FIELD_ERROR = -90;
  public static final int FIELD_WARNING = -89;
  public static final int PACKAGE = -88;
  public static final int PROPERTY = -87;
  public static final int ENHANCEMENT_METHOD_CALL = -86;
  public static final int ENHANCEMENT_PROPERTY = -85;

  public static final Integer KEY_WORD_KEY = new Integer( KEY_WORD );
  public static final Integer PARSE_ERROR_KEY = new Integer( PARSE_ERROR );
  public static final Integer DEPRECATED_KEY = new Integer( DEPRECATED );
  public static final Integer TYPE_LITERAL_KEY = new Integer( TYPE_LITERAL );
  public static final Integer NESTED_TYPE_LITERAL_KEY = new Integer( NESTED_TYPE_LITERAL );
  public static final Integer PARSE_WARNING_KEY = new Integer( PARSE_WARNING );
  public static final Integer METHOD_CALL_KEY = new Integer( METHOD_CALL );
  public static final Integer PROPERTY_KEY = new Integer( PROPERTY );
  public static final Integer ENHANCEMENT_METHOD_CALL_KEY = new Integer( ENHANCEMENT_METHOD_CALL );
  public static final Integer ENHANCEMENT_PROPERTY_KEY = new Integer( ENHANCEMENT_PROPERTY );
  public static final Integer USES_KEY = new Integer( USES );
  public static final Integer PACKAGE_KEY = new Integer( PACKAGE );
  public static final Integer FIELD_ERROR_KEY = new Integer( FIELD_ERROR );
  public static final Integer FIELD_WARNING_KEY = new Integer( FIELD_WARNING );
  public static final String DASHED = "_dashed";
  private static final Component THISISSTUPID = new Component()
  {
  };
  private static String g_defFontFamily =  EditorUtilities.getFontFamilyOrDefault( "Consolas", "Monospaced" );
  private static int g_defFontSize = 12;

  public static final String STYLE_EOL = "EOL";
  public static final String STYLE_EOF = "EOF";
  public static final String STYLE_Whitespace = "Whitespace";
  public static final String STYLE_Comment = "Comment";
  public static final String STYLE_Caret = "Caret";
  public static final String STYLE_Number = "Number";
  public static final String STYLE_Integer = "Integer";
  public static final String STYLE_Word = "Word";
  public static final String STYLE_Operator = "Operator";
  public static final String STYLE_StringLiteral = "StringLiteral";
  public static final String STYLE_KeyWord = "KeyWord";
  public static final String STYLE_ParseError = "ParseError";
  public static final String STYLE_ParseWarning = "ParseWarning";
  public static final String STYLE_DeprecatedMember = "DeprecatedMember";
  public static final String STYLE_TypeLiteral = "ITypeLiteralExpression";
  public static final String STYLE_NestedTypeLiteral = "NestedTypeLiteral";
  public static final String STYLE_MethodCall = "MethodCall";
  public static final String STYLE_Property = "Property";
  public static final String STYLE_EnhancementMethodCall = "EnhancementMethodCall";
  public static final String STYLE_EnhancementProperty = "EnhancementProperty";
  public static final String STYLE_FieldError = "FieldError";
  public static final String STYLE_FieldWarning = "FieldWarning";

  private String _strFontFamily;
  private int _iFontSize;
  private HashMap<AttributeSet, Font> _fontCache;
  private HashMap<Integer, Style> _tokenStyles;

  /**
   * Constructs a set of styles to represent gosu lexical tokens.
   */
  public GosuStyleContext()
  {
    super();
    Style root = getStyle( DEFAULT_STYLE );
    _tokenStyles = new HashMap<>();

    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_EOL ), addStyle( STYLE_EOL, root ) );
    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_EOF ), addStyle( STYLE_EOF, root ) );
    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_WHITESPACE ), addStyle( STYLE_Whitespace, root ) );
    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_COMMENT ), addStyle( STYLE_Comment, root ) );
    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_NUMBER ), addStyle( STYLE_Number, root ) );
    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_INTEGER ), addStyle( STYLE_Integer, root ) );
    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_WORD ), addStyle( STYLE_Word, root ) );
    _tokenStyles.put( new Integer( ISourceCodeTokenizer.TT_OPERATOR ), addStyle( STYLE_Operator, root ) );
    _tokenStyles.put( new Integer( (int)'"' ), addStyle( STYLE_StringLiteral, root ) );
    _tokenStyles.put( (int)'\'', addStyle( STYLE_StringLiteral, root ) );
    _tokenStyles.put( KEY_WORD_KEY, addStyle( STYLE_KeyWord, root ) );
    _tokenStyles.put( PARSE_ERROR_KEY, addStyle( STYLE_ParseError, root ) );
    _tokenStyles.put( PARSE_WARNING_KEY, addStyle( STYLE_ParseWarning, root ) );
    _tokenStyles.put( DEPRECATED_KEY, addStyle( STYLE_DeprecatedMember, root ) );
    _tokenStyles.put( TYPE_LITERAL_KEY, addStyle( STYLE_TypeLiteral, root ) );
    _tokenStyles.put( NESTED_TYPE_LITERAL_KEY, addStyle( STYLE_NestedTypeLiteral, root ) );
    _tokenStyles.put( METHOD_CALL_KEY, addStyle( STYLE_MethodCall, root ) );
    _tokenStyles.put( PROPERTY_KEY, addStyle( STYLE_Property, root ) );
    _tokenStyles.put( ENHANCEMENT_METHOD_CALL_KEY, addStyle( STYLE_EnhancementMethodCall, root ) );
    _tokenStyles.put( ENHANCEMENT_PROPERTY_KEY, addStyle( STYLE_EnhancementProperty, root ) );
    _tokenStyles.put( FIELD_ERROR_KEY, addStyle( STYLE_FieldError, root ) );
    _tokenStyles.put( FIELD_WARNING_KEY, addStyle( STYLE_FieldWarning, root ) );

    addStyle( STYLE_Caret, root );

    _strFontFamily = g_defFontFamily;
    _iFontSize = g_defFontSize;
    _fontCache = new HashMap<>();

    setDefaultStyles();
  }

  public void setForeground( Style style, Color colorFore )
  {
    setAttribute( style, StyleConstants.Foreground, colorFore );
  }

  public void setBackground( Style style, Color colorBack )
  {
    setAttribute( style, StyleConstants.Background, colorBack );
  }

  public void setBold( Style style, boolean bBold )
  {
    setAttribute( style, StyleConstants.Bold, bBold );
  }

  public void setItalic( Style style, boolean bItalic )
  {
    setAttribute( style, StyleConstants.Italic, bItalic );
  }

  public void setUnderline( Style style, boolean bUnderline )
  {
    setAttribute( style, StyleConstants.Underline, bUnderline );
  }

  public void setStrikeThrough( Style style, boolean bStrikeThrough )
  {
    setAttribute( style, StyleConstants.StrikeThrough, bStrikeThrough );
  }

  public void setAttribute( Style style, Object attr, Object value )
  {
    Style defStyle = getStyle( DEFAULT_STYLE );
    if( defStyle == style )
    {
      style.addAttribute( attr, value );
      for( Enumeration<?> names = getStyleNames(); names.hasMoreElements(); )
      {
        Style csr = getStyle( (String)names.nextElement() );
        if( csr != defStyle &&
            GosuObjectUtil.equals( csr.getAttribute( attr ), value ) )
        {
          // Remove attr for child attrs having same value
          csr.removeAttribute( attr );
        }
      }
      return;
    }

    Object defValue = defStyle.getAttribute( attr );
    if( GosuObjectUtil.equals( defValue, value ) )
    {
      style.removeAttribute( attr );
    }
    else
    {
      style.addAttribute( attr, value );
    }
  }

  public void setDefaultStyles()
  {
    boolean bAllowBold = false;

    // Default
    Style style = getStyle( DEFAULT_STYLE );
    setBackground( style, Scheme.active().getCodeWindow() );
    setForeground( style, Scheme.active().getCodeWindowText() );

    // Caret
    // style = getStyle( STYLE_Caret );

    // Whitespace
    // style = getStyleForScanValue( SourceCodeTokenizer.TT_WHITESPACE );

    // Comments
    style = getStyleForScanValue( ISourceCodeTokenizer.TT_COMMENT );
    setForeground( style, Scheme.active().getCodeComment()  );
    setItalic( style, true );

    // EOL (same as Comment... to handle multiline comments)
    style = getStyleForScanValue( ISourceCodeTokenizer.TT_EOL );
    setForeground( style, Scheme.active().getCodeMultilineComment() );
    setItalic( style, true );

    // EOF (same as Comment... to handle multiline comments)
    style = getStyleForScanValue( ISourceCodeTokenizer.TT_EOF );
    setForeground( style, Scheme.active().getCodeMultilineComment() );
    setItalic( style, true );

    // String Literals
    style = getStyleForScanValue( (int)'"' );
    setForeground( style, Scheme.active().getCodeStringLiteral() );
    setBold( style, bAllowBold );

    // Number Literals
    style = getStyleForScanValue( ISourceCodeTokenizer.TT_NUMBER );
    setForeground( style, Scheme.active().getCodeNumberLiteral() );

    // Integer Literals
    style = getStyleForScanValue( ISourceCodeTokenizer.TT_INTEGER );
    setForeground( style, Scheme.active().getCodeNumberLiteral() );

    // Non-key Words (identifiers and bean member access paths)
    // style = getStyleForScanValue( ISourceCodeTokenizer.TT_WORD );

    // Key Words
    style = getStyleForScanValue( GosuStyleContext.KEY_WORD );
    setForeground( style, Scheme.active().getCodeKeyword() );
    setBold( style, bAllowBold );

    // Parse Errors
    style = getStyleForScanValue( GosuStyleContext.PARSE_ERROR );
    setForeground( style, Scheme.active().getCodeError() );

    // Parse Warnings
    style = getStyleForScanValue( GosuStyleContext.PARSE_WARNING );
    setForeground( style, Scheme.active().getCodeWarning() );
    setUnderline( style, true );

    // Deprecated Member
    style = getStyleForScanValue( GosuStyleContext.DEPRECATED );
    setForeground( style, Scheme.active().getCodeDeprecated() );
    setStrikeThrough( style, true );

    // Operators
    style = getStyleForScanValue( ISourceCodeTokenizer.TT_OPERATOR );
    setForeground( style, Scheme.active().getCodeOperator() );
    setBold( style, bAllowBold );

    // Type Literals
    style = getStyleForScanValue( GosuStyleContext.TYPE_LITERAL );
    setForeground( style, Scheme.active().getCodeTypeLiteral() );
    setBold( style, bAllowBold );

    // Type Literals (namespaces and type-parameters)
    style = getStyleForScanValue( GosuStyleContext.NESTED_TYPE_LITERAL );
    setForeground( style, Scheme.active().getCodeTypeLiteralNested() );

    // Type Literals (namespaces and type-parameters)
    style = getStyleForScanValue( GosuStyleContext.FIELD_ERROR );
    setBackground( style, new Color( 255, 230, 230 ) );

    // Type Literals (namespaces and type-parameters)
    style = getStyleForScanValue( GosuStyleContext.FIELD_WARNING );
    setBackground( style, new Color( 254, 251, 94 ) );
  }

  public static void setDefaultFontFamily( String defFontFamily )
  {
    g_defFontFamily = defFontFamily;
  }

  public static String getDefaultFontFamily()
  {
    return g_defFontFamily;
  }

  public static void setDefaultFontSize( int defFontSize )
  {
    g_defFontSize = defFontSize;
  }

  public static int getDefaultFontSize()
  {
    return g_defFontSize;
  }

  /**
   * Fetch the foreground color to use for a lexical token with the given value.
   */
  public Color getForeground( int code )
  {
    Style s = _tokenStyles.get( new Integer( code ) );
    if( s == null )
    {
      s = getStyle( DEFAULT_STYLE );
    }
    return getForeground( s );
  }

  public Color getBackground( int code )
  {
    Style s = _tokenStyles.get( new Integer( code ) );
    if( s == null )
    {
      s = getStyle( DEFAULT_STYLE );
    }
    return getBackground( s );
  }

  /**
   * Fetch the font to use for a lexical token with the given scan value.
   */
  public Font getFont( int code )
  {
    Style s = _tokenStyles.get( new Integer( code ) );
    if( s == null )
    {
      s = getStyle( DEFAULT_STYLE );
    }
    return getFont( s );
  }

  /**
   * Fetches the attribute set to use for the given scan code.  The set is
   * stored in a table to facilitate relatively fast access to use in
   * conjunction with the scanner.
   */
  public Style getStyleForScanValue( int code )
  {
    Style s = _tokenStyles.get( new Integer( code ) );
    if( s == null )
    {
      s = getStyle( DEFAULT_STYLE );
    }
    return s;
  }

  /**
   * Fetch the font to use for a given attribute set.
   */
  @Override
  public Font getFont( AttributeSet attr )
  {
    boolean bUnderline = StyleConstants.isUnderline( attr );
    boolean bStrikethrough = StyleConstants.isStrikeThrough( attr );

    if( !bUnderline && !bStrikethrough )
    {
      // StyleContext ignores the Underline and Strikethrough attribute
      return getFont( attr, getFontFamily( attr ) );
    }

    // Must build the font via TextAttribute map to support Underlined and Strikethrough text

    Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
    map.put( TextAttribute.FAMILY, getFontFamily( attr ) );
    map.put( TextAttribute.SIZE, (float)getFontSize() );
    map.put( TextAttribute.WEIGHT, StyleConstants.isBold( attr ) ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR );
    map.put( TextAttribute.POSTURE, StyleConstants.isItalic( attr ) ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR );
    if( bUnderline )
    {
      map.put( TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON );
      if( attr.getAttribute( DASHED ) != null )
      {
        map.put( TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_GRAY );
      }
    }
    if( bStrikethrough )
    {
      map.put( TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON );
    }

    Font font = _fontCache.get( attr );
    if( font == null )
    {
      font = new Font( map );
      _fontCache.put( attr, font );
    }

    return font;
  }

  private Font getFont( AttributeSet attr, String strFamily )
  {
    int iStyle = Font.PLAIN;
    if( StyleConstants.isBold( attr ) )
    {
      iStyle |= Font.BOLD;
    }
    if( StyleConstants.isItalic( attr ) )
    {
      iStyle |= Font.ITALIC;
    }
    int iSize = getFontSize();

    // If either superscript or subscript is is set, we need to reduce the font
    // size by 2.
    if( StyleConstants.isSuperscript( attr ) ||
        StyleConstants.isSubscript( attr ) )
    {
      iSize -= 2;
    }

    return getFont( strFamily, iStyle, iSize );
  }

  public String getFontFamily( AttributeSet a )
  {
    String strFamily = (String)a.getAttribute( StyleConstants.FontFamily );
    if( strFamily == null )
    {
      strFamily = getFontFamily();
    }
    return strFamily;
  }

  public String getFontFamily()
  {
    return _strFontFamily;
  }

  public void setFontFamily( String strFamily )
  {
    _strFontFamily = strFamily;
    _fontCache.clear();
  }

  public int getFontSize()
  {
    return _iFontSize;
  }

  public void setFontSize( int iSize )
  {
    _iFontSize = iSize;
    _fontCache.clear();
  }

  //----------------------------------------------------------------------------------------------
  //-- ViewFactory methods --

  @Override
  public View create( Element elem )
  {
    return new GosuSourceView( elem );
  }

  public List<Style> getStyles()
  {
    List<Style> styles = new ArrayList<Style>();
    for( Enumeration<?> names = getStyleNames(); names.hasMoreElements(); )
    {
      styles.add( getStyle( (String)names.nextElement() ) );
    }
    return styles;
  }

  public boolean areStylesEquivalent( String styleName1, String styleName2 )
  {
    Style style1 = getStyle( styleName1 );
    Style style2 = getStyle( styleName2 );
    Enumeration<?> names = style1.getAttributeNames();
    while( names.hasMoreElements() )
    {
      Object name = names.nextElement();
      if( name != StyleConstants.NameAttribute )
      {
        if( !style1.getAttribute( name ).equals( style2.getAttribute( name ) ) )
        {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * View that uses the lexical information to determine the style
   * characteristics of the text that it renders.  This simply colorizes the
   * various tokens and assumes a constant font family and size.
   */
  class GosuSourceView extends WrappedPlainView
  {
    GosuDocument.Scanner _lexer;

    /**
     * Construct a simple syntax highlight view of gosu source.
     */
    GosuSourceView( Element elem )
    {
      super( elem );
    }

    /**
     * Renders using the given rendering surface and area on that surface.
     * This is implemented to invalidate the lexical scanner after rendering
     * so that the next request to drawUnselectedText will set a new range
     * for the scanner.
     *
     * @param g The rendering surface to use
     * @param a The allocated region to render into
     */
    @Override
    public void paint( Graphics g, Shape a )
    {
      super.paint( g, a );
      _lexer = null;
    }

    /**
     * Renders the given range in the model as normal unselected text.  This is
     * implemented to paint colors based upon the token-to-color translations.
     * To reduce the number of calls to the Graphics object, text is batched up
     * until a color change is detected or the entire requested range has been
     * reached.
     *
     * @param g  The graphics context
     * @param x  The starting X coordinate
     * @param y  The starting Y coordinate
     * @param p0 The beginning position in the model
     * @param p1 The ending position in the model
     *
     * @return The location of the end of the range
     *
     * @throws javax.swing.text.BadLocationException if the range is invalid
     */
    @Override
    protected int drawSelectedText( Graphics g, int x, int y, int p0, int p1 ) throws BadLocationException
    {
      //return drawUnselectedText( g, x, y, p0, p1 );
      return super.drawSelectedText( g, x, y, p0, p1 );
    }

    @Override
    protected int drawUnselectedText( Graphics g, int x, int y, int p0, int p1 ) throws BadLocationException
    {
      GosuDocument doc = (GosuDocument)getDocument();
      Color lastFg = null;
      Color lastBg = null;
      Font lastFont = null;
      Style lastStyle = null;
      int mark = p0;
      int iCode;
      while( p0 < p1 )
      {
        updateScanner( p0 );
        int p = Math.min( _lexer.getEndOffset(), p1 );
        p = (p <= p0) ? p1 : p;

        Pair<Boolean, ParseWarningForDeprecatedMember> parseWarning = doc.getParseWarningStartingAt( p0 );
        if( parseWarning.getFirst() )
        {
          if( parseWarning.getSecond() != null )
          {
            iCode = DEPRECATED;
            p = Math.max( p, parseWarning.getSecond().getTokenEnd() );
          }
          else
          {
            iCode = PARSE_WARNING;
          }
        }
//        else if(_lexer._tokenizer.getInstructor() != null && (_lexer._tokenizer.isAnalyzingDirective() || _lexer._tokenizer.isAnalyzingSeparately()) ) {
//        else if(_lexer._tokenizer.getInstructor() != null && !(_lexer._tokenizer.isAnalyzingDirective() || _lexer._tokenizer.isAnalyzingSeparately()) ) {
//          iCode = DEFAULT;
//        }
        else
        {
          Integer specialCode = doc.getStyleCodeAtPosition( p0 );
          if( specialCode != null )
          {
            iCode = specialCode;
          }
          else
          {
            iCode = _lexer.getType();
            if( iCode == ISourceCodeTokenizer.TT_KEYWORD )
            {
              iCode = KEY_WORD;
            }
          }
        }

        if( doc.hasErrorStartingAt( p0 ) )
        {
          iCode = PARSE_ERROR;
        }

        Color fg = getForeground( iCode );
        Color bg = getBackground( iCode );
        Font font = getFont( iCode );
        Style style = getStyleForScanValue( iCode );
        if( lastStyle != null ) //&& style != lastStyle )
        {
          // Color change, flush what we have
          g.setColor( lastFg );
          ((Graphics2D)g).setBackground( lastBg );
          g.setFont( lastFont );
          Segment text = getLineBuffer();

          doc.getText( mark, p0 - mark, text );
          x = drawTabbedText( text, lastStyle, lastFont, x, y, (Graphics2D)g, this, mark );
          if( doc.hasErrorStartingAt( mark ) )
          {
            ImageIcon icon = editor.util.EditorUtilities.loadIcon( "images/rule_error_pointer.gif" );
            icon.paintIcon( THISISSTUPID, g, x - icon.getIconWidth() / 2, y );
          }
          mark = p0;
        }

        lastStyle = style;
        lastFg = fg;
        lastBg = bg;
        lastFont = font;
        p0 = p;
      }

      // Flush remaining

      g.setColor( lastFg );
      g.setFont( lastFont );
      Segment text = getLineBuffer();
      doc.getText( mark, p1 - mark, text );

      x = drawTabbedText( text, lastStyle, lastFont, x, y, (Graphics2D)g, this, mark );

      return x;
    }

    /**
     * Update the scanner (if necessary) to point to the appropriate token for
     * the given start position needed for rendering.
     */
    void updateScanner( int p )
    {
      try
      {
        if( _lexer == null )
        {
          GosuDocument doc = (GosuDocument)getDocument();
          _lexer = doc.createScanner();
        }
        while( _lexer.getEndOffset() <= p )
        {
          _lexer.nextToken();
        }
      }
      catch( Throwable e )
      {
        // can't adjust scanner... calling logic
        // will simply render the remaining text.
        editor.util.EditorUtilities.handleUncaughtException( e );
      }
    }

    public final int drawTabbedText( Segment s, Style style, Font font, int x, int y, Graphics2D g,
                                     TabExpander e, int startOffset )
    {
      FontMetrics metrics = g.getFontMetrics();
      int nextX = x;
      char[] txt = s.array;
      int txtOffset = s.offset;
      int flushLen = 0;
      int flushIndex = s.offset;
      int n = s.offset + s.count;
      for( int i = txtOffset; i < n; i++ )
      {
        if( txt[i] == '\t' )
        {
          if( flushLen > 0 )
          {
            // g.drawChars(txt, flushIndex, flushLen, x, y);
            drawText( g, style, font, new String( txt, flushIndex, flushLen ), x, y );
            flushLen = 0;
          }
          flushIndex = i + 1;
          if( e != null )
          {
            nextX = (int)e.nextTabStop( (float)nextX, startOffset + i - txtOffset );
          }
          else
          {
            nextX += metrics.charWidth( ' ' );
          }
          x = nextX;
        }
        else if( (txt[i] == '\n') || (txt[i] == '\r') )
        {
          if( flushLen > 0 )
          {
            //g.drawChars(txt, flushIndex, flushLen, x, y);
            drawText( g, style, font, new String( txt, flushIndex, flushLen ), x, y );
            flushLen = 0;
          }
          flushIndex = i + 1;
          x = nextX;
        }
        else
        {
          flushLen += 1;
          nextX += metrics.charWidth( txt[i] );
        }
      }
      if( flushLen > 0 )
      {
        //g.drawChars(txt, flushIndex, flushLen, x, y);
        String strText = new String( txt, flushIndex, flushLen );
        if( style.getName().equals( STYLE_Operator ) )
        {
          if( strText.length() == 1 && strText.charAt( 0 ) == '\\' )
          {
            strText = "\u03BB"; // lambda
          }
          else if( strText.length() == 2 && strText.equals( "->" ) )
          {
            strText = "\u2192 "; // right arrow
          }
          else if( strText.length() == 1 && strText.equals( "*" ) )
          {
            strText = "\u22C5"; // dot product
          }
        }
        drawText( g, style, font, strText, x, y );
      }
      return nextX;
    }

    /**
     * Draws text using a TextLayout. This method of drawing text is necessary
     * to support certain attribute esp. TextAttribute.UNDERLINE and TextAttribute.STRIKETHROUGH .
     */
    public final void drawText( Graphics2D g2, Style style, Font font, String text, int x, int y )
    {
      boolean bUnderline = StyleConstants.isUnderline( style );
      boolean bStrikethrough = StyleConstants.isStrikeThrough( style );

      if( bUnderline || bStrikethrough )
      {
        AttributedString ats = new AttributedString( text );
        ats.addAttribute( TextAttribute.FONT, font );
        if( bUnderline )
        {
          ats.addAttribute( TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON );

          if( style.getAttribute( DASHED ) != null )
          {
            ats.addAttribute( TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_GRAY );
          }
        }
        if( bStrikethrough )
        {
          ats.addAttribute( TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON );
        }

        AttributedCharacterIterator iter = ats.getIterator();

        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout( iter, frc );
        tl.draw( g2, (float)x, (float)y );
      }
      else
      {
        TextLayout tl = new TextLayout( text, font, g2.getFontRenderContext() );
//## uncomment to allow for style's with varying background colors to render
//        Rectangle bounds = tl.getBounds().getBounds();
//        Color c = g2.getColor();
//        g2.setColor( g2.getBackground() );
//        g2.clearRect( x, y, bounds.width, bounds.height );
//        g2.setColor( c );
        tl.draw( g2, (float)x, (float)y );
      }
    }
  }
}
