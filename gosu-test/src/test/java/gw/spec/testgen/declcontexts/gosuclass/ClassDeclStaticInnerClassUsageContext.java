/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuclass;

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
public class ClassDeclStaticInnerClassUsageContext implements UsageContext {

  private TestGenerationContext _testGenContext;
  public ClassDeclStaticInnerClassUsageContext(TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
  }

  @Override
  public String testClassSuffix() {
    return "StaticInnerClass";
  }

  public boolean isApplicable(Member member) {
    // Everything is fair game from within the class that declared the member
    return true;
  }

  @Override
  public String getTargetClassName() {
    return _testGenContext.constants().gosuClass() + "." + _testGenContext.constants().gosuClassStaticInnerClass();
  }

  @Override
  public ClassFileGenerator getTargetClassFileGenerator(CodeGenerationContext codeGenContext, Member member) {
    ClassFileGenerator cfg = codeGenContext.getClassFileGenerator(_testGenContext.constants().gosuClass()).getOrCreateInnerClass(_testGenContext.constants().gosuClassStaticInnerClass());
    cfg.setStatic(true);
    return cfg;
  }

  @Override
  public List<EvaluationMethod> getEvaluationMethods(Member member) {
    return _testGenContext.getEvaluationMethodGenerator().getDefaultEvaluationMethods(member);
  }
}