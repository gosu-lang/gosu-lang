/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

public abstract class Procedure16 extends AbstractBlock implements IProcedure16 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 16) {
      throw new IllegalArgumentException("You must pass 16 args to this block, but you passed" + args.length);
    } else {
      //noinspection unchecked
      invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15]);
      return null;
    }
  }

}
