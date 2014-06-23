/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 2, 2009
 * Time: 1:26:25 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EvaluationRoot {

  public abstract String getExpressionRoot(Member member);

  public String getSetup(Member member) {
    return "";
  }

  public static final EvaluationRoot DECLARATION_TYPE_LITERAL = new EvaluationRoot() {
    @Override
    public String getExpressionRoot(Member member) {
      return member.getContext().typeName();
    }
  };

  public static final EvaluationRoot NEW_INSTANCE_EXPRESSION = new EvaluationRoot() {
    @Override
    public String getExpressionRoot(Member member) {
      return "new " + member.getContext().typeName() + "()";
    }
  };

  public static final EvaluationRoot VARIABLE_WITH_NEW_INSTANCE_VALUE = new EvaluationRoot() {
    @Override
    public String getExpressionRoot(Member member) {
      return "obj";
    }

    @Override
    public String getSetup(Member member) {
      return "  var obj = new " + member.getContext().typeName() + "()\n";
    }
  };

  public static final EvaluationRoot VARIABLE_WITH_TYPE_LITERAL_VALUE = new EvaluationRoot() {
    @Override
    public String getExpressionRoot(Member member) {
      return "typeVar";
    }

    @Override
    public String getSetup(Member member) {
      return "  var typeVar = " + member.getContext().typeName() + "\n";
    }
  };

  public static final EvaluationRoot THIS = new EvaluationRoot() {
    @Override
    public String getExpressionRoot(Member member) {
      return "this";
    }
  };

  public static final EvaluationRoot OUTER = new EvaluationRoot() {
    @Override
    public String getExpressionRoot(Member member) {
      return "outer";
    }
  };

  public static final EvaluationRoot SUPER = new EvaluationRoot() {
    @Override
    public String getExpressionRoot(Member member) {
      return "super";
    }
  };
}
