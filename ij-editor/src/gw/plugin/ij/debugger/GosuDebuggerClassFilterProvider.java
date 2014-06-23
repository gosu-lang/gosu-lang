/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger;

import com.google.common.collect.ImmutableList;
import com.intellij.ui.classFilter.ClassFilter;
import com.intellij.ui.classFilter.DebuggerClassFilterProvider;

import java.util.List;


public class GosuDebuggerClassFilterProvider /*extends StackFrameFilter*/ implements DebuggerClassFilterProvider {
  private static final List<ClassFilter> FILTERS = ImmutableList.of(
      new ClassFilter("gw.lang.*"),
      new ClassFilter("gw.config.*"),
      new ClassFilter("gw.util.*"),
      new ClassFilter("gw.internal.gosu.*"));

  public List<ClassFilter> getFilters() {
    return FILTERS;
  }

  public boolean isAuxiliaryFrame(String className, String methodName) {
//    if( className.equals( "gw.internal.whatever" ) ||
//        className.equals( "gw.internal.whichever" ) )
//    {
//      return false;
//    }
//
//    for( ClassFilter filter : FILTERS )
//    {
//      final String pattern = filter.getPattern();
//      if( className.startsWith( pattern.substring( 0, pattern.length() - 1 ) ) )
//      {
//        return true;
//      }
//    }
    return false;
  }
}
