/**
 */
package editor;

import javax.swing.event.ChangeListener;

public interface IValuePopup
{
  public void addNodeChangeListener( ChangeListener l );

  public boolean isVisible();
}