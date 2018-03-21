/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.*;


import gw.lang.parser.IExpression;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.expressions.IInitializerExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

import gw.util.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * The 'new' operator as an expression:
 * <pre>
 * <i>new-expression</i>
 *   <b>new</b> &lt;type-expression&gt; <b>(</b> [&lt;argument-list&gt;] <b>)</b> [ <b>{...}</b> ]
 *   <b>new</b> &lt;type-expression&gt; <b>[</b> &lt;expression&gt; <b>]</b>
 *   <b>new</b> &lt;type-expression&gt; <b>[</b><b>]</b> <b>{</b> [&lt;array-value-list&gt;] <b>}</b>
 * </pre>
 *
 * @see gw.lang.parser.IGosuParser
 */
public class NewExpression extends Expression implements INewExpression
{
  private IType[] _argTypes;
  private Expression[] _args;
  private IConstructorInfo _constructor;
  private List<Expression> _valueExpressions;
  private List<Expression> _sizeExpressions;
  private int _iArgPos;
  private ITypeLiteralExpression _typeLiteral;
  private IInitializerExpression _initExpr;
  private boolean _anonymous;
  private int[] _namedArgOrder;

  /**
   * Constructs a BeanMethodCallExpression given an ISymbolTable instance.
   */
  public NewExpression()
  {
  }

  /**
   * @return An array of IType for the arguments of the method call.
   */
  public IType[] getArgTypes()
  {
    return _argTypes;
  }

  /**
   * @param argTypes An array of IType for the arguments of the method call.
   */
  public void setArgTypes( IType... argTypes )
  {
    _argTypes = argTypes;
  }

  /**
   * @return An array of expressions for corresponding to the arguments in the
   *         expression.
   */
  public Expression[] getArgs()
  {
    return _args;
  }

  /**
   * @param args An array of expressions for corresponding to the arguments in
   *             the expression.
   */
  public void setArgs( Expression[] args )
  {
    _args = args;
  }

  public IConstructorInfo getConstructor()
  {
    return _constructor;
  }

  public int getArgPosition()
  {
    return _iArgPos;
  }

  public void setArgPosition( int iArgPos )
  {
    _iArgPos = iArgPos;
  }

  /**
   * The constructor for the new operation.
   * <p/>
   * The properties: Constructor, ValueExpressions, and SizeExpression are
   * mutually exclusive.
   */
  public void setConstructor( IConstructorInfo constructor )
  {
    _constructor = constructor;
  }

  /**
   * The value expression for the new array operation.
   * <p/>
   * The properties: Constructor, ValueExpressions, and SizeExpression are
   * mutually exclusive.
   */
  public void setValueExpressions( List<Expression> valueExpressions )
  {
    _valueExpressions = valueExpressions;
  }
  public List<Expression> getValueExpressions()
  {
    return _valueExpressions;
  }

  public void setInitializer( IInitializerExpression initializerExpression )
  {
    _initExpr = initializerExpression;
  }
  public IInitializerExpression getInitializer()
  {
    return _initExpr;
  }


  /**
   * The size expression for the new array operation.
   * <p/>
   * The properties: Constructor, ValueExpressions, and SizeExpression are
   * mutually exclusive.
   */
  public void addSizeExpression( Expression sizeExpression )
  {
    if( _sizeExpressions == null )
    {
      _sizeExpressions = new ArrayList<Expression>( 2 );
    }
    _sizeExpressions.add( sizeExpression );
  }
  public List<Expression> getSizeExpressions()
  {
    return _sizeExpressions;
  }

  public boolean isCompileTimeConstant()
  {
    IInitializerExpression initializer = getInitializer();
    if( initializer != null && initializer.isCompileTimeConstant() )
    {
      // Transform collection initialization to array constant
      return true;
    }
    if( getValueExpressions() != null )
    {
      // Handle Java-style array initialization
      for( Expression v : getValueExpressions() )
      {
        if( !v.isCompileTimeConstant() )
        {
          return false;
        }
      }
      return true;
    }
    if( getSizeExpressions() != null )
    {
      for( Expression s : getSizeExpressions() )
      {
        if( !s.isCompileTimeConstant() )
        {
          return false;
        }
      }
      return true;
    }
    if( getType().isArray() )
    {
      return true;
    }
    // Handle annotations as arguments
    return JavaTypes.ANNOTATION().isAssignableFrom( getType() );
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    IInitializerExpression initializer = getInitializer();
    if( initializer != null )
    {
      return getInitializer().evaluate();
    }
    if( _valueExpressions != null  )
    {
      // Convert to an array for compile-time constant e.g., {a,b,c} is legal array expr for Annotation args
      Class<?> arrayClass = getArrayClass(getType().getComponentType());
      List<Expression> values = _valueExpressions;
      Object instance = Array.newInstance(arrayClass.getComponentType(), values.size());
      for( int i = 0; i < values.size(); i++ )
      {
        IExpression expr = values.get( i );
        Object value = expr.evaluate();
        if( value instanceof BigDecimal || value instanceof BigInteger )
        {
          value = value.toString();
        }
        Array.set( instance, i, value );
      }
      return instance;
    }
    if( _sizeExpressions != null )
    {
      Class<?> arrayClass = getArrayClass(getType().getComponentType());
      return Array.newInstance(arrayClass.getComponentType(), ((Number)_sizeExpressions.get( 0 ).evaluate()).intValue() );
    }
    if( JavaTypes.ANNOTATION().isAssignableFrom( getType() ) )
    {
      ParseTree loc = getLocation();
      boolean bProbablyFromACrappyTest = getGosuClass() == null;
      return new GosuAnnotationInfo( new GosuAnnotation( (ICompilableTypeInternal)getGosuClass(), getType(), this, null, bProbablyFromACrappyTest ? 0 : loc.getOffset(), bProbablyFromACrappyTest ? -1 : loc.getExtent()+1 ),
                                     getGosuClass() == null ? null : getGosuClass().getTypeInfo(), (IGosuClassInternal)getGosuClass() );
    }
    if( getType().isArray() )
    {
      Class<?> arrayClass = getArrayClass(getType().getComponentType());
      return Array.newInstance(arrayClass.getComponentType(), 0);
    }
    if( this instanceof InferredNewExpression )
    {
      return new Object[] {};
    }
    throw new UnsupportedOperationException( "Cannot evaluate new-expression as compile-time constant value." );
  }

  private Class<?> getArrayClass(IType type) {
    return Array.newInstance(getComponentClass(type), 0).getClass();
  }

  private Class<?> getComponentClass(IType type) {
    if( StandardCoercionManager.isBoxed(type) )
    {
      type = TypeSystem.getPrimitiveType(type);
    }
    else if( type.isEnum() )
    {
      // An enum evaluates as the name of the enum constant field (for compile-time constant evaluation)
      type = JavaTypes.STRING();
    }
    else if( JavaTypes.ANNOTATION().isAssignableFrom( type ) )
    {
      type = TypeSystem.get( IAnnotationInfo.class );
    }
    else if( JavaTypes.CLASS().isAssignableFrom( type  ) )
    {
      type = JavaTypes.ITYPE();
    }
    else if( type == JavaTypes.BIG_DECIMAL() || type == JavaTypes.BIG_INTEGER() )
    {
      type = JavaTypes.STRING();
    }
//    else if( !type.isPrimitive() &&
//             type != JavaTypes.STRING() &&
//             !TypeSystem.get( IAnnotationInfo.class ).isAssignableFrom( type ) &&
//             !JavaTypes.CLASS().isAssignableFrom( type  ) )
//    {
//      throw new IllegalStateException( " Illegal type: " + type.getName() + "  A compile-time constant expression must be either primitive, String, Class, or Enum." );
//    }
    Class<?> cls = ((IJavaType)type).getBackingClass();
    cls = cls != null ? cls : getClassForRareCaseWhenRunningIJEditorProjectWhereGosuCoreJavaTypesAreSourceBased( type );
    return cls;
  }

  private Class<?> getClassForRareCaseWhenRunningIJEditorProjectWhereGosuCoreJavaTypesAreSourceBased( IType type ) {
    String fqn = type.getName();
    Class<?> cls = Primitives.get( fqn );
    if( cls == null ) {
      try {
        cls = Class.forName( fqn, false, getClass().getClassLoader() );
      }
      catch( ClassNotFoundException e ) {
        throw new RuntimeException( e );
      }
    }
    return cls;
  }

  @Override
  public String toString()
  {
    StringBuilder strOut = new StringBuilder( "new " );
    if( _constructor != null )
    {
      strOut.append( getType().getName() ).append( "(" );

      if( _args != null && _args.length > 0 )
      {
        strOut.append( " " );
        for( int i = 0; i < _args.length; i++ )
        {
          if( i != 0 )
          {
            strOut.append( ", " );
          }
          strOut.append( _args[i].toString() );
        }
        strOut.append( " " );
      }
      strOut.append( ")" );
      if (_initExpr != null) {
        strOut.append(_initExpr.toString());
      }
      return strOut.toString();
    }
    else if( _valueExpressions != null )
    {
      strOut.append( getType().getName() ).append( " {" );

      for( int i = 0; i < _valueExpressions.size(); i++ )
      {
        if( i != 0 )
        {
          strOut.append( ", " );
        }
        strOut.append( _valueExpressions.get( i ).toString() );
      }
      return strOut.append( "}" ).toString();
    }
    else if( _sizeExpressions != null )
    {
      IType type = getType();
      int iDims = 0;
      do
      {
        iDims++;
        type = type.getComponentType();
      } while( type.isArray() );
      strOut.append( type.getName() );
      for( int i = 0; i < iDims; i++ )
      {
        strOut.append( '[' );
        if( _sizeExpressions.size() > i )
        {
          strOut.append( _sizeExpressions.get( i ).toString() );
        }
        strOut.append( ']' );
      }
      return strOut.toString();
    }
    else
    {
      if (getType() != null && getType().getComponentType() != null) {
        strOut.append( getType().getComponentType().getName() ).append( "[]{}" );
      }
      return strOut.toString();
    }
  }

  public boolean isAnonymousClass()
  {
    return _anonymous;
  }

  public void setAnonymousClass( boolean anonymous )
  {
    _anonymous = anonymous;
  }
  
  public ITypeLiteralExpression getTypeLiteral() {
    return _typeLiteral;
  }
  
  public void setTypeLiteral(ITypeLiteralExpression typeLiteral) {
    _typeLiteral = typeLiteral;
  }

  public int[] getNamedArgOrder()
  {
    return _namedArgOrder;
  }
  public void setNamedArgOrder( int[] namedArgOrder )
  {
    _namedArgOrder = namedArgOrder;
  }
}
