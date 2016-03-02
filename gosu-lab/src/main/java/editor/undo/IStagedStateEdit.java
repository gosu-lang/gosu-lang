package editor.undo;

import javax.swing.undo.UndoableEdit;

/**
 */
public interface IStagedStateEdit extends UndoableEdit
{
  public IStagedStateEditable getStagedUndoState();

  public IStagedStateEditable getStagedRedoState();
}