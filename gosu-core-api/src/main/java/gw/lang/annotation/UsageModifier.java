/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.annotation;

import gw.lang.GosuShop;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
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
   * Use None to specify this annotation can only appear once on a class
   */
  One,
  /**
   * Use None to specify this annotation can appear many times on a class
   */
  Many;

  public static UsageModifier getUsageModifier( UsageTarget targetType, IType annotationType )
  {
    UsageModifier modifier = null;
    //First look for gosu-style usage annotations
    ArrayList<IAnnotationInfo> usageInfos = getExplicitUsageAnnotations(annotationType);
    if( usageInfos != null && usageInfos.size() > 0 )
    {
      return getUsageModifier( targetType, modifier, usageInfos );
    }
    // if it's a java annotation with no explicit annotation usage, translate the java element type information
    else if( JavaTypes.ANNOTATION().isAssignableFrom( annotationType ) )
    {
      return translateJavaElementTypeToUsageModifier( targetType, annotationType );
    }
    else
    {
      // By default, gosu annotations can appear multiple times
      return UsageModifier.Many;
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

  private static UsageModifier translateJavaElementTypeToUsageModifier( UsageTarget targetType, IType annotationType )
  {
    IAnnotationInfo targetAnnotation = annotationType.getTypeInfo().getAnnotation( TypeSystem.get( Target.class ) );
    boolean bRepeatable = annotationType.getTypeInfo().hasAnnotation( JavaTypes.REPEATABLE() );

    if( targetAnnotation == null )
    {
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
            elementTypeConst.equals( ElementType.FIELD.name() ) && targetType == UsageTarget.PropertyTarget ||
            elementTypeConst.equals( ElementType.ANNOTATION_TYPE.name() ) && targetType == UsageTarget.TypeTarget ||
            elementTypeConst.equals( ElementType.TYPE.name() ) && targetType == UsageTarget.TypeTarget ||
            elementTypeConst.equals( ElementType.METHOD.name() ) && (targetType == UsageTarget.MethodTarget || targetType == UsageTarget.PropertyTarget) ||
            elementTypeConst.equals( ElementType.PARAMETER.name() ) && targetType == UsageTarget.ParameterTarget ) {
          return bRepeatable ? UsageModifier.Many : UsageModifier.One;
        }
      }
      return UsageModifier.None;
    }
  }


}
