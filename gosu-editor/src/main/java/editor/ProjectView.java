package editor;

import editor.util.Project;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 */
public class ProjectView extends JPanel
{
  private Project _project;
  private JTree _tree;
  private JScrollPane _scroller;

  public ProjectView()
  {
    setBorder( null );
  }

  public void load( Project project )
  {
    if( _scroller != null )
    {
      remove( _scroller );
    }

    _project = project;
    DefaultTreeModel model = new DefaultTreeModel( new FileTree( getProject() ) );
    _tree = new JTree( model );
    _tree.setShowsRootHandles( true );
    _tree.setRowHeight( 20 );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setVisibleRowCount( 20 );
    _tree.setCellRenderer( new FileTreeCellRenderer( _tree ) );
    _tree.addMouseListener( new TreeMouseHandler() );
    _scroller = new JScrollPane( _tree );
    _scroller.setBorder( null );
    expandAllNodes( 0, _tree.getRowCount() );
    setLayout( new BorderLayout() );
    add( _scroller, BorderLayout.CENTER );
    revalidate();
  }

  public JTree getTree()
  {
    return _tree;
  }

  public Project getProject()
  {
    return _project;
  }

  private void expandAllNodes( int startingIndex, int rowCount )
  {
    for( int i = startingIndex; i < rowCount; ++i )
    {
      _tree.expandRow( i );
    }

    if( _tree.getRowCount() != rowCount )
    {
      expandAllNodes( rowCount, _tree.getRowCount() );
    }
  }

  private class TreeMouseHandler implements MouseListener
  {
    @Override
    public void mouseClicked( MouseEvent e )
    {
      if( e.getClickCount() == 2 )
      {
        TreePath selectionPath = _tree.getSelectionPath();
        if( selectionPath != null )
        {
          FileTree fileTree = (FileTree)selectionPath.getLastPathComponent();
          if( fileTree.isFile() && fileTree.getType() != null )
          {
            _project.getGosuPanel().openFile( fileTree.getFileOrDir() );
          }
        }
      }
    }

    @Override
    public void mousePressed( MouseEvent e )
    {

    }

    @Override
    public void mouseReleased( MouseEvent e )
    {

    }

    @Override
    public void mouseEntered( MouseEvent e )
    {

    }

    @Override
    public void mouseExited( MouseEvent e )
    {

    }
  }
}
