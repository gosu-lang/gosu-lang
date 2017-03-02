/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.expressions.ConditionalExpression;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.reflect.IConstructorType;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.IFeatureFilter;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;

import java.util.List;

/**
 */
public class BeanAccess
{
  /**
   * private to enforce singleton access.
   */
  private BeanAccess()
  {
  }

  /**
   * Returns true if the method or property is hidden or otherwise not scriptable.
   */
  public static boolean isDescriptorHidden( IAttributedFeatureInfo descriptor )
  {
    return descriptor.isHidden() || !descriptor.isScriptable();
  }

  ////////// type lookup stuff ///////////
  public static boolean isBeanType( IType typeSource )
  {
    return
      typeSource != GosuParserTypes.STRING_TYPE() &&
      typeSource != GosuParserTypes.BOOLEAN_TYPE() &&
     // typeSource != GosuParserTypes.DATETIME_TYPE() &&
      typeSource != GosuParserTypes.NULL_TYPE() &&
      typeSource != GosuParserTypes.NUMBER_TYPE() &&
      !typeSource.isPrimitive() &&
      !typeSource.isArray() &&
      !(typeSource instanceof IFunctionType) &&
      !(typeSource instanceof IConstructorType) &&
      !(typeSource instanceof MetaType);
  }

  public static boolean isNumericType( IType intrType )
  {
    if( intrType == null )
    {
      return false;
    }

    return (intrType.isPrimitive() &&
            intrType != JavaTypes.pBOOLEAN() &&
            intrType != JavaTypes.pVOID()) ||
           JavaTypes.BYTE() == intrType ||
           JavaTypes.SHORT() == intrType ||
           JavaTypes.CHARACTER() == intrType ||
           JavaTypes.INTEGER() == intrType ||
           JavaTypes.LONG() == intrType ||
           JavaTypes.FLOAT() == intrType ||
           JavaTypes.DOUBLE() == intrType ||
           JavaTypes.BIG_INTEGER() == intrType ||
           JavaTypes.BIG_DECIMAL() == intrType ||
           JavaTypes.RATIONAL() == intrType ||
           isDimension( intrType );
  }

  private static boolean isDimension( IType intrType )
  {
//    if( intrType instanceof IGosuClass && TypeSystem.getExecutionEnvironment().isSingleModuleMode() )
//    {
//      // We want to avoid calling IGosuClass.isAssignableFrom() during runtime as it requires header parsing
//      return IDimension.class.isAssignableFrom( ((IGosuClass)intrType).getBackingClass() );
//    }
    return JavaTypes.IDIMENSION().isAssignableFrom( intrType );
  }

  public static boolean isBoxedTypeFor( IType primitiveType, IType boxedType )
  {
    if( primitiveType != null && primitiveType.isPrimitive() )
    {
      if( primitiveType == JavaTypes.pBOOLEAN() && boxedType == JavaTypes.BOOLEAN() )
      {
        return true;
      }
      if( primitiveType == JavaTypes.pBYTE() && boxedType == JavaTypes.BYTE() )
      {
        return true;
      }
      if( primitiveType == JavaTypes.pCHAR() && boxedType == JavaTypes.CHARACTER() )
      {
        return true;
      }
      if( primitiveType == JavaTypes.pDOUBLE() && boxedType == JavaTypes.DOUBLE() )
      {
        return true;
      }
      if( primitiveType == JavaTypes.pFLOAT() && boxedType == JavaTypes.FLOAT() )
      {
        return true;
      }
      if( primitiveType == JavaTypes.pINT() && boxedType == JavaTypes.INTEGER() )
      {
        return true;
      }
      if( primitiveType == JavaTypes.pLONG() && boxedType == JavaTypes.LONG() )
      {
        return true;
      }
      if( primitiveType == JavaTypes.pSHORT() && boxedType == JavaTypes.SHORT() )
      {
        return true;
      }
    }
    return false;
  }

  ///////// Equality stuff -- should this be here ////////

  /**
   * Returns true if the values are logically equal, false otherwise.
   * <p/>
   * Perform an equality comparison.  Heuristics for evaluation follow:
   * <ul>
   * <li> If the LHS is a Number, coerce the RHS to a Number value and compare.
   * <li> If the LHS is a String, coerce the RHS to a String value and String compare.
   * <li> (Ditto for Boolean and DateTime).
   * <li> If the LHS is a Bean,
   * <ul>
   * <li> If the RHS is a Bean, compare with equals().
   * <li> Otherwise, attempt to determine which operand is coercible to the other, coerce, and compare with equals().
   * </ul>
   * <li> Otherwise, compare with equals().
   * </ul>
   */
  public static boolean areValuesEqual( IType lhsType, Object lhsValue,
                                        IType rhsType, Object rhsValue )
  {
    //
    // Note we do *not* verify the compatibility of the lhs & rhs types. That
    // is too much overhead. Instead compare the types only when you need to.
    //

    if( lhsValue == null && rhsValue == null )
    {
      return true;
    }

    if( rhsValue == null || lhsValue == null )
    {
      return false;
    }

    boolean bValue;


    //## todo: This is insane. THe equality operator should be symmetric; we should not be looking at the lhs type!

    if( BeanAccess.isNumericType( lhsType ) )
    {
      bValue = ConditionalExpression.compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    else if( lhsType == GosuParserTypes.STRING_TYPE() )
    {
      bValue = CommonServices.getCoercionManager().makeStringFrom( lhsValue ).equals( CommonServices.getCoercionManager().makeStringFrom( rhsValue ) );
    }
    else if( lhsType == GosuParserTypes.BOOLEAN_TYPE() ||
             lhsType == JavaTypes.pBOOLEAN() )
    {
      bValue = CommonServices.getCoercionManager().makeBooleanFrom( lhsValue ).booleanValue() == CommonServices.getCoercionManager().makeBooleanFrom( rhsValue ).booleanValue();
    }
    else if( lhsType == GosuParserTypes.DATETIME_TYPE() )
    {
      bValue = CommonServices.getCoercionManager().makeDateFrom( lhsValue ).equals( CommonServices.getCoercionManager().makeDateFrom( rhsValue ) );
    }
    else if( isBeanType( lhsType ) )
    {
      if( isBeanType( rhsType ) )
      {
        bValue = areBeansEqual( lhsValue, rhsValue );
      }
      else
      {
        bValue = areObjectsLogicallyEqual( lhsType, rhsType, lhsValue, rhsValue );
      }
    }
    else if( lhsType.isArray() && rhsType.isArray() )
    {
      try
      {
        bValue = true;
        // Determine which operand to convert (support symmetry)
        CommonServices.getCoercionManager().verifyTypesComparable( lhsType, rhsType, true );
        int lhsLength = lhsType.getArrayLength( lhsValue );
        int rhsLength = rhsType.getArrayLength( rhsValue );
        if( lhsLength == rhsLength )
        {
          for( int i = 0; i < lhsLength; i++ )
          {
            bValue &= areValuesEqual( lhsType.getComponentType(), lhsType.getArrayComponent( lhsValue, i ),
                                      rhsType.getComponentType(), rhsType.getArrayComponent( rhsValue, i ) );
            if( !bValue )
            {
              break;
            }
          }
        }
        else
        {
          bValue = false;
        }
      }
      catch( ParseIssue e )
      {
        bValue = false;
      }
    }
    else
    {
      bValue = areObjectsLogicallyEqual( lhsType, rhsType, lhsValue, rhsValue );
    }

    return bValue;
  }

  private static boolean areObjectsLogicallyEqual( IType lhsType, IType rhsType, Object lhsValue, Object rhsValue )
  {
    if( lhsValue instanceof IType && rhsValue instanceof IType )
    {
      return lhsValue.equals( rhsValue );
    }

    boolean bValue;
    IType type;
    try
    {
      // Determine which operand to convert (support symmetry)
      type = CommonServices.getCoercionManager().verifyTypesComparable( lhsType, rhsType, true );
    }
    catch( ParseIssue e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    boolean bConvertLhsType = type != lhsType;
    if( bConvertLhsType )
    {
      lhsValue = CommonServices.getCoercionManager().convertValue(lhsValue, rhsType);
      if( lhsValue == StandardCoercionManager.NO_DICE )
      {
        return false;
      }
    }
    else
    {
      rhsValue = CommonServices.getCoercionManager().convertValue(rhsValue, lhsType);
      if( rhsValue == StandardCoercionManager.NO_DICE )
      {
        return false;
      }
    }
    bValue = lhsValue.equals( rhsValue );
    return bValue;
  }


  /**
   * Test for equality between two BeanType values. Note this method is not entirely appropriate for
   * non-BeanType value i.e., it's not as intelligent as EqualityExpression comparing primitive types,
   * TypeKeys, etc.
   * <p/>
   * Msotly this method is for handling conversion of KeyableBean and Key for comparison.
   *
   * @param bean1 A value having an IType of BeanType
   * @param bean2 A value having an IType of BeanType
   *
   * @return True if the beans are equal
   */
  public static boolean areBeansEqual( Object bean1, Object bean2 )
  {
    if( bean1 == bean2 )
    {
      return true;
    }

    if( bean1 == null || bean2 == null )
    {
      return false;
    }

    IType class1 = TypeLoaderAccess.instance().getIntrinsicTypeFromObject( bean1 );
    IType class2 = TypeLoaderAccess.instance().getIntrinsicTypeFromObject( bean2 );
    if( class1.isAssignableFrom( class2 ) || class2.isAssignableFrom( class1 ) )
    {
      return bean1.equals( bean2 );
    }

    if( CommonServices.getEntityAccess().isDomainInstance( bean1 ) ||
        CommonServices.getEntityAccess().isDomainInstance( bean2 ) )
    {
      return CommonServices.getEntityAccess().areBeansEqual( bean1, bean2 );
    }

    return bean1.equals( bean2 );
  }

  public static List<? extends IPropertyInfo> getProperties( ITypeInfo beanInfo, IType classBean )
  {
    if( beanInfo instanceof IRelativeTypeInfo )
    {
      return ((IRelativeTypeInfo)beanInfo).getProperties( classBean );
    }
    else
    {
      return beanInfo.getProperties();
    }
  }

  public static MethodList getMethods( ITypeInfo beanInfo, IType whosaskin  )
  {
    if( beanInfo instanceof IRelativeTypeInfo && whosaskin != null )
    {
      return ((IRelativeTypeInfo)beanInfo).getMethods( whosaskin );
    }
    else
    {
      return beanInfo.getMethods();
    }
  }

  /**
   * Resolves the property directly, as if the type were requesting it, giving access to all properties
   */
  public static IPropertyInfo getPropertyInfoDirectly( IType classBean, String strProperty ) throws ParseException
  {
    return getPropertyInfo( classBean, classBean, strProperty, null, null, null );    
  }

  public static IPropertyInfo getPropertyInfo( IType classBean, String strProperty, IFeatureFilter filter, ParserBase parser, IScriptabilityModifier scriptabilityConstraint) throws ParseException
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

  public static IPropertyInfo getPropertyInfo( IType classBean, IType whosAskin, String strProperty, IFeatureFilter filter, ParserBase parser, IScriptabilityModifier scriptabilityConstraint) throws ParseException
  {
    if( classBean == null )
    {
      throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_BEAN_CLASS_IS_NULL );
    }

    ITypeInfo beanInfo = classBean.getTypeInfo();
    if( beanInfo == null )
    {
      throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_NO_EXPLICIT_TYPE_INFO_FOUND, classBean.getName() );
    }

    int iArrayBracket = strProperty.indexOf( '[' );
    if( iArrayBracket > 0 )
    {
      strProperty = strProperty.substring( 0, iArrayBracket );
    }

    IPropertyInfo property = getProperty( beanInfo, whosAskin, strProperty );
    if( property != null )
    {
      if( !isDescriptorHidden( property ) &&
          (filter == null || filter.acceptFeature(classBean, property )) )
      {
        if( !property.isVisible(scriptabilityConstraint) )
        {
          throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_PROPERTY_NOT_VISIBLE, property.getName() );
        }
        return property;
      }
    }

    throw new PropertyNotFoundException( strProperty, classBean, parser == null ? null : parser.makeFullParserState() );
  }

  /**
   * Resolves the property directly, as if the type were requesting it, giving access to all properties
   */
  public static IPropertyInfo getPropertyInfoDirectly_NoException( IType classBean, String strProperty )
  {
    return getPropertyInfo_NoException( classBean, classBean, strProperty, null, null, null );
  }

  public static IPropertyInfo getPropertyInfo_NoException( IType classBean, String strProperty, IFeatureFilter filter, ParserBase parser, IScriptabilityModifier scriptabilityConstraint)
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
    return getPropertyInfo_NoException( classBean, whosaskin, strProperty, filter, parser, scriptabilityConstraint );
  }

  private static IPropertyInfo getPropertyInfo_NoException( IType classBean, IType whosAskin, String strProperty, IFeatureFilter filter, ParserBase parser, IScriptabilityModifier scriptabilityConstraint )
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
