package editor.undo;

import javax.swing.undo.StateEditable;

/**
 */
public interface IStagedStateEditable extends StateEditable
{
  public boolean prepareForUndo();

  public boolean prepareForRedo();
}