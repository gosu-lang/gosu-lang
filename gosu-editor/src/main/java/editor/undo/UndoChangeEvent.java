package editor.undo;

import javax.swing.event.ChangeEvent;
import javax.swing.undo.UndoableEdit;

public class UndoChangeEvent extends ChangeEvent
{

  private UndoableEdit _edit;
  private ChangeType _type;

  public enum ChangeType
  {
    ADD_EDIT,
    UNDO,
    REDO, UNDO_OR_REDO, UNDO_TO, REDO_TO,
  }

  public UndoChangeEvent( AtomicUndoManager atomicUndoManager, ChangeType type, UndoableEdit edit )
  {
    super( atomicUndoManager );
    _edit = edit;
    _type = type;
  }

  public UndoableEdit getEdit()
  {
    return _edit;
  }

  public ChangeType getType()
  {
    return _type;
  }

}