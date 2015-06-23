package gw.lang.parser;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.IAsmType;

/**
*/
public class AsmTypeVarMatcher implements TypeVarToTypeMap.ITypeVarMatcher<IAsmType>
{
  private static final AsmTypeVarMatcher INSTANCE = new AsmTypeVarMatcher();

  public static AsmTypeVarMatcher instance() {
    return INSTANCE;
  }

  private AsmTypeVarMatcher() {
  }

  @Override
  public boolean matches( IAsmType thisOne, ITypeVariableType thatOne )
  {
    if( thisOne.getName().equals( thatOne.getName() ) || thisOne.getName().equals( thatOne.getRelativeName() ) )
    {
      // The names must match and they also must belong to the same owner
      boolean bThisOwnerIsFunction = thisOne instanceof AsmType && ((AsmType)thisOne).isFunctionTypeVariable();
      boolean bThatOwnerIsFunction = thatOne.getTypeVarDef().getEnclosingType() instanceof IFunctionType;
      return (bThatOwnerIsFunction && bThisOwnerIsFunction) ||
             (!bThatOwnerIsFunction && !bThisOwnerIsFunction);
    }
    return false;
  }
}
