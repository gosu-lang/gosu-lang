package editor;

import editor.search.MessageDisplay;
import editor.util.EditorUtilities;
import editor.util.Experiment;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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
public class FileTree implements MutableTreeNode, IFileWatcherListener
{
  private String _name;
  private File _fileOrDir;
  private FileTree _parent;
  private List<FileTree> _children;
  private Experiment _experiment;
  private Icon _icon;
  private long _lastModified;

  public FileTree( Experiment experiment )
  {
    _fileOrDir = experiment.getExperimentDir();
    _experiment = experiment;
    _name = experiment.getName();
    addSourcePaths( experiment );
  }

  protected FileTree( File fileOrDir, FileTree parent, Experiment experiment )
  {
    _fileOrDir = fileOrDir;
    _parent = parent;
    _experiment = experiment;
    _name = _fileOrDir.getName();
    if( _fileOrDir.isDirectory() )
    {
      makeSourcePathChildren();
      FileWatcher.instance( _experiment ).register( this );
    }
    else
    {
      _children = Collections.emptyList();
    }
  }

  public String getName()
  {
    return _name;
  }
  private void setName( String name )
  {
    _name = name;
  }

  private void addSourcePaths( Experiment experiment )
  {
    List<String> sourcePath = experiment.getSourcePath();
    _children = new ArrayList<>();
    for( String path: sourcePath )
    {
      File srcPath = new File( path );
      String srcPathAbsolute = srcPath.getAbsolutePath();
      String experimentDir = experiment.getExperimentDir().getAbsolutePath();
      if( srcPathAbsolute.startsWith( experimentDir + File.separator ) )
      {
        // Only include *source* path in the tree; classpath is mixed in source path :|
        String srcPathName = srcPathAbsolute.substring( experimentDir.length() );
        FileTree tree = new FileTree( srcPath, this, experiment );
        tree.setName( srcPathName );
        _children.add( tree );
      }
    }
  }

  private void makeSourcePathChildren()
  {
    makeSourcePathChildren( _fileOrDir );
  }
  private void makeSourcePathChildren( File fileOrDir )
  {
    List<FileTree> children = new ArrayList<>();
    File[] files = fileOrDir.listFiles();
    if( files != null )
    {
      for( File path : files )
      {
        FileTree insert = new FileTree( path, this, _experiment );
        children.add( getSortedIndex( children, insert ), insert );
      }
    }
    _children = children;
  }

  private int getSortedIndex( List<FileTree> children, FileTree insert )
  {
    int count = children.size();
    for( int i = 0; i < count; i++ )
    {
      FileTree tree = children.get( i );
      if( (tree.isDirectory() == insert.isDirectory() &&
           insert.getName().toLowerCase().compareTo( tree.getName().toLowerCase() ) <= 0) ||
          tree.isFile() && insert.isDirectory() )
      {
        return i;
      }
    }
    return count;
  }

  private boolean isInSourcePath( File path )
  {
    for( String sp: _experiment.getSourcePath() )
    {
      sp = new File( sp ).getAbsolutePath();
      String absolutePath = path.getAbsolutePath();
      if( absolutePath.contains( sp ) )
      {
        return true;
      }
    }
    return false;
  }

  public FileTree find( File file )
  {
    if( getFileOrDir().equals( file ) )
    {
      return this;
    }

    for( FileTree tree: getChildren() )
    {
      FileTree found = tree.find( file );
      if( found != null )
      {
        return found;
      }
    }
    return null;
  }

  public FileTree find( String fqn )
  {
    if( fqn == null )
    {
      return null;
    }

    if( fqn.equals( makeFqn() ) )
    {
      return this;
    }

    for( FileTree tree: getChildren() )
    {
      FileTree found = tree.find( fqn );
      if( found != null )
      {
        return found;
      }
    }
    return null;
  }

  public File getFileOrDir()
  {
    return _fileOrDir;
  }

  public boolean isDirectory()
  {
    return _fileOrDir.isDirectory();
  }

  public boolean isFile()
  {
    return _fileOrDir.isFile();
  }

  public List<FileTree> getChildren()
  {
    return _children;
  }

  @Override
  public void insert( MutableTreeNode child, int index )
  {
    getChildren().add( index, (FileTree)child );
    child.setParent( this );
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
    if( ((FileTree)node).isDirectory() )
    {
      FileWatcher.instance( _experiment ).unregister( (FileTree)node );
    }
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
    _parent = (FileTree)newParent;
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
  public FileTree getParent()
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
    return isDirectory() && isInSourcePath( _fileOrDir  );
  }

  @Override
  public boolean isLeaf()
  {
    return isFile();
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
    return _name;
  }

  @Override
  public void fireCreate( String dir, String file )
  {
    File newFileOrDir = new File( dir, file );
    EventQueue.invokeLater( () -> {
      FileTree fileTree = new FileTree( newFileOrDir, this, _experiment );
      ((DefaultTreeModel)getExperimentView().getTree().getModel()).insertNodeInto( fileTree, this, getSortedIndex( getChildren(), fileTree ) );

      if( fileTree.getType() != null )
      {
        EventQueue.invokeLater( () -> {
          File currentFile = getExperiment().getGosuPanel().getCurrentFile();
          if( currentFile != null && currentFile.equals( newFileOrDir ) )
          {
            fileTree.select();
          }
          //## todo: update file if opened in editor
        } );
      }
    } );
  }

  @Override
  public void fireModify( String dir, String file )
  {
    File existingFile = new File( dir, file );
    if( !existingFile.isFile() )
    {
      return;
    }

    FileTree child = find( existingFile );
    if( child == null || child._lastModified - existingFile.lastModified() >= 0 )
    {
      return;
    }

    child._lastModified = existingFile.lastModified();
    EventQueue.invokeLater( () -> getExperiment().getGosuPanel().refresh( existingFile ) );
  }

  @Override
  public void fireDelete( String dir, String file )
  {
    File existingFile = new File( dir, file );
    FileTree fileTree = find( existingFile );
    if( fileTree != null )
    {
      EventQueue.invokeLater( () -> ((DefaultTreeModel)getExperimentView().getTree().getModel()).removeNodeFromParent( fileTree ) );
      getExperiment().getGosuPanel().closeTab( existingFile );
    }
  }

  @Override
  public void setLastModified()
  {
    _lastModified = System.currentTimeMillis() + 100;
  }

  public void select()
  {
    JTree tree = getExperimentView().getTree();
    TreePath path = getPath();
    tree.expandPath( path );
    tree.setSelectionPath( path );
    tree.scrollPathToVisible( path );
  }

  public TreePath getPath()
  {
    List<FileTree> path = makePath( new ArrayList<>() );
    return new TreePath( path.toArray( new FileTree[path.size()] ) );
  }
  private List<FileTree> makePath( List<FileTree> path )
  {
    if( getParent() != null )
    {
      getParent().makePath( path );
    }
    path.add( this );
    return path;
  }

  private ExperimentView getExperimentView()
  {
    return _experiment.getGosuPanel().getExperimentView();
  }

  public Experiment getExperiment()
  {
    return _experiment;
  }

  public boolean isSourcePathRoot()
  {
    return isDirectory() && getExperiment().getSourcePath().contains( getFileOrDir().getAbsolutePath() );
  }

  public FileTree getSourcePathRoot()
  {
    FileTree srcPathRoot = this;
    while( srcPathRoot != null && !srcPathRoot.isSourcePathRoot() )
    {
      srcPathRoot = srcPathRoot.getParent();
    }
    return srcPathRoot;
  }

  public IType getType()
  {
    String fqn = makeFqn();
    if( fqn == null )
    {
      return null;
    }
    return TypeSystem.getByFullNameIfValid( fqn );
  }

  protected String makeFqn()
  {
    FileTree sourcePathRoot = getSourcePathRoot();
    if( isDirectory() || isSourcePathRoot() || sourcePathRoot == null || getFileOrDir().getName().indexOf( '.' ) < 0 )
    {
      return null;
    }
    String fqn = getFileOrDir().getAbsolutePath().substring( sourcePathRoot.getFileOrDir().getAbsolutePath().length() + 1 );
    fqn = fqn.substring( 0, fqn.lastIndexOf( '.' ) ).replace( File.separatorChar, '.' );
    return fqn;
  }

  public boolean canDelete()
  {
    return !isSourcePathRoot() && getParent() != null;
  }

  public void delete()
  {
    if( canDelete() )
    {
      if( isFile() )
      {
        if( MessageDisplay.displayConfirmation(
          "Delete file \"" + getName() + "\"?",
          JOptionPane.OK_CANCEL_OPTION ) != JOptionPane.OK_OPTION )
        {
          return;
        }
      }
      else if( MessageDisplay.displayConfirmation(
        "<html>Delete directory \"" + getName() + "\"?<br>" +
        "All files and subdirectories in \"" + getName() + "\" will be deleted!",
        JOptionPane.OK_CANCEL_OPTION ) != JOptionPane.OK_OPTION )
      {
        return;
      }

      EditorUtilities.delete( getFileOrDir() );
    }
  }

  public Icon getIcon()
  {
    if( _icon == null )
    {
      _icon = findIcon();
    }
    return _icon;
  }

  private Icon findIcon()
  {
    if( getParent() == null )
    {
      return EditorUtilities.loadLabIcon();
    }
    if( isDirectory() )
    {
      if( getSourcePathRoot() != null && !isSourcePathRoot() )
      {
        return EditorUtilities.loadIcon( "images/folder.png" );
      }
      return EditorUtilities.loadIcon( "images/srcfolder.png" );
    }
    if( getType() != null )
    {
      return EditorUtilities.findIcon( getType() );
    }
    return FileSystemView.getFileSystemView().getSystemIcon( getFileOrDir() );
  }

  public int getTotalFiles()
  {
    int iCount = 0;
    if( isDirectory() )
    {
      for( FileTree csr: getChildren() )
      {
        iCount += csr.getTotalFiles();
      }
    }
    else
    {
      return 1;
    }
    return iCount;
  }
}
