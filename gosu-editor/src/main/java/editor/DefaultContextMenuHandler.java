package editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

/**
 */
public class DefaultContextMenuHandler implements IContextMenuHandler<IScriptEditor>
{

  public JPopupMenu getContextMenu( IScriptEditor editor )
  {
    JPopupMenu menu = new JPopupMenu();
    menu.add( new JMenuItem( new ClipCutAction( editor ) ) );
    menu.add( new JMenuItem( new ClipCopyAction( editor ) ) );
    menu.add( new JMenuItem( new ClipPasteAction( editor ) ) );
    menu.add( new JSeparator() );
    menu.add( new JMenuItem( new CompleteCodeAction( editor ) ) );
    return menu;
  }

  public void displayContextMenu( IScriptEditor editor, int iXPos, int iYPos, Component eventSource )
  {
    editor.getEditor().requestFocus();
    try
    {
      Rectangle rcCaretBounds = editor.getEditor().modelToView( editor.getEditor().getCaretPosition() );
      getContextMenu( editor ).show( editor.getEditor(), rcCaretBounds.x, rcCaretBounds.y + rcCaretBounds.height );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  class ClipCopyAction extends AbstractAction implements ClipboardOwner
  {
    IScriptEditor _editor;

    public ClipCopyAction( IScriptEditor editor )
    {
      super( "Copy" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'C' ) );
      _editor = editor;
    }


    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      String strText = _editor.getEditor().getSelectedText();
      return strText != null && strText.length() > 0;
    }

    public void actionPerformed( ActionEvent e )
    {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      String strText = _editor.getEditor().getSelectedText();
      if( strText != null && strText.length() > 0 )
      {
        clipboard.setContents( new StringSelection( strText ), this );
      }
    }
  }

  class ClipPasteAction extends AbstractAction implements ClipboardOwner
  {
    private IScriptEditor _editor;

    public ClipPasteAction( IScriptEditor editor )
    {
      super( "Paste" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'P' ) );
      _editor = editor;
    }

    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable t = clipboard.getContents( this );
      return t != null && t.isDataFlavorSupported( DataFlavor.stringFlavor );
    }

    public void actionPerformed( ActionEvent e )
    {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable t = clipboard.getContents( this );
      if( t != null && t.isDataFlavorSupported( DataFlavor.stringFlavor ) )
      {
        try
        {
          String strText = (String)t.getTransferData( DataFlavor.stringFlavor );
          _editor.getEditor().replaceSelection( strText );
        }
        catch( Exception ex )
        {
          throw new RuntimeException( ex );
        }
      }
    }
  }

  class ClipCutAction extends ClipCopyAction
  {
    public ClipCutAction( IScriptEditor editor )
    {
      super( editor );
      putValue( Action.NAME, "Cut" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'T' ) );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      super.actionPerformed( e );
      _editor.getEditor().replaceSelection( "" );
    }
  }

  class CompleteCodeAction extends AbstractAction implements ClipboardOwner
  {
    private IScriptEditor _editor;

    public CompleteCodeAction( IScriptEditor editor )
    {
      super( "Complete" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'D' ) );
      _editor = editor;
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
      if( !_editor.displayValueCompletionAtCurrentLocation() )
      {
        _editor.handleCompleteCode();
      }
    }
  }
}
