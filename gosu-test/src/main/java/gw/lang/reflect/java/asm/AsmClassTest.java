/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.config.ExecutionMode;
import gw.fs.FileFactory;
import gw.fs.IFile;
import gw.internal.gosu.parser.DefaultPlatformHelper;
import gw.lang.reflect.ReflectUtil;
import gw.test.TestClass;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public class AsmClassTest extends TestClass {
  @Override
  public void beforeTestMethod() {
    super.beforeTestMethod();
    DefaultPlatformHelper.EXECUTION_MODE = ExecutionMode.COMPILER;
  }

  @Override
  public void afterTestMethod( Throwable possibleException ) {
    super.afterTestMethod( possibleException );
    DefaultPlatformHelper.EXECUTION_MODE = ExecutionMode.RUNTIME;
  }

  public void testClassDeclaration() {
    AsmClass asmClass = loadAsmClass( Asm_Simple.class );
    assertEquals( "gw.lang.reflect.java.asm.Asm_Simple<S<java.util.List<T>>, T<java.lang.Comparable<T>>>", asmClass.getType().toString() );
    assertTrue( asmClass.isGeneric() );
    assertEquals( 2, asmClass.getType().getTypeParameters().size() );
    assertTrue( asmClass.getType().getTypeParameters().get( 0 ).isTypeVariable() );
    assertTrue( asmClass.getType().getTypeParameters().get( 1 ).isTypeVariable() );
  }
  
  public void testFields() {
    AsmClass asmClass = loadAsmClass( Asm_Simple.class );
    List<AsmField> fields = asmClass.getDeclaredFields();
    assertEquals( Asm_Simple.class.getDeclaredFields().length, fields.size() );
    int i = 0;
    assertEquals( "private int _int", fields.get( i++ ).toString() );
    assertEquals( "private int[] _intArray", fields.get( i++ ).toString() );
    assertEquals( "private int[][] _intArrayArray", fields.get( i++ ).toString() );
    assertEquals( "private java.lang.String _string", fields.get( i++ ).toString() );
    assertEquals( "private java.lang.String[] _stringArray", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<java.lang.String> _listOfString", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<java.util.List<java.lang.String>> _listOfListOfString", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<S> _listOfS", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<?> _listofWild", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<java.lang.String, ?> _mapofWild", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<?, S> _mapofWildS", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<? extends S> _listofWildS", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<? extends java.util.List<S>> _listofWildListS", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<? super java.util.List<S>> _listofContraWildListS", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<java.lang.String, java.util.List<java.lang.String>> _mapStringListOfString", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<java.lang.String, gw.lang.reflect.java.asm.Asm_Simple$InnerClass<java.lang.String>> _mapStringInnerClassOfString", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<java.lang.String, gw.lang.reflect.java.asm.Asm_Simple$InnerClass<T>> _mapStringInnerClassOfT", fields.get( i++ ).toString() );

    assertEquals( "private gw.lang.reflect.java.asm.Asm_Simple$InnerClass _yay", fields.get( i++ ).toString() );
    assertEquals( "private gw.lang.reflect.java.asm.Asm_Simple$InnerClass<T> _yay1", fields.get( i++ ).toString() );
    assertEquals( "private gw.lang.reflect.java.asm.Asm_Simple$InnerClass<java.lang.String> _yay2", fields.get( i++ ).toString() );
    assertEquals( "private gw.lang.reflect.java.asm.Asm_Simple$InnerClass<gw.lang.reflect.java.asm.Asm_Simple$InnerClass<java.lang.String>> _yay3", fields.get( i++ ).toString() );

    assertEquals( "private java.util.List<java.lang.String>[] _listOfStringA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<java.util.List<java.lang.String>>[] _listOfListOfStringA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<S>[] _listOfSA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<?>[] _listofWildA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<java.lang.String, ?>[] _mapofWildA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<? extends S>[] _listofWildSA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<? extends java.util.List<S>>[] _listofWildListSA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.List<? super java.util.List<S>>[] _listofContraWildListSA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<java.lang.String, java.util.List<java.lang.String>>[] _mapStringListOfStringA", fields.get( i++ ).toString() );
    assertEquals( "private java.util.Map<java.lang.String, gw.lang.reflect.java.asm.Asm_Simple$InnerClass<java.lang.String>>[] _mapStringInnerClassOfStringA", fields.get( i++ ).toString() );
  }

  public void testMethods() {
    AsmClass asmClass = loadAsmClass( Asm_Simple.class );
    List<AsmMethod> methods = asmClass.getDeclaredMethodsAndConstructors();
    assertEquals( Asm_Simple.class.getDeclaredMethods().length + Asm_Simple.class.getDeclaredConstructors().length, methods.size() );
    int i = 0;
    assertEquals( "public void <init>()", methods.get( i++ ).toString() );
    assertEquals( "public int intMethod(int)", methods.get( i++ ).toString() );
    assertEquals( "public transient int varArgIntMethod(int[])", methods.get( i++ ).toString() );
    assertEquals( "public transient int varArgStringMethod(java.lang.String[])", methods.get( i++ ).toString() );
    assertEquals( "public transient <E<java.lang.Object>>E varArgStringMethod(int[])", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<java.lang.Object[]> varArgStringMethod()", methods.get( i++ ).toString() );
    assertEquals( "public int[] intArrayMethod(int[])", methods.get( i++ ).toString() );
    assertEquals( "public int[][] intArrayArrayMethod(int[][])", methods.get( i++ ).toString() );
    assertEquals( "public java.lang.String[][] stringArrayArrayMethod(java.lang.String[][])", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<java.lang.Object[]> enumArrayArrayMethod(gw.lang.reflect.java.asm.Asm_Enum[][], java.lang.String[], java.lang.String, int[], int)", methods.get( i++ ).toString() );
    assertEquals( "public <E<java.lang.Object>>java.lang.String stringMethod(E, java.lang.String)", methods.get( i++ ).toString() );
    assertEquals( "public <E<java.util.List<java.lang.String>>>java.lang.String[] stringArrayMethod(E)", methods.get( i++ ).toString() );
    assertEquals( "public <E<java.lang.Object>, R<java.util.List<? extends java.lang.String>>>java.util.List<java.lang.String> listOfStringMethod(E, R)", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<java.util.List<java.lang.String>> listOfListOfStringMethod()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<S> listOfSMethod()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<?> listofWildMethod()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.Map<java.lang.String, ?> mapofWildMethod()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<? extends S> listofWildSMethod()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<? extends java.util.List<S>> listofWildListSMethod()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.List<? super java.util.List<S>> listofContraWildListSMethod()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.Map<java.lang.String, java.util.List<java.lang.String>> mapStringListOfStringMethod()", methods.get( i++ ).toString() );
    assertEquals( "public static transient <E<java.lang.Object>>java.util.HashSet<E> newHashSet(E[])", methods.get( i++ ).toString() );
    assertEquals( "public <E<java.lang.Object>>gw.lang.reflect.java.asm.Asm_Simple$InnerClass<E> returnsInnerClass(gw.lang.reflect.java.asm.Asm_Simple$InnerClass)", methods.get( i++ ).toString() );
    assertEquals( "public java.util.Map<java.lang.String, byte[]> mapOfStringToPrimitiveByteArray()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.Map<java.lang.String, byte[][]> mapOfStringToPrimitiveByteArrayArray()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.Map<byte[][], java.lang.String> mapOfPrimitiveByteArrayArrayToString()", methods.get( i++ ).toString() );
    assertEquals( "public java.util.Set<gw.lang.reflect.java.asm.Asm_Simple$AccountSyncedField<? extends java.lang.CharSequence, ?>> getAccountSyncedFields()", methods.get( i++ ).toString() );
  }

  public void testInnerClasses() {
    AsmClass asmClass = loadAsmClass( Asm_InnerClasses.class );
    Map<String, AsmInnerClassType> innerClasses = asmClass.getInnerClasses();
    assertEquals( 3, innerClasses.size() );

    asmClass = loadAsmClass( Asm_InnerClasses.Inner1.class );
    innerClasses = asmClass.getInnerClasses();
    assertEquals( 1, innerClasses.size() );

    asmClass = loadAsmClass( Asm_InnerClasses.Inner2.class );
    innerClasses = asmClass.getInnerClasses();
    assertEquals( 3, innerClasses.size() );

    AsmClass muhInner = loadAsmClass( Asm_InnerClasses.Muh.Inner.class );
    assertEquals( 1, muhInner.getInterfaces().size() );
    assertEquals( "java.lang.Comparable<gw.lang.reflect.java.asm.Asm_InnerClasses$Muh$Inner>", muhInner.getInterfaces().get( 0 ).toString() );
    List<AsmMethod> methods = muhInner.getDeclaredMethodsAndConstructors();
    assertEquals( Asm_InnerClasses.Muh.Inner.class.getDeclaredMethods().length + Asm_InnerClasses.Muh.Inner.class.getDeclaredConstructors().length, methods.size() );
    int i = 0;
    assertEquals( "public void <init>(gw.lang.reflect.java.asm.Asm_InnerClasses$Muh)", methods.get( i++ ).toString() );
    assertEquals( "public int compareTo(gw.lang.reflect.java.asm.Asm_InnerClasses$Muh$Inner)", methods.get( i++ ).toString() );
  }

  public void testClassAnnotations() {
    AsmClass asmClass = loadAsmClass( Asm_AnnotationTesting.class );
    List<AsmAnnotation> annotations = asmClass.getDeclaredAnnotations();
    assertEquals( 4, annotations.size() );

    AsmAnnotation annotation = annotations.get( 0 );
    assertEquals( Deprecated.class.getName(), annotation.getType().getName() );
    assertTrue( annotation.getFieldValues().isEmpty() );

    annotation = annotations.get( 1 );
    assertEquals( Annotation_Simple.class.getName(), annotation.getType().getName() );
    assertTrue( annotation.getFieldValues().isEmpty() );

    annotation = annotations.get( 2 );
    assertEquals( Annotation_Default.class.getName(), annotation.getType().getName() );
    Map<String,Object> fieldValues = annotation.getFieldValues();
    assertEquals( 1, fieldValues.size() );
    assertEquals( "value", fieldValues.keySet().iterator().next() );
    assertEquals( "fish", fieldValues.get( "value" ) );

    annotation = annotations.get( 3 );
    assertEquals( Annotation_Fields.class.getName(), annotation.getType().getName() );

    fieldValues = annotation.getFieldValues();
    assertEquals( 7, fieldValues.size() );
    assertEquals( 7, fieldValues.get( "int_field" ) );
    assertEquals( Arrays.asList( new String[]{"a", "b", "c"} ), fieldValues.get( "stringArray_field" ) );
    assertEquals( "Larry", fieldValues.get( "enum_field" ) );

    List<?> values = (List<?>)fieldValues.get( "annoArray_field" );
    assertEquals( 2, values.size() );
    AsmAnnotation anno = (AsmAnnotation)values.get( 0 );
    Map<String, Object> annoFieldValues = anno.getFieldValues();
    assertEquals( 1, annoFieldValues.size() );
    assertEquals( "hi", annoFieldValues.get( "value" ) );
    anno = (AsmAnnotation)values.get( 1 );
    annoFieldValues = anno.getFieldValues();
    assertEquals( 1, annoFieldValues.size() );
    assertEquals( "bye", annoFieldValues.get( "value" ) );

    anno = (AsmAnnotation)fieldValues.get( "anno_field" );
    annoFieldValues = anno.getFieldValues();
    assertEquals( 1, annoFieldValues.size() );
    assertEquals( "hi", annoFieldValues.get( "value" ) );

    assertEquals( StringBuilder.class.getName(), fieldValues.get( "class_field" ) );

    values = (List<?>)fieldValues.get( "intArray_field" );
    assertEquals( Arrays.asList( new Integer[]{1, 2, 3} ), values );
  }

  public void testBullshit() {
    AsmClass asmClass = loadAsmClass( ReflectUtil.class );
    System.out.println( asmClass );
  }

  public void testAnnotationClass() {
    AsmClass asmClass = loadAsmClass( Annotation_Fields.class );

  }

  public void testFieldAnnotations() {
    AsmClass asmClass = loadAsmClass( Asm_AnnotationTesting.class );

    List<AsmField> fields = asmClass.getDeclaredFields();
    assertEquals( 2, fields.size() );

    AsmField field = fields.get( 1 );
    assertEquals( "_string2", field.getName() );
    assertTrue( field.getAnnotations().isEmpty() );

    field = fields.get( 0 );
    assertEquals( "_string", field.getName() );

    List<AsmAnnotation> annotations = field.getAnnotations();
    assertEquals( 4, annotations.size() );

    AsmAnnotation annotation = annotations.get( 0 );
    assertEquals( Deprecated.class.getName(), annotation.getType().getName() );
    assertTrue( annotation.getFieldValues().isEmpty() );

    annotation = annotations.get( 1 );
    assertEquals( Annotation_Simple.class.getName(), annotation.getType().getName() );
    assertTrue( annotation.getFieldValues().isEmpty() );

    annotation = annotations.get( 2 );
    assertEquals( Annotation_Default.class.getName(), annotation.getType().getName() );
    Map<String,Object> fieldValues = annotation.getFieldValues();
    assertEquals( 1, fieldValues.size() );
    assertEquals( "value", fieldValues.keySet().iterator().next() );
    assertEquals( "meat", fieldValues.get( "value" ) );

    annotation = annotations.get( 3 );
    assertEquals( Annotation_Fields.class.getName(), annotation.getType().getName() );

    fieldValues = annotation.getFieldValues();
    assertEquals( 7, fieldValues.size() );
    assertEquals( 7, fieldValues.get( "int_field" ) );
    assertEquals( Arrays.asList( new String[]{"a", "b", "c"} ), fieldValues.get( "stringArray_field" ) );
    assertEquals( "Larry", fieldValues.get( "enum_field" ) );

    List<?> values = (List<?>)fieldValues.get( "annoArray_field" );
    assertEquals( 2, values.size() );
    AsmAnnotation anno = (AsmAnnotation)values.get( 0 );
    Map<String, Object> annoFieldValues = anno.getFieldValues();
    assertEquals( 1, annoFieldValues.size() );
    assertEquals( "hi", annoFieldValues.get( "value" ) );
    anno = (AsmAnnotation)values.get( 1 );
    annoFieldValues = anno.getFieldValues();
    assertEquals( 1, annoFieldValues.size() );
    assertEquals( "bye", annoFieldValues.get( "value" ) );

    anno = (AsmAnnotation)fieldValues.get( "anno_field" );
    annoFieldValues = anno.getFieldValues();
    assertEquals( 1, annoFieldValues.size() );
    assertEquals( "hi", annoFieldValues.get( "value" ) );

    assertEquals( StringBuilder.class.getName(), fieldValues.get( "class_field" ) );

    values = (List<?>)fieldValues.get( "intArray_field" );
    assertEquals( Arrays.asList( new Integer[]{1, 2, 3} ), values );
  }

//  public void testDirectJarFileLoadingWorks() {
//    try {
//      String fileLocation = "jar:file:/c:/Temp/javassist24.jar!/javassist/bytecode/AccessFlag.class";
//      IFile classFile = FileFactory.instance().getIFile( new URL( fileLocation ), false );
//      AsmClass asmClass = AsmClassLoader.loadClass( null, "javasssist.bytecode.AccessFlag", classFile.openInputStream() );
//      System.out.println( asmClass );
//    }
//    catch( Exception e ) {
//      throw new RuntimeException( e );
//    }
//  }
  AsmClassLoader _asmClassLoader = new AsmClassLoader(null);

  private AsmClass loadAsmClass( Class<?> cls ) {
    URL location = cls.getProtectionDomain().getCodeSource().getLocation();
    String fileLocation = "";
    try {
      if( location.getFile().toLowerCase().endsWith( ".jar" ) ) {
        fileLocation = "jar:" + location.toExternalForm() + "!/" + cls.getPackage().getName().replace( '.', '/' ) + '/' + getSimpleName( cls ) + ".class";
        IFile classFile = FileFactory.instance().getIFile( new URL( fileLocation ), false );
        return _asmClassLoader.findClass( cls.getName(), classFile.openInputStream() );
      }
      else {
        File dir = new File( location.toURI() );
        dir = new File( dir, cls.getPackage().getName().replace( '.', '/' ) );
        File classFile = new File( dir, getSimpleName( cls ) + ".class" );
        return _asmClassLoader.findClass( cls.getName(), new FileInputStream( classFile ) );
      }
    }
    catch( Exception e ) {
      throw new RuntimeException( fileLocation, e );
    }
  }

  private String getSimpleName( Class<?> cls ) {
    String name = "";
    if( cls.getEnclosingClass() != null ) {
      name += getSimpleName( cls.getEnclosingClass() ) + '$';
    }
    return name + cls.getSimpleName();
  }
}
