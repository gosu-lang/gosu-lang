/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ICompilationState;

/**
 */
class CompilationState implements ICompilationState
{
  private boolean _bCompilingHeader;
  private boolean _bHeaderCompiled;
  private boolean _bCompilingDeclarations;
  private boolean _bDeclarationsCompiled;
  private boolean _bCompilingDefinitions;
  private boolean _bDefinitionsCompiled;
  private boolean _bReparsingHeader;
  private boolean _bInnerDeclarationsCompiled;

  public boolean isCompilingHeader()
  {
    return _bCompilingHeader;
  }
  void setCompilingHeader( boolean bCompilingHeader )
  {
    _bCompilingHeader = bCompilingHeader;
  }
  public boolean isHeaderCompiled()
  {
    return _bHeaderCompiled;
  }
  void setHeaderCompiled()
  {
    _bHeaderCompiled = true;
  }

  public boolean isReparsingHeader()
  {
    return _bReparsingHeader;
  }
  public void setReparsingHeader( boolean bReparsingHeader )
  {
    _bReparsingHeader = bReparsingHeader;
  }

  public boolean isCompilingDeclarations()
  {
    return _bCompilingDeclarations;
  }
  void setCompilingDeclarations( boolean bCompilingDeclarations )
  {
    _bCompilingDeclarations = bCompilingDeclarations;
  }
  public boolean isDeclarationsCompiled()
  {
    return _bDeclarationsCompiled;
  }
  void setDeclarationsCompiled()
  {
    _bDeclarationsCompiled = true;
  }

  public boolean isInnerDeclarationsCompiled()
  {
    return _bInnerDeclarationsCompiled;
  }
  void setInnerDeclarationsCompiled()
  {
    _bInnerDeclarationsCompiled = true;
  }

  public boolean isCompilingDefinitions()
  {
    return _bCompilingDefinitions;
  }
  void setCompilingDefinitions( boolean bCompilingDefinitions )
  {
    _bCompilingDefinitions = bCompilingDefinitions;
  }
  public boolean isDefinitionsCompiled()
  {
    return _bDefinitionsCompiled;
  }
  void setDefinitionsCompiled()
  {
    _bDefinitionsCompiled = true;
  }

  public void clearDefinitionCompiled()
  {
    _bDefinitionsCompiled = false;
  }

  public String toString()
  {
    return "Compiling Header: " + isCompilingHeader() + "\n" +
           "Compiling Decl: " + isCompilingDeclarations() + "\n" +
           "Compiling Definitions: " + isCompilingDefinitions()  + "\n" +

           "Compiled Header: " + isHeaderCompiled() + "\n" +
           "Compiled Decl: " + isDeclarationsCompiled()  + "\n" +
           "Compiled Definitions: " + isDefinitionsCompiled()  + "\n";
  }
  
}
