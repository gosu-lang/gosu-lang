/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.beanmethodcall.testgen;

import gw.spec.testgen.TestGenerationContext;
import gw.spec.testgen.MemberCombinationGenerator;
import gw.spec.testgen.DeclarationContextConstants;
import gw.spec.testgen.EvaluationMethodGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 5:51:17 PM
 * To change this template use File | Settings | File Templates.
 */
class BeanMethodCallTestGenerationContext implements TestGenerationContext {

  public static final BeanMethodCallTestGenerationContext INSTANCE = new BeanMethodCallTestGenerationContext();

  private BeanMethodCallTestGenerationContext() { }

  @Override
  public MemberCombinationGenerator getMemberCombinationGenerator() {
    return MethodMember.COMBINATION_GENERATOR;
  }

  @Override
  public DeclarationContextConstants constants() {
    return BeanMethodCallClassNameConstants.INSTANCE;
  }

  @Override
  public EvaluationMethodGenerator getEvaluationMethodGenerator() {
    return BeanMethodCallEvaluationMethodGenerator.INSTANCE;
  }

  @Override
  public boolean isBeanMethodCall() {
    return true;
  }
}