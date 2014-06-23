/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.GosuAnnotation;
import gw.internal.gosu.parser.GosuAnnotationInfo;
import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.expressions.IAnnotationExpression;

public class AnnotationExpression extends NewExpression implements IAnnotationExpression
{
  private GosuAnnotation _annotation;

  public GosuAnnotation getAnnotation()
  {
    return _annotation;
  }
  public void setAnnotation( IGosuAnnotation annotation )
  {
    _annotation = (GosuAnnotation)annotation;
  }

  @Override
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    return new GosuAnnotationInfo( getAnnotation(), getAnnotation().getOwnersType().getTypeInfo(), (IGosuClassInternal)getGosuClass() );
  }
}
