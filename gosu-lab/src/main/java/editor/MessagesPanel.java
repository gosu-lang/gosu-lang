package editor;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 */
public class MessagesPanel extends ClearablePanel
{
  private JTree _tree;
  private JScrollPane _scroller;

  public MessagesPanel()
  {
    setLayout( new BorderLayout() );
    configUi();
  }

  private void configUi()
  {
    _tree = new JTree();
    _tree.setModel( new DefaultTreeModel( new MessageTree( _tree ) ) );
    _tree.setBackground( Scheme.active().getWindow() );
    _tree.setRootVisible( false );
    _tree.setShowsRootHandles( true );
    _tree.setRowHeight( 22 );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setVisibleRowCount( 20 );
    _tree.setCellRenderer( new LabTreeCellRenderer( _tree ) );
    _tree.addMouseListener( new MouseHandler() );
    _tree.addKeyListener( new TreeKeyHandler() );
    _scroller = new JScrollPane( _tree );
    _scroller.setBorder( new MatteBorder( 0, 1, 1, 1, Scheme.active().getScrollbarBorderColor() ) );
    add( _scroller, BorderLayout.CENTER );
  }

  public JTree getTree()
  {
    return _tree;
  }

  @Override
  public void clear()
  {
    _tree.setModel( new DefaultTreeModel( new MessageTree( _tree ) ) );
  }

  @Override
  public void dispose()
  {
    LabFrame.instance().getGosuPanel().showMessages( false );
  }

  public MessageTree getSelectedTree()
  {
    return (MessageTree)_tree.getLastSelectedPathComponent();
  }

  public MessageTree addErrorMessage( String message, MessageTree parent, MessageTree.IssueNode data )
  {
    return addTerminalMessage( message, NodeKind.Error, parent, data );
  }

  public MessageTree addWarningMessage( String message, MessageTree parent, MessageTree.IssueNode data )
  {
    return addTerminalMessage( message, NodeKind.Warning, parent, data );
  }

  public MessageTree addInfoMessage( String message, MessageTree parent, MessageTree.IssueNode data )
  {
    return addTerminalMessage( message, NodeKind.Info, parent, data );
  }

  public MessageTree addFailureMessage( String message, MessageTree parent, MessageTree.IssueNode data )
  {
    return addTerminalMessage( message, NodeKind.Failure, parent, data );
  }

  public MessageTree addTypeMessage( String message, MessageTree parent, MessageTree.IssueNode data )
  {
    return addTerminalMessage( message, NodeKind.File, parent, data );
  }

  private MessageTree addTerminalMessage( String message, NodeKind kind, MessageTree parent, MessageTree.IssueNode data )
  {
    MessageTree tree = new MessageTree( message, kind, data );
    if( parent == null )
    {
      parent = (MessageTree)_tree.getModel().getRoot();
    }
    parent.addViaModel( tree );
    return tree;
  }

  public void appendToTop( MessageTree message )
  {
    MessageTree root = (MessageTree)_tree.getModel().getRoot();
    MessageTree csr = null;
    for( MessageTree child: root.getChildren() )
    {
      if( !child.isTerminal() )
      {
        break;
      }
      csr = child;
    }
    if( csr == null )
    {
      root.insertViaModel( message, 0 );
    }
    else
    {
      MessageTree parent = csr.getParent();
      parent.insertViaModel( message, parent.getIndex( csr ) + 1 );
    }
  }

  public void insertAtTop( MessageTree message )
  {
    MessageTree root = (MessageTree)_tree.getModel().getRoot();
    root.insertViaModel( message, 0 );
  }

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

  public boolean hasErrors()
  {
    return ((MessageTree)getTree().getModel().getRoot()).hasErrors();
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
          MessageTree tree = (MessageTree)selectionPath.getLastPathComponent();
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
            () -> new MessageTreeContextMenu().displayContextMenu( (MessageTree)path.getLastPathComponent(), e.getX(), e.getY(), _tree ) );
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
        MessageTree selection = getSelectedTree();
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

}
