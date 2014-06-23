/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.config.CommonServices;
import gw.internal.gosu.ir.nodes.IRTypeFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.CompoundType;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.expression.IRInstanceOfExpression;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ICoercer;
import gw.lang.parser.coercers.BasePrimitiveCoercer;
import gw.lang.parser.coercers.FunctionFromInterfaceCoercer;
import gw.lang.parser.coercers.IdentityCoercer;
import gw.lang.parser.coercers.RuntimeCoercer;
import gw.lang.parser.expressions.ITypeAsExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 */
public class TypeAsTransformer extends AbstractExpressionTransformer<ITypeAsExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, ITypeAsExpression expr )
  {
    TypeAsTransformer gen = new TypeAsTransformer( cc, expr );
    return gen.compile();
  }

  private TypeAsTransformer( TopLevelTransformationContext cc, ITypeAsExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    // Push the LHS expression value
    IRExpression root = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IType asType = _expr().getType();
    IType lhsType = _expr().getLHS().getType();

    if( _expr().getType().getName().equals( GosuTypes.IMONITORLOCK().getName() ) )
    {
      return root;
    }

    if( lhsType == asType )
    {
      return root;
    }

    if( isPrimitiveNumberType( asType ) &&
        isPrimitiveNumberType( lhsType ) )
    {
      return numberConvert( lhsType, asType, root );
    }

    if( isPrimitiveNumberType( lhsType ) )
    {
      // Handle boxing directly (bypass coercion manager)

      if( asType.isAssignableFrom( TypeSystem.getBoxType( lhsType ) ) )
      {
        return boxValue( lhsType, root );
      }
      if( isNonBigBoxedNumberType( asType ) )
      {
        IType primitiveAsType = TypeSystem.getPrimitiveType( asType );
        if( isPrimitiveNumberType( primitiveAsType ) )
        {
          return boxValueToType( asType, numberConvert( lhsType, primitiveAsType, root ) );
        }
      }
    }

    IRExpression asPrimitive = maybeMakePrimitive( root );
    if( asPrimitive != null )
    {
      return asPrimitive;
    }

    IRExpression result = callCoercer( root );

    if( asType.isPrimitive() )
    {
      result = unboxValueToType( asType, result );
    }
    return result;
  }

  private IRExpression callCoercer( IRExpression root )
  {
    // Ensure the value is boxed (the coercer takes an Object)
    IType lhsType = _expr().getLHS().getType();
    if( lhsType.isPrimitive() )
    {
      root = boxValue( lhsType, root );
    }
    ICoercer coercer = _expr().getCoercer();
    if( (coercer == IdentityCoercer.instance() && !_expr().getType().isPrimitive()) ||
        _expr().getType() instanceof CompoundType )
    {
      if( (!lhsType.isPrimitive() || lhsType == JavaTypes.pVOID()) && lhsType != _expr().getType() )
      {
        if( (lhsType == JavaTypes.OBJECT() || lhsType.isInterface()) && isBytecodeType( _expr().getType() ) )
        {
          //## hack:

          // The lhs is Object or an interface and is being cast to a bytecode-based type;
          // The "coercer" is the identity coercer, which, in a sane world, would mean
          // a straight cast and no actual coercion.  Since we are insane poeple, it's possible
          // there could be a runtime coercer involved (dammit), we have to check the type
          // of lhs at runtime and do the cast if the type checks out. The idea is to avoid
          // doing a runtime coercion because that is much more expensive than a
          // simple checkcast.
          //
          // Geneate code like the following:
          //
          // LhsType temp = <lhs-expr>
          // temp instanceof AsType ? (AsType)temp : coerce( temp, AsType )
          //
          IRType asType = getDescriptor( _expr().getType() );
          IRSymbol rootValue = _cc().makeAndIndexTempSymbol( root.getType() );
          root = buildComposite(
            buildAssignment( rootValue, root ),
            buildTernary( new IRInstanceOfExpression( identifier( rootValue ), asType ),
                          checkCast( _expr().getType(), identifier( rootValue ) ),
                          coerce( identifier( rootValue ), RuntimeCoercer.instance() ),
                          asType ) );

        }
        else
        {
          root = checkCast( _expr().getType(), root );
        }

      }
      // Boxing the value is sufficient; identity coercer simply returns whatever you pass in
      return root;
    }

    return coerce( root, coercer );
  }

  private IRExpression coerce( IRExpression root, ICoercer coercer )
  {
    IRExpression result;
    //special handling to inline the function from interface coercer (needs more context information)
    if (coercer instanceof FunctionFromInterfaceCoercer) {
      IFunctionType returnType = (IFunctionType) _expr().getReturnType();
      int length = returnType.getParameterTypes().length;
      String functionTypeName = "gw.lang.function.IFunction" + length;
      IType functionType = TypeSystem.getByFullName(functionTypeName);
      result = callStaticMethod(FunctionFromInterfaceCoercer.class, "doCoercion", new Class[]{Class.class, Class.class, Object.class}, exprList(
        classLiteral(IRTypeFactory.get(functionType)),
        classLiteral(IRTypeFactory.get(_expr().getLHS().getType())),
        root
      ));
    } else {
      // Push the coercer
      IRExpression coercerExpression = null;
      if (coercer != null) {
        if (coercer instanceof BasePrimitiveCoercer) {
          for (Field f : BasePrimitiveCoercer.class.getDeclaredFields()) {
            try {
              if (Modifier.isPublic(f.getModifiers()) && LockingLazyVar.class.isAssignableFrom(f.getType())) {
                Object value = f.get(null);
                LockingLazyVar<BasePrimitiveCoercer> lv = (LockingLazyVar<BasePrimitiveCoercer>) value;
                if (lv.get() == coercer) {
                  IRExpression coercerField = getStaticField(TypeSystem.get(BasePrimitiveCoercer.class), f.getName(),
                    JavaClassIRType.get(LockingLazyVar.class), IRelativeTypeInfo.Accessibility.PUBLIC);
                  IRSymbol coercerSym = new IRSymbol(_cc().makeTempSymbolName(), JavaClassIRType.get(LockingLazyVar.class), true);
                  _cc().putSymbol(coercerSym);
                  IRStatement tempAssignStmt = buildAssignment(coercerSym, coercerField);
                  IRExpression getCoercerCall = buildMethodCall(LockingLazyVar.class, "get", Object.class, new Class[0], new IRIdentifier(coercerSym), Collections.<IRExpression>emptyList());
                  coercerExpression = new IRCompositeExpression(tempAssignStmt, getCoercerCall);
                }
              }
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        } else {
          coercerExpression = callStaticMethod(coercer.getClass(), "instance", new Class[0], exprList());
        }
      } else {
        coercerExpression = pushNull();
      }
      result = callStaticMethod(TypeAsTransformer.class, "coerceValue", new Class[]{Object.class, IType.class, ICoercer.class},
        exprList(root, pushType(_expr().getType()), coercerExpression));
    }
    if (!_expr().getType().isPrimitive()) {
      result = checkCast(_expr().getType(), result);
    }
    return result;
  }

  private IRExpression maybeMakePrimitive( IRExpression lhsExpression )
  {
    IType asType = _expr().getType();
    if( !asType.isPrimitive() )
    {
      return null;
    }

    IType lhsType = _expr().getLHS().getType();
    if( lhsType.isPrimitive() )
    {
      return null;
    }

    if( lhsType == JavaTypes.BOOLEAN() &&
        asType == JavaTypes.pBOOLEAN() )
    {
      return convertBoxedToPrimitive( lhsType, Boolean.class, "booleanValue", lhsExpression);
    }
    else if( lhsType == JavaTypes.BYTE() &&
             asType == JavaTypes.pBYTE() )
    {
      return convertBoxedToPrimitive( lhsType, Byte.class, "byteValue", lhsExpression);
    }
    else if( lhsType == JavaTypes.CHARACTER() &&
             asType == JavaTypes.pCHAR() )
    {
      return convertBoxedToPrimitive( lhsType, Character.class, "charValue", lhsExpression);
    }
    else if( lhsType == JavaTypes.SHORT() &&
             asType == JavaTypes.pSHORT() )
    {
      return convertBoxedToPrimitive( lhsType, Short.class, "shortValue", lhsExpression);
    }
    else if( lhsType == JavaTypes.INTEGER() &&
             asType == JavaTypes.pINT() )
    {
      return convertBoxedToPrimitive( lhsType, Integer.class, "intValue", lhsExpression);
    }
    else if( lhsType == JavaTypes.LONG() &&
             asType == JavaTypes.pLONG() )
    {
      return convertBoxedToPrimitive( lhsType, Long.class, "longValue", lhsExpression);
    }
    else if( lhsType == JavaTypes.FLOAT() &&
             asType == JavaTypes.pFLOAT() )
    {
      return convertBoxedToPrimitive( lhsType, Float.class, "floatValue", lhsExpression);
    }
    else if( lhsType == JavaTypes.DOUBLE() &&
             asType == JavaTypes.pDOUBLE() )
    {
      return convertBoxedToPrimitive( lhsType, Double.class, "doubleValue", lhsExpression);
    }
    else
    {
      return null;
    }
  }

  public static Object coerceValue( Object value, IType type, ICoercer coercer )
  {
    if( type == GosuParserTypes.NUMBER_TYPE() )
    {
      return CommonServices.getCoercionManager().makeDoubleFrom( value );
    }
    else if( type == GosuParserTypes.STRING_TYPE() )
    {
      return CommonServices.getCoercionManager().makeStringFrom( value );
    }
    else if( type == GosuParserTypes.DATETIME_TYPE() )
    {
      return CommonServices.getCoercionManager().makeDateFrom( value );
    }

    if( coercer != null && (value != null || coercer.handlesNull()) )
    {
      return coercer.coerceValue( type, value );
    }

    return value;
  }

  private IRExpression convertBoxedToPrimitive(IType lhsType, Class cls, String methodName, IRExpression lhsExpression) {
    IRSymbol tempRootSymbol = _cc().makeAndIndexTempSymbol( getDescriptor( lhsType ) );
    return buildComposite(
            buildAssignment( tempRootSymbol, lhsExpression ),
            buildNullCheckTernary(
                    identifier( tempRootSymbol ),
                    convertBoxedNullToPrimitive( lhsType ),
                    callMethod( cls,  methodName, new Class[0], identifier(tempRootSymbol), exprList() ) ) );
  }
}
