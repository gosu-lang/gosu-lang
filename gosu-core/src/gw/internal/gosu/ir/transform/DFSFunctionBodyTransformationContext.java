/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.BlockClass;

public class DFSFunctionBodyTransformationContext extends FunctionBodyTransformationContext {

  private DynamicFunctionSymbol _dfs;

  public DFSFunctionBodyTransformationContext(TopLevelTransformationContext context, boolean isStatic, DynamicFunctionSymbol dfs) {
    super(context, isStatic);
    _dfs = dfs;
  }

  public boolean isBlockInvoke()
  {
    return _dfs.getDisplayName().equals( BlockClass.INVOKE_METHOD_NAME ) && _context.compilingBlock();
  }

  public DynamicFunctionSymbol getCurrentDFS()
  {
    return _dfs;
  }
}
