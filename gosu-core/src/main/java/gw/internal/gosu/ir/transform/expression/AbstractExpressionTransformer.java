/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethod;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.ParameterizedGosuConstructorInfo;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.parser.IExpression;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.java.IJavaConstructorInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 */
public abstract class AbstractExpressionTransformer<T extends IExpression> extends AbstractElementTransformer<T>
{
  public AbstractExpressionTransformer( TopLevelTransformationContext cc, T parsedElem )
  {
    super( cc, parsedElem );
  }

  public final IRExpression compile()
  {
    IRExpression expr = compile_impl();

    //## todo: see about re-enabling line numbers at expressions *after* we can filter out all the crap that screws up stepping in the debugger
//    if( _expr() instanceof IHasOperatorLineNumber )
//    {
//      expr.setLineNumber( ((IHasOperatorLineNumber)_expr()).getOperatorLineNumber() );
//    }
//    else
//    {
//      expr.setLineNumber( _expr().getLineNum() );
//    }
    return expr;
  }

  protected abstract IRExpression compile_impl();

  public T _expr()
  {
    return getParsedElement();
  }

  static List<IRType> getIRParameters( IConstructorInfo ci )
  {
    if( ci.getParameters().length == 0 )
    {
      return Collections.emptyList();
    }

    if( ci instanceof IJavaConstructorInfo)
    {
      Class<?>[] parameterClasses = ((IJavaConstructorInfo) ci).getRawConstructor().getParameterTypes();
      List<IRType> irTypes = new ArrayList<IRType>( parameterClasses.length );
      for (Class parameterClass : parameterClasses) {
        irTypes.add( getDescriptor( parameterClass ) );
      }
      return irTypes;
    }
    else if( ci instanceof IGosuConstructorInfo)
    {
      IGosuConstructorInfo cInfo = (IGosuConstructorInfo) ci;
      while( cInfo instanceof ParameterizedGosuConstructorInfo)
      {
        ParameterizedGosuConstructorInfo pdfs = (ParameterizedGosuConstructorInfo)cInfo;
        cInfo = pdfs.getBackingConstructorInfo();
      }
      List<IRType> boundedTypes = new ArrayList<IRType>(cInfo.getArgs().size());
      for( int i = 0; i < cInfo.getArgs().size(); i++ )
      {
        IType type = cInfo.getArgs().get(i).getType();
        boundedTypes.add( getDescriptor( TypeLord.getDefaultParameterizedTypeWithTypeVars( type ) ) );
      }
      return boundedTypes;
    }
    else
    {
      List<IRType> boundedTypes = new ArrayList<IRType>( ci.getParameters().length );
      for( IType type : getTypes( ci.getParameters() ) )
      {
        boundedTypes.add( getDescriptor( type ) );
      }
      return boundedTypes;
    }
  }

  protected IRExpression shortCircuitValue( IRType expressionType )
  {
    if( _expr().getType().isPrimitive() )
    {
      return getDefaultConstIns( _expr().getType() );
    }
    else
    {
      return buildCast( expressionType, nullLiteral() );
    }
  }

  protected void pushArgumentsNoCasting( IRMethod irMethod, IExpression[] args, List<IRExpression> irArgs )
  {
    _pushArguments( irMethod, args, irArgs, false );
  }
  protected void pushArgumentsWithCasting( IRMethod irMethod, IExpression[] args, List<IRExpression> irArgs )
  {
    _pushArguments( irMethod, args, irArgs, true );
  }
  private void _pushArguments( IRMethod irMethod, IExpression[] args, List<IRExpression> irArgs, boolean bCast )
  {
    if( args != null )
    {
      List<IRType> paramClasses = bCast ? irMethod.getExplicitParameterTypes() : Collections.<IRType>emptyList();
      for( int i = 0; i < args.length; i++ )
      {
        IExpression arg = args[i];
        IRExpression irArg = ExpressionTransformer.compile( arg, _cc() );
        if( bCast )
        {
          IRType type = paramClasses.get( i );
          if( type.isPrimitive() && arg.getType() instanceof IPlaceholder && ((IPlaceholder)arg.getType()).isPlaceholder() )
          {
            irArg = unboxValueToType( type, irArg );
          }
          else
          {
            irArg = maybeCast( paramClasses, i, irArg );
          }
        }
        irArgs.add( irArg );
      }
    }
  }

  private IRExpression maybeCast( List<IRType> paramClasses, int i, IRExpression irArg ) {
    // Maybe cast if not directly assignable (e.g., cross cast)
    IRType paramClass = paramClasses.get( i );
    if( !paramClass.isAssignableFrom( irArg.getType() ) )
    {
      irArg = buildCast( paramClass, irArg );
    }
    return irArg;
  }
}
