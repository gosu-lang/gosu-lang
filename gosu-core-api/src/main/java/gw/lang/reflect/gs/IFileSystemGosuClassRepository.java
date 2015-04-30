/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.util.Pair;

import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public interface IFileSystemGosuClassRepository extends IGosuClassRepository
{

  IDirectory[] getSourcePath();

  void setSourcePath(IDirectory[] sourcePath);

  IDirectory[] getExcludedPath();

  void setExcludedPath(IDirectory[] excludedPath);

  String getClassNameFromFile( IDirectory root, IFile file, String[] fileExts );

  List<Pair<String, IFile>> findAllFilesByExtension(String extension);

  public static class ClassPathEntry
  {
    private final IDirectory _path;
    private final boolean _isTestResource;

    public ClassPathEntry( IDirectory path, boolean testResource )
    {
      _path = path;
      _isTestResource = testResource;
    }

    public IDirectory getPath()
    {
      return _path;
    }

    public boolean isTestResource()
    {
      return _isTestResource;
    }

    @Override
    public String toString() {
      return "" + _path;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof ClassPathEntry)) {
        return false;
      }

      ClassPathEntry that = (ClassPathEntry) o;

      if (_isTestResource != that._isTestResource) {
        return false;
      }
      if (_path != null ? !_path.equals(that._path) : that._path != null) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      int result = _path != null ? _path.hashCode() : 0;
      result = 31 * result + (_isTestResource ? 1 : 0);
      return result;
    }
  }

  /**
  */
  public static interface IClassFileInfo
  {
    IDirectory getParentFile();

    IFile getFile();

    Reader getReader();

    String getFileName();

    String getNonCanonicalFileName();

    String getFilePath();

    int getClassPathLength();

    boolean hasInnerClass();

    ISourceFileHandle getSourceFileHandle();

    ClassPathEntry getEntry();

    String getContent();
  }

  class Util
  {

    public static boolean isClassFileName( String strName, String[] fileExts )
    {
      for( String strExt : fileExts )
      {
        if( strName.endsWith( strExt ) )
        {
          return true;
        }
      }
      return false;
    }

    public static void shiftInnerClassToFileName( StringBuilder innerClass, StringBuilder fileName )
    {
      if( innerClass.length() > 0 && fileName.length() > 0 )
      {
        fileName.append( File.separatorChar );
      }
      while( innerClass.length() > 0 && innerClass.charAt( 0 ) != '.' )
      {
        fileName.append( innerClass.charAt( 0 ) );
        innerClass.deleteCharAt( 0 );
      }
      if( innerClass.length() > 0 && innerClass.charAt( 0 ) == '.' )
      {
        innerClass.deleteCharAt( 0 );
      }
    }
  }
}
