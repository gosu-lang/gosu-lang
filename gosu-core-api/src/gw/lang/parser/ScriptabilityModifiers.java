/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.ScriptabilityModifierTypes;
import gw.lang.reflect.ScriptabilityModifier;

public class ScriptabilityModifiers {

  // =============================================== GENERAL

  public static final ScriptabilityModifier SCRIPTABLE =
          new ScriptabilityModifier( ScriptabilityModifierTypes.SCRIPTABLE);

  public static final ScriptabilityModifier HIDDEN =
          new ScriptabilityModifier(ScriptabilityModifierTypes.HIDDEN);

  public static final ScriptabilityModifier DEPRECATED =
          new ScriptabilityModifier(ScriptabilityModifierTypes.DEPRECATED);

  public static final ScriptabilityModifier SCRIPTABLE_DEPRECATED =
          new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
                  new ScriptabilityModifier[]{DEPRECATED});

  // ========================================= EXTERNAL

  private static final ScriptabilityModifier EXTERNAL =
     new ScriptabilityModifier(ScriptabilityModifierTypes.EXTERNAL);

  public static final ScriptabilityModifier SCRIPTABLE_EXTERNAL =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{EXTERNAL});

  public static final ScriptabilityModifier SCRIPTABLE_EXTERNAL_DEPRECATED =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{EXTERNAL, DEPRECATED});

  // ========================================= UI

  private static final ScriptabilityModifier UI =
     new ScriptabilityModifier(ScriptabilityModifierTypes.UI);

  public static final ScriptabilityModifier SCRIPTABLE_UI =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{UI});

  public static final ScriptabilityModifier SCRIPTABLE_UI_DEPRECATED =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{UI, DEPRECATED});

  // ========================================= RULES

  private static final ScriptabilityModifier RULES =
     new ScriptabilityModifier(ScriptabilityModifierTypes.RULES);

  public static final ScriptabilityModifier SCRIPTABLE_RULES =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{RULES});

  public static final ScriptabilityModifier SCRIPTABLE_RULES_DEPRECATED =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{RULES, DEPRECATED});

  // ========================================= WORKFLOW

  private static final ScriptabilityModifier WORKFLOW =
     new ScriptabilityModifier(ScriptabilityModifierTypes.WORKFLOW);

  public static final ScriptabilityModifier SCRIPTABLE_WORKFLOW =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{WORKFLOW});

  public static final ScriptabilityModifier SCRIPTABLE_WORKFLOW_DEPRECATED =
     new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
        new ScriptabilityModifier[]{WORKFLOW, DEPRECATED});


  private static final ScriptabilityModifier WEBSERVICE =
          new ScriptabilityModifier(ScriptabilityModifierTypes.WEBSERVICE);

  public static final ScriptabilityModifier SCRIPTABLE_WEBSERVICE =
          new ScriptabilityModifier(ScriptabilityModifierTypes.SCRIPTABLE,
                  new ScriptabilityModifier[]{WEBSERVICE});

}
