package gw.internal.xml.xsd.typeprovider;

import gw.lang.Autoinsert;

import java.lang.annotation.Annotation;

/**
*/
public class MyAutoinsert implements Autoinsert {
  private static final MyAutoinsert INSTANCE = new MyAutoinsert();

  public static Object instance() {
    return INSTANCE;
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return Autoinsert.class;
  }
}
