/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.lang.reflect.LazyTypeResolver;
import gw.lang.parser.IExpression;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRTypeConstants;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.fragments.GosuFragment;

import java.util.List;
import java.util.ArrayList;

public abstract class EvalBasedTransformer<T extends IExpression> extends AbstractExpressionTransformer<T>
{
  public EvalBasedTransformer( TopLevelTransformationContext cc, T parsedElem )
  {
    super( cc, parsedElem );
  }

  protected IRExpression pushEnclosingContext() {
    if( !_cc().isCurrentFunctionStatic() )
    {
      return pushThisOrOuter( getGosuClass() );
    }
    else
    {
      return pushNull();
    }
  }

  protected IRExpression pushCapturedSymbols( ICompilableTypeInternal enclosingClass, List<ICapturedSymbol> capturedSymbols )
  {
    List<IRExpression> values = new ArrayList<IRExpression>();
    if ( capturedSymbols != null)
    {
      for( ICapturedSymbol sym : capturedSymbols )
      {
        if( enclosingClass.isAnonymous() && enclosingClass.getCapturedSymbols().containsKey( sym.getName() ) )
        {
          values.add( getInstanceField( getGosuClass(), CAPTURED_VAR_PREFIX + sym.getName(), getDescriptor( sym.getType().getArrayType() ), AccessibilityUtil.forCapturedVar(), pushThis() ) );
        }
        else
        {
          values.add( identifier( _cc().getSymbol( sym.getName() ) ) );
        }
      }
    }

    if ( requiresExternalSymbolCapture( enclosingClass ) || enclosingClass instanceof GosuFragment || enclosingClass instanceof IGosuProgram )
    {
      values.add( pushExternalSymbolsMap() );
    }

    if (!values.isEmpty())
    {
      return buildInitializedArray(IRTypeConstants.OBJECT(), values );
    }
    else
    {
      return pushNull();
    }
  }

  protected static void addEnclosingTypeParams( LazyTypeResolver[] immediateFuncTypeParams, List<Object> args )
  {
    if( immediateFuncTypeParams != null )
    {
      //noinspection ManualArrayToCollectionCopy
      for( int i = 0; i < immediateFuncTypeParams.length; i++ )
      {
        args.add( immediateFuncTypeParams[i] );
      }
    }
  }

  protected IRExpression pushEnclosingFunctionTypeParamsInArray( IParsedElement expr )
  {
    int iCount = getFunctionTypeParamsCount( expr );
    if( iCount == 0 )
    {
      return pushNull();
    }

    List<IRExpression> values = new ArrayList<IRExpression>();
    IGosuClassInternal gsClass = null;
    while( expr != null && expr.getLocation() != null )
    {
      IFunctionStatement funcStmt = expr.getLocation().getEnclosingFunctionStatement();
      if( funcStmt != null ) // can be null e.g., anonymous classes can be constructed as field initializers
      {
        IDynamicFunctionSymbol dfs = funcStmt.getDynamicFunctionSymbol();
        if( dfs.getType().isGenericType() && Modifier.isReified( dfs.getModifiers() ) )
        {
          IGenericTypeVariable[] genTypeVars = dfs.getType().getGenericTypeVariables();
          for( int i = 0; i < genTypeVars.length; i++ )
          {
            if( gsClass == null )
            {
              values.add( identifier( _cc().getSymbol( getTypeVarParamName( genTypeVars[i]) ) ) );
            }
            else
            {
              pushThisOrOuter( gsClass );
              values.add( getInstanceField( gsClass, TYPE_PARAM_PREFIX + genTypeVars[i].getName(), getDescriptor( LazyTypeResolver.class ),
                      AccessibilityUtil.forTypeParameter(),
                      pushThisOrOuter( gsClass ) ) );
            }
          }
          return buildInitializedArray( getDescriptor( LazyTypeResolver.class ), values );
        }
      }
      IType type = funcStmt.getDynamicFunctionSymbol().getScriptPart().getContainingType();
      if( type instanceof IGosuClass && ((IGosuClass)type).isAnonymous() )
      {
        gsClass = (IGosuClassInternal)type;
        expr = isEvalProgram( gsClass )
               ? ((IGosuProgram)gsClass).getEnclosingEvalExpression()
               : gsClass.getClassStatement();
      }
      else
      {
        expr = null;
      }
    }

    throw new IllegalStateException("Should have found the appropriate enclosing context ");
  }

  protected int getFunctionTypeParamsCount( IParsedElement pe )
  {
    IGosuClassInternal gsClass;
    int iCount = 0;
    while( pe != null && pe.getLocation() != null )
    {
      IFunctionStatement funcStmt = pe.getLocation().getEnclosingFunctionStatement();
      if( funcStmt != null ) // can be null e.g., anonymous classes can be constructed as field initializers
      {
        IDynamicFunctionSymbol dfs = funcStmt.getDynamicFunctionSymbol();
        if( dfs.getType().isGenericType() )
        {
          IGenericTypeVariable[] genTypeVars = dfs.getType().getGenericTypeVariables();
          iCount += genTypeVars.length;
        }
        IType type = funcStmt.getDynamicFunctionSymbol().getScriptPart().getContainingType();
        if( type instanceof IGosuClass && ((IGosuClass)type).isAnonymous() )
        {
          gsClass = (IGosuClassInternal)type;
          pe = isEvalProgram( gsClass )
               ? ((IGosuProgram)gsClass).getEnclosingEvalExpression()
               : gsClass.getClassStatement();
        }
        else
        {
          pe = null;
        }
      }
      else
      {
        pe = null;
      }
    }
    return iCount;
  }
}