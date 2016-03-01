/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuclass;

import gw.spec.testgen.AccessModifier;
import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.Member;
import gw.spec.testgen.TestGenerationContext;
import gw.spec.testgen.UsageContext;
import gw.spec.testgen.evaluationmethods.EvaluationMethod;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:09:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClassDeclInnerClassOnSubclassUsageContext implements UsageContext {

  private TestGenerationContext _testGenContext;
  public ClassDeclInnerClassOnSubclassUsageContext(TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
  }

  @Override
  public String testClassSuffix() {
    return "SubclassInnerClass";
  }

  public boolean isApplicable(Member member) {
    // The subclass should have access to public and protected members
    return member.getAccessMod() != AccessModifier.Private;
  }

  @Override
  public String getTargetClassName() {
    return _testGenContext.constants().gosuClassSubclass() + "." + _testGenContext.constants().gosuClassSubclassInnerClass();
  }

  @Override
  public ClassFileGenerator getTargetClassFileGenerator(CodeGenerationContext codeGenContext, Member member) {
    return codeGenContext.getClassFileGenerator(_testGenContext.constants().gosuClassSubclass()).getOrCreateInnerClass(_testGenContext.constants().gosuClassSubclassInnerClass());
  }

  @Override
  public List<EvaluationMethod> getEvaluationMethods(Member member) {
    return _testGenContext.getEvaluationMethodGenerator().getDefaultEvaluationMethods(member);
  }
}