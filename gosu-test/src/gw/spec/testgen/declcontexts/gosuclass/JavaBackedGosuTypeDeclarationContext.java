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


public class JavaBackedGosuTypeDeclarationContext implements DeclarationContext {
  private TestGenerationContext _testGenContext;
  private List<? extends Member> _members;

  public JavaBackedGosuTypeDeclarationContext(CodeGenerationContext codeGenContext, TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
    ClassFileGenerator generator = codeGenContext.createJavaClassFileGenerator(testGenContext.constants().javaClassWrappedByJavaBackedType());
    _members = testGenContext.getMemberCombinationGenerator().generateAllPossibleCombinations(this);
    for (Member m : _members) {
      generator.addMember(m.generateJavaMemberDeclaration());
    }
  }

  public String testClassPrefix() {
    return "JavaBackedGosuType";
  }

  @Override
  public List<? extends Member> getMembers() {
    return _members;
  }

  @Override
  public String typeName() {
    return _testGenContext.constants().javaClassWrappedByJavaBackedType() + "JavaBackedWrappedType";
  }

  @Override
  public String memberSuffix() {
    return "JavaBackedGosuType";
  }

  @Override
  public int memberIntSuffix() {
    return 43;
  }

  @Override
  public boolean isInterface() {
    return false;
  }
}