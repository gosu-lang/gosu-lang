/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.memberaccess.testgen;

import gw.spec.testgen.TestMethodStyle;
import gw.spec.testgen.EvaluationRoot;
import gw.spec.testgen.Member;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 2:41:03 PM
 * To change this template use File | Settings | File Templates.
 */
class BracketReflectionEvaluationMethod extends CorePropertyEvaluationMethod {
  public BracketReflectionEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationRoot evalRoot) {
    super(suffix, testMethodStyle, evalRoot);
  }

  @Override
  public String getReturnValue(Member member) {
    return _evalRoot.getExpressionRoot(member) + "[\"" + member.memberName() + "\"] as " + getCastTarget( member );
  }
}
