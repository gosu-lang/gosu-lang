/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public interface IRelativeTypeInfo extends ITypeInfo
{
  public static final int Accessibility_Size = Accessibility.values().length;

  public Accessibility getAccessibilityForType( IType whosaskin );

  public List<? extends IPropertyInfo> getProperties( IType whosaskin );

  public IPropertyInfo getProperty( IType whosaskin, CharSequence propName );

  public MethodList getMethods(IType whosaskin);

  public IMethodInfo getMethod( IType whosaskin, CharSequence methodName, IType... params );

  public List<? extends IConstructorInfo> getConstructors( IType whosaskin );

  public IConstructorInfo getConstructor( IType whosAskin, IType[] params );

  /**
   * Returns a list of <code>IPropertyInfo</code> objects reflecting all the
   * properties declared by the class or interface represented by this
   * <code>IType</code> object. This includes public, protected, internal
   * access, and private properties, but excludes inherited properties.
   * The elements in the list are ordered by declaration order in the file.
   * This method returns an empty list if the type does not contain any properties
   *
   * @return    the list of <code>IPropertyInfo</code> objects representing all the
   * declared properties of this class
   */
  public List<? extends IPropertyInfo> getDeclaredProperties();

  /**
   * Returns a list of <code>IMethodInfo</code> objects reflecting all the
   * methods declared by the class or interface represented by this
   * <code>IType</code> object. This includes public, protected, internal
   * access, and private methods, but excludes inherited methods.
   * The elements in the list are ordered by declaration order in the file.
   * This method returns an empty list if the type does not contain any methods
   *
   * @return    the list of <code>IMethodInfo</code> objects representing all the
   * declared methods of this class
   */
  public List<? extends IMethodInfo> getDeclaredMethods();

  /**
   * Returns a list of <code>IConstructorInfo</code> objects reflecting all the
   * constructors declared by the class represented by this
   * <code>IType</code> object. This includes public, protected, internal
   * access, and private constructors.  Interfaces and arrays always return an empty list.
   * The elements in the list are ordered by declaration order in the file.
   * This method returns an empty list if the type does not contain any methods
   *
   * @return    the list of <code>IConstructorInfo</code> objects representing all the
   * declared methods of this class
   */
  public List<? extends IConstructorInfo> getDeclaredConstructors();

  enum Accessibility
  {
    NONE()
    {
      public boolean isAccessible( int modifiers )
      {
        return false;
      }
    },
    PUBLIC()
    {
      public boolean isAccessible( int modifiers )
      {
        return Modifier.isPublic( modifiers );
      }
    },
    PROTECTED()
    {
      public boolean isAccessible( int modifiers )
      {
        return Modifier.isPublic( modifiers ) || Modifier.isProtected( modifiers );
      }
    },
    INTERNAL()
    {
      public boolean isAccessible( int modifiers )
      {
        return !Modifier.isPrivate( modifiers );
      }
    },
    PRIVATE()
    {
      public boolean isAccessible( int modifiers )
      {
        return true;
      }
    };

    public abstract boolean isAccessible(int modifiers);
    
    public static Accessibility fromModifiers( int iModifiers )
    {
      if( Modifier.isPublic( iModifiers ) )
      {
        return PUBLIC;
      }
      else if( Modifier.isProtected( iModifiers ) )
      {
        return PROTECTED;
      }
      else if( Modifier.isInternal( iModifiers ) )
      {
        return INTERNAL;
      }
      else if( Modifier.isPrivate( iModifiers ) )
      {
        return PRIVATE;
      }
      else
      {
        return PUBLIC;
      }
    }

    public int toModifier()
    {
      switch( this )
      {
        case PUBLIC:
          return Modifier.PUBLIC;
        case PROTECTED:
          return Modifier.PROTECTED;
        case INTERNAL:
          return Modifier.INTERNAL;
        case PRIVATE:
          return Modifier.PRIVATE;
        case NONE:
          return -1;
      }
      throw new IllegalStateException();
    }

    public boolean isAccessible( Accessibility acc )
    {
      return isAccessible( acc.toModifier() );
    }
  }
}
