/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

/**
 * Package private implementation support class for Introspector's
 * internal use.
 * <p/>
 * Mostly this is used as a placeholder for the descriptors.
 */
class GenericBeanInfo
{

  private BeanDescriptor beanDescriptor;
  private PropertyDescriptor[] properties;
  private GWMethodDescriptor[] methods;

  public GenericBeanInfo(BeanDescriptor beanDescriptor, PropertyDescriptor[] properties, GWMethodDescriptor[] methods)
  {
    this.beanDescriptor = beanDescriptor;
    this.properties = properties;
    this.methods = methods;
  }

  public PropertyDescriptor[] getPropertyDescriptors()
  {
    return properties;
  }

  public GWMethodDescriptor[] getGWMethodDescriptors()
  {
    return methods;
  }

  public BeanDescriptor getBeanDescriptor()
  {
    return beanDescriptor;
  }

}
