/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.beanmethodcall.testgen;

import gw.spec.testgen.evaluationmethods.EvaluationMethod;
import gw.spec.testgen.EvaluationRoot;
import gw.spec.testgen.Member;
import gw.spec.testgen.TestMethodStyle;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 2:41:30 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class CoreMethodCallEvaluationMethod extends EvaluationMethod {

  protected EvaluationRoot _evalRoot;

  protected CoreMethodCallEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationRoot evalRoot) {
    super(suffix, testMethodStyle);
    _evalRoot = evalRoot;
  }

  @Override
  protected String getSetup(Member member) {
    return _evalRoot.getSetup(member);
  }
}