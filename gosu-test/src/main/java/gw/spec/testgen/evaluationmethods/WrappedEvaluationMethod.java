/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.evaluationmethods;

import gw.spec.testgen.TestMethodStyle;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 2:25:36 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class WrappedEvaluationMethod extends EvaluationMethod {
  protected EvaluationMethod _underlyingMethod;

  protected WrappedEvaluationMethod(String suffix, TestMethodStyle testMethodStyle, EvaluationMethod underlyingMethod) {
    super(suffix, testMethodStyle);
    _underlyingMethod = underlyingMethod;
  }
}
