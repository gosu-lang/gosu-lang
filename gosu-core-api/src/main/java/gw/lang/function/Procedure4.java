/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

public abstract class Procedure4 extends AbstractBlock implements IProcedure4 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 4) {
      throw new IllegalArgumentException("You must pass 4 args to this block, but you passed" + args.length);
    } else {
      //noinspection unchecked
      invoke(args[0], args[1], args[2], args[3]);
      return null;
    }
  }

}
