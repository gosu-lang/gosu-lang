package gw.lang.parser;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.ITypeVariableType;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

/**
*/
public class RawTypeVarMatcher implements TypeVarToTypeMap.ITypeVarMatcher<TypeVariable>
{
  private static final RawTypeVarMatcher INSTANCE = new RawTypeVarMatcher();
  
  public static RawTypeVarMatcher instance() {
    return INSTANCE;
  }
  
  private RawTypeVarMatcher() {
  }
  
  @Override
  public boolean matches( TypeVariable thisOne, ITypeVariableType thatOne )
  {
    if( thisOne.getName().equals( thatOne.getName() ) || thisOne.getName().equals( thatOne.getRelativeName() ) )
    {
      // The names must match and they also must belong to the same owner
      boolean bThisOwnerIsFunction = thisOne.getGenericDeclaration() instanceof Method;
      boolean bThatOwnerIsFunction = thatOne.getTypeVarDef().getEnclosingType() instanceof IFunctionType;
      return (bThatOwnerIsFunction && bThisOwnerIsFunction) ||
             (!bThatOwnerIsFunction && !bThisOwnerIsFunction);
    }
    return false;
  }
}
