package editor;

import editor.undo.IStagedStateEditable;

import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoableEdit;
import java.util.Hashtable;


public class ScriptEditorUndoItem implements IStagedStateEditable
{
  private ScriptChangeHandler _sch;
  private boolean _bUndo;
  private int _iUndoPos;
  private int _iRedoPos;
  private UndoableEdit _docEdit;

  /**
   * @param sch     The ScriptChangeHandler context.
   * @param docEdit The source of the change.
   */
  public ScriptEditorUndoItem( ScriptChangeHandler sch, UndoableEdit docEdit )
  {
    _sch = sch;
    _docEdit = docEdit;
  }

  @Override
  public void storeState( Hashtable stateTable )
  {
    stateTable.put( "_undo", (_bUndo = !_bUndo) ? Boolean.TRUE : Boolean.FALSE );
    if( _bUndo )
    {
      _iUndoPos = _sch.getBefore();//editor.getCaretPosition();
    }
    else
    {
      _iRedoPos = _sch.getAfter();//editor.getCaretPosition();
    }
  }

  @Override
  public void restoreState( Hashtable stateTable )
  {
    _sch.getEditor().requestFocus();
    _sch.setPaused( true );
    try
    {
      boolean bUndo = ((Boolean)stateTable.get( "_undo" )).booleanValue();
      if( _docEdit != null )
      {
        if( bUndo )
        {
          _docEdit.undo();
        }
        else
        {
          _docEdit.redo();
        }
      }
      _sch.getEditor().setCaretPosition( bUndo ? _iUndoPos : _iRedoPos );
      _sch.updatePositionInfo();
    }
    catch( Exception e )
    {
      editor.util.EditorUtilities.handleUncaughtException( e );
    }
    finally
    {
      _sch.setPaused( false );
    }
  }

  @Override
  public boolean prepareForUndo()
  {
    // Ensure the caret is positioned *exactly after* where the edit was done.
    // We do this so that the user is prepared for the undo operation i.e., he
    // can see the operation happen in proximity.

    JTextComponent editor = _sch.getEditor();
    if( editor.getCaretPosition() != _iRedoPos )
    {
      editor.setCaretPosition( _iRedoPos );
      return false;
    }
    return true;
  }

  @Override
  public boolean prepareForRedo()
  {
    // Ensure the caret is positioned *exactly before* where the edit was done
    // We do this so that the user is prepared for the undo operation i.e., he
    // can see the operation happen in proximity.

    JTextComponent editor = _sch.getEditor();
    if( editor.getCaretPosition() != _iUndoPos )
    {
      editor.setCaretPosition( _iUndoPos );
      return false;
    }
    return true;
  }
}
