package editor;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ScopeHighlighter implements DocumentListener, CaretListener, FocusListener
{
  private GosuEditor _editor;
  private Object _highlightTag;
  private Object _highlightTag2;
  private Runnable _highlightImmediately;
  private Timer _timer;
  private static final GosuEditor.LabHighlighter HIGHLIGHTER = new GosuEditor.LabHighlighter( Scheme.active().scopeHighlightColor() );

  public ScopeHighlighter( GosuEditor gosuEditor )
  {
    _editor = gosuEditor;
    _highlightImmediately = this::highlightImmediately;
    _timer = new Timer( 300, e -> highlightUsagesUnderCaret() );
    _timer.setRepeats( false );
  }

  public void updateState()
  {
    SwingUtilities.invokeLater( _highlightImmediately );
  }

  private void highlightImmediately()
  {
    JEditorPane editor = _editor.getEditor();

    if( _highlightTag != null )
    {
      editor.getHighlighter().removeHighlight( _highlightTag );
      editor.getHighlighter().removeHighlight( _highlightTag2 );
      _highlightTag = null;
      _highlightTag2 = null;
    }

    if( !editor.hasFocus() || editor.getSelectionStart() != editor.getSelectionEnd() )
    {
      return;
    }

   //## still need to tweak and test this feature
   // highlightUsages();

    int caret = editor.getCaretPosition();
    try
    {
      Document doc = editor.getDocument();
      String forwardChar = positionInDoc( caret, doc ) ? doc.getText( caret, 1 ) : "";
      String backwardsChar = positionInDoc( caret - 1, doc ) ? doc.getText( caret - 1, 1 ) : "";
      if( forwardChar.equals( "{" ) )
      {
        highlightMatchForward( editor, caret, forwardChar, "}" );
      }
      else if( forwardChar.equals( "(" ) )
      {
        highlightMatchForward( editor, caret, forwardChar, ")" );
      }
      else if( forwardChar.equals( "[" ) )
      {
        highlightMatchForward( editor, caret, forwardChar, "]" );
      }
      else if( backwardsChar.equals( "}" ) )
      {
        highlightMatchBackwards( editor, caret - 1, backwardsChar, "{" );
      }
      else if( backwardsChar.equals( ")" ) )
      {
        highlightMatchBackwards( editor, caret - 1, backwardsChar, "(" );
      }
      else if( backwardsChar.equals( "]" ) )
      {
        highlightMatchBackwards( editor, caret - 1, backwardsChar, "[" );
      }
    }
    catch( BadLocationException e )
    {
      //ignore
    }
  }

  private void highlightUsages()
  {
    if( _timer.isRunning() )
    {
      _timer.stop();
      _timer.restart();
    }
    else
    {
      _timer.start();
    }
  }
  private void highlightUsagesUnderCaret()
  {
    if( !_editor.isVisible() )
    {
      return;
    }

    if( _editor.isCompletionPopupShowing() || _editor.isCompleteCode() )
    {
      return;
    }

    GosuEditor.postTaskInParserThread( () -> EventQueue.invokeLater( _editor::highlightUsagesOfFeatureUnderCaret ) );
  }

  private boolean positionInDoc( int caret, Document doc )
  {
    return caret >= 0 && caret < doc.getLength();
  }

  private void highlightMatchForward( JEditorPane editor, int i, String c, String oppositeString ) throws BadLocationException
  {
    Document document = editor.getDocument();
    int count = 1;
    int initial = i;
    i = i + 1;
    while( i < document.getLength() )
    {
      String text = document.getText( i, 1 );
      if( text.equals( c ) )
      {
        count++;
      }
      else if( text.equals( oppositeString ) )
      {
        count--;
      }

      if( count == 0 )
      {
        if( i > initial + 1 ) // don't highlight if they are adjacent; it's irritating
        {
          _highlightTag = editor.getHighlighter().addHighlight( i, i + 1, HIGHLIGHTER );
          _highlightTag2 = editor.getHighlighter().addHighlight( initial, initial + 1, HIGHLIGHTER );
        }
        break;
      }
      else
      {
        i++;
      }
    }
  }

  private void highlightMatchBackwards( JEditorPane editor, int i, String c, String oppositeString ) throws BadLocationException
  {
    Document document = editor.getDocument();
    int count = 1;
    int initial = i;
    i = i - 1;
    while( i > 0 )
    {
      String text = document.getText( i, 1 );
      if( text.equals( c ) )
      {
        count++;
      }
      else if( text.equals( oppositeString ) )
      {
        count--;
      }

      if( count == 0 )
      {
        if( i + 1 < initial ) // don't highlight if they are adjacent; it's irritating
        {
          _highlightTag = editor.getHighlighter().addHighlight( i, i + 1, HIGHLIGHTER );
          _highlightTag2 = editor.getHighlighter().addHighlight( initial, initial + 1, HIGHLIGHTER );
        }
        break;
      }
      else
      {
        i--;
      }
    }
  }

  public void insertUpdate( DocumentEvent e )
  {
    updateState();
  }

  public void removeUpdate( DocumentEvent e )
  {
    updateState();
  }

  public void changedUpdate( DocumentEvent e )
  {
  }

  public void caretUpdate( CaretEvent e )
  {
    highlightImmediately();
  }

  public void focusGained( FocusEvent e )
  {
    highlightImmediately();
  }

  public void focusLost( FocusEvent e )
  {
    highlightImmediately();
  }
}
