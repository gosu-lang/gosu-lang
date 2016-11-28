/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.statements.ClassStatement;

import gw.lang.parser.statements.IClassDeclaration;

public class ClassDeclaration extends Expression implements IClassDeclaration
{
  private final String _className;
  private IGosuClassInternal _gsClass;

  public ClassDeclaration( String className )
  {
    _className = className;
  }

  public String getClassName()
  {
    return _className;
  }

  @Override
  public String getName()
  {
    return getClassName();
  }

  public Object evaluate()
  {
    return null; // Nothing to do
  }

  @Override
  public String toString()
  {
    return getClassName();
  }

  @Override
  public int getNameOffset( String identifierName )
  {
    return getLocation().getOffset();
  }
  @Override
  public void setNameOffset( int iOffset, String identifierName )
  {
    throw new UnsupportedOperationException();
  }

  public String[] getDeclarations() {
    IGosuClassInternal gsClass = getGSClass();
    return new String[] {gsClass.getRelativeName()};
  }

  public boolean declares( String identifierName )
  {
    IGosuClassInternal gsClass = getGSClass();
    return identifierName != null &&
           (identifierName.equals( gsClass.getName() ) ||
            identifierName.equals( gsClass.getRelativeName() ));
  }

  public IGosuClassInternal getGSClass() {
    if (_gsClass == null) {
      if (!(getParent() instanceof ClassStatement)) {
        throw new RuntimeException("Class declaration's parent must be ClassStatement but was " + getParent());
      }
      _gsClass = ((ClassStatement) getParent()).getGosuClass();
    }
    return _gsClass;
  }

}