/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

public interface ISequenceable<E extends ISequenceable<E, S, U>, S, U>
{
  E nextInSequence( S step, U unit );
  E nextNthInSequence( S step, U unit, int iIndex );
  E previousInSequence( S step, U unit );
  E previousNthInSequence( S step, U unit, int iIndex );
}
