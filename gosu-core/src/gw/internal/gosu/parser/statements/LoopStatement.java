/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.config.CommonServices;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.expressions.Literal;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.statements.ILoopStatement;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 */
public abstract class LoopStatement extends Statement implements ILoopStatement
{
  public static boolean isIteratorType( IType typeIn )
  {
    return typeIn.isArray() ||
           typeIn instanceof ErrorType ||
           JavaTypes.ITERABLE().isAssignableFrom( typeIn ) ||
           StandardCoercionManager.isStructurallyAssignable_Laxed( JavaTypes.ITERABLE(), typeIn ) ||
           JavaTypes.ITERATOR().isAssignableFrom( typeIn ) ||
           typeIn == GosuParserTypes.STRING_TYPE() ||
           (typeIn instanceof IPlaceholder && ((IPlaceholder)typeIn).isPlaceholder());
  }

  public static IType getArrayComponentType( IType typeIn )
  {
    IType returnType;
    if( typeIn.isArray() )
    {
      returnType = typeIn.getComponentType();
    }
    else if( typeIn == GosuParserTypes.STRING_TYPE() )
    {
      returnType = GosuParserTypes.STRING_TYPE();
    }
    else
    {
      if( BeanAccess.isNumericType( typeIn ) )
      {
        returnType = JavaTypes.pINT();
      }
      else if( typeIn == JavaTypes.INTEGER_INTERVAL() )
      {
        returnType = JavaTypes.pINT();
      }
      else if( typeIn == JavaTypes.LONG_INTERVAL() )
      {
        returnType = JavaTypes.pLONG();
      }
      else if( typeIn instanceof IPlaceholder && ((IPlaceholder)typeIn).isPlaceholder() )
      {
        returnType = typeIn.getComponentType();
      }
      else
      {
        if( typeIn.isGenericType() && !typeIn.isParameterizedType() )
        {
          typeIn = TypeLord.getDefaultParameterizedType( typeIn );
        }
        IType parameterized = TypeLord.findParameterizedType( typeIn, JavaTypes.ITERABLE() );
        if( parameterized != null && parameterized.isParameterizedType() )
        {
          returnType = parameterized.getTypeParameters()[0];
        }
        else
        {
          parameterized = TypeLord.findParameterizedType( typeIn, JavaTypes.ITERATOR() );
          if( parameterized != null && parameterized.isParameterizedType() )
          {
            returnType = parameterized.getTypeParameters()[0];
          }
          else
          {
            IMethodInfo iteratorMethod = typeIn.getTypeInfo().getMethod( "iterator" );
            if( iteratorMethod != null && JavaTypes.ITERATOR().isAssignableFrom( iteratorMethod.getReturnType() ) )
            {
              // Structural Iterable match
              IType retType = iteratorMethod.getReturnType();
              returnType = retType.isParameterizedType() ? retType.getTypeParameters()[0] : JavaTypes.OBJECT();
            }
            else
            {
              returnType = JavaTypes.OBJECT();
            }
          }
        }
      }
    }

    return returnType;
  }

  /**
   * A helper method for creating Iterators for use with 'exists' and 'foreach'
   * elements.  Primarily for use with generated Java code (not necessary for
   * direct interpretation).
   */
  public static Iterator makeIterator( Object obj, IType typeHint )
  {
    if( obj == null )
    {
      return null;
    }

    if( typeHint.isArray() &&
        (obj.getClass().isArray() || TypeSystem.getFromObject( obj ).isArray()) )
    {
      return new ArrayIterator( obj, typeHint );
    }

    if( obj instanceof Iterable )
    {
      return ((Iterable)obj).iterator();
    }

    if( obj instanceof Iterator )
    {
      return(Iterator)obj;
    }

    // Treat a string as a list of characters
    if( obj instanceof String )
    {
      return new StringIterator( (String)obj );
    }

    if( obj instanceof Number )
    {
      return new NumberIterator( (Number)obj );
    }

    // Oh well. Convert to a List of length one and iterate that single element.
    return Collections.nCopies( 1, obj ).iterator();
  }


  /**
   * Return the length of the specified Array or Collection.
   */
  public static int getArrayLength( Object obj )
  {
    if( obj == null )
    {
      return 0;
    }

    IType type = TypeLoaderAccess.instance().getIntrinsicTypeFromObject( obj );
    if( type.isArray() )
    {
      return type.getArrayLength( obj );
    }

    if( obj instanceof CharSequence )
    {
      return ((CharSequence)obj).length();
    }

    if( obj instanceof Collection )
    {
      return ((Collection)obj).size();
    }

    if( obj instanceof Iterable )
    {
      int iCount = 0;
      //noinspection UnusedDeclaration
      for( Object o : (Iterable)obj )
      {
        iCount++;
      }
      return iCount;
    }

    return 0;
  }

  public boolean isConditionLiteralTrue() {
    return getExpression() instanceof Literal &&
           getExpression().isCompileTimeConstant() &&
           CommonServices.getCoercionManager().makePrimitiveBooleanFrom( getExpression().evaluate() );
  }

  static class ArrayIterator implements Iterator
  {
    private int _iCsr;
    private Object _array;
    private IType _arrayType;

    ArrayIterator( Object array, IType arrayType )
    {
      _iCsr = 0;
      _arrayType = arrayType;
      _array = CommonServices.getCoercionManager().convertValue(array, _arrayType);
    }

    public boolean hasNext()
    {
      return _iCsr < _arrayType.getArrayLength( _array );
    }

    public Object next()
    {
      if( !hasNext() )
      {
        throw new NoSuchElementException( "No element at index [" + _iCsr + "] for the array." );
      }

      return _arrayType.getArrayComponent( _array, _iCsr++ );
    }

    public void remove()
    {
      throw new UnsupportedOperationException( "ArrayIterator does not support remove()." );
    }
  }
}
