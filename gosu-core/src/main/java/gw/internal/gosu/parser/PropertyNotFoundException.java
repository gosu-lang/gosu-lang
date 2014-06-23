/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.reflect.IType;


/**
 */
public class PropertyNotFoundException extends ParseException
{
  private String _strProperty;
  private IType _classBean;

  public PropertyNotFoundException( String strProperty, IType classBean, IFullParserState parserState )
  {
    super( parserState, Res.MSG_NO_PROPERTY_DESCRIPTOR_FOUND, classBean instanceof MetaType ? " static" : "", strProperty, classBean.getName() );

    _strProperty = strProperty;
    _classBean = classBean;
  }

  public PropertyNotFoundException( IType classBean, ResourceKey msg, IFullParserState parserState )
  {
    super( parserState, msg );

    _strProperty = "";
    _classBean = classBean;
  }

  public String getProperty()
  {
    return _strProperty;
  }

  public IType getBeanClass()
  {
    return _classBean;
  }
}
