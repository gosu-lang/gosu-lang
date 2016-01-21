package editor;

import java.util.List;

/**
 * Interface description...
 *
 * @author jwilliams
 */
public interface ISelectionPopup
{
  public void setSelection( String strSelection );

  public List<String> getPopupSuggestions();
}