/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.beanmethodcall.testgen;

import gw.spec.testgen.EvaluationMethodGenerator;
import gw.spec.testgen.Member;
import gw.spec.testgen.EvaluationRoot;
import gw.spec.testgen.Scope;
import gw.spec.testgen.TestMethodStyle;
import gw.spec.testgen.evaluationmethods.EvaluationMethod;
import gw.spec.testgen.evaluationmethods.EvalEvaluationMethod;
import gw.spec.testgen.evaluationmethods.BlockEvaluationMethod;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 4, 2009
 * Time: 1:52:54 PM
 * To change this template use File | Settings | File Templates.
 */
class BeanMethodCallEvaluationMethodGenerator implements EvaluationMethodGenerator {

  public static final BeanMethodCallEvaluationMethodGenerator INSTANCE = new BeanMethodCallEvaluationMethodGenerator();

  private BeanMethodCallEvaluationMethodGenerator() { }

  @Override
  public List<EvaluationMethod> getDefaultEvaluationMethods(Member member) {
    EvaluationRoot defaultEvalRoot;
    if (member.getScope() == Scope.Static) {
      defaultEvalRoot = EvaluationRoot.DECLARATION_TYPE_LITERAL;
    } else {
      defaultEvalRoot = EvaluationRoot.NEW_INSTANCE_EXPRESSION;
    }
    return getStandardEvaluationMethods(member, defaultEvalRoot);
  }

  @Override
  public List<EvaluationMethod> getSubclassRootEvaluationMethods(Member member, String subclassName) {
    EvaluationRoot defaultEvalRoot;
    if (member.getScope() == Scope.Static) {
      defaultEvalRoot = new SubclassTypeLiteral(subclassName);
    } else {
      defaultEvalRoot = new SubclassNewInstance(subclassName);
    }

    return getStandardEvaluationMethods(member, defaultEvalRoot);
  }

  @Override
  public List<EvaluationMethod> getStandardThisRootEvaluationMethods(Member member) {
    List<EvaluationMethod> methods = new ArrayList<EvaluationMethod>();
    methods.add(new MethodCallEvaluationMethod("ViaThis", TestMethodStyle.INSTANCE_METHOD, EvaluationRoot.THIS));
    methods.add(new EvalEvaluationMethod("ViaThisInEval", TestMethodStyle.INSTANCE_METHOD, new MethodCallEvaluationMethod("", TestMethodStyle.INSTANCE_METHOD, EvaluationRoot.THIS)));
    methods.add(new BlockEvaluationMethod("ViaThisInBlock", TestMethodStyle.INSTANCE_METHOD, new MethodCallEvaluationMethod("", TestMethodStyle.INSTANCE_METHOD, EvaluationRoot.THIS)));
    return methods;
  }

  protected List<EvaluationMethod> getStandardEvaluationMethods(Member member, EvaluationRoot defaultEvalRoot) {
    List<EvaluationMethod> methods = new ArrayList<EvaluationMethod>();

    // TODO - AHK - At some point think about adding in reflection and such

    methods.add(new MethodCallEvaluationMethod("", TestMethodStyle.STATIC_METHOD, defaultEvalRoot));
//    if (member.getAccessMod() == AccessModifier.Public) {
//      methods.add(new BracketReflectionEvaluationMethod("ViaBracketReflection", TestMethodStyle.STATIC_METHOD, bracketEvalRoot));
//      methods.add(new ExplicitReflectionEvaluationMethod("ViaExplicitReflection", TestMethodStyle.STATIC_METHOD, defaultEvalRoot));
//    }
    methods.add(new EvalEvaluationMethod("ViaEval", TestMethodStyle.STATIC_METHOD, new MethodCallEvaluationMethod("", TestMethodStyle.STATIC_METHOD, defaultEvalRoot)));
    methods.add(new BlockEvaluationMethod("ViaBlock", TestMethodStyle.STATIC_METHOD, new MethodCallEvaluationMethod("", TestMethodStyle.STATIC_METHOD, defaultEvalRoot)));
//    methods.add(new EvalInBlockEvaluationMethod("ViaEvalInBlock", TestMethodStyle.STATIC_METHOD, new MethodCallEvaluationMethod("", TestMethodStyle.STATIC_METHOD, defaultEvalRoot)));
//    methods.add(new BlockInEvalEvaluationMethod("ViaBlockInEval", TestMethodStyle.STATIC_METHOD, new MethodCallEvaluationMethod("", TestMethodStyle.STATIC_METHOD, defaultEvalRoot)));

    return methods;
  }

  public static class SubclassTypeLiteral extends EvaluationRoot {
    private String _typeName;

    public SubclassTypeLiteral(String typeName) {
      _typeName = typeName;
    }

    @Override
    public String getExpressionRoot(Member member) {
      return _typeName;
    }
  };

  public static class SubclassNewInstance extends EvaluationRoot {
    private String _typeName;

    public SubclassNewInstance(String typeName) {
      _typeName = typeName;
    }

    @Override
    public String getExpressionRoot(Member member) {
      return "new " + _typeName + "()";
    }
  };
}