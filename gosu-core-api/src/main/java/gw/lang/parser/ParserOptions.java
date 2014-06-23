/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.resources.ResourceKey;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ParserOptions
{
  private ITypeUsesMap _typeUsesMap;
  private IType _expectedType;
  private String _strEnclosingType;
  private ITokenizerInstructor _ti;
  private IFileContext _fileContext;
  private boolean _bGenRootExprAccess;
  private ISymbolTable _additionalDFSDecls;
  private boolean _captureSymbolsForEval;
  private Map<String, Set<IFunctionSymbol>> _declSymbols;
  private boolean _shouldOptimize = true;
  private IGosuParser _parser;
  private IType _superType;
  private boolean _bStatementsOnly;
  private IScriptPartId _partId;
  private boolean _throwawayProgram;
  private Object _ctxInferenceMgr;
  private Set<ResourceKey> _ignoreWarnings = new HashSet<ResourceKey>();
  private boolean _allowUses;
  private boolean _bAnon;

  public ParserOptions withParser( IGosuParser parser )
  {
    _parser = parser;
    return this;
  }

  public ParserOptions withTypeUsesMap( ITypeUsesMap typeUsesMap )
  {
    _typeUsesMap = typeUsesMap;
    return this;
  }

  public ParserOptions withEnclosingType( String strEnclosingType )
  {
    _strEnclosingType = strEnclosingType;
    return this;
  }

  public ParserOptions withDefaultTypeUsesMap()
  {
    _typeUsesMap = TypeSystem.getDefaultTypeUsesMap().copy();
    return this;
  }

  public ParserOptions withExpectedType( IType expectedType )
  {
    _expectedType = expectedType;
    return this;
  }

  public ParserOptions withTokenizerInstructor( ITokenizerInstructor ti )
  {
    _ti = ti;
    return this;
  }

  public ParserOptions withFileContext( IFileContext fileContext )
  {
    _fileContext = fileContext;
    return this;
  }

  public ParserOptions withCtxInferenceMgr( Object ctxInferenceMgr )
  {
    _ctxInferenceMgr = ctxInferenceMgr;
    return this;
  }

  public ParserOptions withGenRootExprAccess( boolean genRootExprAccess )
  {
    _bGenRootExprAccess = genRootExprAccess;
    return this;
  }

  public ParserOptions withAdditionalDFSDecls( ISymbolTable additionalDFSDecls )
  {
    _additionalDFSDecls = additionalDFSDecls;
    return this;
  }

  public ParserOptions withCaptureSymbolsForEval( boolean captureSymbolsForEval )
  {
    _captureSymbolsForEval = captureSymbolsForEval;
    return this;
  }

  public ParserOptions withDeclSymbols( Map<String, Set<IFunctionSymbol>> declSymbols )
  {
    _declSymbols = declSymbols;
    return this;
  }

  public ParserOptions asThrowawayProgram()
  {
    _throwawayProgram = true;
    return this;
  }

  public ParserOptions asAnonymous()
  {
    _bAnon = true;
    return this;
  }

  public ParserOptions withStatementsOnly()
  {
    _bStatementsOnly = true;
    return this;
  }

  public boolean isStatementsOnly()
  {
    return _bStatementsOnly;
  }

  public void setParserOptions( IGosuParser parser )
  {
    parser.setTypeUsesMap( _typeUsesMap );
    if( _ti != null )
    {
      parser.setTokenizerInstructor( _ti );
    }
    parser.setGenerateRootExpressionAccessForProgram( _bGenRootExprAccess );
    if( _additionalDFSDecls != null )
    {
      parser.putDfsDeclsInTable( _additionalDFSDecls );
    }
    parser.setCaptureSymbolsForEval( _captureSymbolsForEval );
    if( _declSymbols != null )
    {
      parser.setDfsDeclInSetByName( _declSymbols );
    }
    parser.setIgnoreWarnings(_ignoreWarnings);
  }

  public IType getExpectedType()
  {
    return _expectedType;
  }

  public String getEnclosingType()
  {
    return _strEnclosingType;
  }

  public IFileContext getFileContext()
  {
    return _fileContext;
  }

  public ITypeUsesMap getTypeUsesMap()
  {
    return _typeUsesMap;
  }

  public ITokenizerInstructor getTi()
  {
    return _ti;
  }

  public boolean isBGenRootExprAccess()
  {
    return _bGenRootExprAccess;
  }

  public ISymbolTable getAdditionalDFSDecls()
  {
    return _additionalDFSDecls;
  }

  public boolean isCaptureSymbolsForEval()
  {
    return _captureSymbolsForEval;
  }

  public Map<String, Set<IFunctionSymbol>> getDeclSymbols()
  {
    return _declSymbols;
  }

  public ParserOptions withShouldOptimize( boolean shouldOptimize )
  {
    _shouldOptimize = shouldOptimize;
    return this;
  }

  public boolean isShouldOptimize()
  {
    return _shouldOptimize;
  }

  public IGosuParser getParser()
  {
    return _parser;
  }

  public Object getCtxInferenceMgr()
  {
    return _ctxInferenceMgr;
  }

  public ParserOptions withSuperType( IType superType )
  {
    _superType = superType;
    return this;
  }

  public IType getSuperType()
  {
    return _superType;
  }

  public ParserOptions withScriptPartId( IScriptPartId partId )
  {
    _partId = partId;
    return this;
  }

  public IScriptPartId getScriptPartId()
  {
    return _partId;
  }

  public boolean isThrowawayProgram()
  {
    return _throwawayProgram;
  }

  public boolean isAnonymous()
  {
    return _bAnon;
  }

  public boolean allowUses() {
    return _allowUses;
  }

  public ParserOptions withIgnoreWarning(ResourceKey msgKey) {
    _ignoreWarnings.add(msgKey);
    return this;
  }

  public ParserOptions allowUsesInBody() {
    _allowUses = true;
    return this;
  }
}
