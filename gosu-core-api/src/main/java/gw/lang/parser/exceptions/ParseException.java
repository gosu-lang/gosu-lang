/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.IParserState;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IType;
import gw.lang.reflect.IParameterInfo;


public class ParseException extends ParseIssue
{
  private IType[] _paramTypesExpected;
  private IParameterInfo[][] _paramTypesPossible;
  private IType _typeExpected;
  private IMemberAccessExpression _memberAccessContext;
  private ParseException _alternateException;
  private boolean _bCausedByArgumentList;

  /**
   * @param parserState The tokenizer in use by the parser (helpful for
   */
  public ParseException( IParserState parserState, ResourceKey messageKey, Object... args )
  {
    super( parserState, messageKey, args );
  }

  public ParseException( Integer lineNumber, Integer lineOffset, Integer tokenColumn, Integer tokenStart,
                         Integer tokenEnd,
                         ISymbolTable symbolTable, ResourceKey key, Object... msgArgs )
  {
    super( lineNumber, lineOffset, tokenColumn, tokenStart, tokenEnd, symbolTable, key, msgArgs );
  }

  public ParseException( IParserState parserState, IType typeExpected, ResourceKey msgKey, Object... args )
  {
    super( parserState, msgKey, args );
    _typeExpected = typeExpected;
  }

  protected ParseException( ParseException e )
  {
    super( e.getLineNumber(), e.getLineOffset(), e.getTokenColumn(), e.getTokenStart(), e.getTokenEnd(),
           e.getSymbolTable(), e.getMessageKey(), e.getMessageArgs() );
    setSource( e.getSource() );
    try
    {
      setStateSource( e.getStateSource() );
    }
    catch( Throwable t )
    {
      setStateSource( "Error fetching source: " + t );
    }
    _paramTypesExpected = e._paramTypesExpected;
    _paramTypesPossible = e._paramTypesPossible;
    _typeExpected = e._typeExpected;
    _memberAccessContext = e._memberAccessContext;
    _alternateException = e._alternateException;
    _bCausedByArgumentList = e._bCausedByArgumentList;
  }

  private ParseException( Throwable t, IParserState state )
  {
    super( state, t );
  }

  public static ParseException wrap( Throwable t, IParserState state )
  {
    if( t instanceof ParseException )
    {
      ParseException pe = (ParseException)t;
      return new ParseException( state, pe.getMessageKey(), pe.getMessageArgs() );
    }
    else
    {
      return new ParseException( t, state );
    }
  }

  public static ParseException shallowCopy( ParseException source )
  {
    ParseException copy = new ParseException( source.getLineNumber(), source.getLineOffset(), source.getTokenColumn(), source.getTokenStart(),
                                              source.getTokenEnd(), source.getSymbolTable(), source.getMessageKey(), source.getMessageArgs() );
    copy._typeExpected = source._typeExpected;
    copy._paramTypesExpected = source._paramTypesExpected;
    copy._memberAccessContext = source._memberAccessContext;
    copy._alternateException = source._alternateException;
    copy._bCausedByArgumentList = source._bCausedByArgumentList;
    return copy;
  }

  /**
   * Override so we can reset the message if need be.
   */
  public String getConsoleMessage()
  {
    if( _alternateException != null && _alternateException != this )
    {
      return "\nThe script neither parsed as an expression nor as a program.\n" +
             "Expression parse error:\n" + super.getConsoleMessage() + "\n\n" +
             "Program parse error:\n" + _alternateException.getConsoleMessage() + "\n";
    }

    return super.getConsoleMessage();
  }

  /**
   * @return The parser's exprected types.
   */
  public IType getExpectedType()
  {
    return _typeExpected;
  }

  /**
   * Sets the expected type information from the parser.
   */
  public void setExpectedType( IType typeExpected )
  {
    _typeExpected = typeExpected;
  }

  public IType[] getParamTypesExpected()
  {
    return _paramTypesExpected;
  }

  public void setParamTypesExpected( IType... paramTypesExpected )
  {
    _paramTypesExpected = paramTypesExpected;
  }

  public void setParamTypesPossible( IParameterInfo[][] paramTypesPossible )
  {
    _paramTypesPossible = paramTypesPossible;
  }

  public IParameterInfo[][] getParamTypesPossible()
  {
    return _paramTypesPossible;
  }

  public String toString()
  {
    return getUIMessage();
  }

  /**
   * @return A member access context expression to further qualify the expected
   *         type.  Typically the member access expression is an LHS operand in the
   *         containing. For example, the member access expression facilitates in
   *         TypeKey qualification -- we need the name of a field/property to
   *         get an appropriately filtered TypeList for a specific TypeKey on a
   *         specific field in an entity.
   */
  public IMemberAccessExpression getMemberAccessContext()
  {
    return _memberAccessContext;
  }

  /**
   * @param ma A member access expression context to further qualify
   *           the expected type.
   */
  public void setMemberAccessContext( IMemberAccessExpression ma )
  {
    _memberAccessContext = ma;
  }

  /**
   * An alternate ParseException that may provide additional information. For example,
   * if the type of script is not know in advance (is it an expression or a program?),
   * the script is first compiled as an expression. If a ParseException is thrown, the
   * script is compiled as a program. If a ParseException is thrown again, the former
   * exception is rethrown with the latter as the 'alternate'.
   */
  public ParseException getAlternateException()
  {
    return _alternateException;
  }

  public void setAlternateException( ParseException alternateException )
  {
    _alternateException = alternateException;
  }

  public boolean isCausedByArgumentList()
  {
    return _bCausedByArgumentList;
  }

  public void setCausedByArgumentList( boolean bCausedByArgumentList )
  {
    _bCausedByArgumentList = bCausedByArgumentList;
  }

}
