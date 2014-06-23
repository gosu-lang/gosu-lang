/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.HashMap;
import java.util.Map;

public class ScriptabilityModifierTypes
{
  private static Map<String, StandardVisibilityModifierType> _types = new HashMap<String, StandardVisibilityModifierType>();

  private static final String STR_SCRIPTABLE = "scriptable";
  private static final String STR_HIDDEN = "hidden";
  private static final String STR_DEPRECATED = "deprecated";

  public static final StandardVisibilityModifierType SCRIPTABLE = new StandardVisibilityModifierType( STR_SCRIPTABLE, true );
  public static final StandardVisibilityModifierType HIDDEN = new StandardVisibilityModifierType( STR_HIDDEN, true );
  public static final StandardVisibilityModifierType DEPRECATED = new StandardVisibilityModifierType( STR_DEPRECATED, false );

  public static final StandardVisibilityModifierType EXTERNAL = new StandardVisibilityModifierType( "external", true );
  public static final StandardVisibilityModifierType RULES = new StandardVisibilityModifierType( "rules", true );
  public static final StandardVisibilityModifierType UI = new StandardVisibilityModifierType( "ui", true );
  public static final StandardVisibilityModifierType WORKFLOW = new StandardVisibilityModifierType( "wf", true );
  public static final IVisibilityModifierType WEBSERVICE = new StandardVisibilityModifierType( "webservice", true );

  public static StandardVisibilityModifierType getType( String name )
  {
    return _types.get( name );
  }

  public static void putType( String strName, StandardVisibilityModifierType standardVisibilityModifierType )
  {
    _types.put( strName, standardVisibilityModifierType );
  }
}
