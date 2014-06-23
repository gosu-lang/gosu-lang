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
 * Time: 2:27:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlockEvaluationMethod extends WrappedEvaluationMethod {

  public BlockEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationMethod underlyingMethod) {
    super(suffix, testMethodStyle, underlyingMethod);
  }

  @Override
  protected String getSetup(Member member) {
    String setup = _underlyingMethod.getSetup(member);
    setup += "  var myBlock = \\ -> " + _underlyingMethod.getReturnValue(member) + "\n";
    return setup;
  }

  @Override
  protected String getReturnValue(Member member) {
    return "myBlock()";
  }

  @Override
  public boolean disableTestByDefault() {
    return _underlyingMethod.disableTestByDefault();
  }
}
