/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 5:41:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TestGenerationContext {
  MemberCombinationGenerator getMemberCombinationGenerator();
  DeclarationContextConstants constants();
  EvaluationMethodGenerator getEvaluationMethodGenerator();
  boolean isBeanMethodCall();
}
