/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;

@UnstableAPI
public interface IConstructorNode extends IBaseFeatureNode {

  List<IParamNode> getParams();

  void addParam( IParamNode param );

  List<IExceptionNode> getExceptions();

  void addException( IExceptionNode param );

  IExceptionNode getException( IJavaClassInfo exceptionClass );

}
