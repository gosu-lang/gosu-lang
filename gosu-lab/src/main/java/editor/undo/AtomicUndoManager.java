/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor.undo;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;


/**
 * This class extends <code>UndoManager</code> providing nested atomic (or scoped) undo/redo
 * operations.  Note undo/redo operations are captured in <code>StateEditable</code> implementations.
 * <p/>
 * A generic application of this class follows:
 * <p/>
 * <pre>
 * ...
 * _undoMgr = new AtomicUndoManager( 1000 );
 * ...
 * //
 * // This method decorates a typical change operation with undo/redo capability.
 * // Notice the XXXOperationUndoItem class.  You'll need to create one of these
 * // for each undo operation.  Basically these UndoItem classes are extensions
 * // of <code>StateEditable</code>.  They are responsible for capturing the state
 * // before and after an operation and also for restoring the state as directed
 * // by this undo manager.
 * //
 * void performUndoableXXXOperation( ... )
 * {
 *   XXXOperationUndoItem undoItem = new XXXOperationUndoItem( ... );
 *   StateEdit transaction = new StateEdit( undoItem, "XXX Operation" );
 *
 *   performXXXOperation( ... );
 *
 *   transaction.end();
 *   _undoMgr.addEdit( transaction );
 * }
 *
 * // Your typical change operation.
 * void performXXXOperation( ... )
 * {
 *   // do whatever it is you do...
 * }
 *
 * //
 * // This method exercises the atomic and nested features of AtomicUndoManager.
 * // The atomic boundaries should be enforced with the try/finally idiom so
 * // a failed atomic operation can be safely (and optionally) "rolled back".
 * //
 * void performUndoableZZZOperation
 * {
 *   try
 *   {
 *     _undoMgr.beginUndoAtom( "ZZZ Operation" );
 *
 *     performUndoableXXXOperation( ... );
 *     performUndoableYYYOperation( ... );
 *     ...
 *   }
 *   finally
 *   {
 *     _undoMgr.endUndoAtom();
 *   }
 * }
 *
 *
 * //
 * // Create ui components using Actions such as these:
 * //
 * class UndoAction extends AbstractAction
 * {
 *   UndoAction( String strName, Icon icon )
 *   {
 *     super( strName, icon );
 *   }
 *
 *   public void actionPerformed( ActionEvent e )
 *   {
 *     _undoMgr.undo();
 *   }
 *
 *   public boolean isEnabled()
 *   {
 *     return _undoMgr.canUndo();
 *   }
 * }
 *
 * class RedoAction extends AbstractAction
 * {
 *   RedoAction( String strName, Icon icon )
 *   {
 *     super( strName, icon );
 *   }
 *
 *   public void actionPerformed( ActionEvent e )
 *   {
 *     _undoMgr.redo();
 *   }
 *
 *   public boolean isEnabled()
 *   {
 *     return _undoMgr.canRedo();
 *   }
 * }</pre>
 */
public class AtomicUndoManager extends UndoManager
{
  /**
   * A stack used for managing nested undo atoms. Whenever an atomic undo atom
   * begins a new <code>DisplayableCompoundEdit</code> is constructed on it's
   * behalf and pushed onto the stack. When an undo atom ends its <code>DisplayableCompoundEdit</code>
   * is popped off the stack and added to the next atom's edit list in the stack or,
   * if the stack is empty, it is directly added to this undo manager's edit list.
   */
  private Stack<DisplayableCompoundEdit> _undoAtomNest;
  private boolean _bPaused;
  private List<ChangeListener> _changeListeners;
  private long _lLastUndoTime;

  /**
   * An <code>AtomicUndoManager</code> with a default limit of 1000 edits.
   */
  public AtomicUndoManager()
  {
    this( 1000 );
  }

  /**
   * @param iUndoLimit The maximum number of edits this undo manager will hold.
   */
  public AtomicUndoManager( int iUndoLimit )
  {
    super();

    setLimit( iUndoLimit );
    _undoAtomNest = new Stack<DisplayableCompoundEdit>();
    _changeListeners = new ArrayList<ChangeListener>();
  }

  /**
   * @return Is this undo manager in a Paused state? i.e., are edits being ignored?
   */
  public boolean isPaused()
  {
    return _bPaused;
  }

  /**
   * Sets the paused state of the undo manager.  If paused, undo operations are ignored
   *
   * @param bPaused The paused state.
   */
  public void setPaused( boolean bPaused )
  {
    _bPaused = bPaused;
  }

  /**
   * @param edit The edit to be added.  Note nested edits are supported.
   */
  @Override
  public synchronized boolean addEdit( UndoableEdit edit )
  {
    if( isPaused() )
    {
      // Ignore edits while paused...

      return false;
    }

    fireChangeEvent( UndoChangeEvent.ChangeType.ADD_EDIT, edit );
    DisplayableCompoundEdit undoAtom = getUndoAtom();
    if( undoAtom != null )
    {
      return undoAtom.addEdit( edit );
    }

    return super.addEdit( edit );
  }

  /**
   * @param edit The edit to be removed.
   */
  public synchronized void removeEdit( UndoableEdit edit )
  {
    int iIndex = edits.indexOf( edit );
    if( iIndex < 0 )
    {
      return;
    }

    trimEdits( iIndex, iIndex );
  }

  @Override
  public synchronized void discardAllEdits()
  {
    super.discardAllEdits();
    _bPaused = false;
    _undoAtomNest.clear();
  }

  /**
   * Begin an atomic undo boundary.  Edits added to this undo mananger after
   * this call are considered "subatomic" -- they become part of the atom
   * defined by the boundary. The atom's boundary ends with a call to <code>endUndoAtom()</code>.
   * <p/>
   * Note undo atoms can contain other undo atoms allowing for unlimited nesting of atomic
   * undo operations.
   */
  public void beginUndoAtom()
  {
    beginUndoAtom( null );
  }

  /**
   * Begin an atomic undo boundary.  Edits added to this undo mananger after
   * this call are considered "subatomic" -- they become part of the atom
   * defined by the boundary. The atom's boundary ends with a call to <code>endUndoAtom()</code>.
   * <p/>
   * Note undo atoms can contain other undo atoms allowing for unlimited nesting of atomic
   * undo operations.
   *
   * @param strDisplayName The name associated with the undo atom. A user interface typically
   *                       displays this name in Undo/Redo menu items or toolbar button text.
   *
   * @see javax.swing.undo.UndoManager#getUndoPresentationName
   */
  public CompoundEdit beginUndoAtom( String strDisplayName )
  {
    if( isPaused() )
    {
      // Ignore edits while paused...

      return null;
    }

    CompoundEdit parent = null;
    try
    {
      parent = _undoAtomNest.peek();
    }
    catch( EmptyStackException empty )
    {
      // ignore
    }

    parent = parent == null ? this : parent;
    DisplayableCompoundEdit undoAtom = new DisplayableCompoundEdit( strDisplayName );
    parent.addEdit( undoAtom );
    _undoAtomNest.push( undoAtom );
    return undoAtom;
  }

  /**
   * Ends the active atomic undo boundary.
   *
   * @see #beginUndoAtom
   */
  public void endUndoAtom()
  {
    endUndoAtom( null );
  }

  public void endUndoAtom( CompoundEdit undoAtom )
  {
    if( isPaused() )
    {
      // Ignore edits while paused...

      return;
    }

    try
    {
      CompoundEdit csr;
      do
      {
        csr = _undoAtomNest.pop();
        csr.end();
      } while( undoAtom != null && csr != undoAtom );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  /**
   * @return The undo atom currently in progress.
   */
  public DisplayableCompoundEdit getUndoAtom()
  {
    DisplayableCompoundEdit undoAtom = null;
    try
    {
      undoAtom = _undoAtomNest.peek();
    }
    catch( EmptyStackException empty )
    {
      // ignore
    }

    return undoAtom;
  }

  @Override
  public synchronized void redo() throws CannotRedoException
  {
    setLastUndoTime();
    try
    {
      fireChangeEvent( UndoChangeEvent.ChangeType.REDO, null );
      UndoableEdit edit = editToBeRedone();
      if( edit instanceof IStagedStateEdit )
      {
        IStagedStateEditable stagedState = ((IStagedStateEdit)edit).getStagedRedoState();
        if( stagedState != null && !stagedState.prepareForRedo() )
        {
          return;
        }
      }
      super.redo();
    }
    finally
    {
      setLastUndoTime();
    }
  }

  @Override
  public synchronized void undo() throws CannotUndoException
  {
    setLastUndoTime();
    try
    {
      fireChangeEvent( UndoChangeEvent.ChangeType.UNDO, null );
      UndoableEdit edit = editToBeUndone();
      if( edit instanceof IStagedStateEdit )
      {
        IStagedStateEditable stagedState = ((IStagedStateEdit)edit).getStagedUndoState();
        if( stagedState != null && !stagedState.prepareForUndo() )
        {
          return;
        }
      }
      super.undo();
    }
    finally
    {
      setLastUndoTime();
    }
  }

  public long getLastUndoTime()
  {
    return _lLastUndoTime;
  }

  private void setLastUndoTime()
  {
    _lLastUndoTime = System.currentTimeMillis();
  }

  @Override
  public synchronized void undoOrRedo() throws CannotRedoException, CannotUndoException
  {
    fireChangeEvent( UndoChangeEvent.ChangeType.UNDO_OR_REDO, null );
    super.undoOrRedo();
  }

  @Override
  protected void undoTo( UndoableEdit edit ) throws CannotUndoException
  {
    fireChangeEvent( UndoChangeEvent.ChangeType.UNDO_TO, edit );
    super.undoTo( edit );
  }

  @Override
  protected void redoTo( UndoableEdit edit ) throws CannotRedoException
  {
    fireChangeEvent( UndoChangeEvent.ChangeType.REDO_TO, edit );
    super.redoTo( edit );
  }

  public void addChangeListener( ChangeListener listener )
  {
    _changeListeners.add( listener );
  }

  public void removeChangeListener( ChangeListener listener )
  {
    _changeListeners.remove( listener );
  }

  /**
   * Utility method that records a change that can be undone/redone.
   */
  public void recordChange( StateEditable change )
  {
    beginUndoAtom();
    try
    {
      StateEdit transaction = new StateEdit( change );
      transaction.end();
      addEdit( transaction );
    }
    finally
    {
      endUndoAtom();
    }
  }

  private void fireChangeEvent( UndoChangeEvent.ChangeType type, UndoableEdit edit )
  {
    ChangeListener[] listeners = _changeListeners.toArray( new ChangeListener[_changeListeners.size()] );
    if( listeners.length == 0 )
    {
      return;
    }

    ChangeEvent e = new UndoChangeEvent( this, type, edit );
    for( int i = 0; i < listeners.length; i++ )
    {
      ChangeListener listener = listeners[i];
      listener.stateChanged( e );
    }
  }


  /**
   */
  static class DisplayableCompoundEdit extends CompoundEdit implements IStagedStateEdit
  {
    String _strDisplayName;

    DisplayableCompoundEdit()
    {
      this( null );
    }

    DisplayableCompoundEdit( String strDisplayName )
    {
      super();
      _strDisplayName = strDisplayName;
    }

    @Override
    public boolean isInProgress()
    {
      return false;
    }

    @Override
    public IStagedStateEditable getStagedUndoState()
    {
      UndoableEdit edit = lastEdit();
      return edit instanceof IStagedStateEdit ? ((IStagedStateEdit)edit).getStagedUndoState() : null;
    }

    @Override
    public IStagedStateEditable getStagedRedoState()
    {
      UndoableEdit edit = edits.size() > 0 ? edits.elementAt( 0 ) : null;
      return edit instanceof IStagedStateEdit ? ((IStagedStateEdit)edit).getStagedRedoState() : null;
    }

    @Override
    public String getPresentationName()
    {
      if( _strDisplayName != null && _strDisplayName.length() > 0 )
      {
        return _strDisplayName;
      }

      return super.getPresentationName();
    }

    @Override
    public String getUndoPresentationName()
    {
      String strDisplayName = getPresentationName();

      if( strDisplayName != null && strDisplayName.length() > 0 )
      {
        return UndoName + " " + strDisplayName;
      }

      return super.getUndoPresentationName();
    }

    @Override
    public String getRedoPresentationName()
    {
      String strDisplayName = getPresentationName();

      if( strDisplayName != null && strDisplayName.length() > 0 )
      {
        return RedoName + " " + strDisplayName;
      }

      return super.getRedoPresentationName();
    }
  }
}