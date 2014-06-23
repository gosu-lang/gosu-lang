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
 * Time: 2:41:11 PM
 * To change this template use File | Settings | File Templates.
 */
class ExplicitReflectionEvaluationMethod extends CorePropertyEvaluationMethod {

  public ExplicitReflectionEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationRoot evalRoot) {
    super(suffix, testMethodStyle, evalRoot);
  }

  @Override
  public String getReturnValue(Member member) {
    return "MemberAccessTestHelper.findProp(" + member.getContext().typeName() + ", \"" + member.memberName() + "\").getValue(" + _evalRoot.getExpressionRoot(member) + ") as " + getCastTarget( member );
  }
}
