/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuclass;

import gw.spec.testgen.UsageContext;
import gw.spec.testgen.TestGenerationContext;
import gw.spec.testgen.Member;
import gw.spec.testgen.AccessModifier;
import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.evaluationmethods.EvaluationMethod;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 5, 2009
 * Time: 3:35:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassDeclSubclassInOtherPackageUsageContext implements UsageContext {

  private TestGenerationContext _testGenContext;
  public ClassDeclSubclassInOtherPackageUsageContext(TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
  }

  @Override
  public String testClassSuffix() {
    return "SubclassInOtherPackage";
  }

  public boolean isApplicable(Member member) {
    // The subclass should have access to public and protected members
    return member.getAccessMod() == AccessModifier.Public || member.getAccessMod() == AccessModifier.Protected;
  }

  @Override
  public String getTargetClassName() {
    return _testGenContext.constants().gosuClassSubclassInOtherPackage();
  }

  @Override
  public ClassFileGenerator getTargetClassFileGenerator(CodeGenerationContext codeGenContext, Member member) {
    return codeGenContext.getClassFileGenerator(getTargetClassName());
  }

  @Override
  public List<EvaluationMethod> getEvaluationMethods(Member member) {
    return _testGenContext.getEvaluationMethodGenerator().getSubclassRootEvaluationMethods(member, getTargetClassName());
  }
}
