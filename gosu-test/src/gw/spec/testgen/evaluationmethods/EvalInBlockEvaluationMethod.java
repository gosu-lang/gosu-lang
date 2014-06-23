/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.evaluationmethods;

import gw.spec.testgen.Member;
import gw.spec.testgen.TestMethodStyle;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 2:32:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class EvalInBlockEvaluationMethod extends WrappedEvaluationMethod {
  public EvalInBlockEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationMethod underlyingMethod) {
    super(suffix, testMethodStyle, underlyingMethod);
  }

  @Override
  protected String getSetup(Member member) {
    String setup = _underlyingMethod.getSetup(member);
    setup += "  var myBlock = \\ -> " + "eval(\"" + _underlyingMethod.getReturnValue(member) + "\") as " + getCastTarget( member ) + "\n";
    return setup;
  }

  @Override
  protected String getReturnValue(Member member) {
    return "myBlock()";
  }

  @Override
  public boolean disableTestByDefault() {
    return true;
  }

}
