/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class Procedure5 extends AbstractBlock implements IProcedure5 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 5) {
      throw new IllegalArgumentException("You must pass 5 args to this block, but you passed" + args.length);
    } else { 
      invoke(args[0], args[1], args[2], args[3], args[4]);
      return null;
    }
  }

}
