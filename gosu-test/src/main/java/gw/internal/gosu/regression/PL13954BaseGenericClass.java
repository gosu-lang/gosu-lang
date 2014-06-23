/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

import gw.lang.PublishInGosu;

/**
 */
@PublishInGosu
public abstract class PL13954BaseGenericClass<T> implements PL13954GenericInterface<T>
{
    @Override public void edgeTraversed(T source, T target, Object weight) {}
}
