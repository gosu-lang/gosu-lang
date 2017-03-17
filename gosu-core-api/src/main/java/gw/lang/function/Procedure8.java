/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class Procedure8 extends AbstractBlock implements IProcedure8 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 8) {
      throw new IllegalArgumentException("You must pass 8 args to this block, but you passed" + args.length);
    } else { 
      invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
      return null;
    }
  }

}
