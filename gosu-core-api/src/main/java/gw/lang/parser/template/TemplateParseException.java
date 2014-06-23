/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.template;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.resources.Res;

public class TemplateParseException extends Exception {
  private ParseResultsException _pe;
  private ResourceKey _reasonKey;
  private String[] _args;
  private String _strTemplateSource;
  private int _lineNumber;
  private int _column;
  private int _offset;

  public TemplateParseException(ResourceKey reason, int lineNumber, int column, int offset, String... args) {
    this(Res.get(reason, args), null, null, lineNumber, column, offset, reason, args);
  }

  public TemplateParseException(ParseResultsException pe, String strTemplateSource) {
    this(makeTemplateParseExceptionMessage(pe, strTemplateSource), pe, strTemplateSource, 0, 0, 0, null, null);
  }

  public TemplateParseException(String strReason, ParseResultsException pe, String strTemplateSource, int lineNumber, int column, int offset, ResourceKey reason, String[] args) {
    super(strReason);
    _reasonKey = reason;
    _args = args;
    _pe = pe;
    _strTemplateSource = strTemplateSource;
    _lineNumber = lineNumber;
    _column = column;
    _offset = offset;
  }

  public ParseResultsException getParseException() {
    return _pe;
  }

  public String getTemplateSource() {
    return _strTemplateSource;
  }

  public int getLineNumber() {
    return _lineNumber;
  }

  @Override
  public String getMessage() {
    if (_pe != null) {
      return makeTemplateParseExceptionMessage(_pe, _strTemplateSource);
    } else {
      return Res.get(_reasonKey, _args);
    }
  }

  private static String makeTemplateParseExceptionMessage( ParseResultsException e, String strCompiledSource )
  {
    String strFeedback = e.getFeedback();
    strFeedback += "\n" + Res.get(Res.MSG_TEMPLATE_EXCEPTION_GENERATED_SOURCE) + ":\n" + strCompiledSource;
    return strFeedback;
  }

  public int getColumn() {
    return _column;
  }

  public int getOffset() {
    return _offset;
  }
}
