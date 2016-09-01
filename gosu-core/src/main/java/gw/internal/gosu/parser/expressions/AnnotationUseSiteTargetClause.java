package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.AnnotationUseSiteTarget;
import gw.lang.parser.expressions.IAnnotationUseSiteTargetClause;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class AnnotationUseSiteTargetClause extends Expression implements IAnnotationUseSiteTargetClause
{
  private final AnnotationUseSiteTarget _target;

  public AnnotationUseSiteTargetClause( AnnotationUseSiteTarget target )
  {
    setType( JavaTypes.pVOID() );
    _target = target;
  }

  @Override
  public AnnotationUseSiteTarget getTarget()
  {
    return _target;
  }

  public IType evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return (IType)super.evaluate();
    }

    throw new IllegalStateException();
  }

  @Override
  public String toString()
  {
    return getTarget().name() + ":";
  }
}
