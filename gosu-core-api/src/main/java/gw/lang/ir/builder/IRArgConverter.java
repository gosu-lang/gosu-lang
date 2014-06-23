/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.expression.IRCastExpression;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UnstableAPI
public class IRArgConverter {

  public static IRExpression castOrConvertIfNecessary( IRType expectedType, IRExpression root ) {
    IRType rootType = root.getType();
    if ( expectedType.isPrimitive() ) {
      if (!rootType.isPrimitive()) {
        IRType boxedType = getBoxedType( expectedType );
        if (!boxedType.equals(root.getType())) {
          root = cast(root, boxedType);
        }
        root = unbox( root );
      } else {
      }
    } else if (rootType.isPrimitive()) {
      // We know the other side isn't a primitive, so box it.
      root = box( root );
    } else {
      if (!expectedType.isAssignableFrom(rootType)) {
        root = cast(root, expectedType);
      }
    }

    return root;
  }

  public static IRType getBoxedType( Class returnType ) {
    return getBoxedType( getIRType( returnType ) );
  }

  public static IRType getBoxedType( IRType returnType ) {
    if ( returnType.isBoolean() ) {
      return getIRType( Boolean.class );
    } else if ( returnType.isByte() ) {
      return getIRType( Byte.class );
    } else if ( returnType.isChar() ) {
      return getIRType( Character.class );
    } else if ( returnType.isDouble() ) {
      return getIRType( Double.class );
    } else if ( returnType.isFloat() ) {
      return getIRType( Float.class );
    } else if ( returnType.isInt() ) {
      return getIRType( Integer.class );
    } else if ( returnType.isLong() ) {
      return getIRType( Long.class );
    } else if ( returnType.isShort() ) {
      return getIRType( Short.class );
    } else {
      throw new IllegalArgumentException( "Type " + returnType.getName() + " is not a primitive class" );
    }
  }

  public static IRMethodCallExpression unbox( IRExpression root ) {
    IRType rootType = root.getType();

    if (rootType.equals(getIRType(Boolean.class))) {
      return call(root, Boolean.class, "booleanValue");
    } else if (rootType.equals(getIRType(Byte.class))) {
      return call(root, Byte.class, "byteValue");
    } else if (rootType.equals(getIRType(Character.class))) {
      return call(root, Character.class, "charValue");
    } else if (rootType.equals(getIRType(Double.class))) {
      return call(root, Double.class, "doubleValue");
    } else if (rootType.equals(getIRType(Float.class))) {
      return call(root, Float.class, "floatValue");
    } else if (rootType.equals(getIRType(Integer.class))) {
      return call(root, Integer.class, "intValue");
    } else if (rootType.equals(getIRType(Long.class))) {
      return call(root, Long.class, "longValue");
    } else if (rootType.equals(getIRType(Short.class))) {
      return call(root, Short.class, "shortValue");
    }

    throw new IllegalArgumentException( "Type " + rootType.getName() + " is not a boxed type" );
  }

  public static IRMethodCallExpression box( IRExpression root ) {
    IRType rootType = root.getType();
    if ( rootType.isBoolean() ) {
      return call( null, Boolean.class, "valueOf", new Class[]{boolean.class}, root );
    } else if ( rootType.isByte() ) {
      return call( null, Byte.class, "valueOf", new Class[]{byte.class}, root );
    } else if ( rootType.isChar() ) {
      return call( null, IRArgConverter.class, "valueOf", new Class[]{char.class}, root );
    } else if ( rootType.isDouble() ) {
      return call( null, Double.class, "valueOf", new Class[]{double.class}, root );
    } else if ( rootType.isFloat() ) {
      return call( null, Float.class, "valueOf", new Class[]{float.class}, root );
    } else if ( rootType.isInt() ) {
      return call( null, Integer.class, "valueOf", new Class[]{int.class}, root );
    } else if ( rootType.isLong() ) {
      return call( null, Long.class, "valueOf", new Class[]{long.class}, root );
    } else if ( rootType.isShort() ) {
      return call( null, Short.class, "valueOf", new Class[]{short.class}, root );
    } else {
      throw new IllegalArgumentException( "Type " + rootType.getName() + " is not a primitive class" );
    }
  }

  // Necessary so that unicode chars that are in the negative integer range
  // don't cause an IndexOutOfBoundsException e.g., Character.valueOf( char )
  // chokes on negative chars
  public static Character valueOf( char c )
  {
    if( c >= 0 && c <= 127 )
    {
      return c;
    }
    //noinspection UnnecessaryBoxing
    return new Character( c );
  }

  public static IRCastExpression cast(IRExpression root, IRType type) {
    return new IRCastExpression( root, type );
  }

  private static IRMethodCallExpression call( IRExpression root, Class rootClass, String methodName ) {
    return call( root, rootClass, methodName, new Class[0]);
  }

  private static IRMethodCallExpression call( IRExpression root, Class rootClass, String methodName, Class[] argTypes, IRExpression... args) {
    try {
      Method method = rootClass.getMethod(methodName, argTypes);
      return new IRMethodCallExpression( methodName, getIRType( rootClass ), false, getIRType( method.getReturnType() ),
              getIRTypes( method.getParameterTypes() ), root, Arrays.asList(args));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static IRType getIRType( Class cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  private static List<IRType> getIRTypes( Class[] classes ) {
    List<IRType> results = new ArrayList<IRType>();
    for (Class cls : classes) {
      results.add( getIRType( cls ) );
    }
    return results;
  }

}
