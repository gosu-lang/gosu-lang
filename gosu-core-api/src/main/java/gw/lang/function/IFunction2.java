/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@FunctionalInterface
public interface IFunction2<R, P0, P1> {

  R invoke(P0 arg0, P1 arg1);

}
