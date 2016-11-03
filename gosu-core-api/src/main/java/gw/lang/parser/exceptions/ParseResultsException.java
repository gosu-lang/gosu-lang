/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.ICompilableType;

import java.util.ArrayList;
import java.util.List;

public class ParseResultsException extends Exception
{
  private IParsedElement _parsedElement;
  private List<IParseIssue> _parseIssues;
  private List<IParseIssue> _parseExceptions;
  private List<IParseIssue> _parseWarnings;
  private ICompilableType _ctxType;

  public ParseResultsException( IParsedElement parsedElement )
  {
    _parsedElement = parsedElement;
  }

  public ParseResultsException( IParsedElement parsedElement, String message )
  {
    super( message );
    _parsedElement = parsedElement;
  }

  public IParsedElement getParsedElement()
  {
    return _parsedElement;
  }

  public List<IParseIssue> getParseIssues()
  {
    if( _parseIssues == null )
    {
      _parseIssues = _parsedElement.getParseIssues();
    }
    return _parseIssues;
  }

  public boolean hasParseExceptions()
  {
    return getParseExceptions().size() > 0;
  }

  public List<IParseIssue> getParseExceptions()
  {
    if( _parseExceptions == null )
    {
      _parseExceptions = _parsedElement.getParseExceptions();
    }
    return _parseExceptions;
  }

  public boolean hasOnlyParseWarnings()
  {
    return getParseExceptions().size() == 0;
  }

  public boolean hasParseWarnings()
  {
    return getParseWarnings().size() > 0;
  }

  public List<IParseIssue> getParseWarnings()
  {
    if( _parseWarnings == null )
    {
      _parseWarnings = _parsedElement.getParseWarnings();
    }
    return _parseWarnings;
  }

  public String getFeedback()
  {
    return getFeedback(_ctxType, getParseExceptions(), getParseWarnings());
  }

  public static String getFeedback(List<IParseIssue> parseExceptions, List<IParseIssue> parseWarnings) {
    return getFeedback(null, parseExceptions, parseWarnings);
  }

  public static String getFeedback(ICompilableType type, List<IParseIssue> parseExceptions, List<IParseIssue> parseWarnings)
  {
    StringBuilder feedback = new StringBuilder();

    if (type != null) {
      if (type.getSourceFileHandle() != null &&
              type.getSourceFileHandle().getFileName() != null) {
        feedback.append( type.getSourceFileHandle().getFileName() ).append( "\n\n" );
      }
      else
      {
        feedback.append( type.getName() ).append( "\n\n" );
      }
    }
    if( ! parseExceptions.isEmpty() )
    {
      feedback.append( "Errors: \n\n" );

      for( IParseIssue pe : parseExceptions )
      {
        feedback.append( pe.getConsoleMessage() );
        IType typeExpected = pe.getExpectedType();
        if( typeExpected != null )
        {
          String strTypesExpected = getExpectedTypeName( typeExpected );
          if (strTypesExpected.length() > 0)
          {
            feedback.append( " \nExpected Type: " );
            feedback.append( strTypesExpected );
          }
        }
        feedback.append( "\nLine Number: " );
        feedback.append( pe.getLine() );
        feedback.append( "  Column: " );
        feedback.append( pe.getColumn() );
        feedback.append("\n\n");
      }
    }

    if( ! parseWarnings.isEmpty() )
    {
      if( feedback.length() > 0 )
      {
        feedback.append( "\n\n" );
      }
      feedback.append( "Warnings: \n\n" );
      for( IParseIssue pw : parseWarnings )
      {
        feedback.append( pw.getConsoleMessage() );
        feedback.append( "\nLine Number: " );
        feedback.append( pw.getLine() );
        feedback.append( "  Column: " );
        feedback.append( pw.getColumn() );
        feedback.append( "\n\n" );
      }
    }

    return feedback.toString();
  }

  public ITypeLoader getLoader()
  {
    if( _parsedElement instanceof IClassStatement )
    {
      return ((IClassStatement)_parsedElement).getGosuClass().getTypeLoader();
    }
    throw new UnsupportedOperationException();
  }

  public static String getExpectedTypeName( IType type )
  {
    if( type == null )
    {
      return "";
    }

    return getDisplayName( type );
  }

  private static String getDisplayName( IType typeExpected )
  {
    if( typeExpected instanceof IFunctionType )
    {
      return typeExpected.toString();
    }
    else
    {
      return typeExpected.getRelativeName();
    }
  }

  public List<IParseIssue> getIssuesFromPos( int iPos )
  {
    List<IParseIssue> pes = getParseIssues();
    List<IParseIssue> matches = new ArrayList<>();
    for( int i = pes.size() - 1; i >= 0; i-- )
    {
      IParseIssue e = pes.get( i );
      if( e.appliesToPosition( iPos ) )
      {
        matches.add( e );
      }
    }
    return matches;
  }

  public void reset( IParsedElement pe )
  {
    _parsedElement = pe;
    _parseExceptions = null;
    _parseIssues = null;
    _parseWarnings = null;
  }

  public String getMessage()
  {
    return getFeedback();
  }

  public List<IParseIssue> getParseExceptionsForKey( ResourceKey key )
  {
    return findMatchesForKey( key, getParseExceptions() );
  }

  public List<IParseIssue> getParseWarningsForKey( ResourceKey key )
  {
    return findMatchesForKey( key, getParseWarnings() );
  }

  private <T extends IParseIssue> List<T> findMatchesForKey( ResourceKey key, List<T> parseExceptions )
  {
    ArrayList<T> results = new ArrayList<T>();
    for( T parseException : parseExceptions )
    {
      if( parseException.getMessageKey().equals( key ) )
      {
        results.add( parseException );
      }
    }
    return results;
  }

  public void setContextType(ICompilableType contextType) {
    _ctxType = contextType;
  }
}
