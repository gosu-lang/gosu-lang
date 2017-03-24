/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

public abstract class Procedure13 extends AbstractBlock implements IProcedure13 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 13) {
      throw new IllegalArgumentException("You must pass 13 args to this block, but you passed" + args.length);
    } else {
      //noinspection unchecked
      invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12]);
      return null;
    }
  }

}
