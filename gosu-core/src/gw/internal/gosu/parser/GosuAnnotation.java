/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.AnnotationExpression;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.java.JavaTypes;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Holds annotation information for a feature
 */
public class GosuAnnotation implements Serializable, IGosuAnnotation
{
  private ICompilableTypeInternal _enclosingType;
  private IType _type;
  private Expression _expression;
  private int _start;
  private int _end;

  public GosuAnnotation( ICompilableTypeInternal enclosingType, IType type, Expression expression, int iOffset, int end )
  {
    _enclosingType = enclosingType;
    _type = type;
    _expression = expression;
    _start = iOffset;
    _end = end;

  }

  public String getName()
  {
    return getType().getRelativeName();
  }

  public IType getType()
  {
    return _type;
  }

  @Override
  public String getNewExpressionAsString()
  {
    // what, you were expecting something more sophisictated?
    if (_start > _end) {
      return "";
    }
    String typeName = _enclosingType.getSource().substring( _start, _end );
    if( typeName.startsWith( "@" ) )
    {
      typeName = typeName.substring( 1 );
    }
    String s = "new " + typeName;
    if( !s.contains( "(" ) ) // I'll just rub some contains on it...
    {
      s += "()";
    }
    return s;
  }

  public Expression getExpression()
  {
    return _expression;
  }

  @Override
  public void clearExpression() {
    _expression = null;
  }

  public boolean shouldPersistToClass()
  {
    return isJavaAnnotation() && !hasRetentionPolicy(RetentionPolicy.SOURCE) && !hasBadArgs();
  }

  @Override
  public boolean shouldRetainAtRuntime() {
    return isJavaAnnotation() && hasRetentionPolicy(RetentionPolicy.RUNTIME) && !hasBadArgs();
  }

  private boolean isJavaAnnotation()
  {
    return JavaTypes.ANNOTATION().isAssignableFrom( getType() );
  }

  private boolean hasRetentionPolicy(RetentionPolicy policy) {
    List<IAnnotationInfo> annotation = _type.getTypeInfo().getAnnotationsOfType( TypeSystem.get(Retention.class) );
    if( annotation != null )
    {
      for( IAnnotationInfo annotationInfo : annotation )
      {
        String value = (String) annotationInfo.getFieldValue("value");
        if( value.equals(policy.name()) )
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isSourceAnnotation()
  {
    List<IAnnotationInfo> annotations = _type.getTypeInfo().getAnnotationsOfType( JavaTypes.getJreType(Retention.class) );
    if( annotations != null )
    {
      for( IAnnotationInfo annotationInfo : annotations )
      {
        String fieldValue = (String) annotationInfo.getFieldValue( "value" );
        if( fieldValue.equals( RetentionPolicy.SOURCE.name() ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean hasBadArgs()
  {
    if(_expression instanceof AnnotationExpression ){
      if( ((AnnotationExpression)_expression).getArgs() != null )
      {
        for( Expression expr : ((AnnotationExpression)_expression).getArgs() )
        {
          if( !expr.isCompileTimeConstant() )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public ICompilableType getOwnersType()
  {
    return _enclosingType;
  }

  public void clearDebugInfo() {
    if (_expression != null) {
      _expression.clearParseTreeInformation();
    }
  }

}
