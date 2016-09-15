package editor;

import editor.util.EditorUtilities;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 */
public class MessageTree implements MutableTreeNode
{
  private String _message;
  private MessageKind _kind;
  private IMessageTreeNode _data;
  private Icon _icon;
  private MessageTree _parent;
  private List<MessageTree> _children;

  public MessageTree()
  {
    _message = "_root_";
    _kind = MessageKind.Root;
    _children = Collections.emptyList();
  }

  public MessageTree( String message, MessageKind kind, IMessageTreeNode data )
  {
    _message = message;
    _kind = kind;
    _data = data;
    _children = Collections.emptyList();
  }

  private MessageTree( String message, MessageKind kind, IMessageTreeNode data, MessageTree parent )
  {
    _message = message;
    _parent = parent;
    _kind = kind;
    _data = data;
    _children = Collections.emptyList();
  }

  public String getMessage()
  {
    return _message;
  }
  public void setMessage( String message )
  {
    _message = message;
    ((DefaultTreeModel)getTree().getModel()).nodeStructureChanged( this );
  }

  public MessageKind getKind()
  {
    return _kind;
  }

  public IMessageTreeNode getData()
  {
    return _data;
  }

  public boolean isTerminal()
  {
    return _kind.isTerminal();
  }

  public List<MessageTree> getChildren()
  {
    return _children;
  }

  @Override
  public void insert( MutableTreeNode child, int index )
  {
    if( _children.isEmpty() )
    {
      _children = new ArrayList<>();
    }
    _children.add( index, (MessageTree)child );
    child.setParent( this );
  }

  public void addViaModel( MutableTreeNode child )
  {
    ((DefaultTreeModel)getTree().getModel()).insertNodeInto( child, this, getChildCount() );
  }

  public void insertViaModel( MutableTreeNode child, int index )
  {
    ((DefaultTreeModel)getTree().getModel()).insertNodeInto( child, this, index );
  }

  @Override
  public void remove( int index )
  {
    remove( getChildren().get( index ) );
  }

  @Override
  public void remove( MutableTreeNode node )
  {
    //noinspection SuspiciousMethodCalls
    getChildren().remove( node );
  }

  @Override
  public void setUserObject( Object object )
  {

  }

  @Override
  public void removeFromParent()
  {
    _parent.remove( this );
  }

  @Override
  public void setParent( MutableTreeNode newParent )
  {
    _parent = (MessageTree)newParent;
  }

  @Override
  public TreeNode getChildAt( int childIndex )
  {
    return getChildren().get( childIndex );
  }

  @Override
  public int getChildCount()
  {
    return getChildren().size();
  }

  @Override
  public MessageTree getParent()
  {
    return _parent;
  }

  @Override
  public int getIndex( TreeNode node )
  {
    //noinspection SuspiciousMethodCalls
    return getChildren().indexOf( node );
  }

  @Override
  public boolean getAllowsChildren()
  {
    return !_kind.isTerminal();
  }

  @Override
  public boolean isLeaf()
  {
    return isTerminal();
  }

  @Override
  public Enumeration children()
  {
    Iterator iter = getChildren().iterator();
    return new Enumeration()
    {
      @Override
      public boolean hasMoreElements()
      {
        return iter.hasNext();
      }

      @Override
      public Object nextElement()
      {
        return iter.next();
      }
    };
  }

  public String toString()
  {
    return _message;
  }

  public void select()
  {
    JTree tree = getMessagesPanel().getTree();
    TreePath path = getPath();
    tree.expandPath( path );
    tree.setSelectionPath( path );
    tree.scrollPathToVisible( path );
  }

  public TreePath getPath()
  {
    List<MessageTree> path = makePath( new ArrayList<>() );
    return new TreePath( path.toArray( new MessageTree[path.size()] ) );
  }

  private List<MessageTree> makePath( List<MessageTree> path )
  {
    if( getParent() != null )
    {
      getParent().makePath( path );
    }
    path.add( this );
    return path;
  }

  private MessagesPanel getMessagesPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel().getMessagesPanel();
  }

  public Icon getIcon()
  {
    return findIcon();
  }

  private Icon findIcon()
  {
    return hasFailures()
           ? EditorUtilities.loadIcon( "images/failure.png" )
           : hasErrors()
             ? EditorUtilities.loadIcon( "images/error.png" )
             : hasWarnings()
               ? EditorUtilities.loadIcon( "images/warning.png" )
               : EditorUtilities.loadIcon( "images/info.png" );
  }

  private boolean hasFailures()
  {
    return _kind == MessageKind.Failure || getChildren().stream().anyMatch( MessageTree::hasFailures );
  }

  private boolean hasErrors()
  {
    return _kind == MessageKind.Error || getChildren().stream().anyMatch( MessageTree::hasErrors );
  }

  private boolean hasWarnings()
  {
    return _kind == MessageKind.Warning || getChildren().stream().anyMatch( MessageTree::hasWarnings );
  }

  public JTree getTree()
  {
    return RunMe.getEditorFrame().getGosuPanel().getMessagesPanel().getTree();
  }
}
