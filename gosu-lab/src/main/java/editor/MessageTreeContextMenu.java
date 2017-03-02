package editor;

import editor.util.SmartMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

/**
 */
public class MessageTreeContextMenu implements IContextMenuHandler<MessageTree>
{
  public MessageTreeContextMenu()
  {
  }

  public JPopupMenu getContextMenu( MessageTree message )
  {
    JPopupMenu menu = new JPopupMenu();
    if( message.getNode().hasTarget() )
    {
      menu.add( new SmartMenuItem( new OpenAction( message ) ) );
      menu.add( new JSeparator() );
    }
    menu.add( new SmartMenuItem( new ClipCopyAction( message.getText() ) ) );
    return menu;
  }

  public void displayContextMenu( MessageTree message, int x, int y, Component eventSource )
  {
    message.getTree().requestFocus();
    getContextMenu( message ).show( message.getTree(), x, y );
  }

  class OpenAction extends AbstractAction implements ClipboardOwner
  {
    MessageTree _message;

    public OpenAction( MessageTree message )
    {
      super( "Open" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'O' ) );
      _message = message;
    }


    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      return true;
    }

    public void actionPerformed( ActionEvent e )
    {
      _message.getNode().jumpToTarget();
    }
  }

  class ClipCopyAction extends AbstractAction implements ClipboardOwner
  {
    String _message;

    public ClipCopyAction( String message )
    {
      super( "Copy" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'C' ) );
      _message = message;
    }


    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      return true;
    }

    public void actionPerformed( ActionEvent e )
    {
      Clipboard clipboard = RunMe.getEditorFrame().getGosuPanel().getClipboard();
      clipboard.setContents( new StringSelection( _message ), this );
    }
  }
}
