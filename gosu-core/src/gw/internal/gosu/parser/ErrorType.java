/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IExpression;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.AbstractType;
import gw.lang.reflect.DefaultNonLoadableArrayType;
import gw.lang.reflect.IConstructorType;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.Modifier;
import gw.util.GosuClassUtil;

import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A type representing the Error state in the type system.  This is used when
 * we are unable to determine the type for a particular expression due to
 * errors and wish to assign a universally-compatible type to it that will
 * not cascade the compilation error.
 */
public class ErrorType extends AbstractType implements IErrorType
{
  private static final ErrorType INSTANCE = new ErrorType();
  private static final Map<String, ErrorType> ERROR_TYPE_POOL = new HashMap<String, ErrorType>();
  private static final GenericTypeVariable[] EMPTY_GENERIC_TYPE_VARIABLE_ARR = new GenericTypeVariable[0];

  private String _strErrantTypeName;
  private ParseResultsException _error;


  public static ErrorType getInstance()
  {
    return INSTANCE;
  }

  private ErrorType()
  {
    this( NAME );
  }

  private ErrorType( String strErrantTypeName )
  {
    _strErrantTypeName = strErrantTypeName;
  }

  public static ErrorType getInstance(String strErrantTypeName) {
    ErrorType errorType = ERROR_TYPE_POOL.get(strErrantTypeName);
    if (errorType == null) {
      errorType = new ErrorType(strErrantTypeName);
      ERROR_TYPE_POOL.put(strErrantTypeName, errorType);
    }
    return errorType;
  }

  public ErrorType(ParseResultsException e) {
    this(NAME);
    _error = e;
  }

  public String getName()
  {
    return _strErrantTypeName;
  }

  public String getErrantTypeName()
  {
    return _strErrantTypeName;
  }

  public ParseResultsException getError() {
    return _error;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getRelativeName()
  {
    return GosuClassUtil.getNameNoPackage( _strErrantTypeName );
  }

  public String getNamespace()
  {
    return "";
  }

  public ITypeLoader getTypeLoader()
  {
    return DefaultTypeLoader.instance();
  }

  public boolean isInterface()
  {
    return false;
  }

  public boolean isEnum()
  {
    return false;
  }

  public IType[] getInterfaces()
  {
    return EMPTY_TYPE_ARRAY;
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
    return EMPTY_GENERIC_TYPE_VARIABLE_ARR;
  }

  public IType getParameterizedType( IType... ofType )
  {
    return null;
  }

  public IType[] getTypeParameters()
  {
    return EMPTY_ARRAY;
  }

  public Set<? extends IType> getAllTypesInHierarchy()
  {
    return Collections.emptySet();
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
    return new DefaultNonLoadableArrayType( this, null, getTypeLoader() );
  }

  public Object makeArrayInstance( int iLength )
  {
    throw new UnsupportedOperationException( "makeArrayInstance not implemented by gw.internal.gosu.parser.ErrorType" );
  }

  public Object getArrayComponent( Object array, int iIndex ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    throw new UnsupportedOperationException( "getArrayComponent not implemented by gw.internal.gosu.parser.ErrorType" );
  }

  public void setArrayComponent( Object array, int iIndex, Object value ) throws IllegalArgumentException, ArrayIndexOutOfBoundsException
  {
    throw new UnsupportedOperationException( "setArrayComponent not implemented by gw.internal.gosu.parser.ErrorType" );
  }

  public int getArrayLength( Object array ) throws IllegalArgumentException
  {
    throw new UnsupportedOperationException( "getArrayLength not implemented by gw.internal.gosu.parser.ErrorType" );
  }

  public IType getComponentType()
  {
    return this;
  }

  public boolean isAssignableFrom( IType type )
  {
    return true;
  }

  public boolean isMutable()
  {
    return true;
  }

  public ITypeInfo getTypeInfo()
  {
    return ErrorTypeInfo.INSTANCE;
  }

  public void unloadTypeInfo()
  {
    //No-op
  }

  public Object readResolve() throws ObjectStreamException
  {
    return getInstance();
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

  public static boolean shouldHandleAsErrorType( IType type )
  {

    boolean isErrorType = false;

    //if this is a meta intrinsic type, examine it's composite type to determine
    //if it is an error type
    if( type instanceof MetaType)
    {
      MetaType metaType = (MetaType)type;
      type = metaType.getType();
    }

    if( type instanceof ErrorType )
    {
      isErrorType = true;
    }

    return isErrorType;
  }

  public IFunctionType getErrorTypeFunctionType( IExpression[] eArgs, String strMethod, List listAllMatchingMethods )
  {
    ErrorTypeInfo errorTypeInfo = (ErrorTypeInfo)getTypeInfo();
    if( eArgs == null )
    {
      List functionTypes = errorTypeInfo.getUniversalFunctionTypes( strMethod );
      for( int i = 0; i < functionTypes.size(); i++ )
      {
        IFunctionType info = (IFunctionType)functionTypes.get( i );
        if( listAllMatchingMethods != null )
        {
          listAllMatchingMethods.add( info );
        }
      }
      return (IFunctionType)functionTypes.get( 0 );
    }
    else
    {
      IFunctionType info = errorTypeInfo.getUniversalFunctionType( strMethod, eArgs.length );
      if( listAllMatchingMethods != null )
      {
        listAllMatchingMethods.add( info );
      }
      return info;
    }
  }

  public IConstructorType getErrorTypeConstructorType( IExpression[] eArgs, List listAllMatchingMethods )
  {
    ErrorTypeInfo errorTypeInfo = (ErrorTypeInfo)getTypeInfo();
    if( eArgs == null )
    {
      List constructorTypes = errorTypeInfo.getUniversalConstructors();
      for( int i = 0; i < constructorTypes.size(); i++ )
      {
        IConstructorType info = (IConstructorType)constructorTypes.get( i );
        if( listAllMatchingMethods != null )
        {
          listAllMatchingMethods.add( info );
        }
      }
      return (IConstructorType)constructorTypes.get( 0 );
    }
    else
    {
      IConstructorType info = errorTypeInfo.getUniversalConstructor( eArgs.length );
      if( listAllMatchingMethods != null )
      {
        listAllMatchingMethods.add( info );
      }
      return info;
    }
  }

  @Override
  public String toString()
  {
    return "[error type - " + _strErrantTypeName + "]";
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

}
