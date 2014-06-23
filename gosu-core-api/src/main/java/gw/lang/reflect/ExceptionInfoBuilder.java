/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class ExceptionInfoBuilder {

  private String _name;
  private String _description;
  private IType _type;

  public ExceptionInfoBuilder withName(String name) {
    this._name = name;
    return this;
  }

  public ExceptionInfoBuilder withDescription(String description) {
    this._description = description;
    return this;
  }

  public ExceptionInfoBuilder withType(IType type) {
    this._type = type;
    return this;
  }

  public ExceptionInfoBuilder withType(Class clazz) {
    this._type = TypeSystem.get(clazz);
    return this;
  }

  public ExceptionInfoBuilder like(IExceptionInfo exception) {
    withName(exception.getName());
    withDescription(exception.getDescription());
    withType(exception.getExceptionType());
    return this;
  }

  public IExceptionInfo build(IFeatureInfo container) {
    return new BuiltExceptionInfo(this, container);
  }

  private static class BuiltExceptionInfo implements IExceptionInfo {

    private final IFeatureInfo _container;
    private final String _name;
    private final String _description;
    private final IType _type;

    public BuiltExceptionInfo(ExceptionInfoBuilder builder, IFeatureInfo container) {
      assert container != null;
      assert builder._name != null;
      assert builder._type != null;
      this._container = container;
      this._name = builder._name;
      this._description = builder._description;
      this._type = builder._type;
    }

    public IFeatureInfo getContainer() {
      return this._container;
    }

    public IType getOwnersType() {
      return this._container.getOwnersType();
    }

    public String getName() {
      return this._name;
    }

    public String getDisplayName() {
      return this._name;
    }

    public String getDescription() {
      return this._description;
    }

    public IType getExceptionType() {
      return this._type;
    }

  }

}