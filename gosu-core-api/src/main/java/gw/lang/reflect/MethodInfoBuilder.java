/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.CommonServices;
import gw.fs.IFile;
import gw.lang.UnstableAPI;
import gw.lang.parser.IExpression;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UnstableAPI
public class MethodInfoBuilder {

  private static final IMethodCallHandler EMPTY_CALL_HANDLER = new IMethodCallHandler() {
    public Object handleCall(Object ctx, Object... args) {
      return null;
    }
  };

  private String _name;
  private ParameterInfoBuilder[] _parameters = {};
  private IType _returnType;
  private IMethodCallHandler _callHandler;
  private ExceptionInfoBuilder[] _exceptions = {};
  private String _deprecated;
  private boolean _isStatic;
  private String _description;
  private String _returnDescription;
  private IGenericTypeVariable[] _typeVars;
  private boolean _hidden;
  private LocationInfo _locationInfo;

  public MethodInfoBuilder withName(String name) {
    this._name = name;
    return this;
  }

  public MethodInfoBuilder withParameters(ParameterInfoBuilder... parameters) {
    this._parameters = parameters == null ? new ParameterInfoBuilder[] {} : parameters;
    return this;
  }

  public MethodInfoBuilder withReturnType(IType returnType) {
    this._returnType = returnType;
    return this;
  }

  public MethodInfoBuilder withReturnType(Class returnType) {
    return withReturnType(TypeSystem.get(returnType));
  }

  public MethodInfoBuilder withCallHandler( IMethodCallHandler callHandler) {
    this._callHandler = callHandler;
    return this;
  }

  public MethodInfoBuilder withExceptions(ExceptionInfoBuilder... exceptions) {
    this._exceptions = exceptions == null ? new ExceptionInfoBuilder[] {} : exceptions;
    return this;
  }

  public MethodInfoBuilder withDeprecated(String deprecated) {
    this._deprecated = deprecated;
    return this;
  }

  public MethodInfoBuilder withStatic() {
    return withStatic(true);
  }

  public MethodInfoBuilder withStatic(boolean isStatic) {
    this._isStatic = isStatic;
    return this;
  }

  public MethodInfoBuilder withDescription(String description) {
    this._description = description;
    return this;
  }

  public MethodInfoBuilder withReturnDescription(String returnDescription) {
    this._returnDescription = returnDescription;
    return this;
  }

  public MethodInfoBuilder withTypeVars(IGenericTypeVariable... typeVars) {
    _typeVars = typeVars;
    return this;
  }

  public MethodInfoBuilder withHidden() {
    return withHidden(true);
  }

  public MethodInfoBuilder withHidden(boolean hidden) {
    _hidden = hidden;
    return this;
  }

  public MethodInfoBuilder like(Class clazz, String method, Class... params) {
    IType[] types = new IType[params.length];
    for (int i = 0; i < params.length; i++) {
      types[i] = TypeSystem.get(params[i]);
    }
    return like(TypeSystem.get(clazz), method, types);
  }

  public MethodInfoBuilder like(IType type, String method, IType... params) {
    return like(type.getTypeInfo().getMethod(method, params));
  }

  public MethodInfoBuilder like(IMethodInfo method) {
    withName(method.getDisplayName());
    withReturnType(method.getReturnType());
    withCallHandler(method.getCallHandler());
    withStatic(method.isStatic());
    withDeprecated(method.getDeprecatedReason());
    withDescription(method.getDescription());
    withReturnDescription(method.getReturnDescription());

    IParameterInfo[] delegateParams = method.getParameters();
    ParameterInfoBuilder[] params = new ParameterInfoBuilder[delegateParams.length];
    for (int i = 0; i < params.length; i++) {
      params[i] = new ParameterInfoBuilder().like(delegateParams[i]);
    }
    withParameters(params);

    List<IExceptionInfo> delegateExceptions = method.getExceptions();
    ExceptionInfoBuilder[] exceptions = new ExceptionInfoBuilder[delegateExceptions.size()];
    int idx = 0;
    for (IExceptionInfo info : delegateExceptions) {
      exceptions[idx++] = new ExceptionInfoBuilder().like(info);
    }
    withExceptions(exceptions);
    if ( method instanceof ILocationAwareFeature ) {
      ILocationAwareFeature locationAwareFeature = (ILocationAwareFeature) method;
      _locationInfo = locationAwareFeature.getLocationInfo();
    }
    return this;
  }

  public IMethodInfo build(IFeatureInfo container) {
    return _typeVars == null ? new BuiltMethodInfo(this, container) : new BuiltGenericMethodInfo(this, container);
  }

  public MethodInfoBuilder withLocation( LocationInfo locationInfo ) {
    _locationInfo = locationInfo;
    return this;
  }

  private static class BuiltMethodInfo implements IMethodInfo, IOptionalParamCapable, ILocationAwareFeature {

    private final IFeatureInfo _container;
    private final String _name;
    private final String _signature;
    private final IParameterInfo[] _parameters;
    private final IType _returnType;
    private final IMethodCallHandler _callHandler;
    private final List<IExceptionInfo> _exceptions;
    private final String _deprecated;
    private final boolean _isStatic;
    private final String _description;
    private final String _returnDescription;
    private final boolean _hidden;
    private final LocationInfo _locationInfo;

    public BuiltMethodInfo(MethodInfoBuilder builder, IFeatureInfo container) {
      assert container != null;
      assert builder._name != null;
      this._container = container;
      this._name = builder._name;
      this._parameters = new IParameterInfo[builder._parameters.length];
      for (int i = 0; i < builder._parameters.length; i++) {
        this._parameters[i] = builder._parameters[i].build(_container);
      }
      this._returnType = builder._returnType == null ? JavaTypes.pVOID() : builder._returnType;
      this._callHandler = builder._callHandler == null ? EMPTY_CALL_HANDLER : builder._callHandler;
      IExceptionInfo[] tmp = new IExceptionInfo[builder._exceptions.length];
      for (int i = 0; i < builder._exceptions.length; i++) {
        tmp[i] = builder._exceptions[i].build(_container);
      }
      this._exceptions = Collections.unmodifiableList(Arrays.asList(tmp));
      this._deprecated = builder._deprecated;
      this._isStatic = builder._isStatic;
      this._description = builder._description;
      this._returnDescription = builder._returnDescription;
      this._hidden = builder._hidden;
      this._signature = makeSignature();
      this._locationInfo = builder._locationInfo;
    }

    public IParameterInfo[] getParameters() {
      return _parameters;
    }

    @Override
    public IExpression[] getDefaultValueExpressions() {
      IExpression[] defValues = new IExpression[_parameters.length];
      for( int i = 0; i < _parameters.length; i++ ) {
        defValues[i] = ((ParameterInfoBuilder.BuiltParameterInfo)_parameters[i]).getDefaultValue();
      }
      return defValues;
    }

    @Override
    public String[] getParameterNames() {
      String[] names = new String[_parameters.length];
      for( int i = 0; i < _parameters.length; i++ ) {
        names[i] = ((ParameterInfoBuilder.BuiltParameterInfo)_parameters[i]).getName();
      }
      return names;
    }
    
    public IType getReturnType() {
      return _returnType;
    }

    public IMethodCallHandler getCallHandler() {
      return _callHandler;
    }

    public String getReturnDescription() {
      return _returnDescription;
    }

    public List<IExceptionInfo> getExceptions() {
      return _exceptions;
    }

    public boolean isScriptable() {
      return !_hidden;
    }

    public boolean isDeprecated() {
      return _deprecated != null;
    }

    public String getDeprecatedReason() {
      return _deprecated;
    }

    @Override
    public boolean isDefaultImpl() {
      return false;
    }

    public boolean isVisible( IScriptabilityModifier constraint) {
      return true;
    }

    public boolean isHidden() {
      return _hidden;
    }

    public boolean isStatic() {
      return _isStatic;
    }

    public boolean isPrivate() {
      return false;
    }

    public boolean isInternal() {
      return false;
    }

    public boolean isProtected() {
      return false;
    }

    public boolean isPublic() {
      return true;
    }

    public boolean isAbstract() {
      return false;
    }

    public boolean isFinal() {
      return false;
    }

    public List<IAnnotationInfo> getAnnotations() {
      return Collections.emptyList();
    }

    public List<IAnnotationInfo> getDeclaredAnnotations() {
      return Collections.emptyList();
    }

    public List<IAnnotationInfo> getAnnotationsOfType(IType type) {
      return Collections.emptyList();
    }

    @Override
    public IAnnotationInfo getAnnotation( IType type )
    {
      return null;
    }

    @Override
    public boolean hasDeclaredAnnotation( IType type )
    {
      return false;
    }

    public boolean hasAnnotation(IType type) {
      return false;
    }

    public IFeatureInfo getContainer() {
      return _container;
    }

    public IType getOwnersType() {
      return _container.getOwnersType();
    }

    public String getName() {
      return _signature;
    }

    private String makeSignature() {
      String name = getDisplayName();
      name += "(";
      IParameterInfo[] parameterInfos = getParameters();
      if (parameterInfos.length > 0) {
        name += " ";
        for (int i = 0; i < parameterInfos.length; i++) {
          IParameterInfo iParameterInfo = getParameters()[i];
          if (i != 0) {
            name += ", ";
          }
          name += iParameterInfo.getFeatureType().getName();
        }
        name += " ";
      }
      name += ")";
      return name;
    }

    public String getDisplayName() {
      return _name;
    }

    public String getDescription() {
      return _description;
    }

    @Override
    public LocationInfo getLocationInfo() {
      return _locationInfo;
    }

    @Override
    public String toString()
    {
      return getName();
    }
  }

  private static class BuiltGenericMethodInfo extends BuiltMethodInfo implements IGenericMethodInfo {
    private IGenericTypeVariable[] _typeVars;

    public BuiltGenericMethodInfo(MethodInfoBuilder builder, IFeatureInfo container) {
      super(builder, container);
      _typeVars = builder._typeVars;
    }

    public IGenericTypeVariable[] getTypeVariables() {
      return _typeVars;
    }


    public IType getParameterizedReturnType(IType... typeParams) {
      TypeVarToTypeMap actualParamByVarName = TypeSystem.mapTypeByVarName( getOwnersType(), getOwnersType(), false);
      int i = 0;
      for (IGenericTypeVariable tv : getTypeVariables()) {
        if (actualParamByVarName.isEmpty()) {
          actualParamByVarName = new TypeVarToTypeMap();
        }
        actualParamByVarName.put(tv.getTypeVariableDefinition().getType(), typeParams[i++]);
      }
      return TypeSystem.getActualType(getReturnType(), actualParamByVarName, false);
    }

    public IType[] getParameterizedParameterTypes(IType... typeParams) {
      return getParameterizedParameterTypes2( null, typeParams );
    }
    public IType[] getParameterizedParameterTypes2( IGosuClass ownersType, IType... typeParams) {
      IType ot = ownersType == null ? getOwnersType() : ownersType;
      TypeVarToTypeMap actualParamByVarName = TypeSystem.mapTypeByVarName( ot, ot, false);
      int i = 0;
      for (IGenericTypeVariable tv : getTypeVariables()) {
        if (actualParamByVarName.isEmpty()) {
          actualParamByVarName = new TypeVarToTypeMap();
        }
        actualParamByVarName.put(tv.getTypeVariableDefinition().getType(), typeParams[i++]);
      }

      IType[] paramTypes = getParameterTypes();

      for (int j = 0; j < paramTypes.length; j++) {
        paramTypes[j] = TypeSystem.getActualType(paramTypes[j], actualParamByVarName, false);
      }

      return paramTypes;
    }

    private IType[] getParameterTypes() {
      IParameterInfo[] parameters = getParameters();
      IType[] paramTypes = new IType[parameters.length];
      for (int j = 0; j < parameters.length; j++) {
        paramTypes[j] = parameters[j].getFeatureType();
      }
      return paramTypes;
    }

    public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
    {
      return inferTypeParametersFromArgumentTypes2( null, argTypes );
    }
    public TypeVarToTypeMap inferTypeParametersFromArgumentTypes2( IGosuClass ownersTypes, IType... argTypes) {
      IFunctionType funcType = TypeSystem.getOrCreateFunctionType(getDisplayName(), getReturnType(), getParameterTypes());
      IType[] genParamTypes = funcType.getParameterTypes();
      IType ot = ownersTypes == null ? getOwnersType() : ownersTypes;
      TypeVarToTypeMap actualParamByVarName = TypeSystem.mapTypeByVarName( ot, ot, false);
      IGenericTypeVariable[] typeVars = getTypeVariables();
      for (IGenericTypeVariable tv : typeVars) {
        if (actualParamByVarName.isEmpty()) {
          actualParamByVarName = new TypeVarToTypeMap();
        }
        actualParamByVarName.put(tv.getTypeVariableDefinition().getType(), tv.getBoundingType());
      }

      TypeVarToTypeMap map = new TypeVarToTypeMap();
      for (int i = 0; i < argTypes.length; i++) {
        if (genParamTypes.length > i) {
          TypeSystem.inferTypeVariableTypesFromGenParamTypeAndConcreteType(genParamTypes[i], argTypes[i], map);
        }
      }
      return map;
    }
  }

}
