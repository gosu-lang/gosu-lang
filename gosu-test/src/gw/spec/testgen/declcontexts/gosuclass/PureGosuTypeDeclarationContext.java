/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuclass;

import gw.spec.testgen.DeclarationContext;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.Member;
import gw.spec.testgen.TestGenerationContext;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:08:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class PureGosuTypeDeclarationContext implements DeclarationContext {
  private TestGenerationContext _testGenContext;
  private List<? extends Member> _members;

  public PureGosuTypeDeclarationContext(CodeGenerationContext codeGenContext, TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
    ClassFileGenerator generator = codeGenContext.createJavaClassFileGenerator(testGenContext.constants().javaClassWrappedByPureGosuType());
    _members = testGenContext.getMemberCombinationGenerator().generateAllPossibleCombinations(this);
    for (Member m : _members) {
      generator.addMember(m.generateJavaMemberDeclaration());
    }
  }

  public String testClassPrefix() {
    return "PureGosuType";
  }

  @Override
  public List<? extends Member> getMembers() {
    return _members;
  }

  @Override
  public String typeName() {
    return _testGenContext.constants().javaClassWrappedByPureGosuType() + "WrappedType";
  }

  @Override
  public String memberSuffix() {
    return "PureGosuType";
  }

  @Override
  public int memberIntSuffix() {
    return 42;
  }

  @Override
  public boolean isInterface() {
    return false;
  }
}