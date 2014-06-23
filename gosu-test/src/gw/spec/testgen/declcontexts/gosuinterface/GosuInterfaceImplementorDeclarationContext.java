/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuinterface;

import gw.spec.testgen.AccessModifier;
import gw.spec.testgen.ClassFileGenerator;
import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.DeclarationContext;
import gw.spec.testgen.Member;
import gw.spec.testgen.Scope;
import gw.spec.testgen.TestGenerationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:08:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class GosuInterfaceImplementorDeclarationContext implements DeclarationContext {

  private List<Member> _members;
  private TestGenerationContext _testGenContext;

  public GosuInterfaceImplementorDeclarationContext(CodeGenerationContext codeGenContext, TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
    ClassFileGenerator generator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().gosuInterfaceImplementor());
    generator.addImplementedInterface(testGenContext.constants().gosuInterface());

    _members = new ArrayList<Member>();
    List<? extends Member> potentialMembers = testGenContext.getMemberCombinationGenerator().generateAllPossibleCombinations(this);
    for (Member m : potentialMembers) {
      if (m.getAccessMod() == AccessModifier.Public && m.getScope() == Scope.Instance && m.isMethod()) {
        generator.addMember(m.generateGosuMemberDeclaration());
        _members.add(m);
      }
    }
  }

  public String testClassPrefix() {
    return "GosuInterfaceImplementor";
  }

  @Override
  public List<? extends Member> getMembers() {
    return _members;
  }

  @Override
  public String typeName() {
    return _testGenContext.constants().gosuInterfaceImplementor();
  }

  // We want the members to match GosuInterfaceDeclarationContext
  @Override
  public String memberSuffix() {
    return "GosuInterface";
  }

  // We want the members to match GosuInterfaceDeclarationContext
  @Override
  public int memberIntSuffix() {
    return 6;
  }

  @Override
  public boolean isInterface() {
    return false;
  }
}