/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class Function3 extends AbstractBlock implements IFunction3 { 

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 3) {
      throw new IllegalArgumentException("You must pass 3 args to this block, but you passed" + args.length);
    } else { 
      return invoke(args[0], args[1], args[2]);
    }
  }

}
