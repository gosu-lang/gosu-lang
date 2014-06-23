/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import gw.spec.testgen.evaluationmethods.EvaluationMethod;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 3, 2009
 * Time: 11:00:55 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestMethodStyle {

  public abstract void generateTest(CodeGenerationContext codeGenContext, UsageContext usageContext, Member member, EvaluationMethod method);

  public static TestMethodStyle STATIC_METHOD = new TestMethodStyle() {
    @Override
    public void generateTest(CodeGenerationContext codeGenContext, UsageContext usageContext, Member member, EvaluationMethod method) {
      StringBuilder sb = new StringBuilder();
      sb.append("function test" + capitalize(member.memberName()) + "Access" + method.getSuffix() + "() {\n");
      sb.append("  ");
      sb.append("assertEquals(").append(member.memberValue()).append(", ");
      sb.append(usageContext.getTargetClassName()).append(".").append(targetClassMethodName(usageContext, member, method)).append("())\n");
      sb.append("}");
      codeGenContext.getCurrentTestGenerator().addFunction(sb.toString());

      // The actual tests need to go inside the declaration file
      ClassFileGenerator declarationFileGenerator = usageContext.getTargetClassFileGenerator(codeGenContext, member);
      sb = new StringBuilder();
      sb.append("static function ").append(targetClassMethodName(usageContext, member, method)).append("() : ").append(member.memberTypeName()).append(" {\n");
      sb.append(method.getEvaluationCode(member)).append("\n");
      sb.append("}");
      declarationFileGenerator.addFunction(sb.toString());
    }
  };

  public static TestMethodStyle INSTANCE_METHOD = new TestMethodStyle() {
    @Override
    public void generateTest(CodeGenerationContext codeGenContext, UsageContext usageContext, Member member, EvaluationMethod method) {
      StringBuilder sb = new StringBuilder();
      sb.append("function test" + capitalize(member.memberName()) + "Access" + method.getSuffix() + "() {\n");
      sb.append("  ");
      sb.append("assertEquals(").append(member.memberValue()).append(", ");
      sb.append("new ").append(usageContext.getTargetClassName()).append("()").append(".").append(targetClassMethodName(usageContext, member, method)).append("())\n");
      sb.append("}");
      codeGenContext.getCurrentTestGenerator().addFunction(sb.toString());

      // The actual tests need to go inside the declaration file
      ClassFileGenerator declarationFileGenerator = usageContext.getTargetClassFileGenerator(codeGenContext, member);
      sb = new StringBuilder();
      sb.append("function ").append(targetClassMethodName(usageContext, member, method)).append("() : ").append(member.memberTypeName()).append(" {\n");
      sb.append(method.getEvaluationCode(member)).append("\n");
      sb.append("}");
      declarationFileGenerator.addFunction(sb.toString());
    }
  };

  private static String targetClassMethodName(UsageContext usageContext, Member member, EvaluationMethod method) {
    return "do" + capitalize(member.memberName()) + usageContext.testClassSuffix() + "Access" + method.getSuffix();
  }

  private static String capitalize(String str) {
    return Character.toUpperCase(str.charAt(0)) + str.substring(1);
  }
}
