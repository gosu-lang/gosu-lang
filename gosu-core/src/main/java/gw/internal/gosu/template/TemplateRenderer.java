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
import gw.lang.parser.template.StringEscaper;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.gs.ITemplateType;
import gw.lang.reflect.java.JavaTypes;

import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

/**
 * Template types forward to this for rendering
 */
public abstract class TemplateRenderer
{
  public static void render( ITemplateType type, Writer writer, Object[] args )
  {
    render( type, writer, null, args );
  }
  public static void render( ITemplateType type, Writer writer, StringEscaper escaper, Object[] args )
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

    CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable( symbolTable );
    try
    {
      templateGenerator.execute( writer, escaper, CompiledGosuClassSymbolTable.instance() );
    }
    catch( TemplateParseException e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable();
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

    CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable( symbolTable );
    try
    {
      type.getTemplateGenerator().execute( writer, null, CompiledGosuClassSymbolTable.instance() );
    }
    catch( TemplateParseException e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable();
    }
    return writer.toString();
  }
}
