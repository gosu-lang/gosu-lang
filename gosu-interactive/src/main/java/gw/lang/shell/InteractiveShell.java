/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.shell;

import gw.config.CommonServices;
import gw.lang.GosuShop;
import gw.lang.parser.ExternalSymbolMapSymbolTableWrapper;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IParseResult;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.INotAWordExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IProgramInstance;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.GosuStringUtil;
import jline.CandidateListCompletionHandler;
import jline.Completor;
import jline.ConsoleReader;
import jline.Terminal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class InteractiveShell implements Runnable
{
  private static final String GOSU_PROMPT = "gs> ";
  private static final String MORE_PROMPT = "... ";
  private static final String OUTPUT_PREFIX = "  = ";
  private ISymbolTable _interactiveSymbolTable;
  private ConsoleReader _cr;
  private CompletionHandler _completionHandler;
  private ITypeUsesMap _interactiveTypeUsesMap;
  private boolean _logOutput;

  public InteractiveShell()
  {
    this( false );
  }

  public InteractiveShell( boolean logOutput )
  {
    _interactiveTypeUsesMap = CommonServices.getGosuIndustrialPark().createTypeUsesMap( Collections.<String>emptyList() );
    _interactiveSymbolTable = new StandardSymbolTable( true );
    _completionHandler = new CompletionHandler( _interactiveSymbolTable, _interactiveTypeUsesMap );
    _logOutput = logOutput;
  }

  public void run() {

    try
    {
      Runtime.getRuntime().addShutdownHook(new Thread()
      {
        public void run()
        {
          System.out.println();
          System.out.flush();
        }
      });
      Terminal.setupTerminal();
      _cr = new ConsoleReader();
      _cr.setDefaultPrompt( GOSU_PROMPT );

      _cr.setCompletionHandler( new CandidateListCompletionHandler() );
      _cr.addCompletor( _completionHandler );
      _cr.printString( "Type \"help\" to see available commands" );
      _cr.printNewline();

      //noinspection InfiniteLoopStatement
      while( true )
      {
        String expr = readExpr( _cr );
        if (expr == null) {
          return;
        }
        Boolean result = tryAsCommand(expr);
        if ( result == null )
        {
          System.exit( 0 );
        }
        if( result )
        {
          continue;
        }

        try
        {
          if( !GosuStringUtil.isEmpty( expr ) )
          {
            interactivelyEvaluate( expr );
          }
        }
        catch( ParseResultsException e )
        {
          _cr.printString( e.getMessage() );
        }
        catch( Throwable e )
        {
          _cr.printString( e.getMessage() == null ? "" : e.getMessage() );
          _cr.printNewline();
          StringWriter str = new StringWriter();
          e.printStackTrace( new PrintWriter( str ) );
          _cr.printString( str.toString() );
        }
      }
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  protected Object interactivelyEvaluate( String expr ) throws ParseResultsException, IOException
  {
    IGosuProgram gosuProgram = parseProgram( expr );
    IProgramInstance instance = gosuProgram.getProgramInstance();
    Object val = instance.evaluate( new ExternalSymbolMapSymbolTableWrapper( _interactiveSymbolTable, true ) );
    processProgram( gosuProgram, instance );
    IExpression expression = gosuProgram.getExpression();
    boolean noReturnValue = expression == null ||
            JavaTypes.pVOID().equals(expression.getType()) ||
            expression instanceof INotAWordExpression;
    if (!noReturnValue && _logOutput ) {
      _cr.printString( OUTPUT_PREFIX + GosuShop.toString( val ) );
      _cr.printNewline();
    }
    return val;
  }

  private static class CompletionHandler implements Completor
  {
    private ISymbolTable _symbolTable;
    private ITypeUsesMap _typeUsesMap;

    public CompletionHandler( ISymbolTable symbolTable, ITypeUsesMap typeUsesMap )
    {
      _symbolTable = symbolTable;
      _typeUsesMap = typeUsesMap;
    }

    public int complete( String buffer, int cursor, List candidates )
    {
      IGosuParser parser = GosuParserFactory.createParser( buffer, _symbolTable );
      parser.setTypeUsesMap( _typeUsesMap );
      IParsedElement pe;
      try
      {
        pe = parser.parseExpOrProgram( null );
      }
      catch( ParseResultsException e )
      {
        pe = e.getParsedElement();
      }

      IParseTree parseTree = pe.getLocation().getDeepestLocation( cursor, true );
      if( parseTree == null )
      {
        return 0;
      }

      IParsedElement element = parseTree.getParsedElement();
      if( element instanceof IIdentifierExpression)
      {
        return handleIdentifier( candidates, element );
      }
      if( element instanceof IMemberAccessExpression)
      {
        IMemberAccessExpression ma = (IMemberAccessExpression)element;
        String memberName = ma.getMemberName();
        IType type = ma.getRootType();
        if( type instanceof INamespaceType )
        {
          //need to implement
        }
        else
        {
          ITypeInfo typeInfo = type.getTypeInfo();

          ArrayList<String> featureNames = new ArrayList<String>();

          List<? extends IPropertyInfo> propertyInfos = typeInfo.getProperties();
          for( IPropertyInfo propertyInfo : propertyInfos )
          {
            if( propertyInfo.getName().startsWith( memberName ) )
            {
              featureNames.add( propertyInfo.getName() );
            }
          }

          List<? extends IMethodInfo> methodInfos = typeInfo.getMethods();
          for( IMethodInfo mi : methodInfos )
          {
            if( mi.getName().startsWith( memberName ) )
            {
              featureNames.add( mi.getName() );
            }
          }

          Collections.sort( featureNames );
          //noinspection unchecked
          candidates.addAll( featureNames );
          int ext = ma.getRootExpression().getLocation().getExtent();
          return buffer.indexOf( '.', ext ) + 1;
        }
      }

      return 0;
    }

    private int handleIdentifier( List candidates, IParsedElement element )
    {
      IIdentifierExpression identifier = (IIdentifierExpression)element;
      String prefix = identifier.getSymbol().getName();
      ArrayList<String> symNames = new ArrayList<String>();
      for( Object symbolName : _symbolTable.getSymbols().keySet() )
      {
        symNames.add( symbolName.toString() );
      }
      Collections.sort( symNames );
      for( String name : symNames )
      {
        if( name.startsWith( prefix ) )
        {
          //noinspection unchecked
          candidates.add( name );
        }
      }
      return identifier.getLocation().getColumn() - 1;
    }
  }

  private IGosuProgram parseProgram( String expr ) throws ParseResultsException
  {
    IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
    ParserOptions parserOptions = new ParserOptions();
    parserOptions.withTypeUsesMap( _interactiveTypeUsesMap );
    IParseResult result = programParser.parseExpressionOrProgram( expr, _interactiveSymbolTable, parserOptions );
    return result.getProgram();
  }

  private Boolean tryAsCommand( String expr ) throws IOException
  {
    expr = expr.trim();
    if( "help".equals( expr ) || "\"help\"".equals( expr ) )
    {
      printCommandHelp();
      return true;
    }
    if( "ls".equals( expr ) )
    {
      StandardSymbolTable defaultSymbols = new StandardSymbolTable(true);
      @SuppressWarnings({"unchecked"})
      ArrayList<ISymbol> symbols = new ArrayList<ISymbol>( _interactiveSymbolTable.getSymbols().values() );
      Collections.sort( symbols, new Comparator<ISymbol>()
      {
        public int compare( ISymbol o1, ISymbol o2 )
        {
          return o1.getName().compareTo( o2.getName() );
        }
      } );

      _cr.printString( "Symbols : \n\n" );
      for( ISymbol symbol : symbols )
      {
        if ( defaultSymbols.getSymbol( symbol.getName() ) != null )
        {
          _cr.printString( "    " + symbol.getName() + " : (builtin)\n" );
        }
        else
        {
          _cr.printString( "    " + symbol.getName() + " : " + symbol.getType() + " = " + GosuShop.toString( symbol.getValue() ) + "\n" );
        }
      }
      _cr.printNewline();
      return true;
    }
    if( "exit".equals( expr ) || "quit".equals( expr ) )
    {
      return null;
    }
    if( "clear".equals( expr ) )
    {
      _interactiveSymbolTable = new StandardSymbolTable( true );
      return true;
    }
    if( expr.startsWith( "rm " ) )
    {
      String sym = expr.substring( "rm ".length() );
      if ( _interactiveSymbolTable.getSymbol( sym ) == null )
      {
        _cr.printString( "Symbol '" + sym + "' does not exist\n" );
      }
      else {
        try
        {
          _interactiveSymbolTable.removeSymbol( sym );
        }
        catch ( Exception ex )
        {
          _cr.printString( "Cannot remove built-in symbol '" + sym + "'\n" );
        }
      }
      return true;
    }

    return false;
  }

  private void processProgram( IGosuProgram program, IProgramInstance instance ) throws IOException
  {
    maybeHandleVar( program, instance );
    maybeHandleFunction( program );
    maybeHandleUses( program );
  }

  private void maybeHandleUses( IGosuProgram program )
  {
    if( program.getTypeUsesMap() != null && program.getTypeUsesMap().getUsesStatements() != null )
    {
      for( IUsesStatement usesStmt : program.getTypeUsesMap().getUsesStatements() )
      {
        _interactiveTypeUsesMap.addToTypeUses( usesStmt );
      }
    }
  }

  private void maybeHandleFunction( IGosuProgram program )
  {
    if( program.getMemberFunctions() != null )
    {
      for( IDynamicFunctionSymbol function : program.getMemberFunctions() )
      {
        if( function.getArgTypes().length != 1 ||
            !JavaTypes.IEXTERNAL_SYMBOL_MAP().equals( function.getArgs().get( 0 ) ) ||
            (!function.getDisplayName().equals( "evaluate" ) && !function.getDisplayName().equals( "evaluateRootExpr" )) )
        {
          _interactiveSymbolTable.putSymbol( function );
        }
      }
    }
  }

  private void maybeHandleVar( IGosuProgram program, IProgramInstance instance )
  {
    if( program.getStatement() instanceof IStatementList )
    {
      IStatementList statementList = (IStatementList)program.getStatement();
      if( statementList.getStatements() != null &&
          statementList.getStatements().length == 2 &&
          statementList.getStatements()[0] instanceof IVarStatement )
      {
        IVarStatement var = (IVarStatement)statementList.getStatements()[0];
        _interactiveSymbolTable.putSymbol( CommonServices.getGosuIndustrialPark().createSymbol( var.getIdentifierName(), var.getType(), getValue( instance, var ) ) );
      }
    }
  }

  private Object getValue( IProgramInstance instance, IVarStatement var )
  {
    Object value = null;
    try
    {
      Field field = instance.getClass().getDeclaredField( var.getIdentifierName().toString() );
      field.setAccessible( true );
      value = field.get( instance );
    }
    catch( Exception e )
    {
      //ignore
    }
    return value;
  }

  private String readExpr( ConsoleReader cr )
    throws IOException
  {
    String s = cr.readLine();
    if ( s == null )
    {
      return null;
    }
    while( eatMore( s ) )
    {
      cr.setDefaultPrompt( MORE_PROMPT );
      String additionalInput = cr.readLine();

      if ( additionalInput.trim().length() == 0 )
      {
          break;
        }
      s = s + additionalInput + "\n";
    }
    cr.setDefaultPrompt( GOSU_PROMPT );
    return s;
  }

  private boolean eatMore( String s ) {
    ISourceCodeTokenizer tokenizer = CommonServices.getGosuIndustrialPark().createSourceCodeTokenizer(s);
    int openParens = tokenizer.countMatches("(");
    int closedParens = tokenizer.countMatches(")");
    if (openParens != closedParens) {
      return true;
    }
    int openBraces = tokenizer.countMatches("{");
    int closeBraces = tokenizer.countMatches("}");
    return openBraces != closeBraces;
  }

  private void printCommandHelp() throws IOException
  {
    _cr.printString( "The following commands are available:\n" +
                     "\n" +
                     "  help - show this message\n" +
                     "  exit - exit this interpreter\n" +
                     "  quit - exit this interpreter\n" +
                     "  ls - lists all the local variables\n" +
                     "  clear - clears all local variables and functions\n" +
                     "  rm [var_name] - clears the given variable\n" );
    _cr.printNewline();
  }

  public int complete( String s, List completions )
  {
    return complete( s, s.length() - 1, completions );
  }

  public int complete( String s, int pos, List completions )
  {
    return _completionHandler.complete( s, pos, completions );
  }

}
