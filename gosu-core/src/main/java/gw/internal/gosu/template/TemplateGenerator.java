/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.internal.gosu.parser.CommonSymbolsScope;
import gw.internal.gosu.parser.CompiledGosuClassSymbolTable;
import gw.internal.gosu.parser.ContextInferenceManager;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.IGosuEnhancementInternal;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.Program;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ILockedDownSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.resources.Res;
import gw.lang.parser.template.IEscapesAllContent;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.parser.template.StringEscaper;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuClassUtil;
import gw.util.GosuEscapeUtil;
import gw.util.GosuStringUtil;
import gw.util.Stack;
import gw.util.StreamUtil;
import gw.util.concurrent.LockingLazyVar;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A template generator employing Gosu.
 * <p/>
 * Works much like JSP -- uses <% script %> for scriptlets and <%= expr %> for expressions.
 * Also supports JSP comments like this: <%-- comment here --%>
 * <p/>
 * Templates can be any type e.g., XML, HTML, text, whatever.
 */
public class TemplateGenerator implements ITemplateGenerator
{
  public static final String GS_TEMPLATE = "GsTemplate";
  public static final String GS_TEMPLATE_PARSED = "GsTemplateParsed";
  public static final String SCRIPTLET_BEGIN = "<%";
  public static final String SCRIPTLET_END = "%>";
  public static final char EXPRESSION_SUFFIX = '=';
  public static final char DECLARATION_SUFFIX = '!';
  public static final char DIRECTIVE_SUFFIX = '@';
  public static final String COMMENT_BEGIN = "<%--";
  public static final String COMMENT_END = "--%>";
  public static final String ALTERNATE_EXPRESSION_BEGIN = "${";
  public static final String ALTERNATE_EXPRESSION_END = "}";

  public static final char ESCAPED_SCRIPTLET_MARKER = '\uffe0';
  public static final char ESCAPED_SCRIPTLET_BEGIN_CHAR = '\uffe1';
  public static final char ESCAPED_ALTERNATE_EXPRESSION_BEGIN_CHAR = '\uffe2';

  public static final int SCRIPTLET_BEGIN_LEN = SCRIPTLET_BEGIN.length();
  public static final int SCRIPTLET_END_LEN = SCRIPTLET_END.length();
  public static final int COMMENT_BEGIN_LEN = COMMENT_BEGIN.length();
  public static final int COMMENT_END_LEN = COMMENT_END.length();
  public static final int ALTERNATE_EXPRESSION_BEGIN_LEN = ALTERNATE_EXPRESSION_BEGIN.length();
  public static final int ALTERNATE_EXPRESSION_END_LEN = ALTERNATE_EXPRESSION_END.length();

  public static final LockingLazyVar<ISymbol> PRINT_CONTENT_SYMBOL = new LockingLazyVar<ISymbol>() {
    protected ISymbol init() {
      try {
        return new LockedDownSymbol( PRINT_METHOD,
                           new FunctionType( PRINT_METHOD, GosuParserTypes.NULL_TYPE(), new IType[]{GosuParserTypes.STRING_TYPE(), JavaTypes.pBOOLEAN()} ),
                           TemplateGenerator.class.getMethod( PRINT_METHOD, String.class, boolean.class ) );
      } catch (NoSuchMethodException e) {
        throw new RuntimeException( e );
      }
    }
  };
  public static final LockingLazyVar<ISymbol> PRINT_RANGE_SYMBOL = new LockingLazyVar<ISymbol>() {
    protected ISymbol init() {
      try {
        return new LockedDownSymbol( PRINT_RANGE_METHOD,
                           new FunctionType( PRINT_RANGE_METHOD, GosuParserTypes.NULL_TYPE(), new IType[]{JavaTypes.pINT(), JavaTypes.pINT()} ),
                           TemplateGenerator.class.getMethod( PRINT_RANGE_METHOD, int.class, int.class ) );
      } catch (NoSuchMethodException e) {
        throw new RuntimeException( e );
      }
    }
  };
  static
  {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        PRINT_CONTENT_SYMBOL.clear();
        PRINT_RANGE_SYMBOL.clear();
      }
    });
  }
    
  private static ThreadLocal<Stack<RuntimeData>> g_runtimeData = new ThreadLocal<Stack<RuntimeData>>();
  public static final int SUBSTR_CHUNKSIZE = 2048;

  private String _fqn;
  private String _scriptStr;
  private List<ISymbol> _params = new ArrayList<ISymbol>();

  private Program _program;
  private ISymbolTable _compileTimeSymbolTable;
  private IType _supertype;
  private boolean _useStudioEditorParser;
  private boolean _disableAlternative;
  private boolean _hasOwnSymbolScope;
  private ContextInferenceManager _ctxInferenceMgr;
  private boolean _bStringLiteralTemplate;

  /**
   * Generates a template of any format having embedded Gosu.
   *
   * @param readerTemplate The source of the template.
   * @param writerOut      Where the output should go.
   * @param symTable         The symbol table to use.
   * @throws TemplateParseException on execution exception
   */
  public static void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable symTable)
    throws TemplateParseException
  {
    generateTemplate(readerTemplate, writerOut, symTable, false);
  }

  /**
   * Generates a template of any format having embedded Gosu.
   *
   * @param readerTemplate The source of the template.
   * @param writerOut      Where the output should go.
   * @param symTable         The symbol table to use.
   * @param strict  whether to allow althernative template
   * @throws TemplateParseException on execution exception
   */
  public static void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable symTable, boolean strict)
      throws TemplateParseException {
    TemplateGenerator te = new TemplateGenerator( readerTemplate);
    te.setDisableAlternative(strict);
    te.execute( writerOut, symTable);
  }

  public static TemplateGenerator getTemplate( Reader readerTemplate )
  {
    return new TemplateGenerator( readerTemplate );
  }

  public static TemplateGenerator getTemplate(Reader readerTemplate, String fullyQualifiedName) {
    TemplateGenerator template = getTemplate(readerTemplate);
    template.setFqn(fullyQualifiedName);
    template.setHasOwnSymbolScope(true);
    return template;
  }

  private void setHasOwnSymbolScope(boolean hasSymbolScope) {
    _hasOwnSymbolScope = hasSymbolScope;
  }

  public Program getProgram()
  {
    return _program;
  }

  private static RuntimeData getRuntimeData()
  {
    Stack stack = g_runtimeData.get();
    if( stack != null && stack.size() > 0 )
    {
      return (RuntimeData)stack.peek();
    }
    return null;
  }

  private static void pushRuntimeData( RuntimeData pair )
  {
    Stack<RuntimeData> stack = g_runtimeData.get();
    if( stack == null )
    {
      g_runtimeData.set( stack = new Stack<RuntimeData>() );
    }
    stack.push( pair );
  }

  private static void popRuntimeData()
  {
    Stack<RuntimeData> stack = g_runtimeData.get();
    stack.pop();
  }

  /**
   * WARNING:  This will consume the reader and close it!
   * @param reader the reader containing the template
   */
  private TemplateGenerator( Reader reader )
  {
    try {
      _scriptStr = StreamUtil.getContent(reader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @throws TemplateParseException
   */
  public void execute( Writer writer, ISymbolTable symbolTable) throws TemplateParseException {
    execute(writer, null, symbolTable);
  }

  /**
   * @throws TemplateParseException
   */
  public void execute( Writer writer, StringEscaper escaper, ISymbolTable symTable) throws TemplateParseException
  {
    symTable.pushScope();
    String strCompiledSource = null;
    try
    {
      symTable.putSymbol( PRINT_CONTENT_SYMBOL.get() );
      symTable.putSymbol( PRINT_RANGE_SYMBOL.get() );
      if(_supertype != null) {
        symTable.putSymbol(new Symbol("this", _supertype, null));
      }
      pushRuntimeData( new RuntimeData( writer, escaper ) );
      try
      {
        if( _program == null ) {
          TypeSystem.lock();
          try {
            if( _program == null ) {
            List<TemplateParseException> exceptions = new ArrayList<TemplateParseException>();
            strCompiledSource = transformTemplate(_scriptStr, exceptions);
            if (!exceptions.isEmpty()) {
              throw exceptions.get(0);
            }
            _program = compile( new Stack<>(), strCompiledSource, symTable, new HashMap<>(), null, null, null );
            _compileTimeSymbolTable = symTable.copy();
          }
          if( _fqn == null )
          {
            _program.getGosuProgram().setThrowaway( true );
          }
        }
          finally {
            TypeSystem.unlock();
          }
        }
        _program.evaluate(extractExternalSymbols( _compileTimeSymbolTable, symTable ));
      }
      finally
      {
        popRuntimeData();
      }
    }
    catch( ParseResultsException e )
    {
      throw new TemplateParseException( e, strCompiledSource );
    }
    finally
    {
      symTable.popScope();
    }
  }

  public void compile(ISymbolTable symTable) throws TemplateParseException
  {
    compile(new Stack<>(), symTable, new HashMap<>(), null, null, _ctxInferenceMgr);
  }

  public void compile( Stack<IScriptPartId> scriptPartIdStack, ISymbolTable symTable, Map<String, List<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap, Stack<BlockExpression> blocks, ContextInferenceManager ctxInferenceMgr) throws TemplateParseException {
    symTable.pushScope();
    if (ctxInferenceMgr != null) {
      ctxInferenceMgr.suspendRefCollection();
    }
    String strCompiledSource = null;
    try
    {
      symTable.putSymbol( PRINT_CONTENT_SYMBOL.get() );
      symTable.putSymbol( PRINT_RANGE_SYMBOL.get() );

      if( _program == null ) {
        TypeSystem.lock();
        try {
          if( _program == null ) {
          List<TemplateParseException> exceptions = new ArrayList<TemplateParseException>();
          strCompiledSource = transformTemplate(_scriptStr, exceptions);
          if (!exceptions.isEmpty()) {
            throw exceptions.get(0);
          }
          for (ISymbol param : _params) {
            Symbol s = new Symbol( param.getName(), param.getType(), null );
            symTable.putSymbol(s);
          }
          _program = compile( scriptPartIdStack, strCompiledSource, symTable, dfsDeclByName, typeUsesMap, blocks, ctxInferenceMgr );
          _compileTimeSymbolTable = symTable.copy();
        }
      }
        finally {
          TypeSystem.unlock();
        }
      }
    }
    catch( ParseResultsException e )
    {
      throw new TemplateParseException( e, strCompiledSource );
    }
    finally
    {
      symTable.popScope();
      if (ctxInferenceMgr != null) {
        ctxInferenceMgr.resumeRefCollection();
      }
    }
  }

  /**
   * @return the program to execute
   * @throws gw.lang.parser.exceptions.ParseException
   *
   */
  private Program compile( Stack<IScriptPartId> scriptPartIdStack, String strCompiledSource, ISymbolTable symbolTable, Map<String, List<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap, Stack<BlockExpression> blocks, ContextInferenceManager ctxInferenceMgr) throws ParseResultsException
  {
    IGosuParser parser = GosuParserFactory.createParser(symbolTable, ScriptabilityModifiers.SCRIPTABLE);
    for( IScriptPartId id: scriptPartIdStack ) {
      ((GosuParser)parser).pushScriptPart( id );
    }
    parser.setScript( strCompiledSource );
    if(parser instanceof GosuParser) {
      parser.setDfsDeclInSetByName(dfsDeclByName);
      if (ctxInferenceMgr != null) {
        ((GosuParser) parser).setContextInferenceManager(ctxInferenceMgr);
      }
    }
    if (typeUsesMap == null) {
      typeUsesMap = parser.getTypeUsesMap();
    } else {
      parser.setTypeUsesMap(typeUsesMap);
    }
    if(_fqn != null) {
      typeUsesMap.addToTypeUses(GosuClassUtil.getPackage(_fqn) + ".*");
    }
    if( blocks != null )
    {
      ((GosuParser) parser).setBlocks( blocks );
    }
    if(_supertype != null && _supertype instanceof IGosuClassInternal ) {
      IGosuClassInternal supertype = (IGosuClassInternal) _supertype;
      addStaticSymbols(symbolTable, (GosuParser) parser, supertype);

      List<? extends GosuClassTypeLoader> typeLoaders = TypeSystem.getCurrentModule().getTypeLoaders(GosuClassTypeLoader.class);
      for (GosuClassTypeLoader typeLoader : typeLoaders) {
        List<? extends IGosuEnhancement> enhancementsForType = typeLoader.getEnhancementIndex().getEnhancementsForType(supertype);
        for (IGosuEnhancement enhancement : enhancementsForType) {
          if (enhancement instanceof IGosuEnhancementInternal) {
            addStaticSymbols(symbolTable, (GosuParser) parser, (IGosuEnhancementInternal) enhancement);
          }
        }
      }

      // clear out private DFS that may have made their way into the dfsDecldsByName (jove this is ugly)
      for (Entry<String, List<IFunctionSymbol>> dfsDecls : parser.getDfsDecls().entrySet()) {
        for (Iterator<IFunctionSymbol> it = dfsDecls.getValue().iterator(); it.hasNext(); ) {
          IFunctionSymbol fs = it.next();
          if (fs instanceof Symbol && fs.isPrivate()) {
            it.remove();
          }
        }
      }
    }
    IScriptPartId scriptPart;
    ISymbol thisSymbol = symbolTable.getSymbol( Keyword.KW_this.getName() );
    if( thisSymbol != null && thisSymbol.getType() instanceof IGosuClass )
    {
      scriptPart = new ScriptPartId( thisSymbol.getType(), GS_TEMPLATE_PARSED );
    }
    else
    {
      scriptPart = new TypelessScriptPartId( GS_TEMPLATE_PARSED );
    }
    Program program = (Program)parser.parseProgram( scriptPart, null, null, true );
    program.clearParseTreeInformation();
    return program;
  }

  private void addStaticSymbols(ISymbolTable symbolTable, GosuParser parser, IGosuClassInternal supertype) {
    supertype.putClassMembers( parser, symbolTable, supertype, true );
    for(Object entryObj : symbolTable.getSymbols().entrySet()) {
      @SuppressWarnings({"unchecked"})
      Entry<CharSequence, ISymbol> entry = (Entry<CharSequence, ISymbol>) entryObj;
      if((entry.getValue()).isPrivate()) {
        symbolTable.removeSymbol(entry.getKey());
      }
    }
  }

  public void verify( IGosuParser parser, Map<String, List<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap) throws ParseResultsException
  {
    verify( parser, dfsDeclByName, typeUsesMap, false );
  }
  private IProgram verify( IGosuParser parser, Map<String, List<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap, boolean bDoNotThrowParseResultsException ) throws ParseResultsException
  {
    assert _scriptStr != null : "Cannot verify a template after it has been compiled";
    ISymbolTable symTable = parser.getSymbolTable();
    symTable.pushScope();
    try
    {
      parser.setScript( _scriptStr );
      if (parser instanceof GosuParser) {
        parser.setTokenizerInstructor( new TemplateTokenizerInstructor(((GosuParser)parser).getTokenizer()) );
        parser.setDfsDeclInSetByName(dfsDeclByName);
      }
      if (typeUsesMap == null) {
        typeUsesMap = parser.getTypeUsesMap();
      } else {
        parser.setTypeUsesMap(typeUsesMap);
      }
      if(_fqn != null) {
        typeUsesMap.addToTypeUses(GosuClassUtil.getPackage(_fqn) + ".*");
      }
      parser.setTypeUsesMap(typeUsesMap);
      IScriptPartId scriptPart = parser.getScriptPart() == null
                                 ? new TypelessScriptPartId( GS_TEMPLATE )
                                 : new ScriptPartId( parser.getScriptPart().getContainingType(), GS_TEMPLATE );
      return parser.parseProgram( scriptPart, true, false, null, null, false, bDoNotThrowParseResultsException );
    }
    finally
    {
      symTable.popScope();
      for (IParseTree parseTree : parser.getLocations()) {
        parseTree.setLength(Math.min(parseTree.getLength(), _scriptStr.length() - parseTree.getOffset()));
      }
    }
  }

  public void verify( IGosuParser parser ) throws ParseResultsException
  {
    verify( parser, false );
  }

  private IProgram verify( IGosuParser parser, boolean bDoNotThrowParseResultException ) throws ParseResultsException
  {
    HashMap<String, List<IFunctionSymbol>> dfsMap = new HashMap<>();
    dfsMap.put( PRINT_METHOD, Collections.<IFunctionSymbol>singletonList(new DynamicFunctionSymbol(parser.getSymbolTable(),
            PRINT_METHOD,
            new FunctionType(PRINT_METHOD, GosuParserTypes.NULL_TYPE(), new IType[]{GosuParserTypes.STRING_TYPE(), JavaTypes.pBOOLEAN()}),
            Arrays.<ISymbol>asList(new Symbol("content", GosuParserTypes.STRING_TYPE(), null), new Symbol("escape", JavaTypes.pBOOLEAN(), null)),
            (IExpression)null)));
    dfsMap.put(PRINT_RANGE_METHOD, Collections.<IFunctionSymbol>singletonList(new DynamicFunctionSymbol(parser.getSymbolTable(),
        PRINT_RANGE_METHOD,
        new FunctionType(PRINT_RANGE_METHOD, GosuParserTypes.NULL_TYPE(), new IType[]{GosuParserTypes.STRING_TYPE(), JavaTypes.pINT(), JavaTypes.pINT()}),
        Arrays.<ISymbol>asList(new Symbol("start", JavaTypes.pINT(), null), new Symbol("end", JavaTypes.pINT(), null)),
        (IExpression) null)));
    return verify( parser, dfsMap, null, bDoNotThrowParseResultException );
  }

  public List<TemplateParseException> getTemplateSyntaxProblems() {
    List<TemplateParseException> exceptions = new ArrayList<TemplateParseException>();
    transformTemplate(_scriptStr, exceptions);
    return exceptions;
  }

  @SuppressWarnings({"ThrowableInstanceNeverThrown", "ConstantConditions"})
  private String transformTemplate(String strSource, List<TemplateParseException> exceptions)
  {
    _params.clear();
    StringBuilder sbTarget = new StringBuilder( strSource.length() );
    int iIndex = 0;
    while( true )
    {
      int iIndex2 = strSource.indexOf( SCRIPTLET_BEGIN, iIndex );
      int altIndex2 = strSource.indexOf( ALTERNATE_EXPRESSION_BEGIN, iIndex );
      if( iIndex2 >= 0 || altIndex2 >= 0 ) {
        if( iIndex2 >= 0 && (altIndex2 < 0 || iIndex2 < altIndex2) ) {
          if( iIndex2 > 0 && strSource.charAt( iIndex2 - 1 ) == '\\' ) {
            // Handle escaped "\<%"
            addRefText( sbTarget, iIndex, iIndex2 - 1 );
            addText( sbTarget, SCRIPTLET_BEGIN.substring( 0, 1 ) );
            iIndex = iIndex2 + 1;
          }
          else {
            boolean bPrecedingContentEndsWithNewLine = iIndex2-iIndex <= 0 || strSource.charAt( iIndex2-1 ) == '\n';
            addRefText( sbTarget, iIndex, iIndex2 );
            iIndex = iIndex2 + SCRIPTLET_BEGIN_LEN;
            boolean bExpression = false;
            if( iIndex < strSource.length() ) {
              if( strSource.indexOf( "--", iIndex ) == iIndex ) {
                int iEndComment = strSource.indexOf( COMMENT_END, iIndex+2 );
                if( iEndComment > 0 ) {
                  iIndex = iEndComment + COMMENT_END_LEN;
                  continue;
                }
                else {
                  return sbTarget.toString();
                }
              }
              bExpression = strSource.charAt( iIndex ) == EXPRESSION_SUFFIX;
              boolean bDeclaration = strSource.charAt( iIndex ) == DECLARATION_SUFFIX;
              boolean bDirective = strSource.charAt( iIndex ) == DIRECTIVE_SUFFIX;
              iIndex += ((bExpression || bDeclaration || bDirective) ? 1 : 0);

              iIndex2 = strSource.indexOf( SCRIPTLET_END, iIndex );
              if( iIndex2 < 0 ) {
                int iLineNumber = GosuStringUtil.getLineNumberForIndex( strSource, iIndex );
                int iColumn = getColumnForIndex( strSource, iIndex );
                exceptions.add( new TemplateParseException( bExpression ? Res.MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION : Res.MSG_TEMPLATE_MISSING_END_TAG_SCRIPTLET, iLineNumber, iColumn, iIndex ) );
                return sbTarget.toString();
              }

              String strScript = strSource.substring( iIndex, iIndex2 );
              if( bExpression ) {
                addExpression( sbTarget, strScript );
              }
              else if( bDirective ) {
                try {
                  int iLineNumber = GosuStringUtil.getLineNumberForIndex( strSource, iIndex );
                  int iColumn = getColumnForIndex( strSource, iIndex );
                  processDirective( strScript, iLineNumber, iColumn, iIndex );
                }
                catch( TemplateParseException e ) {
                  exceptions.add( e );
                }
              }
              else {
                addScriptlet( sbTarget, strScript );
              }
            }

            iIndex = iIndex2 + SCRIPTLET_END_LEN;
            if( !bExpression && bPrecedingContentEndsWithNewLine ) {
              iIndex = ignoreTrailingLineSeparator( strSource, iIndex );
          }
        }
        }
        else if( _disableAlternative && altIndex2 > 0 && strSource.charAt( altIndex2 - 1 ) != '\\' ) {
          addRefText( sbTarget, iIndex, altIndex2 - 1 );
          addText( sbTarget, "\\" + ALTERNATE_EXPRESSION_BEGIN.substring( 0, 1 ) );
          iIndex = altIndex2 + 1;
        }
        else {
          if( altIndex2 > 0 && strSource.charAt( altIndex2 - 1 ) == '\\' ) {
            addRefText( sbTarget, iIndex, altIndex2 - 1 );
            addText( sbTarget, ALTERNATE_EXPRESSION_BEGIN.substring( 0, 1 ) );
            iIndex = altIndex2 + 1;
          }
          else {
            addRefText( sbTarget, iIndex, altIndex2 );
            iIndex = altIndex2 + ALTERNATE_EXPRESSION_BEGIN_LEN;

            altIndex2 = strSource.indexOf( ALTERNATE_EXPRESSION_END, iIndex );
            int nextOpen = strSource.indexOf( "{", iIndex );
            if( nextOpen != -1 && nextOpen < altIndex2 ) {
              int numOpen = 2;
              while( numOpen > 1 && altIndex2 != -1 ) {
                nextOpen = strSource.indexOf( "{", Math.min( nextOpen, altIndex2 ) + 1 );
                if( nextOpen != -1 && nextOpen < altIndex2 ) {
                  numOpen++;
                }
                else {
                  numOpen--;
                  altIndex2 = strSource.indexOf( "}", altIndex2 + ALTERNATE_EXPRESSION_END_LEN );
                }
              }
            }
            if( altIndex2 < 0 ) {
              int iLineNumber = GosuStringUtil.getLineNumberForIndex( strSource, iIndex );
              int iColumn = getColumnForIndex( strSource, iIndex );
              exceptions.add( new TemplateParseException( Res.MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION_ALT, iLineNumber, iColumn, iIndex ) );
              return sbTarget.toString();
            }

            String strScript = strSource.substring( iIndex, altIndex2 );
            addExpression( sbTarget, strScript );

            iIndex = altIndex2 + ALTERNATE_EXPRESSION_END_LEN;
          }
        }
      }
      else {
        addRefText( sbTarget, iIndex, strSource.length() );
        break;
      }
    }

    return sbTarget.toString();
  }

  private int ignoreTrailingLineSeparator( String strSource, int iIndex ) {
    if( iIndex < strSource.length() ) {
      if( strSource.charAt( iIndex ) == '\n' ) {
        iIndex++;
      }
      else if( strSource.startsWith( System.lineSeparator(), iIndex ) ) {
        iIndex += System.lineSeparator().length();
      }
    }
    return iIndex;
  }

  private void processDirective(String strScript, int lineNumber, int column, int offset) throws TemplateParseException {
    strScript = strScript.trim();
    if(strScript.startsWith("params")) {
      if(!_params.isEmpty()) {
        throw new TemplateParseException(Res.MSG_TEMPLATE_MULTIPLE_PARAMS, lineNumber, column, offset);
      }
      int iOpeningParen = strScript.indexOf( "(" );
      int iClosingParen = strScript.lastIndexOf( ")" );
      String strSignature = "";
      if( iOpeningParen > 0 && iClosingParen > 0 ) {
       strSignature = strScript.substring( iOpeningParen + 1, iClosingParen ).trim();
      }
      if (strSignature.length() > 0) {
        // get type uses map first
        GosuParser usesParser = (GosuParser) GosuParserFactory.createParser(_scriptStr);
        usesParser.setTokenizerInstructor( new TemplateTokenizerInstructor(usesParser.getTokenizer()) );
        if( _fqn != null ) {
          ITypeUsesMap typeUsesMap = usesParser.getTypeUsesMap();
          if( typeUsesMap != null ) {
            typeUsesMap.addToTypeUses(GosuClassUtil.getPackage(_fqn) + ".*");
          }
        }
        try {
          usesParser.parseProgram( new TypelessScriptPartId( GS_TEMPLATE ) );
        } catch (ParseResultsException e) {
          // there are going to be errors, ignore them
        }
        // now parse signature
        GosuParser parser = (GosuParser) GosuParserFactory.createParser(strSignature);
        parser.setEditorParser(_useStudioEditorParser);
        parser.setTypeUsesMap(usesParser.getTypeUsesMap());
        parser.getTokenizer().nextToken();
        Identifier pe = new Identifier();
        _params.addAll(parser.parseParameterDeclarationList(pe, false, null));
        if (pe.hasParseExceptions()) {
          throw new TemplateParseException(Res.MSG_TEMPLATE_INVALID_PARAMS, lineNumber, column, offset, strScript + ": " + pe.getParseExceptions().get(0).getConsoleMessage());
        }
      }
    } else if (strScript.startsWith("extends")) {
      String typeName = strScript.substring(strScript.indexOf("extends") + "extends".length()).trim();
      _supertype = TypeLoaderAccess.instance().getByFullNameIfValid(typeName);
      if(_supertype == null) {
        throw new TemplateParseException(Res.MSG_INVALID_TYPE, lineNumber, column, offset, typeName);
      }
    } else {
      throw new TemplateParseException(Res.MSG_TEMPLATE_UNKNOWN_DIRECTIVE, lineNumber, column, offset, strScript);
    }
  }

  private void addText( StringBuilder strTarget, String strText )
  {
    if( !GosuStringUtil.isEmpty( strText ) )
    {
      strText = escapeForGosuStringLiteral( strText );
      strTarget.append( PRINT_METHOD ).append( "(\"" ).append( strText ).append( "\", false)\r\n" );
    }
  }

  private void addRefText( StringBuilder sbTarget, int iStart, int iEnd )
  {
    if( isForStringLiteral() )
    {
      addText( sbTarget, _scriptStr.substring( iStart, iEnd ) );
    }
    else if( iEnd - iStart > 0 )
    {
      sbTarget.append( PRINT_RANGE_METHOD )
        .append( "(" )
        .append( iStart ).append( "," )
        .append( iEnd )
        .append( ")" );
    }
  }

  private void addExpression( StringBuilder strTarget, String strExpression )
  {
    if( strExpression.trim().length() != 0 )
    {
      strTarget.append( PRINT_METHOD ).append( "((" ).append( strExpression ).append( ") as String, true)\r\n" );
    }
  }

  private void addScriptlet( StringBuilder strTarget, String strScript )
  {
    strTarget.append( strScript ).append( "\r\n" );
  }

  private String escapeForGosuStringLiteral( String strText )
  {
    if( strText == null )
    {
      return null;
    }

    strText = GosuEscapeUtil.escapeForGosuStringLiteral( strText );
    return strText;
  }

  private int getColumnForIndex( String strSource, int iIndex )
  {
    int lastLineBreak = 0;
    for( int i = 0; i <= iIndex && i < strSource.length(); i++ )
    {
      char c = strSource.charAt( i );
      if( c == '\n' )
      {
        lastLineBreak = i;
      }
    }

    return iIndex - lastLineBreak;
  }

  /**
   * For internal use only!!
   */
  @SuppressWarnings("UnusedDeclaration")
  public static void printContent( String strContent , boolean escape )
  {
    try
    {
      RuntimeData pair = getRuntimeData();
      Writer writer = pair._writer;

      if (escape && pair._esc != null) {
        strContent = pair._esc.escape(strContent);
      } else if (pair._esc instanceof IEscapesAllContent) {
        strContent = ((IEscapesAllContent)pair._esc).escapeBody(strContent);
      }

      writer.write( strContent == null ? "null" : strContent );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  public static void printRange( int iStart, int iEnd )
  {
    try
    {
      RuntimeData runtimeData = getRuntimeData();
      String strContent = runtimeData._templateSource.substring( iStart, iEnd );
      Writer writer = runtimeData._writer;
      writer.write( strContent );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void setDisableAlternative(boolean disableAlternative) {
    _disableAlternative = disableAlternative;
  }

  public void setContextInferenceManager( ContextInferenceManager ctxInferenceMgr )
  {
    _ctxInferenceMgr = ctxInferenceMgr.copy();
  }

  public void setForStringLiteral( boolean bForStringLiteralTemplate ) {
    _bStringLiteralTemplate = bForStringLiteralTemplate;
  }
  public boolean isForStringLiteral() {
    return _bStringLiteralTemplate;
  }

  private class RuntimeData {
    Writer _writer;
    StringEscaper _esc;
    String _templateSource;

    public RuntimeData( Writer writer, StringEscaper esc ) {
      _writer = writer;
      _esc = esc;
      _templateSource = getSource();
    }
  }

  public boolean isValid() {
    compileIfNotCompiled();
    return _program != null && !_program.hasParseExceptions();
  }

  private void setFqn(String fullyQualifiedName) {
    _fqn = fullyQualifiedName;
  }

  public String getFullyQualifiedTypeName()
  {
    return _fqn;
  }

  public List<ISymbol> getParameters() {
    compileIfNotCompiled();
    return _params;
  }

  private void compileIfNotCompiled() {
    if(_program == null) {
      if(_hasOwnSymbolScope) {
        CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable();
      }
      try {
        compile(CompiledGosuClassSymbolTable.instance());
      } catch (TemplateParseException e) {
        // ignore?
      } finally {
        if (_hasOwnSymbolScope) {
          CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable();
        }
      }
    }
  }
  
  public IType getSuperType() {
    return _supertype;
  }

  public void setUseStudioEditorParser(boolean useStudioEditorParser) {
    _useStudioEditorParser = useStudioEditorParser;
  }

  @Override
  public String toString() {
    if (_scriptStr != null) {
      return _scriptStr;
    } else {
      return _program.toString();
    }
  }

  public String getSource() {
    return _scriptStr;
  }

  private IExternalSymbolMap extractExternalSymbols( ISymbolTable compileTimeSymbolTable, ISymbolTable runtimeSymbolTable ) {
    HashMap<String, ISymbol> externalSymbolsMap = new HashMap<String, ISymbol>( 8 );
    putSymbols( compileTimeSymbolTable, externalSymbolsMap );
    putSymbols( runtimeSymbolTable, externalSymbolsMap ); // overwrite compile time symbols with the runtime stuff.  yeesh
    return new ExternalSymbolMapForMap(externalSymbolsMap);
  }

  private void putSymbols( ISymbolTable symTable, HashMap<String, ISymbol> externalSymbolsMap )
  {
    Map symbols = symTable.getSymbols();
    if( symbols != null )
    {
      //noinspection unchecked
      for( ISymbol sym : (Collection<ISymbol>)symbols.values() )
      {
        if( !(sym instanceof CommonSymbolsScope.LockedDownSymbol) && sym != null )
        {
          externalSymbolsMap.put( (String)sym.getName(), sym );
        }
      }
    }
  }

  public static class LockedDownSymbol extends Symbol implements ILockedDownSymbol
  {
    public LockedDownSymbol( CharSequence strName, IType type, Method value )
    {
      super( strName.toString(), type, value );
    }

    @Override
    public Object getValue()
    {
      return getValueDirectly();
    }
  }

}
