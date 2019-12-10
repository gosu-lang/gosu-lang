/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.source.tree.Tree;
import gw.lang.parser.expressions.Variance;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.IAsmType;

import java.util.List;

public class AsmTypeVariableJavaClassTypeVariable extends AsmTypeJavaClassType implements IJavaClassTypeVariable {

  private Variance _variance;

  public AsmTypeVariableJavaClassTypeVariable( IAsmType typeVariable ) {
    super(typeVariable );
    _variance = Variance.DEFAULT;
  }

  @Override
  public IJavaClassType getConcreteType() {
    return getBounds()[0].getConcreteType();
  }

  @Override
  public String getName() {
    return getType().getName();
  }

  @Override
  public String getSimpleName() {
    return getType().getSimpleName();
  }

  @Override
  public IJavaClassType[] getBounds() {
    List<AsmType> typeParameters = getType().getTypeParameters();
    if( typeParameters.isEmpty() ) {
      return new IJavaClassType[] {JavaTypes.OBJECT().getBackingClassInfo()};
    }
    else {
      return new IJavaClassType[] {createType( typeParameters.get( 0 ) )};
    }
  }

  @Override
  public boolean isFunctionTypeVar() {
    return getType() instanceof AsmType && ((AsmType)getType()).isFunctionTypeVariable();
  }

  @Override
  public Variance getVariance()
  {
    return _variance;
  }
  @Override
  public void setVariance( Variance variance )
  {
    _variance = variance;
  }

  public String toString() {
    return getName();
  }

  @Override
  public Tree getTree()
  {
    return null;
  }

  @Override
  public IJavaClassInfo getEnclosingClass()
  {
    return null;
  }
}
