/*
 * Copyright 2026 Guidewire Software, Inc.
 */

package gw.internal.gosu.gosuc.argumentcast;

/**
 * Java fixture of {@code SourceFileOrderArgumentCastEndToEndIT}: a public class whose superclass is not public,
 * and a public method that takes the superclass.
 */
public class VisibleAncestor extends HiddenAncestor
{
  public static String nameOf( HiddenAncestor object )
  {
    return object.getClass().getSimpleName();
  }
}
