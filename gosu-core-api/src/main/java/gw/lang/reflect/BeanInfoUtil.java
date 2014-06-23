/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.UnstableAPI;
import gw.lang.parser.ScriptabilityModifiers;

import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.FeatureDescriptor;

@UnstableAPI
public class BeanInfoUtil
{
  /**
   * A FeatureDescriptor value indicating its visibility to Gosu
   */
  public static final String SCRIPT_VISIBILITY_MODIFIERS = "_scriptVisibilityModifiers";

  /**
   * To save allocations.
   */
  protected static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
  protected static final String[] EMPTY_STRING_ARRAY = new String[0];


  /**
   * Builds a no-arg method descriptor that is exposed for scripting everywhere.
   */
  public static MethodDescriptor buildScriptableMethodDescriptorNoArgs( Class actionClass, String methodName )
  {
    MethodDescriptor md = _buildMethodDescriptor( actionClass, methodName, EMPTY_STRING_ARRAY, EMPTY_CLASS_ARRAY, EMPTY_CLASS_ARRAY );
    makeScriptable( md );
    return md;
  }

  /**
   * Builds a method descriptor that is exposed for scripting everywhere.
   */
  public static MethodDescriptor buildScriptableMethodDescriptor(
    Class actionClass, String methodName, String[] parameterNames, Class[] parameterTypes )
  {
    MethodDescriptor md = _buildMethodDescriptor( actionClass, methodName, parameterNames, parameterTypes, parameterTypes );
    makeScriptable( md );
    return md;
  }

  /**
   * Builds a deprecated method descriptor that is exposed for scripting everywhere.
   */
  public static MethodDescriptor buildScriptableDeprecatedMethodDescriptor(
    Class actionClass, String methodName, String[] parameterNames, Class[] parameterTypes )
  {
    MethodDescriptor md = _buildMethodDescriptor( actionClass, methodName, parameterNames, parameterTypes, parameterTypes );
    makeScriptableDeprecated( md );
    return md;
  }

  /**
   * Completely hides a method from scripting.
   */
  public static MethodDescriptor buildHiddenMethodDescriptor(
    Class actionClass, String methodName, String[] parameterNames, Class[] parameterTypes )
  {
    MethodDescriptor md = _buildMethodDescriptor( actionClass, methodName, parameterNames, parameterTypes, parameterTypes );
    md.setHidden( true );
    return md;
  }

  /**
   * @deprecated To properly expose a method for scripting call buildScriptableMethodDescriptor() or a derivative of that.
   */
  public static MethodDescriptor buildMethodDescriptor(
    Class actionClass, String methodName, String[] parameterNames, Class[] parameterTypes )
  {
    return _buildMethodDescriptor( actionClass, methodName, parameterNames, parameterTypes, parameterTypes );
  }

  /**
   * Builds a scriptable property descriptor with the given information.
   *
   * @param propertyName name of the property
   * @param beanClass    class of the bean on which the property is found
   * @param getterName   name of the getter method, or null if write-only
   * @param propertyName name of the setter method, or null if read-only
   */
  static public TypedPropertyDescriptor buildScriptablePropertyDescriptor(
    String propertyName, Class beanClass, String getterName, String setterName )
  {
    TypedPropertyDescriptor pd = _buildPropertyDescriptor( propertyName, beanClass, getterName, setterName );
    makeScriptable( pd );
    return pd;
  }

  /**
   * Expose the method/property descriptor for scripting.
   *
   * @param descriptor A method or property descriptor.
   * @return The same descriptor.
   */
  public static FeatureDescriptor makeScriptable( FeatureDescriptor descriptor )
  {
    setVisibilityModifier( descriptor, ScriptabilityModifiers.SCRIPTABLE );
    return descriptor;
  }

  /**
   * Expose the method/property descriptor for scripting, but mark it
   * deprecated.
   *
   * @param descriptor A method or property descriptor.
   * @return The same descriptor.
   */
  public static FeatureDescriptor makeScriptableDeprecated( FeatureDescriptor descriptor )
  {
    setVisibilityModifier( descriptor, ScriptabilityModifiers.SCRIPTABLE_DEPRECATED );
    return descriptor;
  }

  /**
   * @return True if the descriptor is exposed for scripting.
   */
  public static boolean isScriptable( FeatureDescriptor descriptor )
  {
    return isVisible( descriptor, ScriptabilityModifiers.SCRIPTABLE );
  }

  /**
   * @return True if the descriptor is deprecated.
   */
  public static boolean isDeprecated( FeatureDescriptor descriptor )
  {
    IScriptabilityModifier modifier = getVisibilityModifier( descriptor );
    if( modifier == null )
    {
      return false;
    }

    return modifier.hasModifierWithType( ScriptabilityModifierTypes.DEPRECATED );
  }

  /**
   * Determine if the descriptor is visible given a visibility constraint.
   */
  public static boolean isVisible( FeatureDescriptor descriptor, IScriptabilityModifier constraint )
  {
    if( constraint == null )
    {
      return true;
    }

    IScriptabilityModifier modifier = getVisibilityModifier( descriptor );
    if( modifier == null )
    {
      return true;
    }

    return modifier.satisfiesConstraint( constraint );
  }

  /**
   * @param descriptor A method or property descriptor.
   * @return The descriptor's associated visibility modifier.
   */
  public static IScriptabilityModifier getVisibilityModifier( FeatureDescriptor descriptor )
  {
    return (IScriptabilityModifier)descriptor.getValue( BeanInfoUtil.SCRIPT_VISIBILITY_MODIFIERS );
  }

  /**
   * @param descriptor A method or property descriptor.
   * @param modifier The descriptor's associated visibility modifier.
   */
  public static void setVisibilityModifier( FeatureDescriptor descriptor, IScriptabilityModifier modifier )
  {
    descriptor.setValue( BeanInfoUtil.SCRIPT_VISIBILITY_MODIFIERS, modifier );
  }

  /**
   * Builds a method descriptor with no explicit visibility.
   */
  protected static MethodDescriptor _buildMethodDescriptor(
    Class actionClass, String methodName, String[] parameterNames, Class[] parameterTypes, Class[] actualParameterTypes )
  {
    MethodDescriptor method;
    assert (parameterNames.length == parameterTypes.length) : "Number of parameter names different from number of parameter types.";
    assert (parameterNames.length == actualParameterTypes.length) :
            "Number of parameter names different from number of actual parameters.";

    try
    {
      int numParams = parameterNames.length;
      ParameterDescriptor[] parameters = new ParameterDescriptor[numParams];
      for( int i = 0; i < numParams; i++ )
      {
        parameters[i] = new TypedParameterDescriptor( parameterNames[i], parameterTypes[i] );
      }

      method = new MethodDescriptor( actionClass.getMethod( methodName, actualParameterTypes ), parameters );
    }
    catch( NoSuchMethodException e )
    {
      throw new RuntimeException( e );
    }
    catch( SecurityException e )
    {
      throw new RuntimeException( e );
    }
    return method;
  }

  /**
   * Builds a property descriptor with no explicit visibility.
   */
  protected static TypedPropertyDescriptor _buildPropertyDescriptor(
    String propertyName, Class beanClass, String getterName, String setterName )
  {
    try
    {
      return new TypedPropertyDescriptor( propertyName, beanClass, getterName, setterName );
    }
    catch( IntrospectionException e )
    {
      throw new RuntimeException( "Failed to create property \"" + propertyName + "\" on class \"" + beanClass +
                                  "\": " + e.getClass() + " - " + e.getMessage() );
    }
  }
}
