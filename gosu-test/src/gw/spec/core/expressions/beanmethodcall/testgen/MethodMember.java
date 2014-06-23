/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.beanmethodcall.testgen;

import gw.spec.testgen.AccessModifier;
import gw.spec.testgen.Scope;
import gw.spec.testgen.Member;
import gw.spec.testgen.DeclarationContext;
import gw.spec.testgen.MemberCombinationGenerator;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:04:46 AM
 * To change this template use File | Settings | File Templates.
 */
class MethodMember implements Member {

  public static final MemberCombinationGenerator COMBINATION_GENERATOR = new MemberCombinationGenerator() {
    @Override
    public List<? extends Member> generateAllPossibleCombinations(DeclarationContext declContext) {
      return generateAllMemberCombinations(declContext);
    }
  };

  public static List<MethodMember> generateAllMemberCombinations(DeclarationContext declContext) {
    List<MethodMember> allMembers = new ArrayList<MethodMember>();
    for (Scope s : Scope.values()) {
      for (AccessModifier a : AccessModifier.values()) {
        for (ValueType vt : ValueType.values()) {
          allMembers.add(new MethodMember(a, s, vt, declContext));
        }
      }
    }
    return allMembers;
  }

  private AccessModifier _accessMod;
  private Scope _scope;
  private ValueType _valueType;
  private DeclarationContext _context;

  private MethodMember(AccessModifier accessMod, Scope scope, ValueType valueType, DeclarationContext context) {
    _accessMod = accessMod;
    _scope = scope;
    _valueType = valueType;
    _context = context;
  }

  public AccessModifier getAccessMod() {
    return _accessMod;
  }

  public Scope getScope() {
    return _scope;
  }

  public ValueType getValueType() {
    return _valueType;
  }

  public DeclarationContext getContext() {
    return _context;
  }

  public String generateGosuMemberDeclaration() {
    StringBuilder sb = new StringBuilder();
    sb.append(_accessMod.getModifierName()).append(_scope.getScopeQualifier());
    if (_context.isInterface()) {
      sb.append("function ").append(memberName()).append("() : ").append(memberTypeName());
    } else {
      sb.append("function ").append(memberName()).append("() : ").append(memberTypeName()).append(" {\n");
      sb.append("  return ").append(memberValue()).append("\n");
      sb.append("}");
    }
    return sb.toString();
  }

  public String generateJavaMemberDeclaration() {
    StringBuilder sb = new StringBuilder();
    if (_context.isInterface()) {
      sb.append(_accessMod.getJavaModifierName()).append(_scope.getScopeQualifier()).append(_valueType.getName()).append(" ").append(memberName()).append("();");
    } else {
      sb.append(_accessMod.getJavaModifierName()).append(_scope.getScopeQualifier()).append(_valueType.getName()).append(" ").append(memberName()).append("() {\n");
      sb.append("  return ").append(memberValue()).append(";\n");
      sb.append("}");
    }
    return sb.toString();
  }

  public String memberName() {
    StringBuilder sb = new StringBuilder();
    sb.append(_accessMod).append(_scope).append(_valueType).append(_context.memberSuffix());
    String result = sb.toString();
    result = Character.toLowerCase(result.charAt(0)) + result.substring(1);
    return result;
  }

  @Override
  public String memberTypeName() {
    return _valueType.getName();
  }

  public String memberValue() {
    if (_valueType == ValueType.String) {
      return "\"" + _accessMod + "-" + _scope + "-" + _context.memberSuffix() + "\"";
    } else {
      return "" + (_accessMod.ordinal() + 1) + "" +  + (_scope.ordinal() + 1) + "" + _context.memberIntSuffix();
    }
  }

  @Override
  public boolean isMethod() {
    return true;
  }
}