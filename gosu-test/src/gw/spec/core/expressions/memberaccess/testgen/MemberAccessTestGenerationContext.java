/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.memberaccess.testgen;

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
class MemberAccessTestGenerationContext implements TestGenerationContext {

  public static final MemberAccessTestGenerationContext INSTANCE = new MemberAccessTestGenerationContext();

  private MemberAccessTestGenerationContext() { }

  @Override
  public MemberCombinationGenerator getMemberCombinationGenerator() {
    return PropertyMember.COMBINATION_GENERATOR;
  }

  @Override
  public DeclarationContextConstants constants() {
    return MemberAccessClassNameConstants.INSTANCE;
  }

  @Override
  public EvaluationMethodGenerator getEvaluationMethodGenerator() {
    return MemberAccessEvaluationMethodGenerator.INSTANCE;
  }

  @Override
  public boolean isBeanMethodCall() {
    return false;
  }
}
