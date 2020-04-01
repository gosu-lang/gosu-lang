/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.ParseTree;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.expressions.ClassDeclaration;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import java.util.List;

/**
 */
public final class ClassStatement extends Statement implements IClassStatement
{
  private IGosuClassInternal _gsClass;
  private ClassFileStatement _cfs;
  private ClassDeclaration _classDeclaration;
  private int _afterHeaderOffset;

  public ClassStatement( IGosuClassInternal gsClass )
  {
    _gsClass = gsClass;
    if( _gsClass != null && _gsClass.getEnclosingType() == null )
    {
      _cfs = new ClassFileStatement();
    }
  }

  public ClassFileStatement getClassFileStatement()
  {
    //## todo: get outer-most class file stmt?
    return _cfs;
  }

  public Object execute()
  {
    // No-Op
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return null;
  }

  @Override
  public boolean isNoOp()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return _gsClass.getClassType().keyword() + " " + _gsClass.getName() + "{ ... }";
  }

  public IGosuClassInternal getGosuClass()
  {
    return _gsClass;
  }

  @Override
  public void clearParseTreeInformation()
  {
    TypeSystem.lock();
    try
    {
      super.clearParseTreeInformation();
      if( _cfs != null )
      {
        _cfs.setLocation( null );
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private IGosuClassInternal getEnclosingClass()
  {
    IGosuClassInternal clazz = _gsClass;
    while( clazz.getEnclosingType() != null )
    {
      clazz = (IGosuClassInternal)clazz.getEnclosingType();
    }
    return clazz;
  }

  private IFeatureInfo getFeatureInfoIfAnyThatEnclosesItselfAndItsChildren()
  {
    return getGosuClass().getTypeInfo();
  }

  public ClassDeclaration getClassDeclaration()
  {
    if( _classDeclaration == null && _gsClass instanceof IGosuProgram )
    {
      _classDeclaration = new ClassDeclaration( _gsClass.getName() );
      ParseTree location = new ParseTree( _classDeclaration, 0, 0, getLocation().getScriptPartId() );
      location.setParent( getLocation() );
      _classDeclaration.setLocation( location );
      _classDeclaration.setParent( this );
    }
    return _classDeclaration;
  }

  public void setClassDeclaration( ClassDeclaration classDeclaration )
  {
    _classDeclaration = classDeclaration;
  }

  @Override
  public List<IGosuAnnotation> getAnnotations()
  {
    return getGosuClass().getModifierInfo().getAnnotations();
  }

  public int getAfterHeaderOffset()
  {
    return _afterHeaderOffset;
  }
  public void setAfterHeaderOffset( int mark )
  {
    _afterHeaderOffset = mark;
  }
}
