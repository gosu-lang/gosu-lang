/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.HTMLEscapeUtil;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.reflect.IType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays gosu parser error feedback within a GosuEditor.
 */
public class GosuEditorFeedbackPanel extends JPanel
{
  private JLabel _icon;
  private FeedbackMargin _feedback;
  private GosuEditor _editor;

  public GosuEditorFeedbackPanel()
  {
    configureUI();
  }

  /**
   * Updates this panel with current parser feedback.
   *
   * @param iResCode The parse result code. One of:
   *                 <ul>
   *                 <li> GosuEditor.RESCODE_VALID
   *                 <li> GosuEditor.RESCODE_WARNINGS
   *                 <li> GosuEditor.RESCODE_ERRORS
   *                 </ul>
   * @param editor   The Gosu editor
   */
  public void update( int iResCode, GosuEditor editor )
  {
    _icon.setIcon( loadIcon( iResCode ) );
    _editor = editor;
    _feedback.repaint();
    repaint();
    editor.repaint();
  }

  void configureUI()
  {
    setLayout( new BorderLayout() );
    setBackground( Scheme.active().getControl() );
    setBorder( null );

    _icon = new JLabel();
    _icon.setBorder( BorderFactory.createEmptyBorder( 1, 1, 1, 1 ) );
    add( _icon, BorderLayout.NORTH );

    _feedback = new FeedbackMargin();
    ToolTipManager.sharedInstance().registerComponent( _feedback );
    add( _feedback, BorderLayout.CENTER );
  }

  protected Icon loadIcon( int iResCode )
  {
    switch( iResCode )
    {
      case GosuEditor.RESCODE_VALID:
        return editor.util.EditorUtilities.loadIcon( "images/rule_green.gif" );

      case GosuEditor.RESCODE_WARNINGS:
        return editor.util.EditorUtilities.loadIcon( "images/rule_yellow.gif" );

      case GosuEditor.RESCODE_PENDING:
        return editor.util.EditorUtilities.loadIcon( "images/status_anim.gif" );

      default:
        return editor.util.EditorUtilities.loadIcon( "images/rule_red.gif" );
    }
  }

  private class FeedbackMargin extends JPanel
  {
    private Point _iMousePos;

    FeedbackMargin()
    {
      ErrorMarkMouseHandler errorMarkMouser = new ErrorMarkMouseHandler();
      addMouseMotionListener( errorMarkMouser );
      addMouseListener( errorMarkMouser );
    }

    @Override
    public void paintComponent( Graphics g )
    {
      super.paintComponent( g );

      if( _editor == null )
      {
        return;
      }
      ParseResultsException pe = _editor.getParseResultsException();
      if( pe == null )
      {
        return;
      }
      int iLineCount = getLineCount();

      List pws = pe.getParseWarnings();
      for( int i = 0; i < pws.size(); i++ )
      {
        ParseWarning w = (ParseWarning)pws.get( i );
        paintWarningMark( g, w, iLineCount );
      }

      List pes = pe.getParseExceptions();
      for( int i = 0; i < pes.size(); i++ )
      {
        ParseException e = (ParseException)pes.get( i );
        paintErrorMark( g, e, iLineCount );
      }
    }

    private void paintWarningMark( Graphics g, ParseWarning w, int iLineCount )
    {
      int iMark = getMarkForIssue( w, iLineCount );
      g.setColor( Scheme.active().getColorWarning() );
      g.fillRect( 1, iMark - 1, getWidth() - 2, 3 );
      g.setColor( Scheme.active().getColorWarningShadow() );
      g.drawLine( 2, iMark + 1, getWidth() - 2, iMark + 1 );
      g.drawLine( getWidth() - 2, iMark - 1, getWidth() - 2, iMark + 1 );
    }

    private void paintErrorMark( Graphics g, ParseException e, int iLineCount )
    {
      int iMark = getMarkForIssue( e, iLineCount );
      g.setColor( Scheme.active().getColorError() );
      g.fillRect( 1, iMark - 1, getWidth() - 2, 3 );
      g.setColor( Scheme.active().getColorErrorShadow() );
      g.drawLine( 2, iMark + 1, getWidth() - 2, iMark + 1 );
      g.drawLine( getWidth() - 2, iMark - 1, getWidth() - 2, iMark + 1 );
    }

    @Override
    public String getToolTipText()
    {
      if( _iMousePos == null )
      {
        return null;
      }

      return makeToolTipText( getErrorsFromCursorPos() );
    }

    private String makeToolTipText( List parseExceptions )
    {
      if( parseExceptions == null || parseExceptions.isEmpty() )
      {
        return null;
      }

      String strFeedback = "";
      for( int i = parseExceptions.size() - 1; i >= 0; i-- )
      {
        if( strFeedback.length() > 0 )
        {
          strFeedback += "<br><hr>";
        }
        else
        {
          strFeedback = "<html>";
        }

        IParseIssue pi = (IParseIssue)parseExceptions.get( i );
        strFeedback += "&nbsp;" + HTMLEscapeUtil.escape( pi.getUIMessage() );

        if( pi instanceof ParseException )
        {
          ParseException pe = (ParseException)pi;
          IType typeExpected = pe.getExpectedType();

          if( typeExpected != null )
          {
            String strTypesExpected = ParseResultsException.getExpectedTypeName( typeExpected );

            if( strTypesExpected.length() > 0 )
            {
              strFeedback += "\n" + "Expected Type" + ":" + strTypesExpected;
            }
          }
        }
      }
      return strFeedback;
    }

    private List getErrorsFromCursorPos()
    {
      if( _editor == null )
      {
        return null;
      }
      List matches = new ArrayList();
      ParseResultsException pe = _editor.getParseResultsException();
      if( pe == null )
      {
        return matches;
      }

      int iLineCount = getLineCount();
      List pes = pe.getParseIssues();
      for( int i = pes.size() - 1; i >= 0; i-- )
      {
        IParseIssue e = (IParseIssue)pes.get( i );
        int iMark = getMarkForIssue( e, iLineCount );
        if( iMark >= _iMousePos.y - 3 && iMark <= _iMousePos.y + 3 )
        {
          matches.add( e );
        }
      }
      return matches;
    }

    private int getMarkForIssue( IParseIssue e, int iLineCount )
    {
      iLineCount--;
      if( iLineCount == 0 )
      {
        return getHeight() / 2;
      }
      int iLine = e.getLine() - 1;
      float fOffset = (float)iLine / (float)iLineCount;
      return (int)((getHeight() - 10) * fOffset) + 5;
    }

    public int getLineCount()
    {
      int iLinesOfCode = _editor.getEditor().getDocument().getDefaultRootElement().getElementCount();

      int iViewHeight = _editor.getScroller().getViewport().getHeight();
      FontMetrics fm = _editor.getEditor().getFontMetrics( _editor.getEditor().getFont() );
      int iLineHeight = fm.getHeight();
      int iViewLines = iViewHeight / iLineHeight;

      return Math.max( iViewLines, iLinesOfCode );
    }

    private class ErrorMarkMouseHandler extends MouseAdapter implements MouseMotionListener
    {
      @Override
      public void mouseMoved( MouseEvent e )
      {
        _iMousePos = e.getPoint();
        List errors = getErrorsFromCursorPos();
        setCursor( errors == null || errors.isEmpty()
                   ? Cursor.getDefaultCursor()
                   : Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
      }

      @Override
      public void mouseClicked( MouseEvent e )
      {
        List errors = getErrorsFromCursorPos();
        if( !errors.isEmpty() )
        {
          _editor.getEditor().requestFocusInWindow();
          _editor.setCaretPositionForParseIssue( (IParseIssue)errors.get( 0 ) );
        }
      }

      @Override
      public void mouseDragged( MouseEvent e )
      {
      }
    }
  }
}
