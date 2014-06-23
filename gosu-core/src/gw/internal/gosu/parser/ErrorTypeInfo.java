/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.types.ConstructorType;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IExceptionInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.SimpleParameterInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class ErrorTypeInfo implements ITypeInfo
{

  public static final ITypeInfo INSTANCE = new ErrorTypeInfo();
  private static final String ERROR_TYPE_INFO_NAME = "ErrorTypeInfo";

  private ErrorTypeInfo()
  {
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    return Collections.emptyList();
  }

  public IPropertyInfo getProperty( CharSequence propName )
  {
    return new UniversalProperty( propName.toString() );
  }

  public MethodList getMethods()
  {
    return MethodList.EMPTY;
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return new UniversalMethodInfo( methodName.toString(), params );
  }

  public IMethodInfo getCallableMethod( CharSequence method, IType... params )
  {
    return new UniversalMethodInfo( method.toString(), params );
  }

  public List<IConstructorInfo> getConstructors()
  {
    return Collections.emptyList();
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    throw new UnsupportedOperationException( "getConstructor is not implemented by ErrorTypeInfo" );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    throw new UnsupportedOperationException( "getCallableConstructor is not implemented by ErrorTypeInfo" );
  }

  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  public IEventInfo getEvent( CharSequence event )
  {
    throw new UnsupportedOperationException( "getEvent is not implemented by ErrorTypeInfo" );
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    return Collections.emptyList();
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return Collections.emptyList();
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return null;
  }

  public boolean hasAnnotation( IType type )
  {
    return false;
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return false;
  }

  public IFeatureInfo getContainer()
  {
    return null;
  }

  public IType getOwnersType()
  {
    return ErrorType.getInstance();
  }

  public String getName()
  {
    return ERROR_TYPE_INFO_NAME;
  }

  public String getDisplayName()
  {
    return ERROR_TYPE_INFO_NAME;
  }

  public String getDescription()
  {
    return null;
  }

  public IFunctionType getUniversalFunctionType( String strMethod, int argCount )
  {
    return new UniversalFunctionType( strMethod, argCount );
  }

  public List getUniversalFunctionTypes( String strMethod )
  {
    ArrayList returnList = new ArrayList();
    for( int i = 0; i < 100; i++ )
    {
      returnList.add( getUniversalFunctionType( strMethod, i ) );
    }
    return returnList;
  }

  public List getUniversalConstructors()
  {
    ArrayList returnList = new ArrayList();
    for( int i = 0; i < 100; i++ )
    {
      returnList.add( getUniversalConstructor( i ) );
    }
    return returnList;
  }

  public UniversalConstructorType getUniversalConstructor( int args )
  {
    return new UniversalConstructorType( args );
  }

  static class UniversalFunctionType extends FunctionType
  {
    private IMethodInfo _methodDescriptor;

    public UniversalFunctionType( String strFunctionName, int argCount )
    {
      super( strFunctionName, ErrorType.getInstance(), makeObjArray( argCount ) );
      _methodDescriptor = new UniversalMethodInfo( strFunctionName, makeObjArray( argCount ) );
    }

    private static IType[] makeObjArray( int argCount )
    {
      IType[] objectArgs = new IType[argCount];
      for( int i = 0; i < objectArgs.length; i++ )
      {
        objectArgs[i] = JavaTypes.OBJECT();
      }
      return objectArgs;
    }


    public IMethodInfo getMethodInfo()
    {
      return _methodDescriptor;
    }

    @Override
    public IType newInstance( IType[] paramTypes, IType returnType )
    {
      return new UniversalFunctionType( getName(), getParameterTypes().length );
    }
  }

  private static class UniversalMethodInfo extends BaseFeatureInfo implements IMethodInfo
  {
    private IParameterInfo[] _paramInfo;
    private String _name;

    public UniversalMethodInfo( String name, IType[] argTypes )
    {
      super( ErrorType.getInstance() );
      _name = name;
      _paramInfo = new IParameterInfo[argTypes.length];
      for (int i = 0; i < _paramInfo.length; i++) {
        _paramInfo[i] = new SimpleParameterInfo(this, argTypes[i], i);
      }
    }

    public boolean isStatic()
    {
      return true;
    }

    public String getName()
    {
      return _name;
    }

    public IParameterInfo[] getParameters()
    {
      return _paramInfo;
    }

    public IType getReturnType()
    {
      return ErrorType.getInstance();
    }

    public IMethodCallHandler getCallHandler()
    {
      return new IMethodCallHandler()
      {
        public Object handleCall( Object ctx, Object... args )
        {
          throw new UnsupportedOperationException( "ErrorType cannot invoke methods" );
        }
      };
    }

    public String getReturnDescription()
    {
      return "";
    }

    public List<IExceptionInfo> getExceptions()
    {
      return Collections.emptyList();
    }

    public List<IAnnotationInfo> getDeclaredAnnotations()
    {
      return Collections.emptyList();
    }
  }

  static class UniversalConstructorType extends ConstructorType
  {
    private UniversalConstructorInfo _constructorInfo;

    public UniversalConstructorType( int argCount )
    {
      super( ErrorType.getInstance(), makeObjArray( argCount ) );
      _constructorInfo = new UniversalConstructorInfo( makeObjArray( argCount ) );
    }

    private static IType[] makeObjArray( int argCount )
    {
      IType[] objectArgs = new IType[argCount];
      for( int i = 0; i < objectArgs.length; i++ )
      {
        objectArgs[i] = JavaTypes.OBJECT();
      }
      return objectArgs;
    }

    public IConstructorInfo getConstructor()
    {
      return _constructorInfo;
    }
  }

  private static class UniversalConstructorInfo extends BaseFeatureInfo implements IConstructorInfo
  {
    private IParameterInfo[] _paramInfo;

    public UniversalConstructorInfo( IType[] argTypes )
    {
      super( ErrorType.getInstance() );
      _paramInfo = new IParameterInfo[argTypes.length];
      for (int i = 0; i < _paramInfo.length; i++) {
        _paramInfo[i] = new SimpleParameterInfo(this, argTypes[i], i);
      }
    }

    public boolean isStatic()
    {
      return true;
    }

    public String getName()
    {
      return ErrorType.getInstance().getName() + " Constructor";
    }

    public IType getType()
    {
      return ErrorType.getInstance();
    }

    public IParameterInfo[] getParameters()
    {
      return _paramInfo;
    }

    public IConstructorHandler getConstructor()
    {
      return new IConstructorHandler()
      {
        public Object newInstance( Object... args )
        {
          throw new UnsupportedOperationException( "ErrorType cannot invoke constructors" );
        }
      };
    }

    public List<IExceptionInfo> getExceptions() {
      return Collections.emptyList();
    }

    @Override
    public boolean isDefault() {
      return false;
    }

    public List<IAnnotationInfo> getDeclaredAnnotations() {
      return Collections.emptyList();
    }
  }

  private static class UniversalProperty extends BaseFeatureInfo implements IPropertyInfo
  {
    private String _name;

    public UniversalProperty( String name )
    {
      super( ErrorType.getInstance() );
      _name = name;
    }

    public boolean isStatic()
    {
      return false;
    }

    public String getName()
    {
      return _name;
    }

    public boolean isReadable()
    {
      return true;
    }

    public boolean isWritable(IType whosAskin) {
      return true;
    }

    public boolean isWritable()
    {
      return isWritable(null);
    }

    public IPropertyAccessor getAccessor()
    {
      return new IPropertyAccessor()
      {
        public Object getValue( Object ctx )
        {
          throw new UnsupportedOperationException( "getValue not implemented by UniversalProperty" );
        }

        public void setValue( Object ctx, Object value )
        {
          throw new UnsupportedOperationException( "setValue not implemented by UniversalProperty" );
        }
      };
    }

    public IPresentationInfo getPresentationInfo()
    {
      return IPresentationInfo.Default.GET;
    }

    public IType getFeatureType()
    {
      return ErrorType.getInstance();
    }

    public List<IAnnotationInfo> getDeclaredAnnotations()
    {
      return Collections.emptyList();
    }
  }

  public boolean isDeprecated() {
    return false;
  }

  public String getDeprecatedReason() {
    return null;
  }
}
