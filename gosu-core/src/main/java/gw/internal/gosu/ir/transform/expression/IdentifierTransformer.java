/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.parser.*;
import gw.internal.gosu.parser.OuterFunctionSymbol;
import gw.lang.ir.IRExpression;
import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRProperty;
import gw.internal.gosu.ir.nodes.IRPropertyFactory;
import gw.lang.parser.*;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;

import java.util.Arrays;
import java.util.Collections;

/**
 */
public class IdentifierTransformer extends AbstractExpressionTransformer<IIdentifierExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, IIdentifierExpression expr )
  {
    IdentifierTransformer compiler = new IdentifierTransformer( cc, expr );
    return compiler.compile();
  }

  private IdentifierTransformer( TopLevelTransformationContext cc, IIdentifierExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    ISymbol symbol = _expr().getSymbol();

    ICompilableType gsClass = getGosuClass();
    if( (Keyword.KW_this.equals( symbol.getName() ) ||
         Keyword.KW_super.equals( symbol.getName() )) &&
        // 'this' must be an external symbol when in a program e.g., studio debugger expression
        (!(gsClass instanceof IGosuProgram) || gsClass.isAnonymous()) )
    {
      if( _cc().isBlockInvoke() && _cc().currentlyCompilingBlock() )
      {
        while( gsClass instanceof IBlockClass )
        {
          gsClass = gsClass.getEnclosingType();
        }
        return pushOuter( gsClass );
      }
      else
      {
        return pushThis();
      }
    }
    else if( symbol instanceof DynamicPropertySymbol && ((DynamicPropertySymbol)symbol).getGetterDfs() instanceof OuterFunctionSymbol )
    {
      // 'outer'
      return pushOuterForOuterSymbol();
    }
    else
    {
      return pushSymbolValue( symbol );
    }
  }

  public IRExpression pushSymbolValue( ISymbol symbol )
  {
    IType type = symbol.getType();
    Class symClass;
    IReducedSymbol reducedSym;
    if( symbol instanceof ReducedSymbol.SyntheticSymbol )
    {
      reducedSym = ((ReducedSymbol.SyntheticSymbol) symbol).getReducedSymbol();
      symClass = reducedSym.getSymbolClass();
    }
    else
    {
      reducedSym = symbol;
      symClass = symbol.getClass();
    }
    if ( _cc().isExternalSymbol(reducedSym.getName()) )
    {
      // unbox( symbols.getValue( name ) )
      return unboxValueToType(reducedSym.getType(),
          callMethod( IExternalSymbolMap.class, "getValue", new Class[]{String.class, int.class},
              pushExternalSymbolsMap(),
              Arrays.asList( pushConstant( reducedSym.getName() ), pushConstant( getArrayDims( reducedSym ) ))));
    }
    else if( DynamicSymbol.class.isAssignableFrom( symClass ) )
    {
      // Instance or Static field

      IRProperty irProp = IRPropertyFactory.createIRProperty( reducedSym );
      if( !irProp.isStatic() )
      {
        if( isMemberOnEnclosingType( reducedSym ) != null )
        {
          // Instance field from 'outer'
          return getField_new( irProp, pushOuter( reducedSym.getGosuClass() ), getDescriptor( reducedSym.getType() ) );
        }
        else
        {
          // Instance field from 'this'
          return getField_new( irProp, pushThis(), getDescriptor( reducedSym.getType() ) );
        }
      }
      else
      {
        // Static field
        return getField_new( irProp, null, getDescriptor( reducedSym.getType() ) );
      }
    }
    else if( CapturedSymbol.class.isAssignableFrom( symClass ) )
    {
      // Captured symbol is stored as a Field on an anonymous inner class (one elem array of symbol's type)
      // e.g., val$myFiield[0] = value. Note a captured symbol is duplicated in all nested classes.
      IRProperty irProp = IRPropertyFactory.createIRProperty( getGosuClass(), reducedSym );
      return getField_new( irProp, pushThis(), getDescriptor( reducedSym.getType() ) );
    }
    else if( reducedSym.getIndex() >= 0 )
    {
      // Local var

      if( reducedSym.isValueBoxed() )
      {
        // Local var is captured in an anonymous inner class.
        // Symbol's value maintained as a one elem array of symbol's type.
        return buildArrayLoad( identifier( _cc().getSymbol( reducedSym.getName() ) ), 0, getDescriptor( type ));
      }
      else
      {
        // Simple local var
        return identifier( _cc().getSymbol( reducedSym.getName() ) );
      }
    }
    else if( DynamicPropertySymbol.class.isAssignableFrom( symClass ) )
    {
      IRProperty irProp;
      if( reducedSym instanceof DynamicPropertySymbol )
      {
        irProp = IRPropertyFactory.createIRProperty((DynamicPropertySymbol) reducedSym);
      }
      else
      {
        irProp = IRPropertyFactory.createIRProperty((ReducedDynamicPropertySymbol) reducedSym);
      }

      IRExpression root;
      if( irProp.isStatic() )
      {
        root = null;
      }
      else
      {
        ICompilableType targetType = isMemberOnEnclosingType( reducedSym );
        if( targetType != null )
        {
          root = pushOuter( targetType );
        }
        else
        {
          root = pushThis();
        }
      }
      IRExpression getterCall = callMethod( irProp.getGetterMethod(), root, Collections.<IRExpression>emptyList() );
      return castResultingTypeIfNecessary(getDescriptor(reducedSym.getType()), irProp.getType(), getterCall);
    }
    else
    {
      throw new UnsupportedOperationException( "Don't know how to compile symbol: " + reducedSym.getClass().getSimpleName() + ": " + reducedSym.getName() + ": " + reducedSym.getType() );
    }
  }

  private int getArrayDims( IReducedSymbol reducedSym ) {
    IType type = reducedSym.getType();
    if( type == JavaTypes.OBJECT() ) {
      // Special case for handling Object -- pcf does strange stuff where it says a var is Object but expects a on dim array
      return -1;
    }
    int iDims;
    for( iDims = 0; type.isArray(); iDims++ ) {
      type = type.getComponentType();
    }
    return iDims;
  }

  protected IRExpression pushOuterForOuterSymbol()
  {
    IRExpression root = pushOuter();
    IType currentClass = getGosuClass();
    while( currentClass instanceof IBlockClass )
    {
      currentClass = currentClass.getEnclosingType();
      IRMethod irMethod = IRMethodFactory.createIRMethod( currentClass, OUTER_ACCESS, currentClass.getEnclosingType(), new IType[]{currentClass}, AccessibilityUtil.forOuterAccess(), true );
      root = callMethod( irMethod, null, Collections.singletonList( root ) );
    }
    IType outer = currentClass.getEnclosingType();
    while( outer instanceof IBlockClass )
    {
      IType enclosing = getRuntimeEnclosingType( outer );
      IRMethod irMethod = IRMethodFactory.createIRMethod( outer, OUTER_ACCESS, enclosing, new IType[]{outer}, AccessibilityUtil.forOuterAccess(), true );
      root = callMethod( irMethod, null, Collections.singletonList( root ) );
      outer = enclosing;
    }
    return root;
  }

}
