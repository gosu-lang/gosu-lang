package gw.internal.xml.xsd.typeprovider;

import gw.lang.Autocreate;

import java.lang.annotation.Annotation;
import java.util.concurrent.Callable;

/**
*/
public class MyAutocreate implements Autocreate, Callable {
  private Class<? extends Callable> _nodeTypeToCreate;

  // called reflectively from MemberAccessTransformer#autoCreateEntityInstance()
  public MyAutocreate() {
  }

  public MyAutocreate( Class<? extends Callable> nodeTypeToCreate ) {
    _nodeTypeToCreate = nodeTypeToCreate;
  }

  @Override
  public Class<? extends Callable> value() {
    return _nodeTypeToCreate;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return Autocreate.class;
  }

  @Override
  public Object call() throws Exception {
    return _nodeTypeToCreate.newInstance().call();
  }
}
