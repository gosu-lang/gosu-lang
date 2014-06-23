/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IConstructorNode;
import gw.lang.javadoc.IExceptionNode;
import gw.lang.javadoc.IParamNode;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.util.GosuClassUtil;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 */
class ConstructorNode extends BaseFeatureNode implements IConstructorNode, IDocNodeWithParams
{
  private List<IParamNode> _params = Collections.emptyList();
  private List<IExceptionNode> _exceptions = Collections.emptyList();

  @Override
  public List<IParamNode> getParams() {
    return _params;
  }

  @Override
  public void addParam(IParamNode param) {
    if ( _params == Collections.EMPTY_LIST ) {
      _params = new ArrayList<IParamNode>( 1 );
    }
    _params.add( param );
  }

  @Override
  public List<IExceptionNode> getExceptions() {
    return _exceptions;
  }

  @Override
  public void addException(IExceptionNode exception) {
    if ( _exceptions == Collections.EMPTY_LIST ) {
      _exceptions = new ArrayList<IExceptionNode>( 1 );
    }
    _exceptions.add( exception );
  }

  @Override
  public ExceptionNode getException(IJavaClassInfo exceptionClass) {
    for (IExceptionNode exceptionNode : getExceptions()) {
      if (exceptionNode.getType().equalsIgnoreCase(exceptionClass.getName()) || exceptionNode.getType().equalsIgnoreCase( GosuClassUtil.getShortClassName(exceptionClass.getName()))) {
          return (ExceptionNode)exceptionNode;
      }
    }
    return null;
  }

}
