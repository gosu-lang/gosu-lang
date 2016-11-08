package editor;

import editor.undo.AtomicUndoManager;
import editor.undo.StagedStateEdit;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.StateEdit;
import java.awt.*;

/**
 */
public class ScriptChangeHandler implements UndoableEditListener, CaretListener
{
  private AtomicUndoManager _undoMgr;
  private JTextComponent _editor;
  private boolean _bDirty;
  private boolean _bPaused;
  private int _iLineNumber;
  private boolean _bLineChanged;
  private int _iBefore;
  private int _iAfter;
  private boolean _bCaretPosChangeIsEdit;
  private DocumentEvent.EventType _eventType;

  public ScriptChangeHandler( AtomicUndoManager undoMgr )
  {
    _undoMgr = undoMgr;
    _bDirty = false;
  }

  public void establishUndoableEditListener( EditorHost editor )
  {
    editor.setUndoableEditListener( this );
    editor.getEditor().addCaretListener( this );
    _editor = editor.getEditor();
  }

  public void establishUndoableEditListener( JTextComponent editor )
  {
    editor.getDocument().addUndoableEditListener( this );
    editor.addCaretListener( this );
    _editor = editor;
  }

  @Override
  public void undoableEditHappened( UndoableEditEvent e )
  {
    addEditorUndoItem( e );
  }

  @Override
  public void caretUpdate( CaretEvent e )
  {
    if( isPaused() )
    {
      return;
    }

    updatePositionInfo();

    _bCaretPosChangeIsEdit = false;

    final CompoundEdit undoAtom = _undoMgr.getUndoAtom();
    if( undoAtom != null && undoAtom.getPresentationName().equals( "Text Change" ) )
    {
      // Note we invokeLater because undoableEditHappened is called directly after
      // caretUpdate() i.e., it has to set _bCaretPosChangeIsEdit to true in order for us to
      // know whether or not an actual undoable edit is related to the caret pos change.

      EventQueue.invokeLater(
        () -> {
          if( !_bCaretPosChangeIsEdit )
          {
            if( undoAtom == _undoMgr.getUndoAtom() )
            {
              // The caret moved without a text change e.g., arrow key pressed,
              // so terminate the undo batch with the position change.
              _undoMgr.endUndoAtom();
            }
          }
        } );
    }
  }

  void updatePositionInfo()
  {
    _iBefore = _iAfter;
    _iAfter = _editor.getCaretPosition();
    int iCurrentLine = _editor.getDocument().getDefaultRootElement().getElementIndex( _iAfter );
    _bLineChanged = iCurrentLine != _iLineNumber;
    _iLineNumber = iCurrentLine;
  }

  public int getBefore()
  {
    return _iBefore;
  }

  public int getAfter()
  {
    return _iAfter;
  }

  protected void setDirty( boolean bDirty )
  {
    _bDirty = bDirty;
  }

  public boolean isDirty()
  {
    return _bDirty;
  }

  protected void setPaused( boolean bPaused )
  {
    _bPaused = bPaused;
  }

  public boolean isPaused()
  {
    return _bPaused;
  }

  public JTextComponent getEditor()
  {
    return _editor;
  }

  protected void addEditorUndoItem( UndoableEditEvent e )
  {
    if( isPaused() )
    {
      return;
    }

    // Set _bCaretPosChangeIsEdit so caretUpdate() doesn't terminate the undo batch/atom
    _bCaretPosChangeIsEdit = true;

    CompoundEdit undoAtom = _undoMgr.getUndoAtom();

    if( e.getEdit() instanceof DocumentEvent )
    {
      DocumentEvent docEvent = (DocumentEvent)e.getEdit();
      try
      {
        if( docEvent.getLength() > 1 ||
            docEvent.getType() != _eventType ||
            _bLineChanged )
        {
          // If the undo event is a multi-character change e.g., something pasted in, we
          // want to treat it as a single undo item separate from a Script Change atom.
          // So we must end the current Script Change batch (if exists) before we add this
          // undo event.

          // Ditto if the event type changed e.g., changed from inserting text to deleting text.
          // So if you start backspacing in the editor you essentially get a new undo item for the
          // group of characters you backspace over until you start typing again.

          undoAtom = _undoMgr.getUndoAtom();
          if( undoAtom != null && undoAtom.getPresentationName().equals( "Text Change" ) )
          {
            _undoMgr.endUndoAtom();
            undoAtom = null; // to start a new undo atom for when we change event types
          }

          if( docEvent.getLength() > 1 )
          {
            // We always put a multi-character change in a single batch
            addDocEditChange( e );
            return;
          }
        }
      }
      finally
      {
        _eventType = docEvent.getType();
      }
    }

    if( undoAtom == null || !undoAtom.getPresentationName().equals( "Text Change" ) )
    {
      // There is no Script Change undo atom. Add a new one to collect subsequent text changes

      _undoMgr.beginUndoAtom( "Text Change" );

      final CompoundEdit newUndoAdtom = _undoMgr.getUndoAtom();
      _undoMgr.addChangeListener(
        new ChangeListener()
        {
          @Override
          public void stateChanged( ChangeEvent e )
          {
            if( isPaused() )
            {
              return;
            }
            if( _undoMgr.getUndoAtom() == newUndoAdtom )
            {
              // End the Script Change undo atom/batch.
              // Since this ScriptChangeHandler isPaused() while script change undo items are added
              // to the undo mgr, we assume some other undo change happened which should terminate
              // the script change
              _undoMgr.removeChangeListener( this );
              _undoMgr.endUndoAtom();
            }
            else if( !newUndoAdtom.isInProgress() )
            {
              _undoMgr.removeChangeListener( this );
            }
          }
        } );
    }

    addDocEditChange( e );
  }

  private void addDocEditChange( UndoableEditEvent e )
  {
    ScriptEditorUndoItem undoItem = new ScriptEditorUndoItem( this, e.getEdit() );
    addUndoItem( new StagedStateEdit( undoItem, "Text Change" ) );
  }

  private void addUndoItem( StateEdit transaction )
  {
    setDirty( true );
    transaction.end();

    setPaused( true );
    try
    {
      _undoMgr.addEdit( transaction );
    }
    finally
    {
      setPaused( false );
    }
  }
}
