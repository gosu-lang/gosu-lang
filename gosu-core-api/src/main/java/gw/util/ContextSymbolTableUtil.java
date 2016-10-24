package gw.util;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseResult;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.StringSourceFileHandle;

import java.util.List;

/**
 */
public class ContextSymbolTableUtil
{
  public static ISymbolTable getSymbolTableAtOffset( IGosuClass gsClass, int offset )
  {
    return getSymbolTableAtOffset( new StandardSymbolTable( true ), gsClass, offset );
  }

  public static ISymbolTable getSymbolTableAtOffset( ISymbolTable symTable, IGosuClass gsClass, int offset )
  {
    StringBuffer sb = new StringBuffer( gsClass.getSource() );
    sb.insert( offset, "; +yennikcm ;" ); // Force a parse exception
    IGosuParser parserJavadoc = GosuParserFactory.createParser( symTable, ScriptabilityModifiers.SCRIPTABLE );
    parserJavadoc.setEditorParser( true );

    parserJavadoc.setScript( sb.toString() );
    try
    {
      TypeSystem.lock();
      try
      {
        if( gsClass instanceof IGosuProgram )
        {
          IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
          ParserOptions options = new ParserOptions().withParser( parserJavadoc );
          IParseResult result = programParser.parseExpressionOrProgram( sb.toString(), parserJavadoc.getSymbolTable(), options );
          IGosuClass parsedGosuClass = result.getProgram();
          ParseResultsException parseResultsException = parsedGosuClass.getParseResultsException();
          if( parseResultsException != null )
          {
            throw parseResultsException;
          }
        }
        else if( gsClass instanceof IGosuEnhancement )
        {
          parserJavadoc.parseClass( gsClass.getName(), new StringSourceFileHandle( gsClass.getName(), sb, false, ClassType.Enhancement ), true, true );
        }
        else // class
        {
          parserJavadoc.parseClass( gsClass.getName(), new StringSourceFileHandle( gsClass.getName(), sb, false, ClassType.Class ), true, true );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    catch( ParseResultsException pe )
    {
      List<IParseIssue> errors = pe.getIssuesFromPos( offset + 2 );
      if( errors.isEmpty() )
      {
        errors = pe.getIssuesFromPos( offset );
      }
      if( !errors.isEmpty() )
      {
        for( IParseIssue error : errors )
        {
          if( error.getSymbolTable() != null )
          {
            return error.getSymbolTable();
          }
        }
      }
    }
    return symTable;
  }
}
