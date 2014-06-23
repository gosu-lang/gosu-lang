/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.internal.gosu.parser.CompiledGosuClassSymbolTable;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.parser.template.ITemplateHost;
import gw.lang.reflect.TypeSystem;

import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class SimpleTemplateHost implements ITemplateHost
{
  private static final ThreadLocal g_symTableCtx = new ThreadLocal();

  /**
   */
  public SimpleTemplateHost()
  {
  }

  private static ISymbolTable getSymTableCtx()
  {
    List list = (List)g_symTableCtx.get();
    if( list != null && list.size() > 0 )
    {
      return (ISymbolTable)list.get( 0 );
    }
    return null;
  }

  private static void pushSymTableCtx( ISymbolTable ctx )
  {
    List list = (List)g_symTableCtx.get();
    if( list == null )
    {
      g_symTableCtx.set( list = new LinkedList() );
    }
    list.add( 0, ctx );
  }

  private static void popSymTableCtx()
  {
    List list = (List)g_symTableCtx.get();
    list.remove( 0 );
  }

  //----------------------------------------------------------------------------
  // -- ITemplateHost methods --

  /**
   * @param readerTemplate
   * @param writerOut
   */
  public void executeTemplate( Reader readerTemplate, Writer writerOut ) {
     executeTemplate(readerTemplate, writerOut, false);
  }

  /**
   * @param readerTemplate
   * @param writerOut
   */
  public void executeTemplate( Reader readerTemplate, Writer writerOut, boolean strict )
  {
    try
    {
      TemplateGenerator.generateTemplate( readerTemplate, writerOut, getThreadLocalSymbolTable(), strict );
    }
    catch( Throwable e )
    {
      throw new RuntimeException( e );
    }
  }

  /**
   */
  public void executeTemplate( ITemplateGenerator precompiledTemplate, Writer writerOut )
  {
    pushSymTableCtx( getSymbolTable() );
    try
    {
      precompiledTemplate.execute( writerOut, getThreadLocalSymbolTable());
    }
    catch( RuntimeException e )
    {
      throw e;
    }
    catch( Throwable e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      popSymTableCtx();
    }
  }

  /**
   * @param readerTemplate
   */
  public TemplateGenerator getTemplate( Reader readerTemplate )
  {
    try
    {
      return TemplateGenerator.getTemplate( readerTemplate );
    }
    catch( Throwable e )
    {
      throw new RuntimeException( e );
    }
  }

  public TemplateGenerator getTemplate( Reader readerTemplate, String strFqn )
  {
    try
    {
      return TemplateGenerator.getTemplate( readerTemplate, strFqn );
    }
    catch( Throwable e )
    {
      throw new RuntimeException( e );
    }
  }

  /**
   *
   */
  public synchronized void pushScope()
  {
    getThreadLocalSymbolTable().pushScope();
  }

  /**
   *
   */
  public synchronized void popScope()
  {
    getThreadLocalSymbolTable().popScope();
  }

  /**
   * @param strName
   * @param type
   * @param value
   */
  //## TODO MD: this method needs to take an IType instead of a Class
  public synchronized void putSymbol( String strName, Class type, Object value )
  {
    if( value != null )
    {
      Class typeValue = value.getClass();
      if( !type.isAssignableFrom( typeValue ) && !typeValue.isAssignableFrom( type ) )
      {
        throw new IllegalArgumentException( type.getName() + " is not compatible with " + typeValue.getName() );
      }
    }

    Symbol root = new Symbol( strName,
                              TypeSystem.get( type ),
                              value );
    getThreadLocalSymbolTable().putSymbol( root );
  }

  /**
   * @param symbol
   */
  public synchronized void putSymbol( ISymbol symbol )
  {
    getThreadLocalSymbolTable().putSymbol( symbol );
  }

  /**
   * @param strName
   */
  public synchronized void removeSymbol( String strName )
  {
    getThreadLocalSymbolTable().removeSymbol( strName );
  }

  public ISymbolTable getSymbolTable()
  {
    return getThreadLocalSymbolTable();
  }

  private static ISymbolTable getThreadLocalSymbolTable() {
    return CompiledGosuClassSymbolTable.instance();
  }
  
}
