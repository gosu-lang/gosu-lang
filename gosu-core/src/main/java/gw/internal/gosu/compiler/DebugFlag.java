/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import java.util.EnumSet;

public enum DebugFlag
{
  /** True = echo readable bytecodes for debugging */
  TRACE,
  /** True = checks that ASM visitXxx() methods are being used properly */
  ASM_CHECKER,
  /** True = turn on verification of the compiled class bytes for debugging */
  VERIFY;

//  private static EnumSet<DebugFlag> g_setting = EnumSet.allOf( DebugFlag.class );
  private static EnumSet<DebugFlag> g_setting = EnumSet.noneOf( DebugFlag.class );
//  private static EnumSet<DebugFlag> g_setting = EnumSet.of( VERIFY );
  public static EnumSet<DebugFlag> getDebugFlags()
  {
    return g_setting;
  }
  public static void setDebugFlagsOn()
  {
    g_setting = EnumSet.allOf( DebugFlag.class );
  }
  public static void setDebugFlagsOff()
  {
    g_setting = EnumSet.noneOf( DebugFlag.class );
  }
  public static boolean isDebugFlagsOn()
  {
    return g_setting.contains( VERIFY );
  }
}
