package editor.undo;

import javax.swing.undo.StateEdit;

/**
 */
public class StagedStateEdit extends StateEdit implements IStagedStateEdit
{
  public StagedStateEdit( IStagedStateEditable anObject, String name )
  {
    super( anObject, name );
  }

  public IStagedStateEditable getStagedUndoState()
  {
    return (IStagedStateEditable)object;
  }

  public IStagedStateEditable getStagedRedoState()
  {
    return getStagedUndoState();
  }
}