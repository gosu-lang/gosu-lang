/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

public enum AccessModifier {
  Public("public "),
  Internal("internal ") {
    @Override
    public String getJavaModifierName() {
      return "";
    }},
  Protected("protected "), Private("private ");

  private String _modifierName;
  private AccessModifier(String modifierName) {
    _modifierName = modifierName;
  }

  public String getModifierName() {
    return _modifierName;
  }

  public String getJavaModifierName() {
    return _modifierName;
  }
}