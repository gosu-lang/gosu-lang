/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import java.beans.BeanInfo;

/**
 */
public interface IContextBeanInfo extends BeanInfo
{
  public Class getContext();

  public void setContext( Class ctxClass );
}
