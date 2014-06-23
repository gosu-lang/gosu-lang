/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.optimizer;

import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.reflect.INamespaceType;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.ArrayAccess;
import gw.internal.gosu.parser.statements.MemberAssignmentStatement;
import gw.internal.gosu.parser.statements.ArrayAssignmentStatement;
import gw.lang.parser.IExpressionRuntime;
import gw.lang.parser.IParsedElement;
import gw.lang.reflect.IUncacheableFeature;

/**
 * Note this class really isn't an "expression runtime", it's real purpose is
 * to determine whether or not the corresponding member-access is in the lhs
 * of an assignment. This info can be useful to decide if null values in the
 * member path can be auto-assigned (see the gw.lang.Autocreate annotation)
 */
public class SinglePropertyMemberAccessRuntime implements IExpressionRuntime
{
  private boolean _bNestedInLhs;

  public SinglePropertyMemberAccessRuntime( MemberAccess memberAccess )
  {
    IParsedElement parent = memberAccess.getParent();
    boolean bNested = parent instanceof MemberAccess || parent instanceof ArrayAccess;
    while( bNested && (parent != null) && !(parent instanceof Statement) )
    {
      parent = parent.getParent();
    }
    if( bNested )
    {
      IParsedElement lhsRoot = null;
      if ( parent instanceof MemberAssignmentStatement )
      {
        lhsRoot = ((MemberAssignmentStatement)parent).getRootExpression();
      }
      else if ( parent instanceof ArrayAssignmentStatement )
      {
        lhsRoot = ((ArrayAssignmentStatement)parent).getArrayAccessExpression();
      }
      if ( lhsRoot != null )
      {
        IParsedElement csr = memberAccess;
        while( csr instanceof Expression )
        {
          if( lhsRoot == csr )
          {
            _bNestedInLhs = bNested;
            break;
          }
          csr = csr.getParent();
        }
      }
    }
  }

  public static boolean isConvertible( MemberAccess memberAccess )
  {
    return memberAccess.getMemberExpression() == null &&
           !(memberAccess.getRootType() instanceof INamespaceType) &&
           !(memberAccess.getRootExpression() != null && memberAccess.getRootType().isArray()) &&
           !(memberAccess.getPropertyInfo() instanceof IUncacheableFeature);
  }

  @Override
  public Object evaluate()
  {
    throw new CannotExecuteGosuException();
  }

  public boolean isNestedInLhs()
  {
    return _bNestedInLhs;
  }
}
