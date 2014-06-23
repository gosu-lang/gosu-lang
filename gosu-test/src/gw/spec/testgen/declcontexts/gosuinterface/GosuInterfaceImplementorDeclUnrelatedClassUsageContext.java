/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuinterface;

import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.Member;
import gw.spec.testgen.UsageContext;
import gw.spec.testgen.TestGenerationContext;
import gw.spec.testgen.evaluationmethods.EvaluationMethod;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:09:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class GosuInterfaceImplementorDeclUnrelatedClassUsageContext implements UsageContext {

  private TestGenerationContext _testGenContext;
  public GosuInterfaceImplementorDeclUnrelatedClassUsageContext(TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
  }

  @Override
  public String testClassSuffix() {
    return "UnrelatedClass";
  }

  public boolean isApplicable(Member member) {
    // The implementor side should only test methods, never fields
    return member.isMethod();
  }

  @Override
  public String getTargetClassName() {
    return _testGenContext.constants().unrelatedClass();
  }

  @Override
  public ClassFileGenerator getTargetClassFileGenerator(CodeGenerationContext codeGenContext, Member member) {
    return codeGenContext.getClassFileGenerator(getTargetClassName());
  }

  @Override
  public List<EvaluationMethod> getEvaluationMethods(Member member) {
    return _testGenContext.getEvaluationMethodGenerator().getDefaultEvaluationMethods(member);
  }
}