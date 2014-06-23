/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.expressions.IMemberExpansionExpression;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.ArrayExpansionPropertyInfo;
import gw.internal.gosu.parser.BeanAccess;

/**
 * Represents a member expansion access expression in the Gosu grammar:
 * <pre>
 * <i>member-expansion-access</i>
 *   &lt;iterable-expression&gt;*.&lt;component-member&gt;
 * @see gw.lang.parser.IGosuParser
 */
public class MemberExpansionAccess extends MemberAccess implements IMemberExpansionExpression
{
  public IPropertyInfo getPropertyInfo()
  {
    IType componentType = TypeLord.getExpandableComponentType( getRootType() );
    try
    {
      IPropertyInfo pi = BeanAccess.getPropertyInfoDirectly( componentType, getMemberName() );
      return new ArrayExpansionPropertyInfo( pi );
    }
    catch( ParseException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public boolean isNullSafe()
  {
    return true;
  }

  public String toString()
  {
    return getRootExpression().toString() + "*." + getMemberName();
  }

  public static MemberExpansionAccess wrap( MemberAccess ma )
  {
    if( !(ma.getPropertyInfo() instanceof ArrayExpansionPropertyInfo) )
    {
      throw new IllegalArgumentException( "Only MemberAccess expressions whose property is an ArrayExpansionPropertyInfo can be wrapped" );
    }
    MemberExpansionAccess mea = new MemberExpansionAccess();
    mea.setGosuProgram( ma.getGosuProgram() );
    mea.setRootExpression( ma.getRootExpression() );
    mea.setMemberExpression( ma.getMemberExpression() );
    mea.setMemberName( ma.getMemberName() );
    mea.setType( ma.getType() );
    mea.setMemberAccessKind( ma.getMemberAccessKind() );
    return mea;
  }
}
