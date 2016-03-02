/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import editor.util.TextComponentUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.JTextComponent;
import java.awt.*;


public abstract class AbstractPopup extends JPopupMenu implements IValuePopup
{
  private EventListenerList _nodeListenerList;
  private JTextComponent _editor;


  public AbstractPopup( JTextComponent editor )
  {
    super();
    _editor = editor;
    _nodeListenerList = new EventListenerList();
  }

  public abstract void setValue( Object value );

  public abstract void refresh();

  protected abstract void registerListeners();

  protected abstract void unregisterListeners();

  public JTextComponent getEditor()
  {
    return _editor;
  }

  @Override
  public void setVisible( boolean bVisible )
  {
    boolean wasVisible = isVisible();
    super.setVisible( bVisible );

    if( _editor == null )
    {
      return;
    }

    if( !wasVisible && bVisible )
    {
      registerListeners();
      editor.util.EditorUtilities.removePopupBorder( this );
    }
    else if( wasVisible && !bVisible )
    {
      unregisterListeners();
      _editor.requestFocus();
    }
  }

  @Override
  public void addNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.add( ChangeListener.class, l );
  }

  public void removeNodeChangeListener( ChangeListener l )
  {
    _nodeListenerList.remove( ChangeListener.class, l );
  }

  protected void fireNodeChanged( final ChangeEvent e )
  {
    // NOTE pdalbora 7-Apr-2008 -- This used to always fire the NodeChanged event in an
    // invokeLater(), but this was causing CC-39860: an event would fire when the user
    // clicked another cell in the table. All cells in a column share the same text editor.
    // So, the editor would be enabled on the new cell, and *then* the listener that sets
    // the value in the editor from that in the popup would run, because it had been queued
    // at the end of the event queue.
    if( SwingUtilities.isEventDispatchThread() )
    {
      fireNodeChangedNow( _nodeListenerList, e );
    }
    else
    {
      // TODO pdalbora 7-Apr-2008 -- Will this ever *not* be called on the UI thread? I.e.,
      // do we ever really need to do this in an invokeLater()?
      EventQueue.invokeLater( new Runnable()
      {
        @Override
        public void run()
        {
          fireNodeChangedNow( _nodeListenerList, e );
        }
      } );
    }
  }

  private void fireNodeChangedNow( EventListenerList list, ChangeEvent e )
  {
    // Guaranteed to return a non-null array
    Object[] listeners = list.getListenerList();

    // Process the listeners last to first,
    // notifying those that are interested in this event
    for( int i = listeners.length - 2; i >= 0; i -= 2 )
    {
      if( listeners[i] == ChangeListener.class )
      {
        ((ChangeListener)listeners[i + 1]).stateChanged( e );
      }
    }
  }

  protected String filterDisplay()
  {
    String strPrefix = TextComponentUtil.getWordAtCaret( _editor );
    if( strPrefix != null && strPrefix.length() > 0 &&
        (Character.isWhitespace( strPrefix.charAt( 0 ) ) || !Character.isJavaIdentifierPart( strPrefix.charAt( 0 ) )) )
    {
      strPrefix = TextComponentUtil.getWordBeforeCaret( _editor );
      if( strPrefix != null && strPrefix.length() > 0 &&
          (Character.isWhitespace( strPrefix.charAt( 0 ) ) || !Character.isJavaIdentifierPart( strPrefix.charAt( 0 ) )) )
      {
        strPrefix = null;
      }
    }

    return strPrefix;
  }
}