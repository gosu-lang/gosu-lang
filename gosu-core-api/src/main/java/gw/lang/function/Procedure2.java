/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class Procedure2 extends AbstractBlock implements IProcedure2 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 2) {
      throw new IllegalArgumentException("You must pass 2 args to this block, but you passed " + args.length);
    } else { 
      invoke(args[0], args[1]);
      return null;
    }
  }

}
