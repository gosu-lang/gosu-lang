/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.function;

@FunctionalInterface
public interface IFunction1<R,P0> {

  R invoke(P0 arg0);

}
