package editor.search;

import editor.ClearablePanel;
import editor.ITreeNode;
import editor.LabTreeCellRenderer;
import editor.RunMe;
import editor.Scheme;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class SearchPanel extends ClearablePanel
{
  private JTree _tree;
  private JPanel _replaceButtonPanel;
  private String _replacePattern;
  private JButton _btnReplaceAll;
  private JButton _btnReplaceSelected;

  public SearchPanel()
  {
    setLayout( new BorderLayout() );
    configUi();
  }

  private void configUi()
  {
    _tree = new JTree();
    _tree.setModel( new DefaultTreeModel( new SearchTree( _tree ) ) );
    _tree.setBackground( Scheme.active().getWindow() );
    _tree.setRootVisible( false );
    _tree.setShowsRootHandles( true );
    _tree.setRowHeight( 22 );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION );
    _tree.setVisibleRowCount( 20 );
    _tree.setCellRenderer( new LabTreeCellRenderer( _tree ) );
    _tree.addMouseListener( new MouseHandler() );
    _tree.addKeyListener( new TreeKeyHandler() );
    _tree.addTreeSelectionListener( new TreeSelectionHandler() );
    JScrollPane scroller = new JScrollPane( _tree );
    scroller.setBorder( new MatteBorder( 0, 1, 1, 1, Scheme.active().getScrollbarBorderColor() ) );
    add( scroller, BorderLayout.CENTER );


    addReplaceButtonsPanel();
  }

  private void addReplaceButtonsPanel()
  {
    _replaceButtonPanel = new JPanel( new BorderLayout() );
    _replaceButtonPanel.setBorder( BorderFactory.createEmptyBorder( 5, 0, 5, 0 ) );
    JPanel filler = new JPanel();
    _replaceButtonPanel.add( filler, BorderLayout.CENTER );

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    _btnReplaceAll = new JButton( "Replace All" );
    _btnReplaceAll.setMnemonic( 'A' );
    _btnReplaceAll.addActionListener( e -> replaceAll() );
    buttonPanel.add( _btnReplaceAll );

    _btnReplaceSelected = new JButton( "Replace Selected" );
    _btnReplaceSelected.addActionListener( e -> replaceSelected() );
    buttonPanel.add( _btnReplaceSelected );

    _replaceButtonPanel.add( buttonPanel, BorderLayout.WEST );
  }

  void setReplacePattern( String pattern )
  {
    _replacePattern = pattern;
  }

  private void replaceSelected()
  {
    _btnReplaceAll.setEnabled( false );
    _btnReplaceSelected.setEnabled( false );

    int count = _tree.getSelectionModel().getSelectionCount();
    if( count == 0 )
    {
      return;
    }

    List<SearchTree> list = new ArrayList<>( count );
    for( TreePath path: _tree.getSelectionModel().getSelectionPaths() )
    {
      SearchTree selection = (SearchTree)path.getLastPathComponent();
      list.add( selection );
    }

    list.sort( (l, r) -> {
      if( l.getParent() == r.getParent() )
      {
        SearchTree.SearchTreeNode node = l.getNode();
        if( node != null && node.getLocation() != null )
        {
          if( node.getFile() == r.getNode().getFile() )
          {
            return r.getNode().getLocation()._iOffset - node.getLocation()._iOffset;
          }
        }
      }
      return 0;
    } );

    for( SearchTree selection: list )
    {
      selection.replace( _replacePattern );
    }
  }

  private void replaceAll()
  {
    _btnReplaceAll.setEnabled( false );
    _btnReplaceSelected.setEnabled( false );

    SearchTree root = (SearchTree)_tree.getModel().getRoot();
    root.replace( _replacePattern );
  }

  public JTree getTree()
  {
    return _tree;
  }

  public void showReplace( boolean show )
  {
    if( show )
    {
      _btnReplaceAll.setEnabled( true );
      _btnReplaceSelected.setEnabled( true );
      if(  _replaceButtonPanel.getParent() == null )
      {
        add( _replaceButtonPanel, BorderLayout.SOUTH );
      }
    }
    else if( !show && _replaceButtonPanel.getParent() != null )
    {
      remove( _replaceButtonPanel );
    }
    revalidate();
  }

  @Override
  public void clear()
  {
    _tree.setModel( new DefaultTreeModel( new SearchTree( _tree ) ) );
  }

  @Override
  public void dispose()
  {
    RunMe.getEditorFrame().getGosuPanel().showSearches( false );
  }

  public SearchTree getSelectedTree()
  {
    return (SearchTree)_tree.getLastSelectedPathComponent();
  }

  public void add( SearchTree message )
  {
    SearchTree root = (SearchTree)_tree.getModel().getRoot();
    root.addViaModel( message );
  }

  @SuppressWarnings("UnusedDeclaration")
  public void expandAll()
  {
    expandAll( 0, _tree.getRowCount() );
  }
  void expandAll( int startingIndex, int rowCount )
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

  private class MouseHandler implements MouseListener
  {
    @Override
    public void mouseClicked( MouseEvent e )
    {
      if( e.getClickCount() == 2 )
      {
        TreePath selectionPath = _tree.getSelectionPath();
        if( selectionPath != null )
        {
          SearchTree tree = (SearchTree)selectionPath.getLastPathComponent();
          ITreeNode data = tree.getNode();
          if( data != null && data.hasTarget() )
          {
            data.jumpToTarget();
          }
        }
      }
    }

    @Override
    public void mousePressed( MouseEvent e )
    {
      handleContextMenu( e );
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
      handleContextMenu( e );
    }

    private void handleContextMenu( MouseEvent e )
    {
      if( e.isPopupTrigger() )
      {
        int row = _tree.getRowForLocation( e.getX(), e.getY() );
        TreePath path = _tree.getPathForLocation( e.getX(), e.getY() );
        _tree.setSelectionPath( path );
        if( row > -1 )
        {
          _tree.setSelectionRow( row );
          EventQueue.invokeLater( 
            () -> new SearchTreeContextMenu().displayContextMenu( (SearchTree)path.getLastPathComponent(), e.getX(), e.getY(), _tree ) );
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

  private class TreeKeyHandler implements KeyListener
  {
    @Override
    public void keyTyped( KeyEvent e )
    {

    }

    @Override
    public void keyPressed( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_ENTER )
      {
        SearchTree selection = getSelectedTree();
        if( selection != null && selection.isTerminal() )
        {
          selection.getNode().jumpToTarget();
        }
      }
    }

    @Override
    public void keyReleased( KeyEvent e )
    {

    }
  }

  private class TreeSelectionHandler implements TreeSelectionListener
  {
    @Override
    public void valueChanged( TreeSelectionEvent e )
    {
      if( !e.isAddedPath() )
      {
        return;
      }

      int count = _tree.getSelectionCount();
      if( count <= 1 )
      {
        return;
      }

      // Don't selections must not be supersets / subsets

      SearchTree newSelection = (SearchTree)e.getPath().getLastPathComponent();
      List<SearchTree> selections = getSelections();
      removeAncestors( newSelection.getParent(), selections );
      for( Iterator<SearchTree> iter = selections.iterator(); iter.hasNext(); )
      {
        SearchTree child = iter.next();
        if( newSelection != child && removeDescendant( newSelection, child ) )
        {
          iter.remove();
        }
      }

      List<TreePath> paths = new ArrayList<>();
      for( SearchTree sel: selections )
      {
        paths.add( sel.getPath() );
      }
      _tree.getSelectionModel().setSelectionPaths( paths.toArray( new TreePath[paths.size()] ) );
    }

    private boolean removeDescendant( SearchTree newSelection, SearchTree csr )
    {
      if( csr == null )
      {
        return false;
      }
      if( csr == newSelection )
      {
        return true;
      }
      return removeDescendant( newSelection, csr.getParent() );
    }

    private void removeAncestors( SearchTree selection, List<SearchTree> selections )
    {
      if( selection == null )
      {
        return;
      }
      if( selections.contains( selection ) )
      {
        selections.remove( selection );
      }
      removeAncestors( selection.getParent(), selections );
    }

    private List<SearchTree> getSelections()
    {
      List<SearchTree> list = new ArrayList<>();
      for( TreePath path: _tree.getSelectionModel().getSelectionPaths() )
      {
        SearchTree selection = (SearchTree)path.getLastPathComponent();
        list.add( selection );
      }
      return list;
    }
  }
}
