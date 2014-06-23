/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.evaluationmethods;

import gw.spec.testgen.Member;
import gw.spec.testgen.TestMethodStyle;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 30, 2009
 * Time: 10:13:21 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EvaluationMethod {

  private String _suffix;
  private TestMethodStyle _testMethodStyle;

  protected EvaluationMethod(String suffix, TestMethodStyle testMethodStyle) {
    _suffix = suffix;
    _testMethodStyle = testMethodStyle;
  }

  public final String getSuffix() {
    return _suffix;
  }

  public final TestMethodStyle getTestMethodStyle() {
    return _testMethodStyle;
  }

  public final String getEvaluationCode(Member member) {
    return getSetup(member) + "  return " + getReturnValue(member);
  }

  public boolean disableTestByDefault() {
    return false;
  }

  protected String getCastTarget( Member member )
  {
    String s = member.memberTypeName();
    if( s.equals( "int" ) )
    {
      return "java.lang.Integer";
    }
    else
    {
      return s;
    }
  }  

  protected abstract String getSetup(Member member);
  protected abstract String getReturnValue(Member member);
}
