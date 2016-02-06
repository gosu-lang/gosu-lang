/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosusubclass;

import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.DeclarationContext;
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
public class SubclassDeclarationContext implements DeclarationContext {

  private List<? extends Member> _members;
  private TestGenerationContext _testGenContext;

  public SubclassDeclarationContext(CodeGenerationContext codeGenContext, TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
    ClassFileGenerator generator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().gosuClassSubclass());
    generator.setSuperClass(testGenContext.constants().gosuClass());

    _members = testGenContext.getMemberCombinationGenerator().generateAllPossibleCombinations(this);
    for (Member m : _members) {
      generator.addMember(m.generateGosuMemberDeclaration());
    }
  }

  public String testClassPrefix() {
    return "Subclass";
  }

  @Override
  public List<? extends Member> getMembers() {
    return _members;
  }

  @Override
  public String typeName() {
    return _testGenContext.constants().gosuClassSubclass();
  }

  @Override
  public String memberSuffix() {
    return "Subclass";
  }

  @Override
  public int memberIntSuffix() {
    return 3;
  }


  @Override
  public boolean isInterface() {
    return false;
  }
}