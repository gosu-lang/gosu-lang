/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.config.CommonServices;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.internal.gosu.parser.expressions.ArrayAccess;
import gw.internal.gosu.parser.statements.ArrayAssignmentStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.parser.EvaluationException;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

import gw.util.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public class ArrayAssignmentStatementTransformer extends AbstractStatementTransformer<ArrayAssignmentStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, ArrayAssignmentStatement stmt )
  {
    ArrayAssignmentStatementTransformer gen = new ArrayAssignmentStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private ArrayAssignmentStatementTransformer( TopLevelTransformationContext cc, ArrayAssignmentStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    ArrayAccess arrayAccess = _stmt().getArrayAccessExpression();

    IRExpression originalRoot = ExpressionTransformer.compile( arrayAccess.getRootExpression(), _cc() );

    IRSymbol tempRoot = null;
    IRExpression root;
    boolean needsAutoinsert = needsAutoinsert( arrayAccess );
    if( needsAutoinsert || _stmt().isCompoundStatement() )
    {
      tempRoot = _cc().makeAndIndexTempSymbol( originalRoot.getType() );
      root = identifier( tempRoot );
      if(_stmt().isCompoundStatement())
      {
        ExpressionTransformer.addTempSymbolForCompoundAssignment(arrayAccess.getRootExpression(), tempRoot );
      }
    } else {
      root = originalRoot;
    }

    IRExpression originalIndex = ExpressionTransformer.compile( arrayAccess.getMemberExpression(), _cc() );
    IRSymbol tempIndex = null;
    IRExpression index;
    if( needsAutoinsert || _stmt().isCompoundStatement() )
    {
      tempIndex = _cc().makeAndIndexTempSymbol( originalIndex.getType() );
      index = identifier( tempIndex );
      if( _stmt().isCompoundStatement() )
      {
        ExpressionTransformer.addTempSymbolForCompoundAssignment( arrayAccess.getMemberExpression(), tempIndex );
      }
    }
    else
    {
      index = originalIndex;
    }

    IRExpression value = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );

    IType rootType = arrayAccess.getRootExpression().getType();
    if( rootType.isArray() && isBytecodeType( rootType ) )
    {
      // Normal array access
      IRStatement ret = buildArrayStore( root, index, value, getDescriptor( rootType.getComponentType() ) );
      if( _stmt().isCompoundStatement() )
      {
        ExpressionTransformer.clearTempSymbolForCompoundAssignment();
        return new IRStatementList( false, buildAssignment( tempRoot, originalRoot ), buildAssignment( tempIndex, originalIndex ), ret );
      }
      return ret;
    }
    else
    {
      IRStatement ret;
      if( needsAutoinsert )
      {
        return new IRStatementList( false, buildAssignment( tempRoot, originalRoot ),
                                    buildAssignment( tempIndex, originalIndex ),
                                    buildMethodCall( callStaticMethod( ArrayAssignmentStatementTransformer.class, "setOrAddElement", new Class[]{Object.class, int.class, Object.class},
                                                                       exprList( root, index, value ) ) ) );
      }
      else if( JavaTypes.LIST().isAssignableFrom( rootType ) )
      {
        ret = buildMethodCall( buildMethodCall( List.class, "set", Object.class, new Class[]{int.class, Object.class},
                                                 buildCast( getDescriptor( List.class ), root ), Arrays.asList( index, value ) ) );
      }
      else if( JavaTypes.STRING_BUILDER().isAssignableFrom( rootType ) )
      {
        ret = buildMethodCall( buildMethodCall( StringBuilder.class, "setCharAt", void.class, new Class[]{int.class, char.class},
                                                 buildCast( getDescriptor( StringBuilder.class ), root ), Arrays.asList( index, value ) ) );
      }
      else if( rootType instanceof IPlaceholder )
      {
        ret = buildMethodCall(
          callStaticMethod( ArrayAssignmentStatementTransformer.class, "setPropertyOrElementOnDynamicType", new Class[]{Object.class, Object.class, Object.class},
                            exprList( root, index, value ) ) );
      }
      else
      {
        ret = buildMethodCall(
          callStaticMethod( ArrayAssignmentStatementTransformer.class, "setArrayElement", new Class[]{Object.class, int.class, Object.class},
                            exprList( root, index, value ) ) );
      }
      if( _stmt().isCompoundStatement() )
      {
        ExpressionTransformer.clearTempSymbolForCompoundAssignment();
        return new IRStatementList( false, buildAssignment( tempRoot, originalRoot ), buildAssignment( tempIndex, originalIndex ), ret );
      }
      return ret;
    }
  }

  private static boolean needsAutoinsert( ArrayAccess arrayAccess ) {
    if (ArrayAccess.needsAutoinsert( arrayAccess ) ) {
      return true;
    } else if ( arrayAccess.getRootExpression() instanceof ArrayAccess ) {
      return ArrayAccess.needsAutoinsert((ArrayAccess) arrayAccess.getRootExpression());
    } else {
      return false;
    }
  }

  public static void setPropertyOrElementOnDynamicType( Object obj, Object index, Object value )
  {
    if( obj == null )
    {
      throw new NullPointerException();
    }

    if( index instanceof Number )
    {
      if( obj instanceof Map )
      {
        ((Map)obj).put( index, value );
      }
      else if( obj.getClass().isArray() )
      {
        Array.set( obj, ((Number)index).intValue(), value );
      }
      else if( obj instanceof List )
      {
        ((List)obj).set( ((Number)index).intValue(), value );
      }
      else if( obj instanceof StringBuilder )
      {
        if( value instanceof Character )
        {
          ((StringBuilder)obj).setCharAt( ((Number)index).intValue(), (Character)value );
        }
        else if( value instanceof Number )
        {
          ((StringBuilder)obj).setCharAt( ((Number)index).intValue(), (char)((Number)value).intValue() );
        }
        else
        {
          replaceAt( (StringBuilder)obj, (Number)index, value );
        }
      }
      else
      {
        IType classObj = TypeLoaderAccess.instance().getIntrinsicTypeFromObject( obj );
        if( classObj.isArray() )
        {
          value = CommonServices.getCoercionManager().convertValue( value, classObj.getComponentType() );
          classObj.setArrayComponent( obj, ((Number)index).intValue(), value );
        }
        else
        {
          throw new UnsupportedOperationException( "Don't know how to assign [" + value + "] to [" + obj + "] at index [" + index  + "]" );
        }
      }
    }
    else if( obj instanceof Map )
    {
      ((Map)obj).put( index, value );
    }
    else
    {
      GosuRuntimeMethods.setPropertyDynamically( obj, index.toString(), value );
    }
  }

  private static void replaceAt( StringBuilder obj, Number index, Object value )
  {
    int start = index.intValue();
    int end = start + ((CharSequence)value).length();
    if( end <= obj.length() )
    {
      obj.replace( start, end, value.toString() );
    }
    else
    {
      obj.delete( start, obj.length() );
      String s = value.toString();
      obj.append( s );
    }
  }

  public static void setArrayElement( Object obj, int iIndex, Object value )
  {
    if( obj instanceof List )
    {
      //noinspection unchecked
      ((List)obj).set( iIndex, value );
      return;
    }

    IType classObj = TypeLoaderAccess.instance().getIntrinsicTypeFromObject( obj );
    if( classObj.isArray() )
    {
      value = CommonServices.getCoercionManager().convertValue( value, classObj.getComponentType() );
      classObj.setArrayComponent( obj, iIndex, value );
      return;
    }

    if( obj instanceof StringBuffer )
    {
      ((StringBuffer)obj).setCharAt( iIndex, ((Character)CommonServices.getCoercionManager().convertValue( value, JavaTypes.CHARACTER() )).charValue() );
    }

    throw new EvaluationException( "The type, " + classObj.getName() + ", is not coercible to an indexed-writable array." );
  }

  public static void setOrAddElement( Object obj, int iIndex, Object value )
  {
    List l = (List)obj;
    if( l.size() == iIndex )
    {
      l.add( value );
    }
    else
    {
      l.set( iIndex, value );
    }
  }
}
