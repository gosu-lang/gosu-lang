/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;

import java.util.List;

@UnstableAPI
public class IRSymbol {
  private String _name;
  private IRType _type;
  private boolean _temp;
  private List<IRAnnotation> _annotations;

  public IRSymbol(String name, IRType type, boolean temp) {
    _name = name;
    _type = type;
    _temp = temp;
  }

  public String getName() {
    return _name;
  }

  public IRType getType() {
    return _type;
  }
  public void setType( IRType type ) {
    _type = type;
  }

  public boolean isTemp() {
    return _temp;
  }

  public void setAnnotations( List<IRAnnotation> irAnnotations )
  {
    _annotations = irAnnotations;
  }
  public List<IRAnnotation> getAnnotations()
  {
    return _annotations;
  }

  @Override
  public String toString()
  {
    return _name + " : " + _type;
  }
}
