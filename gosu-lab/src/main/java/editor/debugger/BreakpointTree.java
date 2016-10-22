package editor.debugger;

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

public class BreakpointTree implements MutableTreeNode
{
  private Breakpoint _bp;
  private IBreakpointFactory _factory;
  private BreakpointTree _parent;
  private List<BreakpointTree> _children;

  public BreakpointTree()
  {
    _children = Collections.emptyList();
  }

  public BreakpointTree( IBreakpointFactory factory, BreakpointTree parent )
  {
    _parent = parent;
    _factory = factory;
    _children = Collections.emptyList();
  }

  public BreakpointTree( Breakpoint bp, BreakpointTree parent )
  {
    _parent = parent;
    _bp = bp;
    _children = Collections.emptyList();
  }

  public Breakpoint getBreakpoint()
  {
    return _bp;
  }

  public IBreakpointFactory getFactory()
  {
    return _factory;
  }

  public boolean isTerminal()
  {
    return getBreakpoint() != null;
  }

  public List<BreakpointTree> getChildren()
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
    _children.add( index, (BreakpointTree)child );
    child.setParent( this );
  }
  public void addChild( BreakpointTree child )
  {
    insert( child, _children.size() );
  }

  @SuppressWarnings("UnusedDeclaration")
  public void addViaModel( JTree tree, MutableTreeNode child )
  {
    ((DefaultTreeModel)tree.getModel()).insertNodeInto( child, this, getChildCount() );
  }

  public void insertViaModel( JTree tree, MutableTreeNode child, int index )
  {
    ((DefaultTreeModel)tree.getModel()).insertNodeInto( child, this, index );
  }

  public void deleteViaModel( JTree tree, MutableTreeNode child )
  {
    ((DefaultTreeModel)tree.getModel()).removeNodeFromParent( child );
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
    _parent = (BreakpointTree)newParent;
  }

  @Override
  public BreakpointTree getChildAt( int childIndex )
  {
    return getChildren().get( childIndex );
  }

  @Override
  public int getChildCount()
  {
    return getChildren().size();
  }

  @Override
  public BreakpointTree getParent()
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
    return !isTerminal();
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
    return _bp != null
           ? _bp.getTitle()
           : _factory != null
             ? _factory.getName()
             : "<root>";
  }

  public void select( JTree tree )
  {
    TreePath path = getPath();
    tree.expandPath( path );
    tree.setSelectionPath( path );
    tree.scrollPathToVisible( path );
  }

  public TreePath getPath()
  {
    List<BreakpointTree> path = makePath( new ArrayList<>() );
    return new TreePath( path.toArray( new BreakpointTree[path.size()] ) );
  }

  private List<BreakpointTree> makePath( List<BreakpointTree> path )
  {
    if( getParent() != null )
    {
      getParent().makePath( path );
    }
    path.add( this );
    return path;
  }

  public BreakpointTree find( Breakpoint bp )
  {
    if( bp == null )
    {
      return null;
    }

    if( bp.equals( getBreakpoint() ) )
    {
      return this;
    }

    for( BreakpointTree tree: getChildren() )
    {
      BreakpointTree found = tree.find( bp );
      if( found != null )
      {
        return found;
      }
    }
    return null;
  }

  public Icon getIcon()
  {
    return getBreakpoint() == null || getBreakpoint().isActive()
           ? EditorUtilities.loadIcon( "images/debug_linebreakpoint.png" )
           : EditorUtilities.loadIcon( "images/disabled_breakpoint.png" );
  }
}
