/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotation;

import gw.lang.GosuShop;
import gw.lang.parser.AnnotationUseSiteTarget;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.JavaTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UsageModifier {
  /**
   * Use None to specify this annotation cannot exist on a class
   */
  None,
  /**
   * Use One to specify this annotation can only appear once on a class
   */
  One,
  /**
   * Use Many to specify this annotation can appear many times on a class
   */
  Many;

  public static UsageModifier getUsageModifier( UsageTarget targetType, IAnnotationInfo annotation )
  {
    return getUsageModifier( null, targetType, annotation.getType(), annotation.getTarget() );
  }
  public static UsageModifier getUsageModifier( IParsedElement pe, UsageTarget targetType, IType annotationType, AnnotationUseSiteTarget target )
  {
    UsageModifier modifier = null;
    //First look for gosu-style usage annotations
    ArrayList<IAnnotationInfo> usageInfos = getExplicitUsageAnnotations( annotationType );
    if( usageInfos != null && usageInfos.size() > 0 )
    {
      return getUsageModifier( targetType, modifier, usageInfos );
    }
    else
    {
      // if it's a java annotation with no explicit annotation usage, translate the java element type information
      if( JavaTypes.ANNOTATION().isAssignableFrom( annotationType ) )
      {
        return translateJavaElementTypeToUsageModifier( pe, targetType, annotationType, target );
      }
      else
      {
        // By default, gosu annotations can appear multiple times
        return UsageModifier.Many;
      }
    }
  }


  private static ArrayList<IAnnotationInfo> getExplicitUsageAnnotations(IType type)
  {
    ArrayList<IAnnotationInfo> lst = new ArrayList<IAnnotationInfo>();
    List<IAnnotationInfo> usageAnnotations = type.getTypeInfo().getAnnotationsOfType( JavaTypes.ANNOTATION_USAGE() );
    if( usageAnnotations != null )
    {
      lst.addAll(usageAnnotations);
    }
    List<IAnnotationInfo> usagesAnnotations = type.getTypeInfo().getAnnotationsOfType( JavaTypes.ANNOTATION_USAGES() );
    if( usagesAnnotations != null )
    {
      for( IAnnotationInfo iAnnotationInfo : usagesAnnotations )
      {
        IAnnotationInfo[] values = (IAnnotationInfo[]) GosuShop.getAnnotationFieldValueAsArray(iAnnotationInfo, "value");
        lst.addAll( Arrays.asList(values) );
      }
    }
    return lst;
  }

  private static UsageModifier getUsageModifier( UsageTarget targetType, UsageModifier modifier, ArrayList<IAnnotationInfo> annotationInfos )
  {
    //If there are usages, then we must examine each one to find the one that most specifically applies
    for( IAnnotationInfo usage : annotationInfos )
    {
      String target = (String) usage.getFieldValue("target");
      String usageModifier = (String) usage.getFieldValue("usageModifier");

      // If usage applies to all, and we haven't had a more specific match yet, get the modifier for it
      if( target.equals(UsageTarget.AllTarget.name()) && modifier == null  )
      {
        modifier = UsageModifier.valueOf(usageModifier);
      }
      // If usage applies to the given target, it always overrides whatever else we've seen
      if( target.equals(targetType.name()) )
      {
        modifier = UsageModifier.valueOf(usageModifier);
      }
    }
    // if no usage matched, then that implies that the usage is None.
    if( modifier == null )
    {
      modifier = UsageModifier.None;
    }
    return modifier;
  }

  private static UsageModifier translateJavaElementTypeToUsageModifier( IParsedElement pe, UsageTarget targetType, IType annotationType, AnnotationUseSiteTarget target )
  {
    IAnnotationInfo targetAnnotation = annotationType.getTypeInfo().getAnnotation( TypeSystem.get( Target.class ) );
    boolean bRepeatable = annotationType.getTypeInfo().hasAnnotation( JavaTypes.REPEATABLE() );

    if( targetAnnotation == null )
    {
      if( target != null )
      {
        if( !targetAppliesToParsedElement( pe, target ) )
        {
          return UsageModifier.None;
        }
        // return Many here since we check for repeats in individual parts of properties later (field, get, set), during makeProperties()
        return UsageModifier.Many;
      }
      return bRepeatable ? UsageModifier.Many : UsageModifier.One;
    }
    else
    {
      Object v = targetAnnotation.getFieldValue( "value" );
      String[] value;
      if( v == null ) {
        value = null;
      }
      else if( v.getClass().isArray() ) {
        if( v instanceof String[] ) {
          value = (String[])v;
        }
        else {
          ElementType[] elems = (ElementType[])v;
          value = new String[elems.length];
          for( int i = 0; i < elems.length; i++ ) {
            value[i] = elems[i].name();
          }
        }
      }
      else {
        if( v instanceof String ) {
          value = new String[]{(String)v};
        }
        else {
          value = new String[]{((ElementType)v).name()};
        }
      }
      if( value == null || value.length == 0 ) {
        return bRepeatable ? UsageModifier.Many : UsageModifier.One; // If there are no targets, it can be used everywhere
      }

      // otherwise, look for a target that matches our own UsageTarget
      for( String elementTypeConst : value ) {
        if( elementTypeConst.equals( ElementType.CONSTRUCTOR.name() ) && targetType == UsageTarget.ConstructorTarget ||
            elementTypeConst.equals( ElementType.ANNOTATION_TYPE.name() ) && targetType == UsageTarget.TypeTarget ||
            elementTypeConst.equals( ElementType.TYPE.name() ) && targetType == UsageTarget.TypeTarget ||
            elementTypeConst.equals( ElementType.PARAMETER.name() ) && targetType == UsageTarget.ParameterTarget ) {
          return bRepeatable ? UsageModifier.Many : UsageModifier.One;
        }
        else if( elementTypeConst.equals( ElementType.PARAMETER.name() ) && targetType == UsageTarget.PropertyTarget )
        {
          if( target != null )
          {
            if( !targetAppliesToParsedElement( pe, target ) )
            {
              return UsageModifier.None;
            }
          }
          // return Many here since we check for repeats in individual parts of properties later (field, get, set), during makeProperties()
          return UsageModifier.Many;
        }
        else if( elementTypeConst.equals( ElementType.FIELD.name() ) && targetType == UsageTarget.PropertyTarget )
        {
          if( target != null )
          {
            if( !targetAppliesToParsedElement( pe, target ) )
            {
              return UsageModifier.None;
            }
          }
          // return Many here since we check for repeats in individual parts of properties later (field, get, set), during makeProperties()
          return UsageModifier.Many;
        }
        else if( elementTypeConst.equals( ElementType.METHOD.name() ) && (targetType == UsageTarget.MethodTarget || targetType == UsageTarget.PropertyTarget) )
        {
          if( target != null )
          {
            if( targetType == UsageTarget.MethodTarget )
            {
              // the annotation is on a method, but only shorthand property declarations can have a specific targets e.g., set, get, field
              return UsageModifier.None;
            }
            if( !targetAppliesToParsedElement( pe, target ) )
            {
              return UsageModifier.None;
            }
            // return Many here since we check for repeats in individual parts of properties later (field, get, set), during makeProperties()
            return UsageModifier.Many;
          }

          return bRepeatable ? UsageModifier.Many : UsageModifier.One;
        }
      }
      return UsageModifier.None;
    }
  }

  public static boolean targetAppliesToParsedElement( IParsedElement pe, AnnotationUseSiteTarget target )
  {
    if( target == null )
    {
      return true;
    }

    if( pe instanceof IVarStatement && ((IVarStatement)pe).hasProperty() )
    {
      IVarStatement varStmt = (IVarStatement)pe;
      IDynamicPropertySymbol property = varStmt.getProperty();
      switch( target )
      {
        case get:
          return property != null && property.getGetterDfs() != null;

        case set:
        case param:
          return property != null && property.getSetterDfs() != null;

        case accessors:
          return property != null;

        case field:
          return property != null &&
            (property.getGetterDfs() != null && !property.getGetterDfs().isAbstract() ||
             property.getSetterDfs() != null && !property.getSetterDfs().isAbstract());
      }
    }
    else if( pe instanceof IFunctionStatement &&
             pe.getGosuClass() instanceof IGosuEnhancement &&
             !((IFunctionStatement)pe).getDynamicFunctionSymbol().isStatic() )
    {
      return target == AnnotationUseSiteTarget.receiver;
    }
    return false;
  }
}
