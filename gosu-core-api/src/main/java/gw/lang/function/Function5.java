/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class Function5 extends AbstractBlock implements IFunction5 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 5) {
      throw new IllegalArgumentException("You must pass 5 args to this block, but you passed" + args.length);
    } else { 
      return invoke(args[0], args[1], args[2], args[3], args[4]);
    }
  }

}
