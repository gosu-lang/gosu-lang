package editor.run;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 */
public class RunConfigTree implements MutableTreeNode
{
  private IRunConfig _runConfig;
  private IRunConfigFactory _factory;
  private RunConfigTree _parent;
  private List<RunConfigTree> _children;

  public RunConfigTree()
  {
    _children = Collections.emptyList();
  }

  public RunConfigTree( IRunConfigFactory factory, RunConfigTree parent )
  {
    _parent = parent;
    _factory = factory;
    _children = Collections.emptyList();
  }

  public RunConfigTree( IRunConfig runConfig, RunConfigTree parent )
  {
    _parent = parent;
    _runConfig = runConfig;
    _children = Collections.emptyList();
  }

  public IRunConfig getRunConfig()
  {
    return _runConfig;
  }

  public IRunConfigFactory getFactory()
  {
    return _factory;
  }

  public boolean isTerminal()
  {
    return getRunConfig() != null;
  }

  public List<RunConfigTree> getChildren()
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
    _children.add( index, (RunConfigTree)child );
    child.setParent( this );
  }
  public void addChild( RunConfigTree child )
  {
    insert( child, _children.size() );
  }

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
    _parent = (RunConfigTree)newParent;
  }

  @Override
  public RunConfigTree getChildAt( int childIndex )
  {
    return getChildren().get( childIndex );
  }

  @Override
  public int getChildCount()
  {
    return getChildren().size();
  }

  @Override
  public RunConfigTree getParent()
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
    return _runConfig != null
           ? _runConfig.getName()
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
    List<RunConfigTree> path = makePath( new ArrayList<>() );
    return new TreePath( path.toArray( new RunConfigTree[path.size()] ) );
  }

  private List<RunConfigTree> makePath( List<RunConfigTree> path )
  {
    if( getParent() != null )
    {
      getParent().makePath( path );
    }
    path.add( this );
    return path;
  }

  public RunConfigTree find( IRunConfig runConfig )
  {
    if( runConfig.equals( getRunConfig() ) )
    {
      return this;
    }

    for( RunConfigTree tree: getChildren() )
    {
      RunConfigTree found = tree.find( runConfig );
      if( found != null )
      {
        return found;
      }
    }
    return null;
  }

  public Icon getIcon()
  {
    return _runConfig != null
           ? _runConfig.getIcon()
           : _factory != null
             ? _factory.getIcon()
             : null;
  }
}
