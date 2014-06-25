/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.config.CommonServices;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.expressions.AdditiveExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.runtime.GosuRuntimeMethods;
import gw.lang.IDimension;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.expression.IRStringLiteralExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRMethodCallStatement;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IEnumConstant;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class AdditiveExpressionTransformer extends ArithmeticExpressionTransformer<AdditiveExpression>
{
  private final StringBuilderHandle _stringBuilderFromParent;

  public static IRExpression compile( TopLevelTransformationContext cc, AdditiveExpression expr )
  {
    return compile( cc, expr, null );
  }
  public static IRExpression compile( TopLevelTransformationContext cc, AdditiveExpression expr, StringBuilderHandle stringBuilder )
  {
    AdditiveExpressionTransformer gen = new AdditiveExpressionTransformer( cc, expr, stringBuilder );
    return gen.compile();
  }

  private AdditiveExpressionTransformer( TopLevelTransformationContext cc, AdditiveExpression expr, StringBuilderHandle stringBuilderFromParent )
  {
    super( cc, expr );
    _stringBuilderFromParent = stringBuilderFromParent;
  }

  protected IRExpression compile_impl()
  {
    IRExpression expr = compileNumericArithmetic();
    if( expr != null ) {
      return expr;
    }
    else
    {
      if( isCompileTimeConstantConcatenation() )
      {
        return concatenate();
      }
      else if( isStringConcatenation() )
      {
        return stringConcatenation();
      }
      else {
        IType dimensionType = findDimensionType( _expr().getType() );
        if( dimensionType != null ) {
          return dimensionAddition( dimensionType );
        }
        return dynamicAddition();
      }
    }
  }

  private IRExpression dimensionAddition( IType type ) {
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    IRSymbol tempLhsInit = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getLHS().getType() ) );
    IRAssignmentStatement tempLhsInitAssn = buildAssignment( tempLhsInit, lhs );
    IRSymbol tempRhsInit = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getRHS().getType() ) );
    IRAssignmentStatement tempRhsInitAssn = buildAssignment( tempRhsInit, rhs );
    if( _expr().isNullSafe() ) {
      return buildComposite( tempLhsInitAssn, tempRhsInitAssn,
        buildCast( getDescriptor( _expr().getType() ),
                   buildTernary( buildEquals( identifier( tempLhsInit ), nullLiteral() ),
                      nullLiteral(),
                      buildTernary( buildEquals( identifier( tempRhsInit ), nullLiteral() ),
                                    nullLiteral(),
                                    (type == JavaTypes.BIG_DECIMAL() || type == JavaTypes.BIG_INTEGER())
                                    ? callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( tempLhsInit ),
                                                  Collections.singletonList(  callMethod( ((IJavaType)type).getBackingClassInfo(),
                                                                                _expr().isAdditive() ? "add" : "subtract", new Class[]{type == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class},
                                                                                checkCast( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempLhsInit ), Collections.<IRExpression>emptyList() ) ),
                                                                                Collections.<IRExpression>singletonList( checkCast( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempRhsInit ), Collections.<IRExpression>emptyList() ) ) ) ) ) )
                                    : callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( tempLhsInit ),
                                                  Collections.singletonList( boxValueToType( type,
                                                                               new IRArithmeticExpression( getDescriptor( TypeSystem.getPrimitiveType( type ) ),
                                                                                 unboxValueFromType( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempLhsInit ), Collections.<IRExpression>emptyList() ) ),
                                                                                 unboxValueFromType( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempRhsInit ), Collections.<IRExpression>emptyList() ) ),
                                                                                 _expr().isAdditive() ? IRArithmeticExpression.Operation.Addition : IRArithmeticExpression.Operation.Subtraction ) ) ) ),
                                    getDescriptor( _expr().getType() ) ),
                      getDescriptor( _expr().getType() ) ) ) );
    }
    else {
      return buildComposite( tempLhsInitAssn, tempRhsInitAssn,
        buildCast( getDescriptor( _expr().getType() ),
          (type == JavaTypes.BIG_DECIMAL() || type == JavaTypes.BIG_INTEGER())
          ? callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( tempLhsInit ),
                        Collections.singletonList(  callMethod( ((IJavaType)type).getBackingClassInfo(),
                                                      _expr().isAdditive() ? "add" : "subtract", new Class[]{type == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class},
                                                      checkCast( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempLhsInit ), Collections.<IRExpression>emptyList() ) ),
                                                      Collections.<IRExpression>singletonList( checkCast( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempRhsInit ), Collections.<IRExpression>emptyList() ) ) ) ) ) )
          : callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( tempLhsInit ),
                        Collections.singletonList( boxValueToType( type,
                                                     new IRArithmeticExpression( getDescriptor( TypeSystem.getPrimitiveType( type ) ),
                                                       unboxValueFromType( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempLhsInit ), Collections.<IRExpression>emptyList() ) ),
                                                       unboxValueFromType( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempRhsInit ), Collections.<IRExpression>emptyList() ) ),
                                                       _expr().isAdditive() ? IRArithmeticExpression.Operation.Addition : IRArithmeticExpression.Operation.Subtraction ) ) ) ) ) );
    }
  }

  private IRStringLiteralExpression concatenate()
  {
    return (IRStringLiteralExpression) pushConstant( GosuRuntimeMethods.toString( _expr().getLHS().evaluate() ) +
                                                     GosuRuntimeMethods.toString( _expr().getRHS().evaluate() ) );
  }

  private IRExpression dynamicAddition()
  {
    // Call into Gosu's runtime for the answer.  The arguments are the result type, the boxed LHS, the boxed RHS,
    // the LHS type, the RHS type, whether it's addition, and whether it's numeric
    boolean bNumeric = BeanAccess.isNumericType( _expr().getType() );
    IRExpression evaluateCall = callStaticMethod( AdditiveExpression.class, "evaluate",
                                                  new Class[]{IType.class, Object.class, Object.class, IType.class, IType.class, boolean.class, boolean.class, boolean.class},
                                                  exprList( pushType( _expr().getType() ),
                                                            boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ),
                                                            boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ),
                                                            pushType( _expr().getLHS().getType() ),
                                                            pushType( _expr().getRHS().getType() ),
                                                            pushConstant( _expr().isAdditive() ),
                                                            pushConstant( _expr().isNullSafe() ),
                                                            pushConstant( bNumeric ) ) );

    // Ensure value is unboxed if type is primitive
    return unboxValueToType( _expr().getType(), evaluateCall );
  }

  private boolean isCompileTimeConstantConcatenation()
  {
    return  _expr().getType() == JavaTypes.STRING() &&
            _expr().isCompileTimeConstant() &&
            !containsIdentifier( _expr().isAssignment() ? _expr().getParent() : _expr() );
  }

  private boolean containsIdentifier( IParsedElement expr )
  {
    if ( expr instanceof Identifier &&
         !((Identifier) expr).isStaticFinalInitializedCompileTimeConstant() )
    {
      return true;
    }
    IParseTree location = expr.getLocation();
    if( location == null )
    {
      return true;
    }
    for( IParseTree child: location.getChildren() )
    {
      if( containsIdentifier( child.getParsedElement() ) )
      {
        return true;
      }
    }
    return false;
  }

  private IRExpression stringConcatenation() {
    StringBuilderHandle sbHandle = _stringBuilderFromParent;
    if( sbHandle == null ) {
      sbHandle = new StringBuilderHandle();
    }

    handleConcatOperand( sbHandle, _expr().getLHS() );
    handleConcatOperand( sbHandle, _expr().getRHS() );

    if( _stringBuilderFromParent == null ) {
      //## perf: Maybe create your own MyStringBuilder class that exposes the char[] and use SharedSecrets.getJavaLangAccess().newStringUnsafe( char[] ).
      //## perf: This approach would avoid double-allocating the char[] associated with the resulting String.
      sbHandle.setStringBuilder( callMethod( StringBuilder.class, "toString", new Class[]{}, sbHandle.getStringBuilder(), Collections.<IRExpression>emptyList() ) );
    }

    sbHandle.setAppendedToStringBuilder( true );

    return sbHandle.getStringBuilder();
  }

  private void handleConcatOperand( StringBuilderHandle sbHandle, Expression operand ) {
    IType operandType = operand.getType();
    IRExpression expr = operand instanceof AdditiveExpression
                        ? AdditiveExpressionTransformer.compile( _cc(), (AdditiveExpression)operand, sbHandle )
                        : ExpressionTransformer.compile( operand, _cc() );
    boolean bHandled = sbHandle.isAppendedToStringBuilder();
    sbHandle.setAppendedToStringBuilder( false );
    if( !bHandled ) {
      IRExpression appendExpr;
      if( !operandType.isPrimitive() ) {
        if( operandType == JavaTypes.STRING() ) {
          appendExpr = callMethod( StringBuilder.class, "append", new Class[]{String.class}, sbHandle.getStringBuilder(), Collections.singletonList( expr ) );
        }
        else if( JavaTypes.CHAR_SEQUENCE().isAssignableFrom( operandType ) ) {
          appendExpr = callMethod( StringBuilder.class, "append", new Class[]{CharSequence.class}, sbHandle.getStringBuilder(), Collections.singletonList( expr ) );
        }
        else if( JavaTypes.pCHAR().getArrayType().isAssignableFrom( operandType ) ) {
          appendExpr = callMethod( StringBuilder.class, "append", new Class[]{char[].class}, sbHandle.getStringBuilder(), Collections.singletonList( expr ) );
        }
        else if( ILanguageLevel.Util.STANDARD_GOSU() ||
                 !isHandledByCustomCoercion( operandType ) ) {
          appendExpr = callMethod( StringBuilder.class, "append", new Class[]{Object.class}, sbHandle.getStringBuilder(), Collections.singletonList( expr ) );
        }
        else { // Sucky ICoercionManager#makeStringFrom() must be called e.g., TypeKey's Code is used because its toString() returns DisplayName
          appendExpr = callMethod( ICoercionManager.class, "makeStringFrom", new Class[]{Object.class},
                         callStaticMethod( CommonServices.class, "getCoercionManager", new Class[]{}, Collections.<IRExpression>emptyList() ),
                         Collections.singletonList( expr ) );
          appendExpr = callMethod( StringBuilder.class, "append", new Class[]{String.class}, sbHandle.getStringBuilder(), Collections.singletonList( appendExpr ) );
        }
      }
      else {
        Class primitiveClass = getDescriptor( operandType ).getJavaClass();
        primitiveClass = primitiveClass == void.class
                         ? Object.class // this special case is for 'null' literal, which internally is of type "void"
                         : primitiveClass == short.class || primitiveClass == byte.class
                           ? int.class
                           : primitiveClass;
        appendExpr = callMethod( StringBuilder.class, "append", new Class[]{primitiveClass}, sbHandle.getStringBuilder(), Collections.<IRExpression>singletonList( primitiveClass == Object.class ? nullLiteral() : expr ) );
      }
      sbHandle.setStringBuilder( appendExpr );
    }
    else {
      sbHandle.setStringBuilder( expr );
    }
  }

  private boolean isHandledByCustomCoercion( IType operandType ) {
    return
      operandType == JavaTypes.BIG_DECIMAL() ||
      operandType == JavaTypes.FLOAT() ||
      operandType == JavaTypes.DOUBLE() ||
      operandType == JavaTypes.DATE() ||
      TypeSystem.get( IEnumConstant.class ).isAssignableFrom( operandType ) ||
      CommonServices.getEntityAccess().isTypekey( operandType ) ||
      CommonServices.getEntityAccess().isEntityClass( operandType );
  }

  private boolean isStringConcatenation() {
    return _expr().getType() == JavaTypes.STRING();
  }


  private class StringBuilderHandle {
    private IRExpression _stringBuilder;
    private boolean _bAppendedToStringBuilder;

    private StringBuilderHandle() {
      _stringBuilder = buildNewExpression( StringBuilder.class, new Class[]{}, Collections.<IRExpression>emptyList() );
    }

    private IRExpression getStringBuilder() {
      return _stringBuilder;
    }
    private void setStringBuilder( IRExpression stringBuilder ) {
      _stringBuilder = stringBuilder;
    }

    private boolean isAppendedToStringBuilder() {
      return _bAppendedToStringBuilder;
    }
    private void setAppendedToStringBuilder( boolean bAppendedToStringBuilder ) {
      _bAppendedToStringBuilder = bAppendedToStringBuilder;
    }
  }
}
