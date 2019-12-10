/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import java.util.HashMap;
import java.util.Map;

import gw.lang.GosuShop;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.ThreadSafeSymbolTable;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.util.Stack;
import gw.util.concurrent.LockingLazyVar;

/**
 *
 */
public class CompiledGosuClassSymbolTable extends ThreadSafeSymbolTable
{
  private static final LockingLazyVar<CompiledGosuClassSymbolTable> INSTANCE = new LockingLazyVar<CompiledGosuClassSymbolTable>() {
    protected CompiledGosuClassSymbolTable init() {
      return new CompiledGosuClassSymbolTable();
    }
  };
  private static final ThreadLocal<Stack> SYM_TABLE_STACK = new ThreadLocal<>();
  private static final ThreadLocal<Stack> SYM_TABLE_TRACE_STACK = new ThreadLocal<>();
  private static final ThreadLocal<Map<ICompilableType, ISymbolTable>> MAP_SYM_TABLE_BY_TYPE = new ThreadLocal<>();
  private static final boolean DEBUG = false; //enables collection of sym stack push traces

  static {
    TypeSystem.addShutdownListener( () -> {
      INSTANCE.clear();
      GosuShop.clearThreadLocal(SYM_TABLE_STACK);
      GosuShop.clearThreadLocal(SYM_TABLE_TRACE_STACK);
      GosuShop.clearThreadLocal(MAP_SYM_TABLE_BY_TYPE);
    } );
  }

  public static CompiledGosuClassSymbolTable instance()
  {
    return INSTANCE.get();
  }

  CompiledGosuClassSymbolTable()
  {
    super( true );
  }

  public static ISymbolTable getSymTableCtx()
  {
    Stack stack = getSymbolTableStack();
    if( stack.size() > 0 )
    {
      return (ISymbolTable)stack.peek();
    }
    return null;
  }

  public static void pushSymTableCtx( ISymbolTable ctx )
  {
    Stack stack = getSymbolTableStack();
    stack.push( ctx );
    if( DEBUG )
    {
      Stack traceStack = getSymbolTableTraceStack();
      traceStack.push( new RuntimeException( "MARKER" ) );
    }
  }

  public static ISymbolTable popSymTableCtx()
  {
    Stack stack = getSymbolTableStack();
    ISymbolTable symTable = (ISymbolTable)stack.pop();
    assert stack.size() > 0 : "Must always be at least one thread local symbol table";
    if( DEBUG )
    {
      getSymbolTableTraceStack().pop();
    }
    return symTable;
  }

  public ISymbolTable getSymbolTableForCompilingClass( ICompilableType gsClass )
  {
    return getClassMap().get( gsClass );
  }

  public void pushCompileTimeSymbolTable( ICompilableType gsClass )
  {
    pushCompileTimeSymbolTable( gsClass, instance() );
  }
  public void pushCompileTimeSymbolTable( ICompilableType gsClass, ISymbolTable symTable )
  {
    pushCompileTimeSymbolTable( symTable );
    if( getClassMap().containsKey( gsClass ) )
    {
      throw new IllegalStateException( "Already compiling class " + gsClass.getName() );
    }
    getClassMap().put( gsClass, (ISymbolTable)getSymbolTableStack().peek() );
  }

  public void pushCompileTimeSymbolTable()
  {
    pushCompileTimeSymbolTable( new StandardSymbolTable( true ) );
  }

  public void pushCompileTimeSymbolTable( ISymbolTable symTable )
  {
    // push on a clean symbol table for parsing
    pushSymTableCtx( symTable == this ? new StandardSymbolTable( true ) : symTable );
  }

  public void popCompileTimeSymbolTable()
  {
    ISymbolTable symTable = popSymTableCtx();
    Map<ICompilableType, ISymbolTable> map = getClassMap();
    for( ICompilableType gsClass : map.keySet() )
    {
      if( map.get( gsClass ) == symTable )
      {
        map.remove( gsClass );
        break;
      }
    }
  }

  protected ISymbolTable getThreadLocalSymbolTable()
  {
    ISymbolTable symTableCtx = getSymTableCtx();
    if( symTableCtx == this )
    {
      return null;
    }
    else
    {
      return symTableCtx;
    }
  }

  private static Stack getSymbolTableStack()
  {
    Stack stack = SYM_TABLE_STACK.get();
    if( stack == null )
    {
      stack = new Stack();
      stack.push( new StandardSymbolTable( true ) );
      SYM_TABLE_STACK.set( stack );
    }
    return stack;
  }

  private static Map<ICompilableType, ISymbolTable> getClassMap()
  {
    Map<ICompilableType, ISymbolTable> map = MAP_SYM_TABLE_BY_TYPE.get();
    if( map == null )
    {
      map = new HashMap<>( 8 );
      MAP_SYM_TABLE_BY_TYPE.set( map );
    }
    return map;
  }

  public static Stack getSymbolTableTraceStack()
  {
    Stack stack = SYM_TABLE_TRACE_STACK.get();
    if( stack == null )
    {
      stack = new Stack();
      SYM_TABLE_TRACE_STACK.set( stack );
    }
    return stack;
  }

}
