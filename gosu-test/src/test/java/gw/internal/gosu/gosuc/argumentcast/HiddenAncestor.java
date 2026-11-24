/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc.argumentcast;

/**
 * A class that only the classes of this package can refer to. A class of another package can have it as an
 * ancestor, through {@link VisibleAncestor}, but a cast to it in that class fails with an
 * {@code IllegalAccessError}.
 */
abstract class HiddenAncestor
{
}
