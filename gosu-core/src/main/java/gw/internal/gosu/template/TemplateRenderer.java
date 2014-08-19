/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.internal.gosu.parser.CompiledGosuClassSymbolTable;
import gw.internal.gosu.parser.Symbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.parser.template.ITemplateObserver;
import gw.lang.parser.template.StringEscaper;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Stack;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

/**
 * Template types forward to this for rendering
 */
public abstract class TemplateRenderer
{
  static final ThreadLocal<Stack<ITemplateObserver>> OBSERVERS_STACK = new ThreadLocal<Stack<ITemplateObserver>>();
  private static final ITemplateObserver BASIC_OBSERVER = new ITemplateObserver() {
    @Override
    public boolean beforeTemplateRender(IType type, Writer writer) {
      return true;
    }

    @Override
    public StringEscaper getEscaper() {
      return null;
    }

    @Override
    public void afterTemplateRender(IType type, Writer writer) {
    }
  };

  public static void render( ITemplateType type, Writer writer, Object[] args )
  {
    ISymbolTable symbolTable;
    symbolTable = new StandardSymbolTable();
    ITemplateGenerator templateGenerator = type.getTemplateGenerator();
    List<ISymbol> params = templateGenerator.getParameters();
    if( args != null )
    {
      for( int i = 0; i < args.length; i++ )
      {
        symbolTable.putSymbol( new Symbol( params.get( i ).getName(), params.get( i ).getType(), args[i] ) );
      }
    }
    if( symbolTable.getSymbol( "writer" ) == null )
    {
      symbolTable.putSymbol( new Symbol( "writer", JavaTypes.getJreType( Writer.class ), writer ) );
    }

    ITemplateObserver currentObserver = getCurrentObserver();
    if (currentObserver.beforeTemplateRender(type, writer)) {
      CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable( symbolTable );
      try
      {
        templateGenerator.execute( writer, currentObserver.getEscaper(), CompiledGosuClassSymbolTable.instance() );
      }
      catch( TemplateParseException e )
      {
        throw new RuntimeException( e );
      }
      finally
      {
        CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable();
      }
      currentObserver.afterTemplateRender(type, writer);
    }
  }

  public static String renderToString( ITemplateType type, Object... args )
  {
    ISymbolTable symbolTable;
    symbolTable = new StandardSymbolTable();
    ITemplateGenerator templateGenerator = type.getTemplateGenerator();
    List<ISymbol> params = templateGenerator.getParameters();
    if( args != null  )
    {
      for( int i = 0; i < args.length; i++ )
      {
        symbolTable.putSymbol( new Symbol( params.get( i ).getName(), params.get( i ).getType(), args[i] ) );
      }
    }
    StringWriter writer = new StringWriter();
    if( symbolTable.getSymbol( "writer" ) == null )
    {
      symbolTable.putSymbol( new Symbol( "writer", JavaTypes.getJreType( Writer.class ), writer ) );
    }

    ITemplateObserver currentObserver = getCurrentObserver();
    if (currentObserver.beforeTemplateRender(type, writer)) {
      CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable( symbolTable );
      try
      {
        type.getTemplateGenerator().execute( writer, currentObserver.getEscaper(), CompiledGosuClassSymbolTable.instance() );
      }
      catch( TemplateParseException e )
      {
        throw new RuntimeException( e );
      }
      finally
      {
        CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable();
      }
      currentObserver.afterTemplateRender(type, writer);
    }
    return writer.toString();
  }

  public static ITemplateObserver getCurrentObserver() {
    Stack<ITemplateObserver> iTemplateObserverStack = OBSERVERS_STACK.get();
    if (iTemplateObserverStack == null || iTemplateObserverStack.size() == 0) {
      return BASIC_OBSERVER;
    } else {
      return iTemplateObserverStack.peek();
    }
  }

  public static void pushTemplateObserver(ITemplateObserver observer) {
    Stack<ITemplateObserver> iTemplateObserverStack = OBSERVERS_STACK.get();
    if (iTemplateObserverStack == null) {
      iTemplateObserverStack = new Stack<ITemplateObserver>();
      OBSERVERS_STACK.set(iTemplateObserverStack);
    }
    iTemplateObserverStack.push(observer);
  }

  public static void popTemplateObserver() {
    Stack<ITemplateObserver> iTemplateObserverStack = OBSERVERS_STACK.get();
    if (iTemplateObserverStack == null) {
      iTemplateObserverStack = new Stack<ITemplateObserver>();
      OBSERVERS_STACK.set(iTemplateObserverStack);
    }
    iTemplateObserverStack.pop();
  }
}
