/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class Procedure3 extends AbstractBlock implements IProcedure3 {

  public Object invokeWithArgs(Object[] args) {
    if(args.length != 3) {
      throw new IllegalArgumentException("You must pass 3 args to this block, but you passed" + args.length);
    } else { 
      invoke(args[0], args[1], args[2]);
      return null;
    }
  }

}
