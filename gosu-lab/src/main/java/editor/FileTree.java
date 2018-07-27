package editor;

import editor.util.EditorUtilities;
import editor.util.Experiment;
import gw.util.PathUtil;
import editor.util.SourceFileCreator;
import gw.config.CommonServices;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import manifold.util.JsonUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
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
  private Path _fileOrDir;
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

  protected FileTree( Path fileOrDir, FileTree parent, Experiment experiment )
  {
    _fileOrDir = fileOrDir;
    _parent = parent;
    _experiment = experiment;
    _name = PathUtil.getName( _fileOrDir );
    if( PathUtil.isDirectory( _fileOrDir ) )
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
      Path srcPath = PathUtil.create( path );
      //noinspection ResultOfMethodCallIgnored
      PathUtil.mkdirs( srcPath );
      String srcPathAbsolute = PathUtil.getAbsolutePathName( srcPath );
      String experimentDir = PathUtil.getAbsolutePathName( experiment.getExperimentDir() );
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
  private void makeSourcePathChildren( Path fileOrDir )
  {
    List<FileTree> children = new ArrayList<>();
    Path[] files = PathUtil.listFiles( fileOrDir );
    if( files != null )
    {
      for( Path path : files )
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

  private boolean isInSourcePath( Path path )
  {
    String absolutePath = PathUtil.getAbsolutePathName( path );
    for( String sp: _experiment.getSourcePath() )
    {
      sp = PathUtil.getAbsolutePathName( sp );
      if( absolutePath.contains( sp ) )
      {
        return true;
      }
    }
    return false;
  }

  public FileTree find( Path file )
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

    String thisFqn = makeFqn();
    if( thisFqn != null )
    {
      if( fqn.equals( thisFqn ) ||
          // Also see if this file is an enclosing type of the fqn
          fqn.startsWith( thisFqn ) && fqn.startsWith( thisFqn + '.' ) )
      {
        return this;
      }
    }
    else if( isDirectory() )
    {
      String dotPath = makeDotPath();
      if( dotPath != null && !fqn.startsWith( dotPath ) )
      {
        return null;
      }
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

  public void getAllTypeNames( Set<String> typeNames )
  {
    String fqn = makeFqn();
    if( fqn != null )
    {
      typeNames.add( fqn );
    }

    for( FileTree tree: getChildren() )
    {
      tree.getAllTypeNames( typeNames );
    }
  }

  public Path getFileOrDir()
  {
    return _fileOrDir;
  }

  public boolean isDirectory()
  {
    return PathUtil.isDirectory( _fileOrDir );
  }

  public boolean isFile()
  {
    return PathUtil.isFile( _fileOrDir );
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
    Path newFileOrDir = PathUtil.create( dir, file );
    EventQueue.invokeLater( () -> {
      FileTree fileTree = new FileTree( newFileOrDir, this, _experiment );
      handleNewFileTree( fileTree );
      ((DefaultTreeModel)getExperimentView().getTree().getModel()).insertNodeInto( fileTree, this, getSortedIndex( getChildren(), fileTree ) );
    } );
  }

  private void handleNewFileTree( FileTree fileTree )
  {
    if( fileTree.isDirectory() )
    {
      fileTree.getChildren().forEach( this::handleNewFileTree );
    }
    else
    {
      handleNewFile( fileTree );
    }
  }

  private void handleNewFile( FileTree fileTree )
  {
    Path file = fileTree.getFileOrDir();
    if( isTypeFile( fileTree ) )
    {
      TypeSystem.created( CommonServices.getFileSystem().getIFile( file.toFile() ) );
      TypeSystem.refresh( TypeSystem.getGlobalModule() );
    }
    Path createdFile = SourceFileCreator.instance().getCreated();
    if( createdFile != null && createdFile.equals( file ) )
    {
      EventQueue.invokeLater( () -> openFile( fileTree, file ) );
    }
  }

  private void openFile( FileTree fileTree, Path file )
  {
    LabFrame.instance().openFile( file );
    SourceFileCreator.instance().clearCreated();

    EventQueue.invokeLater( () -> {
      Path currentFile = getExperiment().getGosuPanel().getCurrentFile();
      if( currentFile != null && currentFile.equals( file ) )
      {
        fileTree.select();
      }
      //## todo: update file if opened in editor
    } );
  }

  private boolean isTypeFile( FileTree fileTree )
  {
    if( fileTree.isDirectory() )
    {
      return false;
    }

    for( ITypeLoader tl: TypeSystem.getAllTypeLoaders() )
    {
      if( tl.handlesFile( PathUtil.getIFile( fileTree.getFileOrDir() ) ) )
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public void fireModify( String dir, String file )
  {
    Path existingFile = PathUtil.create( dir, file );
    if( !PathUtil.exists( existingFile ) )
    {
      return;
    }

    FileTree child = find( existingFile );
    if( child == null || child._lastModified - PathUtil.lastModified( existingFile ) >= 0 )
    {
      return;
    }

    child._lastModified = PathUtil.lastModified( existingFile );
    EventQueue.invokeLater( () -> getExperiment().getGosuPanel().refresh( existingFile ) );
  }

  @Override
  public void fireDelete( String dir, String file )
  {
    Path existingFile = PathUtil.create( dir, file );
    FileTree fileTree = find( existingFile );
    if( fileTree != null )
    {
      EventQueue.invokeLater( () -> ((DefaultTreeModel)getExperimentView().getTree().getModel()).removeNodeFromParent( fileTree ) );
      getExperiment().getGosuPanel().closeTab( existingFile );

      handleDeletedFileTree( fileTree );
    }
  }

  private void handleDeletedFileTree( FileTree fileTree )
  {
    if( fileTree.isDirectory() )
    {
      fileTree.getChildren().forEach( this::handleDeletedFileTree );
    }
    else
    {
      handlePossibleDeletedType( fileTree );
    }
  }

  private boolean handlePossibleDeletedType( FileTree fileTree )
  {
    if( isTypeFile( fileTree ) )
    {
      TypeSystem.deleted( PathUtil.getIFile( fileTree.getFileOrDir() ) );
      return true;
    }
    return false;
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
    return isDirectory() && getExperiment().getSourcePath().contains( PathUtil.getAbsolutePathName( getFileOrDir() ) );
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

  public String makeFqn()
  {
    FileTree sourcePathRoot = getSourcePathRoot();
    if( isDirectory() || isSourcePathRoot() || sourcePathRoot == null || PathUtil.getName( getFileOrDir() ).indexOf( '.' ) < 0 )
    {
      return null;
    }
    return _makeDotPath( sourcePathRoot );
  }

  private String makeDotPath()
  {
    return makeDotPath( getSourcePathRoot() );
  }
  private String makeDotPath( FileTree sourcePathRoot )
  {
    if( isSourcePathRoot() || sourcePathRoot == null )
    {
      return null;
    }
    return _makeDotPath( sourcePathRoot );
  }
  private String _makeDotPath( FileTree sourcePathRoot )
  {
    String fqn = PathUtil.getAbsolutePathName( getFileOrDir() ).substring( PathUtil.getAbsolutePathName( sourcePathRoot.getFileOrDir() ).length() + 1 );
    fqn = makeIdentifierName( fqn );
    return fqn;
  }

  private String makeIdentifierName( String fqn )
  {
    int iDot = fqn.lastIndexOf( '.' );
    if( iDot >= 0 )
    {
      fqn = fqn.substring( 0, iDot );
    }
    fqn = fqn.replace( File.separatorChar, '.' );
    iDot = fqn.lastIndexOf( '.' );
    String filePart = fqn.substring( iDot+1 );
    filePart = JsonUtil.makeIdentifier( filePart );
    fqn = fqn.substring( 0, iDot+1 ) + filePart;
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
        if( JOptionPane.showConfirmDialog( LabFrame.instance(),
              "Delete file \"" + getName() + "\"?",
              "Gosu Lab", JOptionPane.OK_CANCEL_OPTION ) != JOptionPane.OK_OPTION )
        {
          return;
        }
      }
      else if( JOptionPane.showConfirmDialog( LabFrame.instance(),
                 "<html>Delete directory \"" + getName() + "\"?<br>" +
                 "All files and subdirectories in \"" + getName() + "\" will be deleted!",
                 "Gosu Lab", JOptionPane.OK_CANCEL_OPTION ) != JOptionPane.OK_OPTION )
      {
        return;
      }

      PathUtil.delete( getFileOrDir(), true );
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
      return LabFrame.loadLabIcon();
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
    try
    {
      File file = getFileOrDir().toFile();
      return FileSystemView.getFileSystemView().getSystemIcon( file );
    }
    catch( UnsupportedOperationException e )
    {
      return EditorUtilities.loadIcon( "images/FileText.png" );
    }
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

  public boolean traverse( Predicate<FileTree> visitor )
  {
    if( !visitor.test( this ) )
    {
      return false;
    }

    for( FileTree ft: getChildren() )
    {
      if( !ft.traverse( visitor ) )
      {
        return false;
      }
    }

    return true;
  }
}
