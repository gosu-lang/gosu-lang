/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import gw.spec.testgen.evaluationmethods.EvaluationMethod;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:07:17 AM
 * To change this template use File | Settings | File Templates.
 */
public interface UsageContext {
  // Suffix to use on the test class name
  String testClassSuffix();

  // Whether or not the given Member is usable from this context
  boolean isApplicable(Member member);

   List<EvaluationMethod> getEvaluationMethods(Member member);

  // The name of the class to generate the usage in
  String getTargetClassName();

  // The CodeGenerationContext for the target class
  ClassFileGenerator getTargetClassFileGenerator(CodeGenerationContext codeGenContext, Member member);

}