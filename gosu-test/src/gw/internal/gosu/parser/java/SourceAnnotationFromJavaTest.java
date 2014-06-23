/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.data.SampleClassWithAnnotations;
import gw.internal.gosu.parser.ExecutionEnvironment;
import gw.internal.gosu.parser.java.classinfo.JavaSourceAnnotationInfo;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.module.IModule;
import gw.test.TestClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 */
public class SourceAnnotationFromJavaTest extends TestClass
{
  public void testEntryPoint() throws Exception {
    ExecutionEnvironment execEnv = (ExecutionEnvironment)TypeSystem.getExecutionEnvironment();
    execEnv.uninitializeMultipleModules();
    TypeSystemSetup typeSystemSetup = new TypeSystemSetup( execEnv );
    typeSystemSetup.initializeGosu();
    IModule mod = execEnv.getModule( "myModule" );
    JavaSourceType javaClassInfo = (JavaSourceType)TypeSystem.getJavaClassInfo( SampleClassWithAnnotations.class.getName(), mod );

    _testMe( javaClassInfo );
  }

  /**
   * This test checks that all the Java annotations in the accompanying SampleClassWithAnnotations class
   * agree with with the annotation info we build from the class info in the JavaSourceType.
   */
  public void _testMe( JavaSourceType javaClass ) throws Exception {
    System.out.println( "Testing class: " + javaClass.getName() );
    for( Method m : SampleClassWithAnnotations.class.getDeclaredMethods() ) {
      System.out.println( "  Method: " + m.getName() );
      for( Annotation ann : m.getDeclaredAnnotations() ) {
        System.out.println( "    Annotation: " + ann.annotationType().getName() );
        for( Method am : ann.annotationType().getDeclaredMethods() ) {
          System.out.println( "      Arg: " + am.getName() );
          verify( javaClass,
                  m.getName(),
                  new FieldValue( am.getName(), am.invoke( ann ) ) );
        }
      }
    }
  }

  private void verify( JavaSourceType javaClass, String funcName, FieldValue... fieldValuePairs ) throws Exception {
    IJavaClassMethod method = javaClass.getMethod( funcName );
    IAnnotationInfo annotation = method.getDeclaredAnnotations()[0];
    assertSame( annotation.getClass(), JavaSourceAnnotationInfo.class );
    for( FieldValue fv : fieldValuePairs ) {
      Object valueFromBytecode = fv.getValue();
      Object valueFromOurStuff = annotation.getFieldValue( fv.getField() );
      assertValuesEqual( valueFromBytecode, valueFromOurStuff );
    }
  }

  private void assertValuesEqual( Object valueFromBytecode, Object valueFromOurStuff ) {
    if( valueFromOurStuff.getClass().isArray() ) {
      for( int i = 0; i < ((Object[])valueFromOurStuff).length; i++ ) {
        assertValuesEqual( ((Object[])valueFromBytecode)[i], ((Object[])valueFromOurStuff)[i] );
      }
    }
    else {
      if( valueFromBytecode instanceof Enum ) {
        assertEquals( ((Enum)valueFromBytecode).name(), (String)valueFromOurStuff );
      }
      else if( valueFromBytecode instanceof Annotation ) {
        assertEquals( ((Annotation)valueFromBytecode).annotationType().getName(), ((IAnnotationInfo)valueFromOurStuff).getName() );
      }
      else if( valueFromBytecode instanceof Class ) {
        assertEquals( TypeSystem.get( (Class)valueFromBytecode ), (IType)valueFromOurStuff );
      }
      else {
        assertEquals( valueFromBytecode, valueFromOurStuff );
      }
    }
  }

  class FieldValue  {
    private String _field;
    private Object _value;

    public FieldValue( String field, Object value ) {
      _field = field;
      _value = value;
    }

    public String getField() {
      return _field;
    }

    public Object getValue() {
      return _value;
    }
  }
}
