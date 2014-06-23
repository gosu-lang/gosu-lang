/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

import gw.lang.PublishInGosu;

/**
 */
@PublishInGosu
public interface PL13954GenericInterface<T>
{
    public void visit(T node);
    public void edgeTraversed(T source, T target, Object weight);
}
