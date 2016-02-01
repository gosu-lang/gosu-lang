package editor;

import gw.lang.parser.ISymbolTable;

/**
 */
public interface IPathCompletionHandler
{
  public void setGosuEditor( GosuEditor gsEditor );

  public GosuEditor getGosuEditor();

  /**
   * @param transientSymTable The symbol table corresponding with the scope at
   *                          the editor's caret position.
   *
   * @return True if this handler handled path completion. Note it is the
   * handler's responsibility to determine whether or not it can handle
   * completing the path. If it can't, it should return false and have no
   * side effects on the system.
   */
  public boolean handleCompletePath( ISymbolTable transientSymTable );
}
