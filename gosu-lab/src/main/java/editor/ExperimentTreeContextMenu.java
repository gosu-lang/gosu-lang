package editor;

import editor.util.EditorUtilities;
import java.nio.file.Path;
import gw.util.PathUtil;
import editor.util.PlatformUtil;
import editor.util.Experiment;
import editor.util.SmartMenu;
import editor.util.SmartMenuItem;
import gw.lang.reflect.IType;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.io.OutputStream;

/**
 */
public class ExperimentTreeContextMenu implements IContextMenuHandler<JTree>
{
  private static final String CUT_PREFIX = "-CUT-";
  private final Experiment _experiment;

  public ExperimentTreeContextMenu( Experiment experiment )
  {
    _experiment = experiment;
  }

  public JPopupMenu getContextMenu( JTree tree )
  {
    JPopupMenu menu = new JPopupMenu();
    JMenu newMenu = new SmartMenu( "New" );
    NewFilePopup.addMenuItems( newMenu );
    menu.add( newMenu );
    menu.add( new JSeparator() );
    menu.add( new SmartMenuItem( new OpenAction( tree ) ) );
    menu.add( new JSeparator() );
    menu.add( new SmartMenuItem( new OpenOnDesktopAction( tree ) ) );
    menu.add( new JSeparator() );
    menu.add( new SmartMenuItem( new ClipCutAction( tree ), false ) );
    menu.add( new SmartMenuItem( new ClipCopyAction( tree ), false ) );
    menu.add( new SmartMenuItem( new ClipPasteAction( tree ), false ) );
    menu.add( new JSeparator() );
    menu.add( new SmartMenuItem( new DeleteAction( tree ), false ) );
    menu.add( new JSeparator() );

    FileTree item = (FileTree)tree.getLastSelectedPathComponent();

    menu.add( new SmartMenuItem( new CommonMenus.FindInPathActionHandler( () -> item ) ) );
    menu.add( new SmartMenuItem( new CommonMenus.ReplaceInPathActionHandler( () -> item ) ) );

    menu.add( new JSeparator() );

    menu.add( new SmartMenuItem( new CommonMenus.FindUsagesInPathActionHandler( () -> item ) ) );

    if( item != null )
    {
      IType type = item.getType();
      if( EditorUtilities.isRunnable( type ) )
      {
        menu.add( new JSeparator() );
        menu.add( CommonMenus.makeRun( () -> _experiment.getOrCreateRunConfig( type ) ) );
        menu.add( CommonMenus.makeDebug( () -> _experiment.getOrCreateRunConfig( type ) ) );
      }
    }
    return menu;
  }

  public void displayContextMenu( JTree tree, int x, int y, Component eventSource )
  {
    tree.requestFocus();
    getContextMenu( tree ).show( tree, x, y );
  }

  class NewAction extends AbstractAction implements ClipboardOwner
  {
    JTree _tree;

    public NewAction( JTree tree )
    {
      super( "New..." );
      putValue( Action.MNEMONIC_KEY, new Integer( 'N' ) );
      _tree = tree;
    }


    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      return item != null && item.getParent() != null;
    }

    public void actionPerformed( ActionEvent e )
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      if( item != null )
      {
        NewFilePopup popup = new NewFilePopup();
        popup.show( LabFrame.instance().getGosuPanel(), 200, 200 );
      }
    }
  }

  class OpenAction extends AbstractAction implements ClipboardOwner
  {
    JTree _tree;

    public OpenAction( JTree tree )
    {
      super( "Open" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'O' ) );
      _tree = tree;
    }

    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      return item != null && item.isFile();
    }

    public void actionPerformed( ActionEvent e )
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      if( item != null )
      {
        _experiment.getGosuPanel().openFile( item.getFileOrDir(), true );
      }
    }
  }

  static class OpenOnDesktopAction extends AbstractAction implements ClipboardOwner
  {
    JTree _tree;

    private static String getFileManagerString()
    {
      if( PlatformUtil.isMac() )
      {
        return "Open in Finder";
      }
      if( PlatformUtil.isWindows() )
      {
        return "Open in Explorer";
      }
      return "Open in Path Manager";
    }

    public OpenOnDesktopAction( JTree tree )
    {
      super( getFileManagerString() );
      putValue( Action.MNEMONIC_KEY, new Integer( 'P' ) );
      _tree = tree;
    }


    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      return item != null;
    }

    public void actionPerformed( ActionEvent e )
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      if( item != null )
      {
        LabFrame.openFileOrDir( item.getFileOrDir() );
      }
    }
  }

  class ClipCopyAction extends AbstractAction implements ClipboardOwner
  {
    JTree _tree;

    public ClipCopyAction( JTree tree )
    {
      super( "Copy" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'C' ) );
      _tree = tree;
    }


    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      return item != null && !item.isSourcePathRoot() && item.getParent() != null;
    }

    public void actionPerformed( ActionEvent e )
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      if( item != null )
      {
        Clipboard clipboard = _experiment.getGosuPanel().getClipboard();
        clipboard.setContents( new StringSelection( PathUtil.getAbsolutePathName( item.getFileOrDir() ) ), this );
      }
    }
  }

  class ClipPasteAction extends AbstractAction implements ClipboardOwner
  {
    private JTree _tree;

    public ClipPasteAction( JTree tree )
    {
      super( "Paste" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'P' ) );
      _tree = tree;
    }

    public void lostOwnership( Clipboard clipboard, Transferable contents )
    {
    }

    @Override
    public boolean isEnabled()
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      if( item == null || item.getParent() == null )
      {
        return false;
      }

      Clipboard clipboard = _experiment.getGosuPanel().getClipboard();
      Transferable t = clipboard.getContents( this );
      try
      {
        if( t != null && t.isDataFlavorSupported( DataFlavor.stringFlavor ) )
        {
          String path = (String)t.getTransferData( DataFlavor.stringFlavor );
          if( path.startsWith( CUT_PREFIX ) )
          {
            path = path.substring( CUT_PREFIX.length() );
          }
          return PathUtil.exists( PathUtil.create( path ) );
        }
        return false;
      }
      catch( Exception e )
      {
        return false;
      }
    }

    public void actionPerformed( ActionEvent e )
    {
      Clipboard clipboard = _experiment.getGosuPanel().getClipboard();
      Transferable t = clipboard.getContents( this );
      if( t != null && t.isDataFlavorSupported( DataFlavor.stringFlavor ) )
      {
        try
        {
          FileTree selection = (FileTree)_tree.getLastSelectedPathComponent();
          String path = (String)t.getTransferData( DataFlavor.stringFlavor );
          boolean bCut = path.startsWith( CUT_PREFIX );
          if( bCut )
          {
            path = path.substring( CUT_PREFIX.length() );
          }
          Path source = PathUtil.create( path );
          Path target = PathUtil.isFile( selection.getFileOrDir() ) ? selection.getFileOrDir().getParent() : selection.getFileOrDir();
          Path newSource = PathUtil.create( PathUtil.getAbsolutePath( target ), PathUtil.getName( source ) );
          if( bCut )
          {
            if( !PathUtil.exists( newSource ) )
            {
              PathUtil.renameTo( source, newSource );
            }
          }
          else
          {
            copy( source, newSource );
          }
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
    public ClipCutAction( JTree tree )
    {
      super( tree );
      putValue( Action.NAME, "Cut" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'T' ) );
    }

    @Override
    public boolean isEnabled()
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      return item != null && !item.isSourcePathRoot() && item.getParent() != null;
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      if( item != null )
      {
        Clipboard clipboard = _experiment.getGosuPanel().getClipboard();
        clipboard.setContents( new StringSelection( CUT_PREFIX + PathUtil.getAbsolutePathName( item.getFileOrDir() ) ), this );
      }
    }
  }

  class DeleteAction extends ClipCopyAction
  {
    public DeleteAction( JTree tree )
    {
      super( tree );
      putValue( Action.NAME, "Delete" );
      putValue( Action.MNEMONIC_KEY, new Integer( 'D' ) );
    }

    @Override
    public boolean isEnabled()
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      return item != null && item.canDelete();
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      FileTree item = (FileTree)_tree.getLastSelectedPathComponent();
      if( item != null )
      {
        item.delete();
      }
    }
  }

  public void copy( Path from, Path to )
  {
    if( PathUtil.isDirectory( from ) )
    {
      PathUtil.mkdir( to );

      Path[] children = PathUtil.listFiles( from );
      for( int i = 0; i < children.length; i++ )
      {
        String name = PathUtil.getName( children[i] );
        copy( PathUtil.create( from, name ), PathUtil.create( to, name ) );
      }
    }
    else
    {
      try
      {
        InputStream in = PathUtil.createInputStream( from );
        OutputStream out = PathUtil.createOutputStream( to );
        byte[] buf = new byte[1024];
        int len;
        while( (len = in.read( buf )) > 0 )
        {
          out.write( buf, 0, len );
        }
        in.close();
        out.close();
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
  }
}
