package editor.util;

import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
 */
public interface IEditableLabel extends ILabel
{
  public void setDisplayName( String strDisplayName );
  public void setIcon( Icon icon, int iIconType );

  public void addChangeListener( ChangeListener l );
  public void removeChangeListener( ChangeListener l );
}
