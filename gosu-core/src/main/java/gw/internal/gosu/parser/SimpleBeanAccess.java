/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.IFeatureFilter;

/**
 * BeanAccess is a helper class to facilitate JavaBean introspection esp. Descriptor
 * hierarchy navigation. Its primary function is to aid in Property and Method type
 * discovery as well as Property and Method evaluation and invocation, respectively.
 */
public class SimpleBeanAccess
{
  /**
   * private to enforce singleton access.
   */
  private SimpleBeanAccess()
  {
  }


  /**
   * Resolves the property directly, as if the type were requesting it, giving access to all properties
   */
  public static IPropertyInfo getPropertyInfoDirectly( IType classBean, String strProperty )
  {
    return getPropertyInfo( classBean, classBean, strProperty, null, null, null );    
  }

  public static IPropertyInfo getPropertyInfo( IType classBean, String strProperty, IFeatureFilter filter, ParserBase parser, IScriptabilityModifier scriptabilityConstraint)
  {
    IType whosaskin = classBean;
    if( parser != null )
    {
      whosaskin = parser.getGosuClass();
      // Hack to ensure that we adhere to visibility rules when parsing
      whosaskin = whosaskin != null || GosuClassTypeInfo.isIncludeAll() ? whosaskin : JavaTypes.OBJECT();
      if( GosuClassTypeInfo.isIncludeAll() && whosaskin instanceof IGosuProgram &&
          // Well... downstream we check for null on whosaskin and ignore TypeSystem.isIncludeAll(), that can't happen for CompileTimeExpressionParser
          !whosaskin.getName().startsWith( IGosuProgram.PACKAGE )  )
      {
        whosaskin = null;
      }
    }
    return getPropertyInfo( classBean, whosaskin, strProperty, filter, parser, scriptabilityConstraint );
  }

  public static IPropertyInfo getPropertyInfo( IType classBean, IType whosAskin, String strProperty, IFeatureFilter filter, ParserBase parser, IScriptabilityModifier scriptabilityConstraint)
  {
    if( classBean == null )
    {
      return null;
    }

    ITypeInfo beanInfo = classBean.getTypeInfo();
    if( beanInfo == null )
    {
      return null;
    }

    int iArrayBracket = strProperty.indexOf( '[' );
    if( iArrayBracket > 0 )
    {
      strProperty = strProperty.substring( 0, iArrayBracket );
    }

    IPropertyInfo property = getProperty( beanInfo, whosAskin, strProperty );
    if( property != null )
    {
      if( !BeanAccess.isDescriptorHidden(property) &&
          (filter == null || filter.acceptFeature(classBean, property )) )
      {
        if( !property.isVisible(scriptabilityConstraint) )
        {
          return null;
        }
        return property;
      }
    }
    else
    {
      IType componentType = TypeLord.getExpandableComponentType( classBean );
      if( componentType != null )
      {
        // parser must be != then null otherwise we we have a stack overflow by recursing in getPropertyInfo
        if( parser != null )
        {
          // Allow expressions of the form: <array-of-foo>.<property-of-foo>. The
          // result of evaluating such an expression is of type array-of-property-type
          IPropertyInfo propertyInfo = getPropertyInfo(componentType, strProperty, filter, parser, scriptabilityConstraint);
          if (propertyInfo != null) {
            return new ArrayExpansionPropertyInfo(propertyInfo);
          }
        }
      }
    }

    return null;
  }

  public static IPropertyInfo getProperty( ITypeInfo beanInfo, IType classBean, String strMember )
  {
    IPropertyInfo property;
    if( beanInfo instanceof IRelativeTypeInfo)
    {
      property = ((IRelativeTypeInfo)beanInfo).getProperty( classBean, strMember );
    }
    else
    {
      property = beanInfo.getProperty( strMember );
    }

    return property;
  }
}
