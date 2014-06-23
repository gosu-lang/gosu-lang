/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IClassDocNode;
import gw.lang.javadoc.IExceptionNode;
import gw.lang.javadoc.IJavaDocFactory;
import gw.lang.javadoc.IParamNode;

/**
 */
public class JavaDocFactoryImpl implements IJavaDocFactory
{
  public IClassDocNode create( Class javaClass )
  {
    return ClassDocNode.get( javaClass );
  }

  public IClassDocNode create()
  {
    return new ClassDocNode();
  }

  /**
   * @return a new, empty IParamNode
   * @deprecated Please don't create these manually, and please fix any code that does.
   */
  @Deprecated
  public IParamNode createParam()
  {
    return new ParamNode();
  }

  /**
   * @return a new, empty IExceptionNode
   * @deprecated Please don't create these manually, and please fix any code that does.
   */
  @Deprecated
  public IExceptionNode createException()
  {
    return new ExceptionNode();
  }

}