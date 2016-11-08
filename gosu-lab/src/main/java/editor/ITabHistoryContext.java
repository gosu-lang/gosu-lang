package editor;

import editor.util.ILabel;

/**
 */
public interface ITabHistoryContext extends ILabel
{
  /**
   * @return true if this history context represents the given view, false otherwise
   */
  public boolean represents( EditorHost editor );

  public Object getContentId();

  public boolean equals( ITabHistoryContext other );

  public int hashCode();
}
