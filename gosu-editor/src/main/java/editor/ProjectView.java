package editor;

import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.util.EditorUtilities;
import editor.util.Project;
import editor.util.XPToolbarButton;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 */
public class ProjectView extends JPanel
{
  private Project _project;
  private JTree _tree;
  private JPanel _examplesList;
  private JPanel _examplesListNorth;
  private CollapsibleSplitPane _splitPane;
  private JScrollPane _scroller;

  public ProjectView()
  {
    setBorder( null );
    setLayout( new BorderLayout() );

    _splitPane = new CollapsibleSplitPane( SwingConstants.VERTICAL, new JPanel(), makeExamplesList() );
    add( _splitPane, BorderLayout.CENTER );
    _splitPane.setPosition( 70 );
  }

  private JComponent makeExamplesList()
  {
    _examplesList = new JPanel();
    _examplesList.setLayout( new BorderLayout() );
    _examplesList.add( new JPanel(), BorderLayout.CENTER );
    _examplesList.add( _examplesListNorth = new JPanel( new GridLayout( 0, 1 ) ), BorderLayout.NORTH );
    addExamples();
    _examplesList.setBorder( null );
    JScrollPane scrollPane = new JScrollPane( _examplesList );
    scrollPane.getVerticalScrollBar().setUnitIncrement( 22 );
    scrollPane.setBorder( null );

    TabPane tabPane = new TabPane( TabPosition.TOP, TabPane.MINIMIZABLE | TabPane.RESTORABLE );
    tabPane.addTab( "Examples", null, scrollPane );
    return tabPane;
  }

  public void load( Project project )
  {
    _splitPane.clearTop();

    _project = project;
    DefaultTreeModel model = new DefaultTreeModel( new FileTree( getProject() ) );
    _tree = new JTree( model );
    _tree.setBackground( EditorUtilities.GRAY_252 );
    _tree.setShowsRootHandles( true );
    _tree.setRowHeight( 22 );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setVisibleRowCount( 20 );
    _tree.setCellRenderer( new FileTreeCellRenderer( _tree ) );
    _tree.addMouseListener( new TreeMouseHandler() );
    _scroller = new JScrollPane( _tree );
    _scroller.setBorder( new MatteBorder( 0, 0, 1, 1, SystemColor.controlShadow ) );
    expandToFirstSourcePath( 0, _tree.getRowCount() );

    _splitPane.setTop( _scroller );

    revalidate();
  }

  private void addExamples()
  {
    java.util.List<File> examples = EditorUtilities.getStockExampleProjects();
    for( File dir: examples )
    {
      XPToolbarButton item = new XPToolbarButton( dir.getName(), EditorUtilities.loadIcon( "images/g_16.png" ) );
      item.setHorizontalAlignment( SwingConstants.LEFT );
      item.addActionListener( e -> getProject().getGosuPanel().openProject( dir ) );
      _examplesListNorth.add( item );
    }
  }

  public JTree getTree()
  {
    return _tree;
  }

  public Project getProject()
  {
    return _project;
  }

  private void expandToFirstSourcePath( int startingIndex, int rowCount )
  {
    FileTree fileTree = (FileTree)_tree.getPathForRow( startingIndex ).getLastPathComponent();
    if( fileTree.getParent() != null && fileTree.getParent().isSourcePathRoot() )
    {
      // Stop after expanding first source path
      _tree.expandRow( startingIndex );
      return;
    }

    for( int i = startingIndex; i < rowCount; ++i )
    {
      _tree.expandRow( i );
    }

    if( _tree.getRowCount() != rowCount )
    {
      expandToFirstSourcePath( rowCount, _tree.getRowCount() );
    }
  }

  private void expandAll( int startingIndex, int rowCount )
  {
    for( int i = startingIndex; i < rowCount; ++i )
    {
      _tree.expandRow( i );
    }

    if( _tree.getRowCount() != rowCount )
    {
      expandAll( rowCount, _tree.getRowCount() );
    }
  }

  public FileTree getSelectedTree()
  {
    return (FileTree)_tree.getLastSelectedPathComponent();
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
      if( e.isPopupTrigger() )
      {
        int row = _tree.getRowForLocation( e.getX(), e.getY() );
        TreePath path = _tree.getPathForLocation( e.getX(), e.getY() );
        _tree.setSelectionPath( path );
        if( row > -1 )
        {
          _tree.setSelectionRow( row );
          EventQueue.invokeLater( () ->
                                    new ProjectTreeContextMenu( getProject() ).displayContextMenu( _tree, e.getX(), e.getY(), _tree ) );
        }
      }
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
