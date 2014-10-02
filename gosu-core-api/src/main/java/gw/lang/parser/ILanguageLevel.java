/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.config.CommonServices;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ICompilableType;

public interface ILanguageLevel
{
  boolean isStandard();

  static class Util {
    private static Boolean g_standardGosu = null;
    private static Boolean g_dynamicType = null;
    public static void reset() {
      g_standardGosu = null;
    }

    public static boolean STANDARD_GOSU()
    {
      return g_standardGosu == null
             ? g_standardGosu = CommonServices.getEntityAccess().getLanguageLevel().isStandard()
             : g_standardGosu;
    }

    public static boolean DYNAMICE_TYPE()
    {
      return true;
//             g_dynamicType == null
//             ? g_dynamicType = STANDARD_GOSU() || System.getProperty( "gosu.dynamic", null ) != null
//             : g_standardGosu;
    }
  }


  //## todo: These all should be implied by the answer to isStandard() above... better, just stop supporting this crap

  boolean allowAllImplicitCoercions();

  boolean isUnreachableCodeDetectionOn();

  boolean isWarnOnImplicitCoercionsOn();

  boolean areUsesStatementsAllowedInStatementLists(ICompilableType gosuClass);

  boolean shouldAddWarning( IType type, IParseIssue warning );

  ITypeUsesMap getDefaultTypeUses();
  boolean supportsNakedCatchStatements();
}
