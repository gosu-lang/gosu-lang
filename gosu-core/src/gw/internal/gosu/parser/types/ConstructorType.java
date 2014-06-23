/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.types;

import gw.internal.gosu.parser.GenericTypeVariable;
import gw.internal.gosu.parser.GosuConstructorInfo;
import gw.lang.parser.IExpression;
import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.*;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 */
public class ConstructorType extends AbstractType implements IConstructorType
{
  protected IType _declaringType;
  protected IType[] _argTypes;
  protected IConstructorInfo _constructor;

  public ConstructorType( IType declaringType, IType[] argTypes )
  {
    _declaringType = declaringType;
    _argTypes = argTypes == null ? IType.EMPTY_ARRAY : argTypes;

    _constructor = null;
  }

  public ConstructorType( IConstructorInfo constructor )
  {
    _constructor = constructor;

    IParameterInfo[] argTypes = constructor.getParameters();
    int iArgs = argTypes.length;
    _argTypes = iArgs == 0 ? IType.EMPTY_ARRAY : new IType[iArgs];
    for( int i = 0; i < iArgs; i++ )
    {
      _argTypes[i] = argTypes[i].getFeatureType();
    }

    _declaringType = _constructor.getType();
  }

  /**
   * @return The instrinic type of this ConstructorType's return type.
   */
  public IType getIntrinsicType()
  {
    return getDeclaringType();
  }

  public IType getDeclaringType()
  {
    return _declaringType;
  }

  public IType[] getParameterTypes()
  {
    return _argTypes;
  }

  public IConstructorInfo getConstructor()
  {
    return _constructor;
  }

  public String getArgSignature()
  {
    String strParams = "(";
    for( int i = 0; i < _argTypes.length; i++ )
    {
      strParams += (i == 0 ? "" : ", " ) + _argTypes[i].getName();
    }
    strParams += ")";

    return strParams;
  }

  public String getParamSignature()
  {
    return getArgSignature();
  }

  public String getName()
  {
    return getDeclaringType().getName() + getArgSignature();
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return getDeclaringType().getRelativeName() + getArgSignature();
  }

  public String getNamespace()
  {
    return getDeclaringType().getNamespace();
  }

  public ITypeLoader getTypeLoader()
  {
    return getDeclaringType().getTypeLoader();
  }

  public boolean isInterface()
  {
    return false;
  }

  public IType[] getInterfaces()
  {
    return EMPTY_TYPE_ARRAY;
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType getSupertype()
  {
    return null;
  }

  public IType getEnclosingType()
  {
    return null;
  }

  public IType getGenericType()
  {
    return null;
  }

  public boolean isFinal()
  {
    return false;
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
    return GenericTypeVariable.EMPTY_TYPEVARS;
  }

  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  public IType[] getTypeParameters()
  {
    return IType.EMPTY_ARRAY;
  }

  public Set getAllTypesInHierarchy()
  {
    return Collections.singleton( this );
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
    return null;
  }

  public Object makeArrayInstance( int iLength )
  {
    return null;
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    return null;
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    return 0;
  }

  public IType getComponentType()
  {
    return null;
  }

  public boolean isAssignableFrom( IType type )
  {
    return type == this;
  }

  public boolean isMutable()
  {
    return false;
  }

  public ITypeInfo getTypeInfo()
  {
    return null;
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
    return _constructor != null ? Modifier.getModifiersFrom( _constructor ) : Modifier.PUBLIC;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean equals( Object obj )
  {
    if( obj == this )
    {
      return true;
    }

    if( obj == null || !(obj instanceof ConstructorType) )
    {
      return false;
    }

    ConstructorType ct = (ConstructorType)obj;

    return getDeclaringType().equals( ct.getDeclaringType() ) &&
           Arrays.equals( getParameterTypes(), ct.getParameterTypes() );
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
  public String[] getParameterNames()
  {
    List<String> names = new ArrayList<String>();
    if ( _constructor == null ) {
      return new String[0];
    } 
    else if ( _constructor instanceof GosuConstructorInfo )
    {
      GosuConstructorInfo ci = (GosuConstructorInfo)_constructor;
      for( IReducedSymbol s : ci.getArgs() )
      {
        names.add( s.getName() );
      }
    }
    else
    {
      IParameterInfo[] parameters = _constructor.getParameters();
      for (IParameterInfo parameter : parameters) {
        names.add(parameter.getName());
      }
    }
    return names.toArray( new String[names.size()] );
  }

  @Override
  public IExpression[] getDefaultValueExpressions()
  {
    if( getConstructor() instanceof IOptionalParamCapable )
    {
      return ((IOptionalParamCapable)getConstructor()).getDefaultValueExpressions();
    }
    return IExpression.EMPTY_ARRAY;
  }

  @Override
  public boolean hasOptionalParams()
  {
    for( IExpression o : getDefaultValueExpressions() )
    {
      if( o != null )
      {
        return true;
      }
    }
    return false;
  }
}
