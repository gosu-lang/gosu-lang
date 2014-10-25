package gw.internal.gosu.parser;

import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INonLoadableType;

/**
 */
public class DelegateFunctionType extends FunctionType {
  public DelegateFunctionType( IMethodInfo mi ) {
    super( mi );
  }

  @Override
  public int hashCode() {
    int result = getDisplayName().hashCode();
    for( int i = 0; i < getParameterTypes().length; i++ ) {
      if( getParameterTypes()[i] instanceof INonLoadableType ) {
        result = 31 * result + getParameterTypes()[i].hashCode();
      }
      else {
        result = 31 * result + getParameterTypes()[i].getName().hashCode();
      }
    }

    return result;
  }

  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( !getClass().isInstance( o ) ) {
      return false;
    }

    final FunctionType funcType = (FunctionType)o;

    // Name
    if( !funcType.getDisplayName().equals( getDisplayName() ) &&
        !(o instanceof IBlockType) &&
        !(this instanceof IBlockType) ) {
      return false;
    }

//    // Enclosing Type
//    if( !areEnclosingTypesEqual( funcType ) ) {
//      return false;
//    }

    // Parameter Types
    if( funcType.getParameterTypes().length != getParameterTypes().length ) {
      return false;
    }
    for( int i = 0; i < getParameterTypes().length; i++ ) {
      if( !areSameTypes( getParameterTypes()[i], funcType.getParameterTypes()[i] ) ) {
        return false;
      }
    }

//    // Return Type
//    return areSameTypes( getReturnType(), funcType.getReturnType() );
    return true;
  }

}
