package gw.lang.gosuc;

import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import manifold.internal.javac.IIssue;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import manifold.internal.javac.IIssueContainer;

/**
 */
public class GosuIssueContainer implements IIssueContainer
{
  private final ParseResultsException _pe;
  private List<IIssue> _issues;

  public GosuIssueContainer( ParseResultsException pe )
  {
    _pe = pe;
  }

  @Override
  public List<IIssue> getIssues()
  {
    if( _issues == null )
    {
      List<IIssue> issues = new ArrayList<>();
      if( _pe != null )
      {
        for( IParseIssue diagnostic : _pe.getParseIssues() )
        {
          GosuIssue issue = new GosuIssue( diagnostic );
          issues.add( issue );
        }
      }
      _issues = issues;
    }

    return _issues;
  }

  @Override
  public List<IIssue> getWarnings()
  {
    return getIssues().stream().filter( issue -> issue.getKind() == IIssue.Kind.Warning ).collect( Collectors.toList() );
  }

  @Override
  public List<IIssue> getErrors()
  {
    return getIssues().stream().filter( issue -> issue.getKind() == IIssue.Kind.Error ).collect( Collectors.toList() );
  }

  @Override
  public boolean isEmpty()
  {
    return getIssues().isEmpty();
  }}
