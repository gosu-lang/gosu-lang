package gw.lang.parser;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.java.IJavaClassTypeVariable;

/**
*/
public class JavaClassTypeVarMatcher implements TypeVarToTypeMap.ITypeVarMatcher<IJavaClassTypeVariable>
{
  private static final JavaClassTypeVarMatcher INSTANCE = new JavaClassTypeVarMatcher();
  
  public static JavaClassTypeVarMatcher instance() {
    return INSTANCE;
  }
  
  private JavaClassTypeVarMatcher() {
  }

  @Override
  public boolean matches( IJavaClassTypeVariable thisOne, ITypeVariableType thatOne )
  {
    if( thisOne.getName().equals( thatOne.getName() ) || thisOne.getName().equals( thatOne.getRelativeName() ) )
    {
      // The names must match and they also must belong to the same owner
      boolean bThisOwnerIsFunction = thisOne.isFunctionTypeVar();
      boolean bThatOwnerIsFunction = thatOne.getTypeVarDef().getEnclosingType() instanceof IFunctionType;
      return (bThatOwnerIsFunction && bThisOwnerIsFunction) ||
             (!bThatOwnerIsFunction && !bThisOwnerIsFunction);
    }
    return false;
  }
}
