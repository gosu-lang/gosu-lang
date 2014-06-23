/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

import gw.spec.testgen.evaluationmethods.EvaluationMethod;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Nov 4, 2009
 * Time: 1:47:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EvaluationMethodGenerator {

  List<EvaluationMethod> getDefaultEvaluationMethods(Member member);

  List<EvaluationMethod> getSubclassRootEvaluationMethods(Member member, String subclassName);

  List<EvaluationMethod> getStandardThisRootEvaluationMethods(Member member);
}
