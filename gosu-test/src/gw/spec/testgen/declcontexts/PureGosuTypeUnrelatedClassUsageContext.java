/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts;

import gw.spec.testgen.AccessModifier;
import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.Member;
import gw.spec.testgen.TestGenerationContext;
import gw.spec.testgen.UsageContext;
import gw.spec.testgen.Scope;
import gw.spec.testgen.evaluationmethods.EvaluationMethod;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:09:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class PureGosuTypeUnrelatedClassUsageContext implements UsageContext {

  private TestGenerationContext _testGenContext;
  public PureGosuTypeUnrelatedClassUsageContext(TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
  }

  @Override
  public String testClassSuffix() {
    return "PureGosuTypeUnrelatedClass";
  }

  public boolean isApplicable(Member member) {
    // Unrelated classes only have access to public members
    return member.getAccessMod() == AccessModifier.Public &&
            (member.getScope() == Scope.Instance ||
                    !member.isMethod() ||
                    _testGenContext.isBeanMethodCall());
  }

  @Override
  public String getTargetClassName() {
    return _testGenContext.constants().unrelatedClass();
  }

  @Override
  public ClassFileGenerator getTargetClassFileGenerator(CodeGenerationContext codeGenContext, Member member) {
    return codeGenContext.getClassFileGenerator(_testGenContext.constants().unrelatedClass());
  }

  @Override
  public List<EvaluationMethod> getEvaluationMethods(Member member) {
    return _testGenContext.getEvaluationMethodGenerator().getDefaultEvaluationMethods(member);
  }
}