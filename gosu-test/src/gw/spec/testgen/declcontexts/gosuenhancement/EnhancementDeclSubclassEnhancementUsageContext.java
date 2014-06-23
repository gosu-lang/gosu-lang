/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuenhancement;

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
 * Date: Nov 2, 2009
 * Time: 6:13:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class EnhancementDeclSubclassEnhancementUsageContext implements UsageContext {

  private TestGenerationContext _testGenContext;
  public EnhancementDeclSubclassEnhancementUsageContext(TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
  }

  @Override
  public String testClassSuffix() {
    return "EnhancementOfSubclass";
  }

  @Override
  public boolean isApplicable(Member member) {
    return member.getAccessMod() == AccessModifier.Public || member.getAccessMod() == AccessModifier.Internal;
  }

  @Override
  public String getTargetClassName() {
    return _testGenContext.constants().gosuClassSubclassEnhancement();
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