package editor.search;

import editor.FileTree;

/**
 */
public class SearchDialog extends AbstractSearchDialog
{
  public static State STATE = null;

  public SearchDialog( FileTree searchDir, boolean bReplace )
  {
    super( searchDir, bReplace, bReplace ? "Replace in Path" : "Find in Path" );
  }

  @Override
  protected State getState()
  {
    return STATE;
  }
  @Override
  protected void setState( State state )
  {
    STATE = state;
  }
}
