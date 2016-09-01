package gw.lang.parser.expressions;

import gw.lang.parser.AnnotationUseSiteTarget;
import gw.lang.parser.IExpression;

/**
 */
public interface IAnnotationUseSiteTargetClause extends IExpression
{
  AnnotationUseSiteTarget getTarget();
}
