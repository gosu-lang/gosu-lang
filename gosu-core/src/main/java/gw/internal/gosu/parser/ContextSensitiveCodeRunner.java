/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.compiler.SingleServingGosuClassLoader;
import gw.internal.gosu.ir.transform.expression.EvalExpressionTransformer;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseResult;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IProgramClassFunctionSymbol;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ParseResult;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IParameterDeclaration;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IProgramInstance;
import gw.lang.reflect.java.JavaTypes;
import gw.util.ContextSymbolTableUtil;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class ContextSensitiveCodeRunner
{
  private static ConcurrentHashMap<String, IGosuProgramInternal> _cacheProgramByFingerprint = new ConcurrentHashMap<>();
  private static int _refreshChecksum;

  //!! Needed to ensure this class is loaded so a debugger can call into it remotely
  static void ensureLoadedForDebuggerEval() {
    System.out.println( "~~~~~LOADED");
  }

  //!! Do not remove! This is called from the debugger via jdwp.
  /**
   * Intended for use with a debugger to evaluate arbitrary expressions/programs
   * in the context of a source position being debugged, usually at a breakpoint.
   *
   * @param enclosingInstance The instance of the object immediately enclosing the source position.
   * @param extSyms An array of adjacent name/value pairs corresponding with the names and values of local symbols in scope.
   * @param strText The text of the expression/program.
   * @param strClassContext The name of the top-level class enclosing the the source position.
   * @param strContextElementClass The name of the class immediately enclosing the source position (can be same as strClassContext).
   * @param iSourcePosition  The index of the source position within the containing file.
   * @return The result of the expression or, in the case of a program, the return value of the program.
   */
  public static Object runMeSomeCode( Object enclosingInstance, ClassLoader cl, Object[] extSyms, String strText, final String strClassContext, String strContextElementClass, int iSourcePosition )
  {
     // Must execute in caller's classloader
    try
    {
      Class<?> cls;
      try
      {
        cls = Class.forName( ContextSensitiveCodeRunner.class.getName(), false, cl );
      }
      catch( Exception e )
      {
        cls = ContextSensitiveCodeRunner.class;
      }
      Method m = cls.getDeclaredMethod( "_runMeSomeCode", Object.class, Object[].class, String.class, String.class, String.class, int.class );
      m.setAccessible( true );
      return m.invoke( null, enclosingInstance, extSyms, strText, strClassContext, strContextElementClass, iSourcePosition );
    }
    catch( Exception e ) {
      e.printStackTrace();
      Throwable cause = GosuExceptionUtil.findExceptionCause( e );
      if( cause instanceof ParseResultsException ) {
        List<IParseIssue> parseExceptions = ((ParseResultsException)cause).getParseExceptions();
        if( parseExceptions != null && parseExceptions.size() >= 0 ) {
          throw GosuExceptionUtil.forceThrow( (Throwable)parseExceptions.get( 0 ) );
        }
      }
      throw GosuExceptionUtil.forceThrow( cause );
    }
  }
  private static Object _runMeSomeCode( Object enclosingInstance, Object[] extSyms, String strText, String strClassContext, String strContextElementClass, int iSourcePosition )
  {
    if( enclosingInstance != null )
    {
      String fqn = enclosingInstance.getClass().getTypeName();
      if( isBlock( fqn ) )
      {
        // if the enclosing instance is a block, use it as the context
        strClassContext = fqn;
        strContextElementClass = fqn;
      }
    }
    IType type = TypeSystem.getByFullName( strClassContext, TypeSystem.getGlobalModule() );
    if( type instanceof IGosuClassInternal )
    {
      IGosuClassInternal gsClass = (IGosuClassInternal)type;
      gsClass.isValid();
      IGosuClassInternal gsImmediateClass = (IGosuClassInternal)TypeSystem.getByFullName( strContextElementClass );
      return compileAndRunMeSomeCode( strText, gsClass, enclosingInstance, gsImmediateClass, extSyms, iSourcePosition );
    }
    else
    {
      IType gsImmediateClass = TypeSystem.getByFullName( strContextElementClass );
      return compileAndRunMeSomeCode( strText, null, enclosingInstance, gsImmediateClass, extSyms, 0 );
    }
  }

  public static Object compileAndRunMeSomeCode( Object source, IGosuClass ctxClass, Object outer, IType enclosingClass, Object[] extSyms, int offset )
  {
    String typeName = GosuProgramParser.makeEvalKey( source.toString(), enclosingClass, offset );
    IGosuProgramInternal program = getCachedProgram( typeName );
    IParseResult res;
    // use parent class if nested class has no recorded location eg., closure
    while( outer != null && isBlock( outer.getClass().getTypeName() ) )
    {
      enclosingClass = enclosingClass.getEnclosingType();
      try
      {
        Field f = outer.getClass().getDeclaredField( "this$0" );
        f.setAccessible( true );
        outer = f.get( outer );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }

    if( program != null )
    {
      program.isValid();
      res = new ParseResult( program );
    }
    else
    {
      ISymbolTable compileTimeLocalContextSymbols = ctxClass == null ? new StandardSymbolTable( true ) : findCompileTimeSymbols( (IGosuClassInternal)ctxClass, offset ); // ContextSymbolTableUtil.getSymbolTableAtOffset( ctxClass, offset );
      String strSource = CommonServices.getCoercionManager().makeStringFrom( source );
      IGosuProgramParser parser = GosuParserFactory.createProgramParser();
      //debugInfo( compileTimeLocalContextSymbols );

      TypeSystem.pushIncludeAll();
      try
      {
        IParseTree ctxElem = null;
        if( enclosingClass instanceof IGosuClassInternal )
        {
          //## todo: for Java types we can fake a ctxElem by getting the Gosu proxy class and then get the corresponding function stmt
          ctxElem = ((IGosuClassInternal)enclosingClass).getClassStatement().getLocation().getDeepestLocation( offset, false );
        }
        res = parser.parseRuntimeExpr( typeName, strSource, enclosingClass, compileTimeLocalContextSymbols, ctxElem );
      }
      finally
      {
        TypeSystem.popIncludeAll();
      }
      cacheProgram( typeName, (IGosuProgramInternal)res.getProgram() );
    }

    IExternalSymbolMap runtimeLocalSymbolValues = makeRuntimeNamesAndValues( extSyms );


    TypeSystem.pushIncludeAll();
    IGosuProgram gp;
    try
    {
      gp = res.getProgram();
      if( !gp.isValid() )
      {
        System.out.println( gp.getParseResultsException() );
        throw GosuExceptionUtil.forceThrow( gp.getParseResultsException() );
      }

    }
    finally
    {
      TypeSystem.popIncludeAll();
    }
    Class<?> javaClass = gp.getBackingClass();
    ClassLoader classLoader = javaClass.getClassLoader();
    assert classLoader instanceof SingleServingGosuClassLoader;
    List<Object> args = new ArrayList<>();
    if( !gp.isStatic() )
    {
      args.add( outer );
    }

    Constructor ctor = EvalExpressionTransformer._ctorAccessor.get().getConstructor( javaClass );
    Class[] parameterTypes = ctor.getParameterTypes();
    if( parameterTypes.length != args.size() )
    {
      if( parameterTypes.length > args.size() &&
          parameterTypes[parameterTypes.length-1].getName().equals( IExternalSymbolMap.class.getName() ) )
      {
        args.add( runtimeLocalSymbolValues );
      }
      else
      {
        throw new IllegalStateException( "Runtime expr constructor param count is not " + args.size() + "\nPassed in args " + printArgs( args ) + "\nActual args: " + printArgs( ctor.getParameterTypes() ) );
      }
    }
    try
    {
      IProgramInstance evalInstance = (IProgramInstance)ctor.newInstance( args.toArray() );
      return evalInstance.evaluate( runtimeLocalSymbolValues );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }


  public static void cacheProgram( String strTypeName, IGosuProgramInternal program )
  {
    clearCacheOnChecksumChange();
    _cacheProgramByFingerprint.put( strTypeName, program );
  }
  public static IGosuProgramInternal getCachedProgram( String strTypeName )
  {
    clearCacheOnChecksumChange();
    return _cacheProgramByFingerprint.get( strTypeName );
  }
  private static void clearCacheOnChecksumChange()
  {
    if( _refreshChecksum != TypeSystem.getRefreshChecksum() )
    {
      _cacheProgramByFingerprint.clear();
      _refreshChecksum = TypeSystem.getRefreshChecksum();
    }
  }

  private static String printArgs( Class[] parameterTypes ) {
    String str = "";
    for( Class c: parameterTypes ) {
      str += c.getName() + ", ";
    }
    return str;
  }
  private static String printArgs( List<Object> args ) {
    String str = "";
    for( Object a: args ) {
      str += a + ", ";
    }
    return str;
  }

  private static boolean isBlock( String fqn )
  {
    return fqn.contains( "$block_" );
  }

  private static IExternalSymbolMap makeRuntimeNamesAndValues( Object[] extSyms ) {
    HashMap<String, ISymbol> map = new HashMap<>();
    for( int i = 0; i < extSyms.length; i++ ) {
      String name = (String)extSyms[i];
      Object value = extSyms[++i];
      map.put( name, new Symbol( name, JavaTypes.OBJECT(), value ) );
    }

    return new ExternalSymbolMapForMap( map );
  }

  private static IParsedElement findElemAt( IGosuClassInternal gsClass, int iContextLocation ) {
    IParseTree elem = ((IGosuClass)TypeLord.getOuterMostEnclosingClass( gsClass )).getClassStatement().getClassFileStatement().getLocation().getDeepestLocation( iContextLocation, false );
    return elem == null ? gsClass.getClassStatement().getClassFileStatement() : elem.getParsedElement();
  }


  private static ISymbolTable findCompileTimeSymbols( IGosuClassInternal enclosingClass, int iLocation )
  {
    ISymbolTable symTable = new StandardSymbolTable( false );
    while( enclosingClass.getClassStatement().getLocation() == null ) {
      enclosingClass = (IGosuClassInternal)enclosingClass.getEnclosingType();
    }
    IParseTree deepestLocation = enclosingClass.getClassStatement().getLocation().getDeepestLocation( iLocation, false );
    collectLocalSymbols( enclosingClass, symTable,
                         deepestLocation.getParsedElement(),
                         iLocation );
    return symTable;
  }

  public static void collectLocalSymbols( IType enclosingType, ISymbolTable symTable, IParsedElement parsedElement, int iOffset )
  {
    if( parsedElement == null )
    {
      return;
    }

    if( parsedElement instanceof IFunctionStatement )
    {
      IFunctionStatement declStmt = (IFunctionStatement)parsedElement;
      if( !declStmt.getDynamicFunctionSymbol().isStatic() )
      {
        addThisSymbolForEnhancement( enclosingType, symTable );
      }
      for( IParameterDeclaration localVar : declStmt.getParameters() )
      {
        if( localVar != null && localVar.getLocation().getOffset() < iOffset )
        {
          ISymbol symbol = localVar.getSymbol();
          symTable.putSymbol( symbol );
        }
      }
    }
    else if( parsedElement instanceof IParsedElementWithAtLeastOneDeclaration )
    {
      IParsedElementWithAtLeastOneDeclaration declStmt = (IParsedElementWithAtLeastOneDeclaration)parsedElement;
      for( String strVar : declStmt.getDeclarations() )
      {
        ILocalVarDeclaration localVar = findLocalVarSymbol( strVar, declStmt );
        if( localVar != null && localVar.getLocation().getOffset() < iOffset )
        {
          ISymbol symbol = localVar.getSymbol();
          symTable.putSymbol( symbol );
        }
      }
    }
    else if( parsedElement instanceof IStatementList )
    {
      IStatementList stmtList = (IStatementList)parsedElement;
      for( IStatement stmt : stmtList.getStatements() )
      {
        if( stmt instanceof IVarStatement && !((IVarStatement)stmt).isFieldDeclaration() && stmt.getLocation().getOffset() < iOffset )
        {
          ISymbol symbol = ((IVarStatement)stmt).getSymbol();
          if( isProgramFieldVar( stmt ) )
          {
            continue;
          }
          symTable.putSymbol( symbol );
        }
      }
    }
    else if( parsedElement instanceof IBlockExpression ) {
      IBlockExpression blockExpr = (IBlockExpression)parsedElement;
      for( ISymbol arg: blockExpr.getArgs() ) {
        symTable.putSymbol( arg );
      }
    }

    IParsedElement parent = parsedElement.getParent();
    if( parent != parsedElement )
    {
      collectLocalSymbols( enclosingType, symTable, parent, iOffset );
    }
  }

  private static void addThisSymbolForEnhancement( IType enclosingType, ISymbolTable symTable )
  {
    if( enclosingType instanceof IGosuEnhancementInternal )
    {
      IType thisType = ((IGosuEnhancementInternal)enclosingType).getEnhancedType();
      if( thisType != null )
      {
        thisType = TypeLord.getConcreteType( thisType );
        symTable.putSymbol( new ThisSymbol( thisType, symTable ) );
      }
    }
  }

  private static boolean isProgramFieldVar( IStatement stmt )
  {
    if( stmt.getParent() != null )
    {
      IParsedElement parent = stmt.getParent().getParent();
      if( parent instanceof IFunctionStatement )
      {
        IDynamicFunctionSymbol dfs = ((IFunctionStatement)parent).getDynamicFunctionSymbol();
        if( dfs instanceof IProgramClassFunctionSymbol )
        {
          return true;
        }
      }
    }
    return false;
  }

  private static ILocalVarDeclaration findLocalVarSymbol( String strVar, IParsedElement pe )
  {
    if( pe instanceof ILocalVarDeclaration )
    {
      ISymbol symbol = ((ILocalVarDeclaration)pe).getSymbol();
      if( symbol != null && symbol.getName().equals( strVar ) )
      {
        return (ILocalVarDeclaration)pe;
      }
      return null;
    }
    if( pe == null )
    {
      return null;
    }
    for( IParseTree child : pe.getLocation().getChildren() )
    {
      ILocalVarDeclaration localVar = findLocalVarSymbol( strVar, child.getParsedElement() );
      if( localVar != null )
      {
        return localVar;
      }
    }
    return null;
  }
}
