/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.java;

public interface Mapper<I, O> {
    O map( I elt );
}
