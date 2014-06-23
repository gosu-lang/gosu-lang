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
 * Time: 2:29:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlockInEvalEvaluationMethod extends WrappedEvaluationMethod {
  public BlockInEvalEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationMethod underlyingMethod) {
    super(suffix, testMethodStyle, underlyingMethod);
  }

  @Override
  protected String getSetup(Member member) {
    String setup = _underlyingMethod.getSetup(member);
    setup += "  var evalString = \"var myNestedBLock = (\\\\ -> " + _underlyingMethod.getReturnValue(member) + "); return myNestedBLock() \"\n";
    return setup;
  }

  @Override
  protected String getReturnValue(Member member)
  {
      return "eval(evalString) as " + getCastTarget( member );
  }

  @Override
  public boolean disableTestByDefault() {
    return true;
  }
}
