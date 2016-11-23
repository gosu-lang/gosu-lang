package editor.plugin.typeloader.java;

import editor.IIssue;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseException;

/**
 */
public class GosuIssue implements IIssue
{
  private final IParseIssue _issue;

  public GosuIssue( IParseIssue issue )
  {
    _issue = issue;
  }

  @Override
  public Kind getKind()
  {
    return _issue instanceof ParseException
           ? Kind.Error
           : Kind.Warning;
  }

  @Override
  public int getStartOffset()
  {
    return _issue.getTokenStart();
  }

  @Override
  public int getEndOffset()
  {
    return _issue.getTokenEnd();
  }

  @Override
  public int getLine()
  {
    return _issue.getLine();
  }

  @Override
  public int getColumn()
  {
    return _issue.getColumn();
  }

  @Override
  public String getMessage()
  {
    return _issue.getUIMessage();
  }
}
