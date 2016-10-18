package editor.search;

import editor.util.EditorUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

public class MessageDisplay implements SwingConstants
{
  public static final String HOME_DIR = System.getProperty( "user.dir" );

  static final String STRING_DELIMITERS = " \t\n\r";
  static final Rectangle RCTEXT = new Rectangle();
  static final Rectangle SHOULDPAINT_RECT = new Rectangle();
  static final Rectangle SHOULDPAINT_RECT_IN = new Rectangle();
  static final Rectangle SCRATCH_RECT = new Rectangle();
  static final Rectangle SCRATCH_RECT2 = new Rectangle();
  private static String _testError;

  public static void Log( Throwable t )
  {
    System.err.println( "\r\n" + t.getMessage() + "\r\n" + new Date( System.currentTimeMillis() ) + "\r\n" );
    t.printStackTrace();

    displayError( "Error performing operation." );
  }

  
  public static void displayError( Throwable e )
  {
    displayMessageBox( e == null ? "Error performing operation." : e.getMessage(), JOptionPane.ERROR_MESSAGE, false );
  }

  public static void displayError( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.ERROR_MESSAGE, false );
  }

  public static void displayErrorNoWrap( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.ERROR_MESSAGE, false, false );
  }

  public static void displayWarning( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.WARNING_MESSAGE, false );
  }

  public static void displayInformation( String strMsg )
  {
    displayMessageBox( strMsg, JOptionPane.INFORMATION_MESSAGE, false );
  }

  public static void displayMessageBox( String strMsg, int iType, boolean bNonModalMsg )
  {
    displayMessageBox( strMsg, iType, bNonModalMsg, true );
  }

  public static void displayMessageBox( String strMsg, final int iType, final boolean bNonModalMsg, boolean bWrapText )
  {
    final String strWrappedMsg = bWrapText ? EditorUtilities.wrapText( strMsg ) : strMsg;

    Runnable logMsgBox = () -> {
      if( !bNonModalMsg )
      {
        MessageBox.showMessageDialog( strWrappedMsg, iType );
      }
      else
      {
        showMessageNonModal( strWrappedMsg, iType );
      }
    };

    if( EventQueue.isDispatchThread() )
    {
      logMsgBox.run();
    }
    else
    {
      try
      {
        EventQueue.invokeAndWait( logMsgBox );
      }
      catch( Throwable t )
      {
        t.printStackTrace();
      }
    }
  }

  public static String getLastTestError()
  {
    return _testError;
  }

  public static void clearTestError()
  {
    _testError = null;
  }

  public static void showMessageNonModal( String strMsg )
  {
    showMessageNonModal( strMsg, JOptionPane.INFORMATION_MESSAGE );
  }

  public static void showMessageNonModal( String strMsg, int iType )
  {
    JOptionPane pane = new JOptionPane( EditorUtilities.wrapText( strMsg ), iType );
    JDialog dialog = pane.createDialog( null, "" );
    dialog.setModal( false );
    dialog.show();

    // Must set dialog back to modal after it displays because swing recycles the dialog
    // for ALL modal dialogs (!)
    dialog.setModal( true );
  }

  public static int displayConfirmation( String strMsg, int iButtonsType )
  {
    return displayConfirmation( strMsg, iButtonsType, null );
  }

  public static int displayConfirmation( String strMsg, int iButtonsType, String[] astrButtonLabels )
  {
    return displayConfirmation( strMsg, iButtonsType, JOptionPane.QUESTION_MESSAGE, astrButtonLabels, null );
  }

  public static int displayConfirmation( String strMsg, int iButtonsType, String[] astrButtonLabels, Point loc )
  {
    return displayConfirmation( strMsg, iButtonsType, JOptionPane.QUESTION_MESSAGE, astrButtonLabels, loc );
  }

  public static int displayConfirmation( String strMsg, final int iButtonsType, final int iType, final String[] astrButtonLabels, final Point loc )
  {
    final String strWrappedMsg = EditorUtilities.wrapText( strMsg );
    final int[] aiResult = new int[1];
    Runnable confirmationBox = () -> aiResult[0] = MessageBox.showConfirmDialog( getFrame(), strWrappedMsg, iButtonsType, iType, loc, astrButtonLabels );

    if( EventQueue.isDispatchThread() )
    {
      confirmationBox.run();
    }
    else
    {
      try
      {
        EventQueue.invokeAndWait( confirmationBox );
      }
      catch( Throwable t )
      {
        t.printStackTrace();
      }
    }

    return aiResult[0];
  }

  public static Point getLastMessageLocation()
  {
    return MessageBox.getLastMessageLocation();
  }

  public static JFrame getFrame()
  {
    if( EditorUtilities.getActiveWindow() instanceof JFrame )
    {
      return (JFrame)EditorUtilities.getActiveWindow();
    }
    else
    {
      return null;
    }
  }

  public static void centerWindowInFrame( Window window, Frame frame )
  {
    EditorUtilities.centerWindowInFrame( window, frame );
  }

  public static Rectangle drawStringInRectClipped( Graphics g, String strText, Rectangle rcClip, int iHorzAlign, int iVertAlign )
  {
    return drawStringInRectClipped( g, null, strText, rcClip, iHorzAlign, iVertAlign, true );
  }

  public static Rectangle drawStringInRectClipped( Graphics g, FontMetrics fm, String strText, Rectangle rcClip, int iHorzAlign, int iVertAlign, boolean bReturnBounds )
  {
    RCTEXT.setBounds( 0, 0, 0, 0 );
    MessageDisplay.SCRATCH_RECT.setBounds( 0, 0, 0, 0 );
    fm = fm == null ? g.getFontMetrics() : fm;

    String str = SwingUtilities.layoutCompoundLabel( fm,
                                                     strText,
                                                     null, // no icon
                                                     iVertAlign, // vertical alignment
                                                     iHorzAlign, // horizontal alignment
                                                     BOTTOM, // vertical text pos relative to icon
                                                     RIGHT, // horizontal text pos relative to icon
                                                     rcClip,
                                                     MessageDisplay.SCRATCH_RECT,
                                                     RCTEXT,
                                                     0 );    // text/icon gap

    if( str != null && !str.equals( "" ) )
    {
      g.drawString( str, RCTEXT.x, RCTEXT.y + fm.getAscent() );
    }

    return bReturnBounds ? new Rectangle( RCTEXT ) : null;
  }

  public static void drawStringInRect( Graphics g, String strText, Rectangle rc, int justification, boolean bClip )
  {
    drawStringInRect( g, strText, rc.x, rc.y, rc.width, rc.height, justification, bClip );
  }

  public static void drawStringInRect( Graphics g, String strText, int x, int y,
                                       int width, int height, int justification, boolean bClip )
  {
    FontMetrics fontMetrics;
    int drawWidth, startX, startY, delta;

    if( g.getFont() == null )
    {
      return;
    }
    fontMetrics = g.getFontMetrics();
    if( fontMetrics == null )
    {
      return;
    }

    if( bClip )
    {
      MessageDisplay.SCRATCH_RECT.setBounds( g.getClipBounds() );

      MessageDisplay.SCRATCH_RECT2.setBounds( x, y, width, height );
      if( MessageDisplay.SCRATCH_RECT.intersects( MessageDisplay.SCRATCH_RECT2 ) )
      {
        MessageDisplay.SCRATCH_RECT2.setBounds( MessageDisplay.SCRATCH_RECT.intersection( MessageDisplay.SCRATCH_RECT2 ) );
        g.setClip( MessageDisplay.SCRATCH_RECT2 );
      }
      else
      {
        return;
      }
    }

    if( justification == CENTER )
    {
      drawWidth = fontMetrics.stringWidth( strText );
      if( drawWidth > width )
      {
        drawWidth = width;
      }
      startX = x + (width - drawWidth) / 2;
    }
    else if( justification == RIGHT )
    {
      drawWidth = fontMetrics.stringWidth( strText );
      if( drawWidth > width )
      {
        drawWidth = width;
      }
      startX = x + width - drawWidth;
    }
    else
    {
      startX = x;
    }

    delta = (height - fontMetrics.getAscent() - fontMetrics.getDescent()) / 2;
    if( delta < 0 )
    {
      delta = 0;
    }

    startY = y + height - delta - fontMetrics.getDescent();

    g.drawString( strText, startX, startY );

    if( bClip )
    {
      g.setClip( MessageDisplay.SCRATCH_RECT );
    }
  }

  public static boolean shouldPaint( Graphics g, Component comp )
  {
    Rectangle rcBounds = comp.getBounds();
    return shouldPaint( g, rcBounds.x, rcBounds.y, rcBounds.width, rcBounds.height );
  }

  public static boolean shouldPaint( Graphics g, int x, int y, int iWidth, int iHeight )
  {
    Rectangle rcClipBounds = g.getClipBounds();
    if( rcClipBounds == null )
    {
      return true;
    }

    SHOULDPAINT_RECT.setBounds( rcClipBounds );
    SHOULDPAINT_RECT_IN.setBounds( x, y, iWidth, iHeight );

    return SHOULDPAINT_RECT.intersects( SHOULDPAINT_RECT_IN );
  }

  public static Vector<String> drawTextWrapped( Graphics g, Rectangle rect, String strText )
  {
    return drawTextWrapped( g, rect, strText, null, 0, true, true );
  }

  public static Vector<String> drawTextWrapped( Graphics g, Rectangle rect, String strText, Font font, int iLineLengthChars, boolean bClip, boolean bAppendLineFeed )
  {
    try
    {
      Vector<String> vStrings = (g == null) ? new Vector<String>() : null;

      if( bClip && (g != null) && (rect != null) && !rect.isEmpty() )
      {
        MessageDisplay.SCRATCH_RECT.setBounds( g.getClipBounds() );

        MessageDisplay.SCRATCH_RECT2.setBounds( rect.x, rect.y, rect.width, rect.height );
        if( MessageDisplay.SCRATCH_RECT.intersects( MessageDisplay.SCRATCH_RECT2 ) )
        {
          MessageDisplay.SCRATCH_RECT2.setBounds( MessageDisplay.SCRATCH_RECT.intersection( MessageDisplay.SCRATCH_RECT2 ) );
          g.setClip( MessageDisplay.SCRATCH_RECT2 );
        }
        else
        {
          return vStrings;
        }
      }
      else
      {
        bClip = false;
      }

      if( strText == null || strText.length() == 0 ||
          ((iLineLengthChars <= 0) && (rect == null || rect.width == 0)) )
      {
        return vStrings;
      }

      // Replace tabs with single spaces
      strText = (strText.indexOf( '\t' ) >= 0) ? strText.replace( '\t', ' ' ) : strText;

      FontMetrics fm = (g == null) ? Toolkit.getDefaultToolkit().getFontMetrics( font ) : g.getFontMetrics();

      if( iLineLengthChars > 0 )
      {
        rect.width = fm.charWidth( 'p' ) * iLineLengthChars;
      }

      int x = rect.x;
      int y = rect.y + fm.getAscent();
      int iWidth = rect.width;
      if( iWidth < 0 )
      {
        return vStrings;
      }

      boolean bSkip;
      String strLine = "";

      boolean bNeedToDrawLastToken = false;

      StringTokenizer tokenizer = new StringTokenizer( strText, MessageDisplay.STRING_DELIMITERS, true );
      String token = null;
      while( tokenizer.hasMoreTokens() || bNeedToDrawLastToken )
      {
        token = bNeedToDrawLastToken ? token : tokenizer.nextToken();

        bSkip = false;

        for( int i = 0; i < MessageDisplay.STRING_DELIMITERS.length(); i++ )
        {
          if( token.charAt( 0 ) == MessageDisplay.STRING_DELIMITERS.charAt( i ) )
          {
            // Skip all delimiters but spaces and new lines, they have effect on output.
            if( MessageDisplay.STRING_DELIMITERS.charAt( i ) != ' ' &&
                MessageDisplay.STRING_DELIMITERS.charAt( i ) != '\n' )
            {
              bSkip = true;
            }
            break;
          }
        }

        if( bSkip )
        {
          continue;
        }

        bNeedToDrawLastToken = !tokenizer.hasMoreTokens();

        int iLinePlusToken = fm.stringWidth( strLine ) + fm.stringWidth( token );

        // if( bNeedToDrawLastToken && (iLinePlusToken <= iWidth) )
        // {
        //   strLine = strLine.concat( token );
        //   bNeedToDrawLastToken = false;
        // }

        boolean bLastLineOfRect = false;
        if( bNeedToDrawLastToken )
        {
          if( (iLinePlusToken <= iWidth) ||
              (bLastLineOfRect = (rect.height > 0) && ((rect.y + rect.height) < (y - fm.getAscent() + fm.getHeight() + fm.getAscent() / 2))) )// last line of the rectangle
          {
            // Force the last token to paint if:
            //
            // 1.) We have room for it on the line
            // 2.) We are on the last *visible* line in our bounds.
            //     Note determining the last visible line is a fuzzy scheme as the math above shows.

            strLine = strLine.concat( token );
            bNeedToDrawLastToken = false;
          }
        }


        if( (iLinePlusToken > iWidth) ||
            (token.charAt( 0 ) == '\n') ||
            !tokenizer.hasMoreTokens() ) // Force draw of last line
        {
          if( (strLine.length() > 0) &&
              ((token.charAt( 0 ) == '\n') || tokenizer.hasMoreTokens() || (iLinePlusToken <= iWidth) || bNeedToDrawLastToken || bLastLineOfRect) )
          {
            if( g != null )
            {
              g.drawString( strLine, x, y );
            }
            if( vStrings != null )
            {
              vStrings.addElement( bAppendLineFeed ? (strLine + "\n") : strLine );
            }

            y += fm.getHeight();

            strLine = "";

            if( token.charAt( 0 ) == ' ' ||
                token.charAt( 0 ) == '\n' )
            {
              continue;
            }
          }
          else if( token.charAt( 0 ) == '\n' )
          {
            y += fm.getHeight();
            continue;
          }
          else
          {
            do
            {
              if( token.length() == 1 )
              {
                // Rectangle isn't wide enough to display a single character.
                return vStrings;
              }

              int iSeg = token.length() / 2 + token.length() % 2;

              int iLen = iSeg;

              String strTokenPart = null;

              while( iSeg >= 1 )
              {
                try
                {
                  strTokenPart = token.substring( 0, iLen - 1 );
                }
                catch( Exception e )
                {
                  // ignore
                }

                int iTestWidth = fm.stringWidth( strTokenPart );

                iSeg = iSeg / 2 + (iSeg == 1 ? 0 : iSeg % 2);

                if( iTestWidth > iWidth )
                {
                  iLen -= iSeg;
                }
                else if( iTestWidth == iWidth )
                {
                  break;
                }
                else
                {
                  iLen += iSeg;
                }
              }

              if( strTokenPart != null )
              {
                if( strText.startsWith( "<html>" ) && strTokenPart.lastIndexOf( "<" ) > strTokenPart.lastIndexOf( ">" ) )
                {
                  strTokenPart = strTokenPart.substring( 0, strTokenPart.lastIndexOf( "<" ) );
                  iLen = strTokenPart.length();
                }
                if( g != null )
                {
                  g.drawString( strTokenPart, x, y );
                }
                if( vStrings != null )
                {
                  vStrings.addElement( bAppendLineFeed ? (strTokenPart + "\n") : strTokenPart );
                }
              }
              else
              {
                break;
              }

              y += fm.getHeight();

              if( iLen >= token.length() )
              {
                // We're drawing the token anyway, so we might as well just leave
                bNeedToDrawLastToken = false;
                break;
              }

              token = token.substring( strTokenPart.length() );

            }
            while( fm.stringWidth( token ) > iWidth );
          }
        }

        strLine = bNeedToDrawLastToken ? "" : strLine.concat( token );
      }

      if( bAppendLineFeed && !vStrings.isEmpty() )
      {
        // Remove the last line feed

        String strLast = vStrings.lastElement();
        if( strLast.endsWith( "\n" ) )
        {
          vStrings.setElementAt( strLast.substring( 0, strLast.length() - 1 ), vStrings.size() - 1 );
        }
      }

      return vStrings;
    }
    finally
    {
      if( bClip )
      {
        g.setClip( MessageDisplay.SCRATCH_RECT );
      }
    }
  }
}
