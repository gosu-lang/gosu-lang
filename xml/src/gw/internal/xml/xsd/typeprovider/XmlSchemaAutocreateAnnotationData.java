/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 *
 */
class XmlSchemaAutocreateAnnotationData implements IAnnotationInfo {
  private final IType _concreteType;
  private final IFeatureInfo _container;

  public XmlSchemaAutocreateAnnotationData( IType concreteType, IFeatureInfo container ) {
    _concreteType = concreteType;
    _container = container;
  }

  @Override
  public Object getInstance() {
    if( _concreteType != null && JavaTypes.LIST().isAssignableFrom( _concreteType ) ) {
      return new MyAutocreate( ArrayListCreator.class );
    }
    return new MyAutocreate( null );
  }

  public static class ArrayListCreator implements Callable {
    @Override
    public Object call() throws Exception {
      return new ArrayList();
    }
  }

  @Override
  public Object getFieldValue(String field) {
    throw new RuntimeException("Not supported yet");
  }

  @Override
  public IType getType() {
    return GosuTypes.AUTOCREATE();
  }

  @Override
  public String getName() {
    return GosuTypes.AUTOCREATE().getName();
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public IType getOwnersType() {
    return _container.getOwnersType();
  }

}
