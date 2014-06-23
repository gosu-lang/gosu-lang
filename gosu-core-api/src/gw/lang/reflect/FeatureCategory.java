/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class FeatureCategory
{
  private String _name;
  private int _order;
  private boolean _important;

  public FeatureCategory( String name, int order, boolean important )
  {
    _important = important;
    _name = name;
    _order = order;
  }

  public boolean isImportant()
  {
    return _important;
  }

  public String getName()
  {
    return _name;
  }

  public int getOrder()
  {
    return _order;
  }
}