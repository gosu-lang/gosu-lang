package editor.search;

import editor.IContextMenuHandler;
import editor.LabFrame;
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
public class SearchTreeContextMenu implements IContextMenuHandler<SearchTree>
{
  public SearchTreeContextMenu()
  {
  }

  public JPopupMenu getContextMenu( SearchTree search )
  {
    JPopupMenu menu = new JPopupMenu();
    if( search.getNode().hasTarget() )
    {
      menu.add( new SmartMenuItem( new OpenAction( search ) ) );
      menu.add( new JSeparator() );
    }
    menu.add( new SmartMenuItem( new ClipCopyAction( search.getText() ) ) );
    return menu;
  }

  public void displayContextMenu( SearchTree search, int x, int y, Component eventSource )
  {
    search.getTree().requestFocus();
    getContextMenu( search ).show( search.getTree(), x, y );
  }

  class OpenAction extends AbstractAction implements ClipboardOwner
  {
    SearchTree _search;

    public OpenAction( SearchTree search )
    {
      super( "Open" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'O' ) );
      _search = search;
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
      _search.getNode().jumpToTarget();
    }
  }

  class ClipCopyAction extends AbstractAction implements ClipboardOwner
  {
    String _search;

    public ClipCopyAction( String search )
    {
      super( "Copy" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'C' ) );
      _search = search;
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
      Clipboard clipboard = LabFrame.instance().getGosuPanel().getClipboard();
      clipboard.setContents( new StringSelection( _search ), this );
    }
  }
}
