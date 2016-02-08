/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.evaluationmethods;

import gw.spec.testgen.TestMethodStyle;
import gw.spec.testgen.Member;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 2:25:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class EvalEvaluationMethod extends WrappedEvaluationMethod {
  public EvalEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationMethod underlyingMethod) {
    super(suffix, testMethodStyle, underlyingMethod);
  }

  @Override
  protected String getSetup(Member member) {
    return _underlyingMethod.getSetup(member);
  }

  @Override
  protected String getReturnValue(Member member) {
    return "eval(\"" + _underlyingMethod.getReturnValue(member) + "\") as " + getCastTarget( member );
  }

  @Override
  public boolean disableTestByDefault() {
    return true;
  }
}
