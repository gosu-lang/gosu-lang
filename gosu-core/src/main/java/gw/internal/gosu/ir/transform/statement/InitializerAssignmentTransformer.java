/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.expressions.InitializerAssignment;
import gw.internal.gosu.parser.BeanAccess;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.internal.gosu.ir.nodes.IRProperty;
import gw.internal.gosu.ir.nodes.IRPropertyFactory;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collection;
import java.util.Map;
import java.util.Collections;

/**
 */
public class InitializerAssignmentTransformer extends AbstractStatementTransformer<InitializerAssignment>
{
  public static IRStatement compile( TopLevelTransformationContext cc, InitializerAssignment stmt, IRExpression root )
  {
    InitializerAssignmentTransformer gen = new InitializerAssignmentTransformer( cc, stmt, root );
    return gen.compile( );
  }

  private IRExpression _root;
  private InitializerAssignmentTransformer( TopLevelTransformationContext cc, InitializerAssignment stmt, IRExpression root )
  {
    super( cc, stmt );
    _root = root;
  }

  protected IRStatement compile_impl( )
  {
    IPropertyInfo pi = getPropertyInfo();
    IRProperty irProperty = IRPropertyFactory.createIRProperty(pi);
    IRExpression value = ExpressionTransformer.compile( _stmt().getRhs(), _cc() );
    if( !pi.isWritable( getGosuClass() ) )
    {
      IRExpression property = callMethod( irProperty.getGetterMethod(), _root, Collections.EMPTY_LIST );
      if( JavaTypes.COLLECTION().isAssignableFrom( pi.getFeatureType() ) )
      {
        return buildMethodCall( callMethod( Collection.class, "addAll", new Class[]{Collection.class},
                                            property,
                                            exprList( value ) ) );
      }
      else if( JavaTypes.MAP().isAssignableFrom( pi.getFeatureType() ) )
      {
        return buildMethodCall( callMethod( Map.class, "putAll", new Class[]{Map.class},
                                            property,
                                            exprList( value ) ) );

      }
      else
      {
        throw new IllegalStateException( "Cannot initialize read-only property " + pi.getName() + " that's not a Collection or a Map " );
      }
    }
    else
    {
      if( irProperty.isBytecodeProperty() )
      {
        if ( irProperty.isField() ) {
          return setField( irProperty, _root, value );
        } else {
          return buildMethodCall( callMethod( irProperty.getSetterMethod(), _root, exprList( value ) ) );
        }
      }
      else
      {
        IRExpression typeInfo = callMethod( IType.class, "getTypeInfo", new Class[0], pushType( _stmt().getOwnerType() ), exprList() );
        pushConstant( _stmt().getPropertyName() );
        IRExpression property = callMethod( ITypeInfo.class, "getProperty", new Class[]{CharSequence.class},
                                                    typeInfo,
                                                    exprList( pushConstant( _stmt().getPropertyName() ) ) );
        IRExpression accessor = callMethod( IPropertyInfo.class, "getAccessor", new Class[0], property, exprList() );
        IRExpression setterCall = callMethod( IPropertyAccessor.class, "setValue", new Class[]{Object.class, Object.class},
                                                      accessor,
                                                      exprList( _root, boxValue( value.getType(), value) ) );
        return buildMethodCall( setterCall );
      }
    }
  }

  private IPropertyInfo getPropertyInfo()
  {
    IPropertyInfo pi;
    try
    {
      pi = BeanAccess.getPropertyInfo( _stmt().getOwnerType(), _stmt().getPropertyName(), null, null, null );
    }
    catch( ParseException e )
    {
      throw new RuntimeException( e );
    }
    return pi;
  }
}