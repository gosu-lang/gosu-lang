/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

/**
 */
public class AsmInnerClassType extends AsmType {
  private int _modifiers;
  AsmInnerClassType( String normalDotName, int access ) {
    super( normalDotName );
    _modifiers = access;
  }

  public int getModifiers() {
    return _modifiers;
  }
}
