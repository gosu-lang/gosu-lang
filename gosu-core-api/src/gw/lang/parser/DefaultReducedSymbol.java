/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.internal.gosu.parser.IGosuAnnotation;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

import java.util.Collections;
import java.util.List;

/**
 */
public class DefaultReducedSymbol implements IReducedSymbol
{
  private String _name;
  private IType _type;

  public DefaultReducedSymbol( String name, IType type ) {
    _type = type;
    _name = name;
  }

  @Override
  public boolean isStatic() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public int getModifiers() {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public List<IGosuAnnotation> getAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getFullDescription() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isPrivate() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isInternal() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isProtected() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isPublic() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isAbstract() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isFinal() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public IType getType() {
    return _type;
  }

  @Override
  public IScriptPartId getScriptPart() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public IGosuClass getGosuClass() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean hasTypeVariables() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Class getSymbolClass() {
    return getClass();
  }

  @Override
  public GlobalScope getScope() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isValueBoxed() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public int getIndex() {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public IExpression getDefaultValueExpression() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void setType(IType type) {
    _type = type;
  }
  
  public IReducedSymbol createReducedSymbol() {
    return this;
  }
  
}
