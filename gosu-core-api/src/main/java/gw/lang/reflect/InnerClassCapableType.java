/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.IHasInnerClass;

public abstract class InnerClassCapableType extends AbstractType implements IHasInnerClass
{
  public IType resolveRelativeInnerClass( String strRelativeInnerClassName, boolean bForce )
  {
    // Try to resolve the inner class name relative to this class and its enclosing class[s] and its hierarchy
    for( IType outerClass = getTheRef();
         outerClass != null;
         outerClass = outerClass.getEnclosingType() )
    {
      String strContainingType = outerClass.getName();
      if( !strRelativeInnerClassName.startsWith( strContainingType ) )
      {
        IType innerClass = ((IHasInnerClass)outerClass).getInnerClass( strRelativeInnerClassName );
        if( innerClass != null )
        {
          return innerClass;
        }
        else
        {
          IType superType = outerClass.getSupertype();
          if( superType instanceof IHasInnerClass )
          {
            innerClass = ((IHasInnerClass)superType).resolveRelativeInnerClass( strRelativeInnerClassName, bForce );
            if( innerClass != null )
            {
              return innerClass;
            }
          }
          for( IType iface: outerClass.getInterfaces() )
          {
            innerClass = ((IHasInnerClass)iface).resolveRelativeInnerClass( strRelativeInnerClassName, bForce );
            if( innerClass != null )
            {
              return innerClass;
            }
          }
        }
      }
    }
    return null;
  }

}
