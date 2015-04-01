/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.TypeVariableDefinition;
import gw.lang.reflect.AbstractType;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.IGosuClass;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Set;

/**
 */
public class TypeVariableType extends AbstractType implements ITypeVariableType
{
  private ITypeVariableDefinition _typeVarDef;
  transient private TypeVariableArrayType _arrayType;
  private boolean _bFunctionStatement;

  public TypeVariableType( ITypeVariableDefinition typeVarDef, boolean forFunction )
  {
    _typeVarDef = typeVarDef;
    _bFunctionStatement = forFunction;
  }

  public TypeVariableType( IType ownersType, IGenericTypeVariable typeVar )
  {
    _typeVarDef = new TypeVariableDefinition( ownersType, typeVar );
  }

  public ITypeVariableDefinition getTypeVarDef()
  {
    return _typeVarDef;
  }

  public String getName()
  {
    return _typeVarDef.getName();
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return getName();
  }

  public String getNameWithEnclosingType()
  {
    String strEnclosingType = getEnclosingType().getName();
    if( getEnclosingType() instanceof FunctionType )
    {
      // Add id to distinguish between overloaded methods. We can't use the signature
      // because we parse the type vars before the signature.
      strEnclosingType += "." + System.identityHashCode( this );
    }
    return strEnclosingType + '.' + getRelativeName();
  }

  public String getNameWithBoundingType()
  {
    return getRelativeName() + "." + getBoundingType().getName();
  }

  public String getNamespace()
  {
    return null;
  }

  public ITypeLoader getTypeLoader()
  {
    return null;
  }

  public IType getBoundingType()
  {
    return _typeVarDef.getTypeVar().getBoundingType();
  }

  public boolean isInterface()
  {
    return getBoundingType().isInterface();
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType[] getInterfaces()
  {
    if( getBoundingType().isInterface() )
    {
      return new IType[] { getBoundingType() };
    }
    return EMPTY_TYPE_ARRAY;
  }

  public IType getSupertype()
  {
    if( !getBoundingType().isInterface() )
    {
      return getBoundingType();
    }
    return null;
  }

  public IType getEnclosingType()
  {
    return _typeVarDef.getEnclosingType();
  }

  public IType getGenericType()
  {
    return null;
  }

  public boolean isFinal()
  {
    return true;
  }

  public boolean isParameterizedType()
  {
    return false;
  }

  public boolean isGenericType()
  {
    return false;
  }

  public GenericTypeVariable[] getGenericTypeVariables()
  {
    return null;
  }

  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  public IType[] getTypeParameters()
  {
    return null;
  }

  public Set<IType> getAllTypesInHierarchy()
  {
    //noinspection unchecked
    return (Set<IType>)(getBoundingType() == null ? Collections.emptySet() : getBoundingType().getAllTypesInHierarchy());
  }

  public boolean isArray()
  {
    return false;
  }

  public boolean isPrimitive()
  {
    return false;
  }

  public IType getArrayType()
  {
    if( _arrayType == null )
    {
      synchronized( this )
      {
        if( _arrayType == null )
        {
          // Note: we do the first assignment, then the second, so that the assignment to the instance variable
          // doesn't happen prior to the constructor completing, as the initial assignment might happen
          // before the constructor is done
          //noinspection UnnecessaryLocalVariable
          TypeVariableArrayType arrayType = new TypeVariableArrayType( this, null, getTypeLoader() );
          _arrayType = arrayType;
        }
      }
    }
    return _arrayType;
  }

  public Object makeArrayInstance( int iLength )
  {
    return getBoundingType().makeArrayInstance( iLength );
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    throw new UnsupportedOperationException();
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    throw new UnsupportedOperationException();
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    throw new UnsupportedOperationException();
  }

  public IType getComponentType()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * A type variable type is assignable only to/from itself or a reference thereof.
   * <p>
   * For example: <pre>
   *   function foo<T,S>( t: T, s: S )
   *   {
   *     t = s         // illegal, T and S may not have the same type at runtime
   *     t = "hello"   // illegal, T may not be a String
   *     var x: T = t  // ok, x and t are both of type T
   *   }
   */
  public boolean isAssignableFrom( IType type )
  {
    if( type == this )
    {
      return true;
    }

    if( !(type instanceof TypeVariableType) )
    {
      return false;
    }

    if( !getRelativeName().equals( type.getRelativeName() ) )
    {
      return false;
    }

    IType thatEncType = type.getEnclosingType();
    IType thisEncType = getEnclosingType();
    return thatEncType == thisEncType ||
           thatEncType instanceof IGosuClassInternal &&
           ((IGosuClassInternal)thatEncType).isProxy() &&
           thisEncType == ((IGosuClassInternal)thatEncType).getJavaType();
  }

  /**
   * Ok, here's the deal. By all rights type variable types should have identity
   * equality. That's not the case right now for at least these two reasons:
   * <ul>
   * <li> We compile in multiple phases (header, decl, def). Each phase recompiles
   *    the header, including type vars. Each time a new type var type is
   *    created for the type var definition. It's not too hard to re-use the
   *    prior type var type, but then there's this problem...
   * <li> If a subclass is generic and parameterizes its super with its type vars
   *    the super parameterizd type retains whatever the type var type from the
   *    first phase of the subclass' compilation. And, in studio, we don't
   *    refresh the type system, just the type we're editing. So the super class
   *    parameterized with the subclass' type vars and whatever else stemming
   *    from there that refs the type vars remains. So the subclass can be edited
   *    and refreshed a zillion time, but the super parameterized class will still
   *    have the type var from way back.
   * </ul>
   * Probably the best way to handle this is to make TypeVariableType a loadable
   * type i.e., there will only ever be a single ref for a particular type var.
   * The challege is not so much with class type vars, but with function type
   * vars, since 1) we parse them before we parse the function's args we don't
   * quite have a fq name (yay! for overloading), and 2) function types
   * themselves are non-loadable and must remain that way as long as other types
   * are permitted to be non-loadable. Specifically, since a param type in a
   * function can be of any type, the fq name of the function may not be unique
   * i.e., the reason a type is non-loadable is usually because it can't have a
   * unique name. (yay! again for overloading)
   */
  public boolean equals( Object obj )
  {
    return obj instanceof IType && isAssignableFrom( (IType)obj );
  }

  @Override
  public int hashCode()
  {
    int result = getName().hashCode();
    result = 31 * result + (_bFunctionStatement ? 1 : 0);
    return result;
  }

  public boolean isMutable()
  {
    return false;
  }

  public ITypeInfo getTypeInfo()
  {
    return getBoundingType().getTypeInfo();
  }

  public void unloadTypeInfo()
  {
  }

  public Object readResolve() throws ObjectStreamException
  {
    return this;
  }

  public boolean isValid()
  {
    return true;
  }

  public int getModifiers()
  {
    return Modifier.PUBLIC;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean isFunctionStatement()
  {
    return _bFunctionStatement;
  }

  public boolean isDiscarded()
  {
    return false;
  }

  public void setDiscarded( boolean bDiscarded )
  {
  }

  public boolean isCompoundType()
  {
    return false;
  }

  public Set<IType> getCompoundTypeComponents()
  {
    return null;
  }

  @Override
  public String toString()
  {
    return "<" + getName() + ">";
  }
}
