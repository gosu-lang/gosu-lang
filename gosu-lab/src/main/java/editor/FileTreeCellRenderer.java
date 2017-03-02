package editor;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 */
public class FileTreeCellRenderer extends AbstractTreeCellRenderer<FileTree>
{
  public FileTreeCellRenderer( JTree tree )
  {
    super( tree );
  }

  public void configure()
  {
    FileTree node = getNode();
    if( node == null )
    {
      return;
    }

    setBorder( new EmptyBorder( 0, 3, 0, 3 ) );

    TypeSystem.lock();
    try
    {
      if( node.isDirectory() || !node.isFile() && node.getFileOrDir().getName().indexOf( '.' ) < 0 )
      {
        setText( node.getName() );
      }
      else
      {
        IType type = node.getType();
        setText( type == null ? node.getName() : type.getRelativeName() );
      }
      setIcon( node.getIcon() );
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }
    finally
    {
      TypeSystem.unlock();
    }
  }
}
