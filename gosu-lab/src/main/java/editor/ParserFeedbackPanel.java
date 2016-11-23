/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.EditorUtilities;
import editor.util.HTMLEscapeUtil;

import javax.swing.*;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Displays gosu parser error feedback within a GosuEditor.
 */
public class ParserFeedbackPanel extends JPanel
{
  private JLabel _icon;
  private FeedbackMargin _feedback;
  private EditorHost _editor;

  public ParserFeedbackPanel()
  {
    configureUI();
  }

  /**
   * Updates this panel with current parser feedback.
   *
   * @param editor   The Gosu editor
   */
  public void update( EditorHost editor )
  {
    _editor = editor;
    _icon.setIcon( findIconForResults() );
    _feedback.repaint();
    repaint();
    editor.repaint();
  }

  private Icon findIconForResults()
  {
    IIssueContainer issues = _editor.getIssues();
    if( issues.isEmpty() )
    {
      return EditorUtilities.loadIcon( "images/rule_green.gif" );
    }
    if( !issues.getErrors().isEmpty() )
    {
      return EditorUtilities.loadIcon( "images/rule_red.gif" );
    }
    return EditorUtilities.loadIcon( "images/rule_yellow.gif" );
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

  private class FeedbackMargin extends JPanel
  {
    private Point _iMousePos;

    FeedbackMargin()
    {
      MarkMouseHandler errorMarkMouser = new MarkMouseHandler();
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

      int iLineCount = getLineCount();

      paintHighlightMarks( g, iLineCount );
      paintIssueMarks( g, iLineCount );
    }

    private void paintIssueMarks( Graphics g, int iLineCount )
    {
      //noinspection ThrowableResultOfMethodCallIgnored
      IIssueContainer pe = _editor.getIssues();
      if( pe != null )
      {
        Iterable<IIssue> warnings = pe.getWarnings();
        for( IIssue issue: warnings )
        {
          paintWarningMark( g, issue, iLineCount );
        }

        Iterable<IIssue> errors = pe.getErrors();
        for( IIssue issue: errors )
        {
          paintErrorMark( g, issue, iLineCount );
        }
      }
    }

    private void paintHighlightMarks( Graphics g, int iLineCount )
    {
      for( Highlighter.Highlight highlight : _editor.getEditor().getHighlighter().getHighlights() )
      {
        Highlighter.HighlightPainter painter = highlight.getPainter();
        if( painter == GosuEditor.LabHighlighter.TEXT || painter == GosuEditor.LabHighlighter.USAGE )
        {
          int line = _editor.getEditor().getDocument().getDefaultRootElement().getElementIndex( highlight.getStartOffset() );
          paintHighlightMark( g, line, iLineCount );
        }
      }
    }

    private void paintWarningMark( Graphics g, IIssue w, int iLineCount )
    {
      int iMark = getMarkForLine( w.getLine(), iLineCount );
      g.setColor( Scheme.active().getColorWarning() );
      g.fillRect( 1, iMark - 1, getWidth() - 2, 3 );
      g.setColor( Scheme.active().getColorWarningShadow() );
      g.drawLine( 2, iMark + 1, getWidth() - 2, iMark + 1 );
      g.drawLine( getWidth() - 2, iMark - 1, getWidth() - 2, iMark + 1 );
    }

    private void paintErrorMark( Graphics g, IIssue e, int iLineCount )
    {
      int iMark = getMarkForLine( e.getLine(), iLineCount );
      g.setColor( Scheme.active().getColorError() );
      g.fillRect( 1, iMark - 1, getWidth() - 2, 3 );
      g.setColor( Scheme.active().getColorErrorShadow() );
      g.drawLine( 2, iMark + 1, getWidth() - 2, iMark + 1 );
      g.drawLine( getWidth() - 2, iMark - 1, getWidth() - 2, iMark + 1 );
    }

    private void paintHighlightMark( Graphics g, int line, int iLineCount )
    {
      int iMark = getMarkForLine( line, iLineCount );
      g.setColor( Scheme.active().usageReadHighlightColor() );
      g.fillRect( 1, iMark - 1, getWidth() - 2, 3 );
      g.setColor( Scheme.active().usageReadHighlightShadowColor() );
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

    private String makeToolTipText( List<IIssue> issues )
    {
      if( issues == null || issues.isEmpty() )
      {
        return null;
      }

      String strFeedback = "";
      for( int i = issues.size() - 1; i >= 0; i-- )
      {
        if( strFeedback.length() > 0 )
        {
          strFeedback += "<br><hr>";
        }
        else
        {
          strFeedback = "<html>";
        }

        IIssue pi = issues.get( i );
        strFeedback += "&nbsp;" + HTMLEscapeUtil.escape( pi.getMessage() );
      }
      return strFeedback;
    }

    private List<IIssue> getErrorsFromCursorPos()
    {
      if( _editor == null )
      {
        return Collections.emptyList();
      }
      List<IIssue> matches = new ArrayList<>();
      //noinspection ThrowableResultOfMethodCallIgnored
      IIssueContainer pe = _editor.getIssues();
      if( pe == null )
      {
        return matches;
      }

      int iLineCount = getLineCount();
      List<IIssue> pes = pe.getIssues();
      for( int i = pes.size() - 1; i >= 0; i-- )
      {
        IIssue e = pes.get( i );
        int iMark = getMarkForLine( e.getLine(), iLineCount );
        if( iMark >= _iMousePos.y - 3 && iMark <= _iMousePos.y + 3 )
        {
          matches.add( e );
        }
      }
      return matches;
    }

    private List<Highlighter.Highlight> getHighlightsFromCursorPos()
    {
      if( _editor == null )
      {
        return Collections.emptyList();
      }
      List<Highlighter.Highlight> matches = new ArrayList<>();

      int iLineCount = getLineCount();
      for( Highlighter.Highlight highlight : _editor.getEditor().getHighlighter().getHighlights() )
      {
        Highlighter.HighlightPainter painter = highlight.getPainter();
        if( painter == GosuEditor.LabHighlighter.TEXT || painter == GosuEditor.LabHighlighter.USAGE )
        {
          int line = _editor.getEditor().getDocument().getDefaultRootElement().getElementIndex( highlight.getStartOffset() );
          int iMark = getMarkForLine( line, iLineCount );
          if( iMark >= _iMousePos.y - 3 && iMark <= _iMousePos.y + 3 )
          {
            matches.add( highlight );
          }
        }
      }

      return matches;
    }

    private int getMarkForLine( int line, int iLineCount )
    {
      iLineCount--;
      if( iLineCount == 0 )
      {
        return getHeight() / 2;
      }
      int iLine = line - 1;
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

    private class MarkMouseHandler extends MouseAdapter implements MouseMotionListener
    {
      @Override
      public void mouseMoved( MouseEvent e )
      {
        _iMousePos = e.getPoint();
        List<IIssue> errors = getErrorsFromCursorPos();
        if( !errors.isEmpty() )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
        }
        else
        {
          List<Highlighter.Highlight> highlights = getHighlightsFromCursorPos();
          if( !highlights.isEmpty() )
          {
            setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
          }
          else
          {
            setCursor( Cursor.getDefaultCursor() );
          }
        }
      }

      @Override
      public void mouseClicked( MouseEvent e )
      {
        List<IIssue> errors = getErrorsFromCursorPos();
        if( !errors.isEmpty() )
        {
          _editor.getEditor().requestFocusInWindow();
          _editor.getEditor().setCaretPosition( errors.get( 0 ).getStartOffset() );
        }
        else
        {
          List<Highlighter.Highlight> highlights = getHighlightsFromCursorPos();
          if( !highlights.isEmpty() )
          {
            _editor.getEditor().requestFocusInWindow();
            _editor.getEditor().setCaretPosition( highlights.get( 0 ).getStartOffset() );
          }
        }
      }

      @Override
      public void mouseDragged( MouseEvent e )
      {
      }
    }
  }
}
