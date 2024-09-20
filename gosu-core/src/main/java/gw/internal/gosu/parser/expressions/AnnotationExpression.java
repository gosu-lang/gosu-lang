/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.GosuAnnotation;
import gw.internal.gosu.parser.GosuAnnotationInfo;
import gw.internal.gosu.parser.IGosuAnnotation;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.parser.expressions.IAnnotationExpression;
import gw.lang.reflect.gs.ICompilableType;

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

    ICompilableType ownersType = getAnnotation().getOwnersType();
    ownersType = ownersType == null ? (ICompilableType) getAnnotation().getType() : ownersType;
    return new GosuAnnotationInfo( getAnnotation(), ownersType.getTypeInfo(), (IGosuClassInternal)getGosuClass() );
  }
}
