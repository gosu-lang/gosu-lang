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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */
public abstract class AbstractTree<T extends AbstractTree<T, N>, N extends ITreeNode> implements MutableTreeNode
{
  protected String _text;
  private NodeKind _kind;
  private N _node;
  private Map<String, Object> _userData;
  private T _parent;
  private List<T> _children;

  public AbstractTree( JTree tree )
  {
    _text = "_root_";
    _kind = NodeKind.Root;
    _userData = new HashMap<>();
    _children = Collections.emptyList();
    putUserData( "_tree", tree );
  }

  public AbstractTree( NodeKind kind, N node )
  {
    this( null, kind, node );
  }
  public AbstractTree( String text, NodeKind kind, N node )
  {
    _text = text;
    _kind = kind;
    _node = node;
    _children = Collections.emptyList();
  }

  abstract public Icon getIcon();

  public JTree getTree()
  {
    T root = getRoot();
    return root == null ? null : (JTree)root.getUserData( "_tree" );
  }

  public T getRoot()
  {
    if( _kind == NodeKind.Root )
    {
      //noinspection unchecked
      return (T)this;
    }
    return getParent() == null ? null : getParent().getRoot();
  }

  public String getText()
  {
    return _text;
  }
  public void setText( String text )
  {
    _text = text;
    ((DefaultTreeModel)getTree().getModel()).nodeStructureChanged( this );
  }

  public NodeKind getKind()
  {
    return _kind;
  }

  public N getNode()
  {
    return _node;
  }

  public boolean isTerminal()
  {
    return _kind.isTerminal();
  }

  public List<T> getChildren()
  {
    return _children;
  }

  @Override
  public void insert( MutableTreeNode child, int index )
  {
    if( _children.isEmpty() )
    {
      _children = new CopyOnWriteArrayList<>();
    }
    //noinspection unchecked
    _children.add( index, (T)child );
    child.setParent( this );
  }

  public void addViaModel( MutableTreeNode child )
  {
    EditorUtilities.invokeInDispatchThread(
      ()-> {
        JTree tree = getTree();
        if( tree != null )
        {
          DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
          model.insertNodeInto( child, this, getChildCount() );
        }
        else
        {
          insert( child, getChildCount() );
        }
      } );
  }

  public void insertViaModel( MutableTreeNode child, int index )
  {
    EditorUtilities.invokeInDispatchThread(
     ()-> {
       JTree tree = getTree();
       if( tree != null )
       {
         DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
         model.insertNodeInto( child, this, index );
       }
     } );
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

  public void putUserData( String name, Object value )
  {
    if( _userData == null )
    {
      _userData = new HashMap<>();
    }
    _userData.put( name, value );
  }
  public Object getUserData( String name )
  {
    if( _userData == null )
    {
      return null;
    }
    return _userData.get( name );
  }

  @Override
  public void removeFromParent()
  {
    _parent.remove( this );
  }

  @Override
  public void setParent( MutableTreeNode newParent )
  {
    //noinspection unchecked
    _parent = (T)newParent;
  }

  @Override
  public T getChildAt( int childIndex )
  {
    return getChildren().get( childIndex );
  }

  @Override
  public int getChildCount()
  {
    return getChildren().size();
  }

  @Override
  public T getParent()
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
    return _text;
  }

  public void select()
  {
    JTree tree = getTree();
    TreePath path = getPath();
    tree.expandPath( path );
    tree.setSelectionPath( path );
    tree.scrollPathToVisible( path );
  }

  public TreePath getPath()
  {
    List<T> path = makePath( new ArrayList<>() );
    return new TreePath( path.toArray( new AbstractTree[path.size()] ) );
  }

  List<T> makePath( List<T> path )
  {
    if( getParent() != null )
    {
      getParent().makePath( path );
    }
    //noinspection unchecked
    path.add( (T)this );
    return path;
  }

  public boolean hasFailures()
  {
    //noinspection Convert2MethodRef
    return _kind == NodeKind.Failure || getChildren().stream().anyMatch( child -> child.hasFailures() );
  }

  public boolean hasErrors()
  {
    //noinspection Convert2MethodRef
    return _kind == NodeKind.Error || getChildren().stream().anyMatch( child -> child.hasErrors() );
  }

  public boolean hasWarnings()
  {
    //noinspection Convert2MethodRef
    return _kind == NodeKind.Warning || getChildren().stream().anyMatch( child -> child.hasWarnings() );
  }

  public int depth()
  {
    int depth = 0;
    //noinspection unchecked
    T csr = (T)this;
    while( csr.getParent() != null )
    {
      csr = csr.getParent();
      depth++;
    }
    return depth;
  }
}
