/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen.declcontexts.gosuenhancement;

import gw.spec.testgen.CodeGenerationContext;
import gw.spec.testgen.DeclarationContext;
import gw.spec.testgen.GosuClassFileGenerator;
import gw.spec.testgen.Member;
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
public class EnhancementOfClassDeclarationContext implements DeclarationContext {

  private List<Member> _members;
  private TestGenerationContext _testGenContext;

  public EnhancementOfClassDeclarationContext(CodeGenerationContext codeGenContext, TestGenerationContext testGenContext) {
    _testGenContext = testGenContext;
    GosuClassFileGenerator generator = codeGenContext.createGosuClassFileGenerator(testGenContext.constants().gosuClassEnhancement());
    generator.setEnhancement(true);
    generator.setSuperClass(testGenContext.constants().gosuClass());

    _members = new ArrayList<Member>();
    List<? extends Member> possibleMembers = testGenContext.getMemberCombinationGenerator().generateAllPossibleCombinations(this);
    for (Member m : possibleMembers) {
      if (m.isMethod()) {
        generator.addMember(m.generateGosuMemberDeclaration());
        _members.add(m);
      }
    }
  }

  public String testClassPrefix() {
    return "Enhancement";
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
    return "OnEnhancement";
  }

  @Override
  public int memberIntSuffix() {
    return 2;
  }

  @Override
  public boolean isInterface() {
    return false;
  }

}