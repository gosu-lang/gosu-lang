package editor.tabpane;

import editor.util.IEditableLabel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 */
public class SimpleLabel implements IEditableLabel
{
  private String _strText;
  private Icon _icon;
  private EventListenerList _changeListeners;

  public SimpleLabel( String strText, Icon icon )
  {
    innerSetDisplayName(strText);
    _icon = icon;
    _changeListeners = new EventListenerList();
  }

  @Override
  public String getDisplayName() {

    return _strText;
  }

  @Override
  public void setDisplayName( String strName )
  {
    innerSetDisplayName(strName);
    fireChanged();
  }

  private void innerSetDisplayName(String strText) {
      _strText = strText;
  }

  @Override
  public Icon getIcon( int iTypeFlags )
  {
    return _icon;
  }
  @Override
  public void setIcon( Icon icon, int iTypeFlags )
  {
    _icon = icon;
    fireChanged();
  }

  @Override
  public void addChangeListener( ChangeListener l )
  {
    _changeListeners.add( ChangeListener.class, l );
  }
  @Override
  public void removeChangeListener( ChangeListener l )
  {
    _changeListeners.remove( ChangeListener.class, l );
  }
  private void fireChanged()
  {
    Object[] listeners = _changeListeners.getListenerList();
    if( listeners.length == 0 )
    {
      return;
    }

    ChangeEvent e = new ChangeEvent( this );
    for( int i = listeners.length - 2; i >= 0; i -= 2 )
    {
      if( listeners[i] == ChangeListener.class )
      {
        ((ChangeListener)listeners[i + 1]).stateChanged( e );
      }
    }
  }
}
