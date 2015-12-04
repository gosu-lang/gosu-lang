/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.*;
import gw.lang.reflect.IType;
import gw.lang.reflect.IModifierInfo;
import gw.lang.reflect.gs.IGosuClass;

import java.util.Collections;
import java.util.List;

/**
 */
public class MemberFieldSymbol implements ISymbol
{
  private int _index;
  private String _name;

  public MemberFieldSymbol( int index, String name )
  {
    _index = index;
    _name = name;
  }

  public int getIndex()
  {
    return _index;
  }
  public void setIndex( int iIndex )
  {
    _index = iIndex;
  }

  public boolean canBeCaptured() {
    return false;
  }

  public ICapturedSymbol makeCapturedSymbol( String strName, ISymbolTable symbolTable, IScope scope) {
    throw new UnsupportedOperationException();
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

  public String getName()
  {
    return _name;
  }

  public String getDisplayName()
  {
    throw new UnsupportedOperationException();
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
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public IType getType()
  {
    throw new UnsupportedOperationException();
  }

  public void setType( IType type )
  {
  }

  public Object getValue()
  {
    throw new UnsupportedOperationException();
  }
  public void setValue( Object value )
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public IExpression getDefaultValueExpression()
  {
    throw new UnsupportedOperationException();
  }
  @Override
  public void setDefaultValueExpression( IExpression defaultValue )
  {
    throw new UnsupportedOperationException();
  }

  public void setDynamicSymbolTable( ISymbolTable symTable )
  {
    throw new UnsupportedOperationException();
  }

  public boolean hasDynamicSymbolTable()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public ISymbolTable getDynamicSymbolTable() 
  {
    throw new UnsupportedOperationException();
  }

  public ISymbol getLightWeightReference()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isWritable()
  {
    throw new UnsupportedOperationException();
  }

  public void setValueIsBoxed( boolean b )
  {
    throw new IllegalStateException( "Cannot capture symbols of this type in blocks" );
  }

  @Override
  public boolean isValueBoxed()
  {
    return false;
  }

  @Override
  public boolean isLocal() {
    return false;
  }

  @Override
  public IModifierInfo getModifierInfo() {
    return null;
  }
  
  public IReducedSymbol createReducedSymbol() {
    return this;
  }
  
}