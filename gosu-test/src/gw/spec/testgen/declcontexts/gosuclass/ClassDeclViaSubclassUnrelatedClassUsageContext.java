/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuclass;

import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.AccessModifier;
import gw.spec.testgen.evaluationmethods.EvaluationMethod;
import gw.spec.testgen.Member;
import gw.spec.testgen.TestGenerationContext;
import gw.spec.testgen.UsageContext;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 2, 2009
 * Time: 2:21:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClassDeclViaSubclassUnrelatedClassUsageContext implements UsageContext {

  private TestGenerationContext _testGenContext;
  public ClassDeclViaSubclassUnrelatedClassUsageContext(TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
  }

  @Override
  public String testClassSuffix() {
    return "SubclassRootOnUnrelatedClass";
  }

  @Override
  public boolean isApplicable(Member member) {
    return member.getAccessMod() != AccessModifier.Private;
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
    return _testGenContext.getEvaluationMethodGenerator().getSubclassRootEvaluationMethods(member, _testGenContext.constants().gosuClassSubclass());
  }

}
