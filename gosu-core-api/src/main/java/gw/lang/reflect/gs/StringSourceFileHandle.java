/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.lang.parser.ISource;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.StringSource;
import gw.lang.parser.expressions.ITypeVariableDefinition;

import java.io.File;
import java.util.Map;

public class StringSourceFileHandle implements ISourceFileHandle
{
  private String _source;
  private boolean _bTestResource;
  private ClassType _classType;
  private String _typeName;
  private String _strEnclosingType;
  private ITypeUsesMap _typeUsesMap;
  private Map<String, ITypeVariableDefinition> _capturedTypeVars;
  private int _iOffset;
  private int _iEnd;

  private IFile _file;
  private String _fileRef;
  private ISymbolTable _extSyms;

  public StringSourceFileHandle( String typeName, CharSequence source, boolean isTestResource, ClassType classType )
  {
    this( typeName, source, null, isTestResource, classType );
  }
  public StringSourceFileHandle( String typeName, CharSequence source, IFile strPath, boolean isTestResource, ClassType classType )
  {
    _typeName = typeName;
    _source = source != null ? source.toString() : null;
    _bTestResource = isTestResource;
    _classType = classType;
    _file = strPath;
    assignFileRef();
  }

  private void assignFileRef()
  {
    if( _file == null )
    {
      return;
    }

    String strFile = getFilePath().replace('/', File.separatorChar);
    int iIndex = strFile.lastIndexOf( File.separatorChar );
    if( iIndex >= 0 )
    {
      strFile = strFile.substring( iIndex + 1 );
    }
    _fileRef = strFile;
  }

  protected String getRawSource()
  {
    return _source;
  }
  protected void setRawSource( CharSequence source )
  {
    _source = source.toString();
  }

  public ISource getSource()
  {
    return new StringSource(getRawSource());
  }

  public String getParentType()
  {
    return _strEnclosingType;
  }
  public void setParentType( String strEnclosingType )
  {
    _strEnclosingType = strEnclosingType;
  }

  public String getNamespace()
  {
    return getTypeNamespace();
  }

  public String getFilePath()
  {
    return _file == null ? null : _file.getPath().getPathString();
  }

  public boolean isTestClass()
  {
    return _bTestResource;
  }

  public boolean isValid()
  {
    return true;
  }

  public boolean isStandardPath()
  {
    return false;
  }

  public boolean isIncludeModulePath()
  {
    return false;
  }

  public void cleanAfterCompile()
  {
    _source = null;
  }

  public ClassType getClassType()
  {
    return _classType;
  }

  public String getTypeName()
  {
    return _typeName;
  }

  public String getRelativeName() {
    if (_typeName.lastIndexOf('.') == -1) {
      return _typeName;
    }
    return _typeName.substring(_typeName.lastIndexOf('.') +1);
  }

  public String getTypeNamespace() {
    if (_typeName.lastIndexOf('.') == -1) {
      return "";
    }
    return _typeName.substring(0, _typeName.lastIndexOf('.'));
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

  public void setFilePath( String filePath )
  {
    if( filePath != null ) {
      _file = CommonServices.getFileSystem().getIFile( new File( filePath ) );
      assignFileRef();
    }
  }

  @Override
  public String getFileName()
  {
    return _fileRef;
  }

  @Override
  public IFile getFile() {
    return _file;
  }

  public void setTypeUsesMap( ITypeUsesMap typeUsesMap )
  {
    _typeUsesMap = typeUsesMap;
  }
  public ITypeUsesMap getTypeUsesMap()
  {
    return _typeUsesMap;
  }

  public void setCapturedTypeVars( Map<String, ITypeVariableDefinition> capturedTypeVars )
  {
    _capturedTypeVars = capturedTypeVars;
  }
  public Map<String, ITypeVariableDefinition> getCapturedTypeVars()
  {
    return _capturedTypeVars;
  }

  @Override
  public String toString() {
    return _typeName;
  }

  public void setExternalSymbols( ISymbolTable extSyms ) {
    _extSyms = extSyms;
  }
  public ISymbolTable getExternalSymbols() {
    return _extSyms;
  }
}
