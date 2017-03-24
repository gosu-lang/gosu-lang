/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

public abstract class Procedure6 extends AbstractBlock implements IProcedure6 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 6) {
      throw new IllegalArgumentException("You must pass 6 args to this block, but you passed" + args.length);
    } else {
      //noinspection unchecked
      invoke(args[0], args[1], args[2], args[3], args[4], args[5]);
      return null;
    }
  }

}
