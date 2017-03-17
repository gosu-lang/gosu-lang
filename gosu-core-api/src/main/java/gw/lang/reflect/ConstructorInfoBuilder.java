/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.IExpression;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConstructorInfoBuilder {

  private ParameterInfoBuilder[] _parameters = {};
  private IConstructorHandler _constructorHandler;
  private ExceptionInfoBuilder[] _exceptions = {};
  private String _deprecated;
  private String _description;
  private IRelativeTypeInfo.Accessibility _accessibility = IRelativeTypeInfo.Accessibility.PUBLIC;
  private Object _userData;

  public ConstructorInfoBuilder withAccessibility(IRelativeTypeInfo.Accessibility accessibility) {
    this._accessibility = accessibility;
    return this;
  }
  public ConstructorInfoBuilder withParameters(ParameterInfoBuilder... parameters) {
    this._parameters = parameters == null ? new ParameterInfoBuilder[] {} : parameters;
    return this;
  }

  public ConstructorInfoBuilder withConstructorHandler(IConstructorHandler constructorHandler) {
    this._constructorHandler = constructorHandler;
    return this;
  }

  public ConstructorInfoBuilder withExceptions(ExceptionInfoBuilder... exceptions) {
    this._exceptions = exceptions == null ? new ExceptionInfoBuilder[] {} : exceptions;
    return this;
  }

  public ConstructorInfoBuilder withDeprecated(String deprecated) {
    this._deprecated = deprecated;
    return this;
  }

  public ConstructorInfoBuilder withDescription(String description) {
    this._description = description;
    return this;
  }

  public ConstructorInfoBuilder withUserData(Object userData) {
    this._userData = userData;
    return this;
  }

  public ConstructorInfoBuilder like(Class clazz, Class... params) {
    IType[] types = new IType[params.length];
    for (int i = 0; i < params.length; i++) {
      types[i] = TypeSystem.get(params[i]);
    }
    return like(TypeSystem.get(clazz), types);
  }

  public ConstructorInfoBuilder like(IType type, IType... params) {
    return like(type.getTypeInfo().getConstructor(params));
  }

  public ConstructorInfoBuilder like(IConstructorInfo constructor) {
    withConstructorHandler(constructor.getConstructor());
    withDeprecated(constructor.getDeprecatedReason());
    withDescription(constructor.getDescription());

    IParameterInfo[] delegateParams = constructor.getParameters();
    ParameterInfoBuilder[] params = new ParameterInfoBuilder[delegateParams.length];
    for (int i = 0; i < params.length; i++) {
      params[i] = new ParameterInfoBuilder().like(delegateParams[i]);
    }
    withParameters(params);

    List<IExceptionInfo> delegateExceptions = constructor.getExceptions();
    ExceptionInfoBuilder[] exceptions = new ExceptionInfoBuilder[delegateExceptions.size()];
    int idx = 0;
    for (IExceptionInfo info : delegateExceptions) {
      exceptions[idx++] = new ExceptionInfoBuilder().like(info);
    }
    withExceptions(exceptions);
    return this;
  }

  public IConstructorInfo build(IFeatureInfo container) {
    return new BuiltConstructorInfo(this, container);
  }

  public interface IBuilt {
    Object getUserData();
  }
  private static class BuiltConstructorInfo implements IConstructorInfo, IOptionalParamCapable, IBuilt {

    private final IFeatureInfo _container;
    private final IParameterInfo[] _parameters;
    private final IConstructorHandler _constructorHandler;
    private final List<IExceptionInfo> _exceptions;
    private final String _deprecated;
    private final String _description;
    private final IRelativeTypeInfo.Accessibility _accessibility;
    private final Object _userData;

    public BuiltConstructorInfo(ConstructorInfoBuilder builder, IFeatureInfo container) {
      assert container != null;
//      assert builder._constructorHandler != null;
      this._container = container;
      this._parameters = new IParameterInfo[builder._parameters.length];
      for (int i = 0; i < builder._parameters.length; i++) {
        this._parameters[i] = builder._parameters[i].build(_container);
      }
      this._constructorHandler = builder._constructorHandler;
      IExceptionInfo[] tmp = new IExceptionInfo[builder._exceptions.length];
      for (int i = 0; i < builder._exceptions.length; i++) {
        tmp[i] = builder._exceptions[i].build(_container);
      }
      this._exceptions = Collections.unmodifiableList(Arrays.asList(tmp));
      this._deprecated = builder._deprecated;
      this._description = builder._description;
      _userData = builder._userData;
      _accessibility = builder._accessibility;
    }

    public IParameterInfo[] getParameters() {
      return _parameters;
    }

    public IType getType() {
      return _container.getOwnersType();
    }

    public IConstructorHandler getConstructor() {
      return _constructorHandler;
    }

    public List<IExceptionInfo> getExceptions() {
      return _exceptions;
    }

    public Object getUserData() {
      return _userData;
    }

    @Override
    public boolean isDefault() {
      return false;
    }

    public boolean isScriptable() {
      return true;
    }

    public boolean isDeprecated() {
      return _deprecated != null;
    }

    public String getDeprecatedReason() {
      return _deprecated;
    }

    public boolean isDefaultImpl() {
      return false;
    }

    public boolean isVisible( IScriptabilityModifier constraint) {
      return true;
    }

    public boolean isHidden() {
      return false;
    }

    public boolean isStatic() {
      return true;
    }

    public boolean isPrivate() {
      return _accessibility.equals(IRelativeTypeInfo.Accessibility.PRIVATE);
    }

    public boolean isInternal() {
      return _accessibility.equals(IRelativeTypeInfo.Accessibility.INTERNAL);
    }

    public boolean isProtected() {
      return _accessibility.equals(IRelativeTypeInfo.Accessibility.PROTECTED);
    }

    public boolean isPublic() {
      return _accessibility.equals(IRelativeTypeInfo.Accessibility.PUBLIC);
    }

    public boolean isAbstract() {
      return false;
    }

    public boolean isFinal() {
      return false;
    }

    public boolean isReified() {
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
      return makeSignature();
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
      return getOwnersType().getRelativeName();
    }

    public String getDescription() {
      return _description;
    }

    @Override
    public String toString()
    {
      return getName();
    }

    @Override
    public boolean equals( Object o )
    {
      if( this == o )
      {
        return true;
      }
      if( o == null || getClass() != o.getClass() )
      {
        return false;
      }

      BuiltConstructorInfo that = (BuiltConstructorInfo)o;

      return _container.getOwnersType() == that.getOwnersType() &&
             getName().equals( that.getName() );
    }

    @Override
    public int hashCode()
    {
      int result = _container.getOwnersType().hashCode();
      result = 31 * result + getName().hashCode();
      return result;
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
        names[i] = (_parameters[i]).getName();
      }
      return names;
    }

  }

}
