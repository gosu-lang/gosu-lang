package editor.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 */
public class SettingsTree implements MutableTreeNode
{
  private ISettings _settings;
  private SettingsTree _parent;
  private List<SettingsTree> _children;

  public SettingsTree()
  {
    _children = Collections.emptyList();
  }

  public SettingsTree( ISettings settings, SettingsTree parent )
  {
    _parent = parent;
    _settings = settings;
    _children = Collections.emptyList();
  }

  public ISettings getSettings()
  {
    return _settings;
  }

  public boolean isTerminal()
  {
    return getSettings() != null;
  }

  public List<SettingsTree> getChildren()
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
    _children.add( index, (SettingsTree)child );
    child.setParent( this );
  }
  public void addChild( SettingsTree child )
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
    _parent = (SettingsTree)newParent;
  }

  @Override
  public SettingsTree getChildAt( int childIndex )
  {
    return getChildren().get( childIndex );
  }

  @Override
  public int getChildCount()
  {
    return getChildren().size();
  }

  @Override
  public SettingsTree getParent()
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
    return _settings != null
           ? _settings.getName()
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
    List<SettingsTree> path = makePath( new ArrayList<>() );
    return new TreePath( path.toArray( new SettingsTree[path.size()] ) );
  }

  private List<SettingsTree> makePath( List<SettingsTree> path )
  {
    if( getParent() != null )
    {
      getParent().makePath( path );
    }
    path.add( this );
    return path;
  }

  public SettingsTree find( String settingsPath )
  {
    if( getSettings() == null && settingsPath.isEmpty() ||
        settingsPath.equals( getSettings().getPath() ) )
    {
      return this;
    }

    for( SettingsTree tree: getChildren() )
    {
      SettingsTree found = tree.find( settingsPath );
      if( found != null )
      {
        return found;
      }
    }
    return null;
  }

  public SettingsTree find( ISettings settings )
  {
    if( settings.equals( getSettings() ) )
    {
      return this;
    }

    for( SettingsTree tree: getChildren() )
    {
      SettingsTree found = tree.find( settings );
      if( found != null )
      {
        return found;
      }
    }
    return null;
  }

  public Icon getIcon()
  {
    return _settings != null
           ? _settings.getIcon()
           : null;
  }
}
