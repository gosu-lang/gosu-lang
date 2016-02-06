/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.memberaccess.testgen;

import gw.spec.testgen.AccessModifier;
import gw.spec.core.expressions.memberaccess.testgen.ValueType;
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
class PropertyMember implements Member {

  public static final MemberCombinationGenerator COMBINATION_GENERATOR = new MemberCombinationGenerator() {
    @Override
    public List<? extends Member> generateAllPossibleCombinations(DeclarationContext declContext) {
      return generateAllMemberCombinations(declContext);
    }
  };

  public static List<PropertyMember> generateAllMemberCombinations(DeclarationContext declContext) {
    List<PropertyMember> allMembers = new ArrayList<PropertyMember>();
    for (PropertyType t : PropertyType.values()) {
      for (Scope s : Scope.values()) {
        for (AccessModifier a : AccessModifier.values()) {
          for (ValueType vt : ValueType.values()) {
            allMembers.add(new PropertyMember(a, s, t, vt, declContext));
          }
        }
      }
    }
    return allMembers;
  }

  private AccessModifier _accessMod;
  private Scope _scope;
  private PropertyType _propertyType;
  private ValueType _valueType;
  private DeclarationContext _context;

  private PropertyMember(AccessModifier accessMod, Scope scope, PropertyType propertyType, ValueType valueType, DeclarationContext context) {
    _accessMod = accessMod;
    _scope = scope;
    _propertyType = propertyType;
    _valueType = valueType;
    _context = context;
  }

  public AccessModifier getAccessMod() {
    return _accessMod;
  }

  public Scope getScope() {
    return _scope;
  }

  public PropertyType getPropertyType() {
    return _propertyType;
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
    if (_propertyType == PropertyType.Property) {
      sb.append("property get ");
    } else {
      sb.append("var ");
    }
    sb.append(memberName());
    if (_propertyType == PropertyType.Property) {
      if (_context.isInterface()) {
        sb.append("() ").append(" : ").append(_valueType.getName());
      } else {
        sb.append("() ").append(" : ").append(_valueType.getName()).append(" { return ").append(memberValue()).append(" }");
      }
    } else {
      sb.append(" : ").append(_valueType.getName()).append(" = ").append(memberValue());
    }
    return sb.toString();
  }

  public String generateJavaMemberDeclaration() {
    StringBuilder sb = new StringBuilder();
    sb.append(_accessMod.getJavaModifierName()).append(_scope.getScopeQualifier()).append(_valueType.getName()).append(" ");
    if (_propertyType == PropertyType.Property) {
      if (_context.isInterface()) {
        sb.append("get").append(memberName()).append("();");
      } else {
        sb.append("get").append(memberName()).append("() { return ").append(memberValue()).append("; }");
      }
    } else {
      sb.append(memberName()).append(" = ").append(memberValue()).append(";");
    }
    return sb.toString();
  }

  public String memberName() {
    StringBuilder sb = new StringBuilder();
    sb.append(_accessMod).append(_scope).append(_valueType).append(_propertyType).append(_context.memberSuffix());
    String result = sb.toString();
    if (_propertyType == PropertyType.Field) {
      result = Character.toLowerCase(result.charAt(0)) + result.substring(1);
    }
    return result;
  }

  @Override
  public String memberTypeName() {
    return _valueType.getName();
  }

  public String memberValue() {
    if (_valueType == ValueType.String) {
      return "\"" + _accessMod + "-" + _scope + "-" + _propertyType + "-" + _context.memberSuffix() + "\"";
    } else {
      return "" + (_accessMod.ordinal() + 1) + "" +  + (_scope.ordinal() + 1) + "" + (_propertyType.ordinal() + 1) + "" + _context.memberIntSuffix();
    }
  }

  @Override
  public boolean isMethod() {
    return _propertyType == PropertyType.Property;
  }
}
