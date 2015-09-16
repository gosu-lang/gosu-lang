package gw.internal.gosu.regression;

import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.Res;
import gw.test.TestClass;
import gw.util.GosuTestUtil;

import java.util.List;

public class PL33235Test  extends TestClass
{
  public void testSaneWhileLoopErrorOnAssignment()
  {
    ParseResultsException pre = GosuTestUtil.getParseResultsException( "var x : boolean\n" +
                                                                       "while(x = true) {}" );
    List<IParseIssue> issues = pre.getParseIssues();
    assertEquals( 1, issues.size() );
    assertEquals( Res.MSG_ASSIGNMENT_IN_LOOP_STATEMENT, issues.get(0).getMessageKey() );
  }

  public void testSaneDoWhileLoopErrorOnAssignment()
  {
    ParseResultsException pre = GosuTestUtil.getParseResultsException( "var x : boolean\n" +
                                                                       "do {} while(x = true)" );
    List<IParseIssue> issues = pre.getParseIssues();
    assertEquals( 1, issues.size() );
    assertEquals( Res.MSG_ASSIGNMENT_IN_LOOP_STATEMENT, issues.get(0).getMessageKey() );
  }
}