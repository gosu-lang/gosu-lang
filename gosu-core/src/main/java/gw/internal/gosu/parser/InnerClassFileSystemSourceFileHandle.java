/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.fs.IFile;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.ISource;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.TypeSystem;

/**
 */
public class InnerClassFileSystemSourceFileHandle implements ISourceFileHandle
{
  private String _strInnerClass;
  private String _strEnclosingType; // Note enclosing type can be an inner class
  private String _strNamespace;
  private boolean _bTestClass;
  private int _iOffset;
  private int _iEnd;
  private int _mark;
  private ClassType _classType;

  public InnerClassFileSystemSourceFileHandle( ClassType classType, String strEnclosingType, String strInnerClass, boolean bTestClass )
  {
    if (classType == null) {
      throw new NullPointerException("ClassType cannot be null");
    }
    _strInnerClass = strInnerClass;
    _strEnclosingType = strEnclosingType;
    _bTestClass = bTestClass;

    int iLastDot = strEnclosingType.lastIndexOf('.');
    if( iLastDot > 0 )
    {
      _strNamespace = _strEnclosingType.substring( 0, iLastDot ).intern();
    }
    else
    {
      _strNamespace = "";
    }

    _strEnclosingType = _strEnclosingType == null ? null : _strEnclosingType.intern();
    _classType = classType;
  }

  @Override
  public ISource getSource()
  {
    IFileRepositoryBasedType enclosingType = (IFileRepositoryBasedType) TypeSystem.getByFullNameIfValid(_strEnclosingType);
    return enclosingType.getSourceFileHandle().getSource();
  }

  public String getParentType()
  {
    return _strEnclosingType;
  }

  public String getNamespace()
  {
    return _strNamespace;
  }

  public String getFilePath()
  {
    IFileRepositoryBasedType enclosingType = (IFileRepositoryBasedType) TypeSystem.getByFullNameIfValid(_strEnclosingType);
    return enclosingType == null ? null : enclosingType.getSourceFileHandle().getFilePath();
  }

  @Override
  public IFile getFile() {
    IFileRepositoryBasedType enclosingType = (IFileRepositoryBasedType) TypeSystem.getByFullNameIfValid(_strEnclosingType);
    return enclosingType.getSourceFileHandle().getFile();
  }

  public boolean isTestClass()
  {
    return _bTestClass;
  }

  public boolean isValid()
  {
    IHasInnerClass enclosingType = (IHasInnerClass)TypeSystem.getByFullNameIfValid( _strEnclosingType );
    return enclosingType != null && enclosingType.getInnerClass( getRelativeName() ) != null;
  }

  @Override
  public boolean isStandardPath() {
    IFileRepositoryBasedType enclosingType = (IFileRepositoryBasedType) TypeSystem.getByFullNameIfValid(_strEnclosingType);
    return enclosingType.getSourceFileHandle().isStandardPath();
  }

  @Override
  public boolean isIncludeModulePath() {
    IFileRepositoryBasedType enclosingType = (IFileRepositoryBasedType) TypeSystem.getByFullNameIfValid(_strEnclosingType);
    return enclosingType.getSourceFileHandle().isIncludeModulePath();
  }

  public void cleanAfterCompile()
  {
    // nothing required
  }

  public ClassType getClassType()
  {
    return _classType;
  }

  public String getTypeNamespace()
  {
    return _strEnclosingType;
  }

  public String getRelativeName()
  {
    return _strInnerClass;
  }

  @Override
  public void setOffset( int iOffset )
  {
    _iOffset = iOffset;
  }
  @Override
  public int getOffset()
  {
    return _iOffset;
  }

  @Override
  public void setEnd( int iEnd )
  {
    _iEnd = iEnd;
  }
  @Override
  public int getEnd()
  {
    return _iEnd;
  }

  @Override
  public String getFileName()
  {
    return null;
  }

  public int getMark()
  {
    return _mark;
  }
  void setMark( int mark )
  {
    _mark = mark;
  }

}
