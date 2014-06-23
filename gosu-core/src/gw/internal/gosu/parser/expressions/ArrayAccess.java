/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.dynamic.IDynamicType;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.statements.MemberAssignmentStatement;
import gw.internal.gosu.parser.statements.ArrayAssignmentStatement;
import gw.internal.gosu.parser.GosuParser;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IArrayAccessExpression;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a member access expression in the Gosu grammar:
 * <pre>
 * <i>array-access</i>
 *   &lt;array-reference&gt; [ &lt;member&gt; ]
 * <p/>
 * <i>array-reference</i>
 *   &lt;expression&gt;
 * <p/>
 * <i>member</i>
 *   &lt;array-access&gt;
 *   &lt;expression&gt;
 * </pre>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ArrayAccess extends Expression implements IArrayAccessExpression
{
  /** The array expression */
  private Expression _rootExpression;
  /** An expression for accessing a bean member/property dynamically */
  private Expression _memberExpression;

  private boolean _bNullSafe;


  public ArrayAccess()
  {
  }

  public Expression getRootExpression()
  {
    return _rootExpression;
  }

  public void setRootExpression( Expression rootExpression )
  {
    _rootExpression = rootExpression;
    IType rootType = _rootExpression.getType();
    setTypeInternal( rootType );
  }

  public Expression getMemberExpression()
  {
    return _memberExpression;
  }

  public void setMemberExpression( Expression memberExpression )
  {
    _memberExpression = memberExpression;
  }

  public boolean isNullSafe()
  {
    return _bNullSafe;
  }
  public void setNullSafe( boolean bNullSafe )
  {
    _bNullSafe = bNullSafe;
  }
  
  /**
   * Evaluates the expression.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      //## todo: maybe make this expression evaluate directly, making a class for this seems a bit much
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  public static IType getTypeToAutoInsert( IExpression rootExpression )
  {
    IType featureType = ((MemberAccess)rootExpression).getPropertyInfo().getFeatureType().getTypeParameters()[0];
    if( (featureType.isAbstract() || featureType.isInterface()) )
    {
      IType concreteType = GosuParser.findImpl( featureType );
      if( concreteType != null )
      {
        featureType = concreteType;
      }
    }
    return featureType;
  }

  public static boolean needsAutoinsert( ArrayAccess arrayAccess )
  {
    Expression rootExpr = arrayAccess.getRootExpression();
    // if the root is not a list member access, we do not auto insert
    if( !(rootExpr instanceof MemberAccess) || !JavaTypes.LIST().isAssignableFrom(rootExpr.getType()))
    {
      return false;
    }

    // check if the root has the Autoinsert annotation
    MemberAccess memberAccess = (MemberAccess)rootExpr;
    IType autoinsertAnnotationType = JavaTypes.AUTOINSERT();
    List<IAnnotationInfo> list = autoinsertAnnotationType == null ? null : memberAccess.getPropertyInfo().getAnnotationsOfType( autoinsertAnnotationType );
    if( list == null || list.isEmpty() )
    {
      return false;
    }

    IParsedElement parent = arrayAccess.getParent();
    boolean bNestedInLhs = false;
    boolean bNested = true;
    while( bNested && (parent != null) && !(parent instanceof Statement) )
    {
      bNested = parent instanceof MemberAccess || parent instanceof ArrayAccess;
      parent = parent.getParent();
    }
    if( bNested )
    {
      IParsedElement lhsRoot = null;
      if( parent instanceof MemberAssignmentStatement )
      {
        lhsRoot = ((MemberAssignmentStatement)parent).getRootExpression();
      }
      else if ( parent instanceof ArrayAssignmentStatement ) {
        lhsRoot = ((ArrayAssignmentStatement)parent).getArrayAccessExpression();
      }
      IParsedElement csr = arrayAccess;
      while( csr instanceof Expression )
      {
        if( lhsRoot == csr )
        {
          bNestedInLhs = true;
          break;
        }
        csr = csr.getParent();
      }
    }
    return bNestedInLhs;
  }

  public IType getComponentType()
  {
    return getType();
  }

  @Override
  public String toString()
  {
    return getRootExpression().toString() + "[" + getMemberExpression().toString() + "]";
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public static Object getArrayElement( Object obj, int iIndex, boolean bNullSafe )
  {
    if( obj == null )
    {
      if( bNullSafe )
      {
        return null;
      }
      else
      {
        throw new NullPointerException();
      }
    }

    IType objType = TypeLoaderAccess.instance().getIntrinsicTypeFromObject( obj );
    if( objType.isArray() )
    {
      return objType.getArrayComponent( obj, iIndex );
    }

    if( obj instanceof CharSequence )
    {
      return String.valueOf( ((CharSequence)obj).charAt( iIndex ) );
    }

    if( obj instanceof List )
    {
      return ((List)obj).get( iIndex );
    }

    if( obj instanceof Collection )
    {
      Iterator iter = ((Collection)obj).iterator();
      return getElementFromIterator( iter, iIndex );
    }

    if( obj instanceof Iterator )
    {
      Iterator iter = (Iterator)obj;
      return getElementFromIterator( iter, iIndex );
    }

    return null;
  }

  private void setTypeInternal( IType rootType )
  {
    if( rootType.isArray() )
    {
      setType( rootType.getComponentType() );
    }
    else if( rootType == GosuParserTypes.STRING_TYPE() )
    {
      setType( GosuParserTypes.STRING_TYPE() );
    }
    else if( JavaTypes.COLLECTION().isAssignableFrom(rootType) )
    {
      IType paramedType = TypeLord.findParameterizedType(rootType, JavaTypes.COLLECTION());
      if( paramedType != null )
      {
        setType( paramedType.getTypeParameters()[0] );
      }
      else
      {
        setType( JavaTypes.OBJECT() );
      }
    }
    else if( JavaTypes.ITERATOR().isAssignableFrom(rootType) )
    {
      IType paramedType = TypeLord.findParameterizedType(rootType, JavaTypes.ITERATOR());
      if( paramedType != null )
      {
        setType( paramedType.getTypeParameters()[0] );
      }
      else
      {
        setType( JavaTypes.OBJECT() );
      }
    }
    else if( rootType instanceof IPlaceholder && ((IPlaceholder)rootType).isPlaceholder() )
    {
      setType( rootType.getComponentType() );
    }
    else
    {
      setType( JavaTypes.OBJECT() );
    }
  }


  public static boolean supportsArrayAccess( IType type )
  {
    return type.isArray() ||
           (JavaTypes.LIST().isAssignableFrom(type) &&
            !JavaTypes.LINKED_LIST().isAssignableFrom( type )) ||
           JavaTypes.CHAR_SEQUENCE().isAssignableFrom(type) ||
           (type instanceof IDynamicType);
  }

  private static Object getElementFromIterator( Iterator iter, int iIndex )
  {
    int iCount = 0;
    while( iter.hasNext() )
    {
      Object elem = iter.next();
      if( iCount++ == iIndex )
      {
        return elem;
      }
    }
    return null;
  }

}
