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
public class ClassDeclarationContext implements DeclarationContext {
  private TestGenerationContext _testGenContext;
  private List<? extends Member> _members;

  public ClassDeclarationContext(CodeGenerationContext codeGenContext, TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
    ClassFileGenerator generator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().gosuClass());
    _members = testGenContext.getMemberCombinationGenerator().generateAllPossibleCombinations(this);
    for (Member m : _members) {
      generator.addMember(m.generateGosuMemberDeclaration());
    }
  }

  public String testClassPrefix() {
    return "GosuClass";
  }

  @Override
  public List<? extends Member> getMembers() {
    return _members;
  }

  @Override
  public String typeName() {
    return _testGenContext.constants().gosuClass();
  }

  @Override
  public String memberSuffix() {
    return "GosuClass";
  }

  @Override
  public int memberIntSuffix() {
    return 1;
  }

  @Override
  public boolean isInterface() {
    return false;
  }
}
