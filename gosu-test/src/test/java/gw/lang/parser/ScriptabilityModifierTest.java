/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.ScriptabilityModifierTypes;
import gw.lang.reflect.StandardVisibilityModifierType;
import gw.lang.reflect.ScriptabilityModifier;
import gw.test.TestClass;

/**
 */
public class ScriptabilityModifierTest extends TestClass {

  public void testSatisfiesConstraint() {
    assertTrue( ScriptabilityModifiers.SCRIPTABLE.satisfiesConstraint( ScriptabilityModifiers.SCRIPTABLE ) );
    assertTrue( ScriptabilityModifiers.SCRIPTABLE_DEPRECATED.satisfiesConstraint( ScriptabilityModifiers.SCRIPTABLE ) );
    assertFalse( ScriptabilityModifiers.DEPRECATED.satisfiesConstraint( ScriptabilityModifiers.SCRIPTABLE ) );

    ScriptabilityModifier test_type =
      new ScriptabilityModifier( new StandardVisibilityModifierType( "test", true ) );

    ScriptabilityModifier test_type2 =
      new ScriptabilityModifier( new StandardVisibilityModifierType( "test2", true ) );

    ScriptabilityModifier scriptable_test =
      new ScriptabilityModifier( ScriptabilityModifierTypes.SCRIPTABLE,
                                        new ScriptabilityModifier[] {test_type} );
    assertTrue( scriptable_test.satisfiesConstraint( scriptable_test ) );
    assertTrue( scriptable_test.satisfiesConstraint( ScriptabilityModifiers.SCRIPTABLE ) );

    ScriptabilityModifier scriptable_test2_deprecated =
      new ScriptabilityModifier( ScriptabilityModifierTypes.SCRIPTABLE,
                                        new ScriptabilityModifier[] {test_type2, ScriptabilityModifiers.DEPRECATED} );
    assertTrue( scriptable_test.satisfiesConstraint( ScriptabilityModifiers.SCRIPTABLE_DEPRECATED ) );
    assertTrue( scriptable_test2_deprecated.satisfiesConstraint( scriptable_test2_deprecated ) );
    assertFalse( scriptable_test.satisfiesConstraint( scriptable_test2_deprecated ) );
  }
}