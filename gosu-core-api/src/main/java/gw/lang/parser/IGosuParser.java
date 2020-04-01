/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.parser;

import gw.lang.parser.statements.IUsesStatementList;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ISourceFileHandle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGosuParser extends IParserPart
{
  //
  // Reusable, cached objects
  //

  // Cache high-volume values
  public Double NaN = Double.NaN;
  public Double ZERO = (double)0;
  public Double ONE = (double)1;
  public Double[] DOUBLE_DIGITS = new Double[]{ZERO, ONE, 2d, 3d, 4d, 5d, 6d, 7d, 8d, 9d};
  public BigDecimal BIGD_ZERO = new BigDecimal( 0 );

  /**
   * The context associated with the parser's script. E.g., a file name, a library,
   * a rule, etc.
   */
  public IScriptPartId getScriptPart();

  /**
   * Set the script or expression to parse and execute.
   *
   * @param script The text of the Gosu source to parse/execute.
   */
  public void setScript( String script );

  /**
   * Set the script or expression to parse and execute.
   *
   * @param reader A reader for the rule (Gosu) source to parse/execute.
   */
  public void setScript( SourceCodeReader reader );

  /**
   * Set the script or expression to parse and execute.
   *
   * @param reader A reader for the rule (Gosu) source to parse/execute.
   */
  public void setScript( ISource reader );

  /**
   * Returns the parser's symbol table.
   */
  public ISymbolTable getSymbolTable();

  /**
   * Sets the parser's symbol table.
   *
   * @param symTable The symbol table the parser will use when parsing and executing rules.
   */
  public void setSymbolTable( ISymbolTable symTable );

  /**
   * Get the type uses map.
   */
  public ITypeUsesMap getTypeUsesMap();

  /**
   * Set the type uses map.
   */
  public void setTypeUsesMap( ITypeUsesMap typeUsesMap );

  /**
   * Parses a set of Gosu statements.  To execute all of the Statements at once call Statement.execute().
   *
   * @return The parsed Statement[s].
   *
   * @throws ParseResultsException if any of the statements do not parse according to the Gosu grammar.
   */
  public IStatement parseStatements( IScriptPartId partId ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime, boolean bDoNotThrowParseResultsException ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime ) throws ParseResultsException;  

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime, boolean bDoNotThrowParseResultsException ) throws ParseResultsException;

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime, boolean bDoNotThrowParseResultsException, IType superType ) throws ParseResultsException;
  /**
   * For use by code editors etc.
   */
  public IGosuClass parseClass( String strQualifiedClassName, ISourceFileHandle sourceFile, boolean bThrowOnWarnings, boolean bFullyCompile ) throws ParseResultsException;

  /**
   * Parses a Gosu expression. To evaluate the Expression simply call Expression.evaluate().
   *
   * @return The parsed Expression.
   *
   * @throws ParseResultsException if the expression does not parse according to the Gosu grammar.
   */
  public IExpression parseExp( IScriptPartId partId ) throws ParseResultsException;


  /**
   * Parses a Gosu expression. To evaluate the Expression simply call Expression.evaluate().
   *
   * @return The parsed Expression.
   *
   * @throws ParseResultsException if the expression does not parse according to the Gosu grammar.
   */
  public IExpression parseExp( IScriptPartId partId, IType expectedExpressionType ) throws ParseResultsException;

  /**
   * Parses a Gosu expression. To evaluate the Expression simply call Expression.evaluate().
   *
   * @return The parsed Expression.
   *
   * @throws ParseResultsException if the expression does not parse according to the Gosu grammar.
   */
  public IExpression parseExp( IScriptPartId partId, IType expectedExpressionType, IFileContext context, boolean assignRuntime ) throws ParseResultsException;

  /**
   * Parses a Gosu expression.  If that fails, attempts to parse a Gosu program (which is also an expression, but
   * which has a different grammar.
   *
   * @param partId Script part id
   * @return either a pure expression or Program, depending on the source
   *
   * @throws ParseResultsException if neither an expression nor a program parses according to the Gosu grammar.  We
   *                               try to make a best guess as to which IParseResultsException to throw, so that the
   *                               errors are as close as possible to the true cause of the IParseResultsException
   */
  public IExpression parseExpOrProgram( IScriptPartId partId ) throws ParseResultsException;

  /**
   * Parses a Gosu expression.  If that fails, attempts to parse a Gosu program (which is also an expression, but
   * which has a different grammar.
   *
   * @param partId Script part id
   * @param isolatedScope if false, the program will modify the symbol table at the current scope
   * @return either a pure expression or Program, depending on the source
   *
   * @throws ParseResultsException if neither an expression nor a program parses according to the Gosu grammar.  We
   *                               try to make a best guess as to which IParseResultsException to throw, so that the
   *                               errors are as close as possible to the true cause of the IParseResultsException
   */
  public IExpression parseExpOrProgram( IScriptPartId partId, boolean isolatedScope, boolean assignRuntime ) throws ParseResultsException;

  /**
   * Parses a type literal expression.  The source must obviously satisfy the type literal syntax.
   */
  ITypeLiteralExpression parseTypeLiteral( IScriptPartId partId ) throws ParseResultsException;

  /**
   * Consumes a type literal from the current tokenizer, if one exists.
   *
   * @return true if a type literal was found, false otherwise
   */
  boolean parseTypeLiteral();

  /**
   * @return Whether or not the referenced Gosu source has been parsed.
   */
  public boolean isParsed();

  /**
   * @return Did the most recent parse have warnings
   */
  public boolean hasWarnings();

  /**
   * @return All the locations corresponding to parsed elements.
   */
  public List<IParseTree> getLocations();

  /**
   * The TokenizerInstructor to use for this parser. Optional.
   */
  public ITokenizerInstructor getTokenizerInstructor();

  public void setTokenizerInstructor( ITokenizerInstructor instructor );

  public Map<String, ITypeVariableDefinition> getTypeVariables();

  public boolean isThrowParseResultsExceptionForWarnings();

  public void setThrowParseExceptionForWarnings( boolean bThrowParseExceptionForWarnings );

  void setWarnOnCaseIssue( boolean warnOnCaseIssue );

  void setEditorParser( boolean bEditorParser );

  boolean isEditorParser();

  void putDfsDeclsInTable( ISymbolTable table );

  ISourceCodeTokenizer getTokenizer();

  ArrayList<ISymbol> parseParameterDeclarationList( IParsedElement pe, boolean bStatic, List<IType> inferredArgumentTypes );

  void putDfsDeclInSetByName( IDynamicFunctionSymbol specialFunction );

  ITypeLiteralExpression resolveTypeLiteral( String strName );

  Map<String, List<IFunctionSymbol>> getDfsDecls();

  public IParserState getState();

  boolean isCaptureSymbolsForEval();

  void setCaptureSymbolsForEval( boolean bCaputreSymbolsForEval );

  void setDfsDeclInSetByName( Map<String, List<IFunctionSymbol>> dfsDecl );

  boolean isParsingFunction();

  boolean isParsingBlock();

  IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, IType expectedExpressionType ) throws ParseResultsException;

  void setGenerateRootExpressionAccessForProgram( boolean bGenRootExprAccess );
  
  void setSnapshotSymbols();
  
  IUsesStatementList parseUsesStatementList( boolean resolveTypes );

  IExpression popExpression();

  void setTokenizer( ISourceCodeTokenizer tokenizer );

  void setIgnoreWarnings(Set<ResourceKey> ignoreWarnings);

  enum ParseType
  {
    EXPRESSION,
    PROGRAM,
    CLASS_FRAGMENT,
    EXPRESSION_OR_PROGRAM,
  }

  class Settings
  {
    // Thread local to allow a thread (e.g. bulk fix thread) the ability to turn on case warnings for all
    // Gosu parsers in its thread
    public static ThreadLocal<Boolean> WARN_ON_CASE_DEFAULT = new ThreadLocal<Boolean>();

    // Thread local to allow a thread (e.g. bulk fix thread) the ability to retain parse tree info for warnings
    public static ThreadLocal<Boolean> IDE_EDITOR_PARSER_DEFAULT = new ThreadLocal<Boolean>();

    public static void setWarnOnCaseIssue()
    {
      WARN_ON_CASE_DEFAULT.set( Boolean.TRUE );
    }

    public static String highlightCurrentToken( int start, int end, String source )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( source.substring( 0, start ) );
      sb.append( ">>" );
      sb.append( source.substring( start, end ) );
      sb.append( "<<" );
      sb.append( source.substring( end, source.length() ) );
      return sb.toString();
    }
  }

}
