/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.config.CommonServices;
import gw.internal.gosu.coercer.FunctionToInterfaceClassGenerator;
import gw.internal.gosu.ir.nodes.IRTypeFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.IDimension;
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
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ICoercer;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.coercers.BasePrimitiveCoercer;
import gw.lang.parser.coercers.FunctionFromInterfaceCoercer;
import gw.lang.parser.coercers.IdentityCoercer;
import gw.lang.parser.coercers.RuntimeCoercer;
import gw.lang.parser.expressions.ITypeAsExpression;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
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

    if( lhsType == JavaTypes.pVOID() ) {
      // null Literal -> Any Type
      if( !ILanguageLevel.Util.STANDARD_GOSU() && asType.isPrimitive() ) {
        if( isNumberType( asType ) ) {
          return convertNullToPrimitive( asType );
        }
        else if( asType == JavaTypes.pBOOLEAN() ) {
          return booleanLiteral( false );
        }
        else {
          return root;
        }
      }
      return checkCast( asType, root );
    }

    if( asType == JavaTypes.pVOID() ) {
      // Any -> void (no-op)
      return root;
    }

    if( isPrimitiveNumberType( asType ) && isPrimitiveNumberType( lhsType ) ) {
      // Primitive -> Primitive (bypass coercion manager)
      return numberConvert( lhsType, asType, root );
    }

    if( asType == JavaTypes.pBOOLEAN() ) {
      if( lhsType == JavaTypes.BOOLEAN() ) {
        // Boolean -> boolean (bypass coercion manager)
        IRSymbol tempRoot = _cc().makeAndIndexTempSymbol( getDescriptor( lhsType ) );
        IRAssignmentStatement tempRootAssn = buildAssignment( tempRoot, root );
        return buildComposite( tempRootAssn, buildTernary( buildEquals( identifier( tempRoot ), nullLiteral() ),
                                                           booleanLiteral( false ),
                                                           callMethod( Boolean.class, "booleanValue", new Class[]{}, identifier( tempRoot ), Collections.<IRExpression>emptyList() ),
                                                           getDescriptor( boolean.class ) ) );
      }
      else if( isNumberType( lhsType ) ) {
        // Boxed/Primitive -> boolean (bypass coercion manager)
        IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getLHS().getType() ) );
        IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, root );

        IRSymbol tempLhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( int.class ) );
        IRAssignmentStatement lhsConversionAssn = convertOperandToPrimitive( JavaTypes.pINT(), _expr().getLHS().getType(), identifier( tempLhs ), tempLhsRet );

        IRExpression compareExpr = buildComposite( lhsConversionAssn, buildNotEquals( identifier( tempLhsRet ), numericLiteral( 0 ) ) );
        IRExpression expr = _expr().getLHS().getType().isPrimitive()
                            ? compareExpr
                            : buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                            booleanLiteral( false ),
                                            compareExpr,
                                            getDescriptor( boolean.class ) );
        return buildComposite( tempLhsAssn, expr );
      }
    }

    if( asType == JavaTypes.BOOLEAN() ) {
      if( lhsType == JavaTypes.pBOOLEAN() ) {
        // boolean -> Boolean (bypass coercion manager)
        return callStaticMethod( Boolean.class, "valueOf", new Class[]{boolean.class}, Collections.singletonList( root ) );
      }
      else if( isNumberType( lhsType ) ) {
        // Boxed/Primitive -> Boolean (bypass coercion manager)
        IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getLHS().getType() ) );
        IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, root );

        IRSymbol tempLhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( int.class ) );
        IRAssignmentStatement lhsConversionAssn = convertOperandToPrimitive( JavaTypes.pINT(), _expr().getLHS().getType(), identifier( tempLhs ), tempLhsRet );

        IRExpression compareExpr = buildComposite( lhsConversionAssn, buildNotEquals( identifier( tempLhsRet ), numericLiteral( 0 ) ) );
        IRExpression expr = _expr().getLHS().getType().isPrimitive()
                            ? boxValue( getDescriptor( boolean.class ), compareExpr )
                            : buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                            nullLiteral(),
                                            boxValue( getDescriptor( boolean.class ), compareExpr ),
                                            getDescriptor( Boolean.class ) );
        return buildComposite( tempLhsAssn, expr );
      }
    }

    if( isBigType( asType ) && (isNumberType( lhsType ) || isBigType( lhsType )) ) {
      // Any Big/Boxed/Primitive -> Any Big (bypass coercion manager)

      IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( lhsType ) );
      IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, root );
      IRSymbol tempRet = _cc().makeAndIndexTempSymbol( getDescriptor( asType ) );
      return buildComposite( tempLhsAssn, lhsType.isPrimitive()
                                          ? buildComposite( convertOperandToBig( asType, asType == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class, lhsType, identifier( tempLhs ), tempRet ), identifier( tempRet ) )
                                          : checkCast( asType, buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ), nullLiteral(), buildComposite( convertOperandToBig( asType, asType == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class, lhsType, identifier( tempLhs ), tempRet ), identifier( tempRet ) ), getDescriptor( asType ) ) ) );
    }

    if( isNumberType( asType ) && isNumberType( lhsType ) ) {
      // Any Boxed/Primitive -> Any Boxed/Primitive  (bypass coercion manager)
      if( StandardCoercionManager.isBoxed( lhsType ) ) {
        // Non-primitive type, must handle null
        IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( lhsType ) );
        IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, root );

        root = unboxValueFromType( lhsType, identifier( tempLhs ) );
        IType primitiveTypeAsType = asType.isPrimitive() ? asType : TypeSystem.getPrimitiveType( asType );
        root = numberConvert( TypeSystem.getPrimitiveType( lhsType ), primitiveTypeAsType, root );
        if( StandardCoercionManager.isBoxed( asType ) ) {
          root = boxValueToType( asType, root );
        }
        return buildComposite( tempLhsAssn, buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                                          asType.isPrimitive()
                                                          ? ILanguageLevel.Util.STANDARD_GOSU()
                                                            ? buildComposite( buildThrow( buildNewExpression( getDescriptor( NullPointerException.class ), Collections.<IRType>emptyList(), Collections.<IRExpression>emptyList() ) ) )
                                                            : convertBoxedNullToPrimitive( TypeLord.getBoxedTypeFromPrimitiveType( asType ) )
                                                          : nullLiteral(),
                                                          root, getDescriptor( asType ) ) );
      }
      else {
        IType primitiveTypeAsType = asType.isPrimitive() ? asType : TypeSystem.getPrimitiveType( asType );
        root = numberConvert( lhsType, primitiveTypeAsType, root );

        if( StandardCoercionManager.isBoxed( asType ) ) {
          root = boxValueToType( asType, root );
        }
        return root;
      }
    }

    if( asType == JavaTypes.OBJECT() && lhsType.isPrimitive() ) {
      // Any Primitive -> Object (bypass coercion manager)
      return boxValue( lhsType, root );
    }

    if( asType.isPrimitive() && lhsType == JavaTypes.OBJECT() ) {
      // Object -> Any Primitive (bypass coercion)
      return unboxValueToType( asType, root );
    }

    if( asType.isInterface() && lhsType instanceof IBlockType ) {
      IGosuClass gsClass = FunctionToInterfaceClassGenerator.getBlockToInterfaceConversionClass( asType, _cc().getGosuClass() );
      return buildNewExpression( IRTypeFactory.get( gsClass ), Collections.singletonList( IRTypeFactory.get( JavaTypes.IBLOCK() ) ), Collections.singletonList( root ) );
    }

    IType lhsDimensionNumberType = findDimensionType( lhsType );
    if( lhsDimensionNumberType != null ) {
      // Any Dimension -> Any Number (bypass coercion manager)
      lhsType = lhsDimensionNumberType;
      IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( lhsType ) );
      IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, callMethod( IDimension.class, "toNumber", new Class[]{}, root, Collections.<IRExpression>emptyList() ) );

      if( isNumberType( asType ) ) {
        // Any Dimension -> Any Boxed/Primitive (bypass coercion manager)
        root = unboxValueFromType( lhsType, identifier( tempLhs ) );
        IType primitiveTypeAsType = asType.isPrimitive() ? asType : TypeSystem.getPrimitiveType( asType );
        root = numberConvert( TypeSystem.getPrimitiveType( lhsType ), primitiveTypeAsType, root );
        if( StandardCoercionManager.isBoxed( asType ) ) {
          root = boxValueToType( asType, root );
        }
        return buildComposite( tempLhsAssn, buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                                          asType.isPrimitive()
                                                          ? ILanguageLevel.Util.STANDARD_GOSU()
                                                            ? buildComposite( buildThrow( buildNewExpression( getDescriptor( NullPointerException.class ), Collections.<IRType>emptyList(), Collections.<IRExpression>emptyList() ) ) )
                                                            : convertBoxedNullToPrimitive( TypeLord.getBoxedTypeFromPrimitiveType( asType ) )
                                                          : nullLiteral(),
                                                          root, getDescriptor( asType ) ) );
      }
      else if( isBigType( asType ) ) {
        //  Any Dimension -> Any Big (bypass coercion manager)
        IRSymbol tempRet = _cc().makeAndIndexTempSymbol( getDescriptor( asType ) );
        return buildComposite( tempLhsAssn, lhsType.isPrimitive()
                                            ? buildComposite( convertOperandToBig( asType, asType == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class, lhsType, identifier( tempLhs ), tempRet ), identifier( tempRet ) )
                                            : checkCast( asType, buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ), nullLiteral(), buildComposite( convertOperandToBig( asType, asType == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class, lhsType, identifier( tempLhs ), tempRet ), identifier( tempRet ) ), getDescriptor( asType ) ) ) );
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
    IType exprType = _expr().getType();
    if( (coercer == IdentityCoercer.instance() && !exprType.isPrimitive()) ||
        exprType instanceof CompoundType )
    {
      if( !lhsType.isPrimitive() && lhsType != exprType )
      {
        if( (lhsType == JavaTypes.OBJECT() || lhsType.isInterface()) && (isBytecodeType( exprType ) && !isStructureType( exprType )) )
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
          // Generate code like the following:
          //
          // LhsType temp = <lhs-expr>
          // temp instanceof AsType ? (AsType)temp : coerce( temp, AsType )
          //
          IRType asType = getDescriptor( exprType );
          IRSymbol rootValue = _cc().makeAndIndexTempSymbol( root.getType() );
          root = buildComposite(
            buildAssignment( rootValue, root ),
            buildTernary( new IRInstanceOfExpression( identifier( rootValue ), asType ),
                          checkCast( exprType, identifier( rootValue ) ),
                          coerce( identifier( rootValue ), RuntimeCoercer.instance() ),
                          asType ) );

        }
        else
        {
          root = checkCast( exprType, root );
        }

      }
      // Boxing the value is sufficient; identity coercer simply returns whatever you pass in
      return root;
    }

    return coerce( root, coercer );
  }

  private boolean isStructureType( IType exprType ) {
    return exprType instanceof IGosuClass && ((IGosuClass)exprType).isStructure();
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
