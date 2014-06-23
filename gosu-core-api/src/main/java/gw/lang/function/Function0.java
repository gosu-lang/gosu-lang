/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class Function0 extends AbstractBlock implements IFunction0 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 0) {
      throw new IllegalArgumentException("You must pass 0 args to this block, but you passed" + args.length);
    } else { 
      return invoke();
    }
  }

}
