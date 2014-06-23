/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.java;

import gw.util.StreamUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;

public class JavaAnnotationInclusionTestGenerator
{
  static String OUTPUT_DIR = "C:\\eng\\pl\\diamond\\rough\\active\\gosu\\platform\\gosu-test\\src\\gw\\spec\\core\\annotations\\java\\";

  static String TEST_CLASS_NAME = "GeneratedJavaAnnotationInstancesTest";
  static String TEST_CLASS_ENHANCEMENTS_DIRECTLY_NAME = "GeneratedJavaAnnotationInstancesOnEnhancementDirectlyTest";
  static String TEST_CLASS_ENHANCEMENTS_THROUGH_ENHANCED_NAME = "GeneratedJavaAnnotationInstancesOnEnhancementThroughEnhancedTest";
  static String TEST_CLASS_TOP_LEVEL_CLASS_ANNOTATIONS = "GeneratedJavaAnnotationInstancesOnTopLevelClassTest";
  static String TEST_CLASS_TOP_LEVEL_INTERFACE_ANNOTATIONS = "GeneratedJavaAnnotationInstancesOnTopLevelInterfacesTest";

  static String ANNOTATION_HOLDER_CLASS_NAME = "GeneratedJavaAnnotationHolderClass";
  static String ANNOTATION_HOLDER_ENHANCEMMENT_CLASS_NAME = "GeneratedJavaAnnotationHolderClassEnhancement";

  static String TOP_LEVEL_CLASS_NAME_PREFIX = "_GeneratedTopLevelHolderClassForJava_";
  static String TOP_LEVEL_INTERFACE_NAME_PREFIX = "_GeneratedTopLevelHolderInterfaceForJava_";

  static String[][] FEATURE_DECLS = {

    {"func", "%MODIFIER% function func_%NAME%() {}", "JavaAnnotationUtil.getMethod(gsClass, \"func_%NAME%\")"},

    {"prop", "%MODIFIER% property get prop_%NAME%() : String { return null }", "JavaAnnotationUtil.getMethod(gsClass, \"getprop_%NAME%\")"},

    {"var", "%MODIFIER% var var_%NAME% : String", "gsClass.getBackingClassInfo().getDeclaredField(\"var_%NAME%\")"},

    {"var_w_prop", "%MODIFIER% var var_w_prop_%NAME% : String as var_prop_%NAME%", "JavaAnnotationUtil.getMethod(gsClass, \"getvar_prop_%NAME%\")"},

    //TODO cgross - need to ensure that modifiers are on generated setter DFS
//    {"var_w_prop_setter", "%MODIFIER% var var_w_prop_w_setter%NAME% : String as var_prop_w_setter%NAME%", "JavaAnnotationUtil.getMethod(gsClass, \"setvar_prop_w_setter%NAME%\")"},

    {"class", "%MODIFIER% class Class_%NAME%{}", "((IGosuClass) gsClass.getInnerClass(\"Class_%NAME%\")).getBackingClassInfo()"},
    {"iface", "%MODIFIER% interface Interface_%NAME%{}", "((IGosuClass) gsClass.getInnerClass(\"Interface_%NAME%\")).getBackingClassInfo()"},
  };

  static String[][] ANNOTATIONS = {
    {"simple_string", "@SingleStringAnnotation(\"foo\")",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertEquals( \"foo\", ((SingleStringAnnotation) annotations[0]).value() );\n"},

    {"simple_string_class_retention_string", "@SingleStringAnnotationClassRetention(\"foo\")",
      "    assertEquals( 0, annotations.length );"},

    {"simple_string_source_retention_string", "@SingleStringAnnotationSourceRetention(\"foo\")",
      "    assertEquals( 0, annotations.length );"},

    {"simple_int", "@SingleIntAnnotation( 42 )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertEquals( 42, ((SingleIntAnnotation) annotations[0]).value() );\n"},

    {"simple_enum", "@SingleEnumAnnotation( BAR )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );\n"},

    {"simple_enum_fully_qualified", "@SingleEnumAnnotation( SampleEnum.BAR )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertEquals( SampleEnum.BAR, ((SingleEnumAnnotation) annotations[0]).value() );\n"},

    {"simple_class", "@SingleClassAnnotation( String )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertEquals( String.class, ((SingleClassAnnotation) annotations[0]).value() );\n"},

    {"simple_annotation", "@SingleAnnotationAnnotation( new SingleStringAnnotation( \"foo\" ) )",
      "    assertEquals( 1, annotations.length );\n" +
      "    SingleStringAnnotation ssa = ((SingleAnnotationAnnotation) annotations[0]).value();\n" +
      "    assertEquals( \"foo\",  ssa.value() );\n"},

    {"simple_string_array", "@SingleStringArrayAnnotation( {\"foo\", \"bar\"} )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertArrayEquals( new String[]{\"foo\", \"bar\"}, ((SingleStringArrayAnnotation) annotations[0]).value() );\n"},

    {"simple_string_array_explicit_new", "@SingleStringArrayAnnotation( new String[]{\"foo\", \"bar\"} )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertArrayEquals( new String[]{\"foo\", \"bar\"}, ((SingleStringArrayAnnotation) annotations[0]).value() );\n"},

    {"simple_string_array_no_values", "@SingleStringArrayAnnotation( new String[0] )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertArrayEquals( new String[0], ((SingleStringArrayAnnotation) annotations[0]).value() );\n"},

    {"simple_int_array", "@SingleIntArrayAnnotation( {1, 2} )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertEquals( 1, ((SingleIntArrayAnnotation) annotations[0]).value()[0] );\n"+
      "    assertEquals( 2, ((SingleIntArrayAnnotation) annotations[0]).value()[1] );\n"},

    {"simple_enum_array", "@SingleEnumArrayAnnotation( {FOO, BAR} )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );\n"},

    {"simple_enum_array_fully_qualified", "@SingleEnumArrayAnnotation( {SampleEnum.FOO, SampleEnum.BAR} )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertArrayEquals( new SampleEnum[]{SampleEnum.FOO, SampleEnum.BAR}, ((SingleEnumArrayAnnotation) annotations[0]).value() );\n"},

    {"simple_class_array", "@SingleClassArrayAnnotation( {String, String} )",
      "    assertEquals( 1, annotations.length );\n" +
      "    assertArrayEquals( new Class[]{String.class, String.class}, ((SingleClassArrayAnnotation) annotations[0]).value() );\n"},

    {"simple_annotation_array", "@SingleAnnotationArrayAnnotation( {new SingleStringAnnotation( \"foo\" ), " +
                          "                                   new SingleStringAnnotation( \"bar\" )} )",
      "    assertEquals( 1, annotations.length );\n" +
      "    SingleStringAnnotation[] ssa = ((SingleAnnotationArrayAnnotation) annotations[0]).value();\n" +
      "    assertEquals( \"foo\",  ssa[0].value() );\n" +
      "    assertEquals( \"bar\",  ssa[1].value() );\n"},

  };

  static String[] ACC = {
    "",
    "public",
    "private",
    "internal",
    "protected",
  };

  static String[] STATIC = {
    "static",
    "",
  };

  public static void main( String[] args ) throws Exception
  {
    //Main holder
    writeFile( OUTPUT_DIR + ANNOTATION_HOLDER_CLASS_NAME + ".gs", generateHolderClass() );

    //Main enhancement
    writeFile( OUTPUT_DIR + ANNOTATION_HOLDER_ENHANCEMMENT_CLASS_NAME + ".gsx", generateHolderEnhancement() );

    // Main tests
    writeFile( OUTPUT_DIR + TEST_CLASS_NAME + ".java", generateTestClassForDirectAnnotationUsages() );
    writeFile( OUTPUT_DIR + TEST_CLASS_ENHANCEMENTS_DIRECTLY_NAME + ".java", generateTestClassForEnhancementAnnotationUsagesAccessedDirectly() );

    // Top Level tests
    //generateTopLevelClassHolders();
//    writeFile( OUTPUT_DIR + TEST_CLASS_TOP_LEVEL_CLASS_ANNOTATIONS + ".java", generateTestClassForTopLevelClassHolders() );

//    generateTopLevelInterfaceHolders();
//    writeFile( OUTPUT_DIR + TEST_CLASS_TOP_LEVEL_INTERFACE_ANNOTATIONS + ".java", generateTestClassForTopLevelInterfaceHolders() );
  }

//  private static String generateTestClassForTopLevelClassHolders()
//  {
//    StringBuilder sb = new StringBuilder();
//    genTestHeader( sb, TEST_CLASS_TOP_LEVEL_CLASS_ANNOTATIONS );
//    for( String[] annotationDecls : ANNOTATIONS )
//    {
//      for( String accesibility : ACC )
//      {
//        if( !accesibility.contains( "private" ) && !accesibility.contains( "protected" ) )
//        {
//          // ============================================================================
//          // regular test
//          // ============================================================================
//          String className = TOP_LEVEL_CLASS_NAME_PREFIX + annotationDecls[0] + "_" + accesibility;
//          sb.append( "  public void test" );
//          sb.append( "_" );
//          sb.append( annotationDecls[0] );
//          sb.append( "_" );
//          sb.append( accesibility );
//          sb.append( "(){\n" );
//
//          // get type
//          sb.append( "    Map<IType, List<IAnnotationInfo>> map = TypeSystem.getByFullName( \"" );
//          sb.append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() );
//          sb.append( "." );
//          sb.append( className );
//          sb.append( "\" ).getTypeInfo().getAnnotations();\n" );
//
//          // jam in the assertions
//          sb.append( annotationDecls[2] );
//
//          sb.append( "  }\n" );
//          sb.append( "\n" );
//        }
//      }
//    }
//    sb.append( "}" );
//    return sb.toString();
//  }

//  private static String generateTestClassForTopLevelInterfaceHolders()
//  {
//    StringBuilder sb = new StringBuilder();
//    genTestHeader( sb, TEST_CLASS_TOP_LEVEL_INTERFACE_ANNOTATIONS );
//    for( String[] annotationDecls : ANNOTATIONS )
//    {
//      // ============================================================================
//      // regular test
//      // ============================================================================
//      String className = TOP_LEVEL_INTERFACE_NAME_PREFIX + annotationDecls[0];
//      sb.append( "  public void test" );
//      sb.append( "_" );
//      sb.append( annotationDecls[0] );
//      sb.append( "(){\n" );
//
//      // get type
//      sb.append( "    Map<IType, List<IAnnotationInfo>> map = TypeSystem.getByFullName( \"" );
//      sb.append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() );
//      sb.append( "." );
//      sb.append( className );
//      sb.append( "\" ).getTypeInfo().getAnnotations();\n" );
//
//      // jam in the assertions
//      sb.append( annotationDecls[2] );
//
//      sb.append( "  }\n" );
//      sb.append( "\n" );
//    }
//    sb.append( "}" );
//    return sb.toString();
//  }

  private static String generateTestClassForDirectAnnotationUsages()
  {
    StringBuilder sb = new StringBuilder();
    genTestHeader( sb, TEST_CLASS_NAME );
    for( String[] annotationDecls : ANNOTATIONS )
    {
      for( String[] feature : FEATURE_DECLS )
      {
        for( String accesibility : ACC )
        {
          for( String stc : STATIC )
          {
            String modifierString = makeModifierString( accesibility, stc );
            if( modifierApplies( accesibility, feature ) )
            {
              // ============================================================================
              // regular test
              // ============================================================================
              sb.append( "  public void test" );
              sb.append( "_" );
              sb.append( annotationDecls[0] );
              sb.append( "_" );
              sb.append( modifierString );
              sb.append( "_" );
              sb.append( feature[0] );
              sb.append( "() throws Exception {\n" );

              // get type
              sb.append( "    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( \"" );
              sb.append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() );
              sb.append( "." );
              sb.append( ANNOTATION_HOLDER_CLASS_NAME );
              sb.append( "\" );\n" );

              // get annotations
              sb.append( "    Annotation[] annotations = " );
              sb.append( feature[2].replace( "%NAME%", annotationDecls[0] + "_" + modifierString ) );
              sb.append( ".getAnnotations();\n" );

              // jam in the assertions
              sb.append( annotationDecls[2] );

              sb.append( "  }\n" );
              sb.append( "\n" );
            }
          }
        }
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  private static String generateTestClassForEnhancementAnnotationUsagesAccessedDirectly()
  {
    StringBuilder sb = new StringBuilder();
    genTestHeader( sb, TEST_CLASS_ENHANCEMENTS_DIRECTLY_NAME );
    for( String[] annotationDecls : ANNOTATIONS )
    {
      for( String[] feature : FEATURE_DECLS )
      {
        for( String accesibility : ACC )
        {
          for( String stc : STATIC )
          {
            String modifierString = makeModifierString( accesibility, stc );
            if( modifierApplies( accesibility, feature ) )
            {
              // ============================================================================
              // enhancement features
              // ============================================================================
              if( isEnhancementFeature( feature ) )
              {

                // Verify that the annotation is on the
                sb.append( "  public void test_enhancement_directly" );
                sb.append( "_" );
                sb.append( annotationDecls[0] );
                sb.append( "_" );
                sb.append( modifierString );
                sb.append( "_" );
                sb.append( feature[0] );
                sb.append( "() throws Exception {\n" );

                // get type
                sb.append( "    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( \"" );
                sb.append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() );
                sb.append( "." );
                sb.append( ANNOTATION_HOLDER_ENHANCEMMENT_CLASS_NAME );
                sb.append( "\" );\n" );

                // get annotations
                sb.append( "    Annotation[] annotations = " );
                sb.append( feature[2].replace( "%NAME%", annotationDecls[0] + "_" + modifierString + "_enhancement" ) );
                sb.append( ".getAnnotations();\n" );

                // jam in the assertions
                sb.append( annotationDecls[2] );

                sb.append( "  }\n" );
                sb.append( "\n" );

              }
            }
          }
        }
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  private static String generateTestClassForEnhancementAnnotationUsagesThroughEnhanced()
  {
    StringBuilder sb = new StringBuilder();
    genTestHeader( sb, TEST_CLASS_ENHANCEMENTS_THROUGH_ENHANCED_NAME );
    for( String[] annotationDecls : ANNOTATIONS )
    {
      for( String[] feature : FEATURE_DECLS )
      {
        for( String accesibility : ACC )
        {
          for( String stc : STATIC )
          {
            String modifierString = makeModifierString( accesibility, stc );
            if( modifierApplies( accesibility, feature ) )
            {
              // ============================================================================
              // enhancement features
              // ============================================================================
              // All non-private features end up on the class as well
              if( isEnhancementFeature( feature  ) && !accesibility.contains( "private" ) )
              {
                sb.append( "  public void test_enhancement_through_enhanced" );
                sb.append( "_" );
                sb.append( annotationDecls[0] );
                sb.append( "_" );
                sb.append( modifierString );
                sb.append( "_" );
                sb.append( feature[0] );
                sb.append( "(){\n" );

                // get type
                sb.append( "    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( \"" );
                sb.append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() );
                sb.append( "." );
                sb.append( ANNOTATION_HOLDER_CLASS_NAME );
                sb.append( "\" );\n" );

                // get annotations
                sb.append( "    Annotation[] annotations = " );
                sb.append( feature[2].replace( "%NAME%", annotationDecls[0] + "_" + modifierString + "_enhancement" ) );
                sb.append( ".getAnnotations();\n" );

                // jam in the assertions
                sb.append( annotationDecls[2] );

                sb.append( "  }\n" );
                sb.append( "\n" );
              }            }
          }
        }
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  private static void genTestHeader( StringBuilder sb, String testName )
  {
    sb.append( "package " ).append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() ).append( ";\n" );
    sb.append( "\n" );
    sb.append( "import gw.test.TestClass;\n" );
    sb.append( "import gw.lang.*;\n" );
    sb.append( "import gw.lang.reflect.*;\n" );
    sb.append( "import gw.lang.reflect.gs.*;\n" );
    sb.append( "import java.util.*;\n" );
    sb.append( "import gw.internal.gosu.parser.GosuClass;\n" );
    sb.append( "import java.lang.annotation.Annotation;\n" );
    sb.append( "\n" );
    sb.append( "public class " ).append( testName ).append( " extends TestClass {\n" );
    sb.append( "\n" );
  }

  private static String makeModifierString( String accesibility, String stc )
  {
    return accesibility + "_" + stc;
  }

  private static boolean modifierApplies( String accesibility, String[] feature )
  {
    return accesibility.contains( "public" ) || !feature[0].contains( "iface" );
  }

  private static String generateHolderClass()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() ).append( "\n" );
    sb.append( "\n" );
    sb.append( "public class " ).append( ANNOTATION_HOLDER_CLASS_NAME ).append( "{\n" );
    for( String[] annotationDecls : ANNOTATIONS )
    {
      for( String[] feature : FEATURE_DECLS )
      {
        for( String accesibility : ACC )
        {
          for( String stc : STATIC )
          {
            String modifierString = makeModifierString( accesibility, stc );
            if( modifierApplies( modifierString, feature ) )
            {
              sb.append( "  " );
              sb.append( annotationDecls[1] );
              sb.append( "\n" );
              sb.append( "  " );
              sb.append( feature[1]
                .replace( "%NAME%", annotationDecls[0] + "_" + modifierString )
                .replace( "%MODIFIER%", accesibility + " " + stc ) );
              sb.append( "\n" );
              sb.append( "\n" );
            }
          }
        }
      }
    }
    sb.append( "\n" );
    sb.append( "}" );
    return sb.toString();
  }

//  private static void generateTopLevelClassHolders() throws Exception
//  {
//    for( String[] annotationDecls : ANNOTATIONS )
//    {
//      for( String accesibility : ACC )
//      {
//        if( !accesibility.contains( "private" ) && !accesibility.contains( "protected" ) )
//        {
//          String className = TOP_LEVEL_CLASS_NAME_PREFIX + annotationDecls[0] + "_" + accesibility;
//          StringBuilder sb = new StringBuilder();
//          sb.append( "package " ).append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() ).append( "\n" );
//          sb.append( "\n" );
//          sb.append( annotationDecls[1] );
//          sb.append( "\n" );
//          sb.append( accesibility );
//          sb.append( " class " ).append( className ).append( "{}\n" );
//          String s = sb.toString();
//          writeFile( OUTPUT_DIR + className + ".gs", s );
//        }
//      }
//    }
//  }

//  private static void generateTopLevelInterfaceHolders() throws Exception
//  {
//    for( String[] annotationDecls : ANNOTATIONS )
//    {
//      String className = TOP_LEVEL_INTERFACE_NAME_PREFIX + annotationDecls[0];
//      StringBuilder sb = new StringBuilder();
//      sb.append( "package " ).append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() ).append( "\n" );
//      sb.append( "\n" );
//      sb.append( annotationDecls[1] );
//      sb.append( "\n" );
//      sb.append( "interface " ).append( className ).append( "{}\n" );
//      String s = sb.toString();
//      writeFile( OUTPUT_DIR + className + ".gs", s );
//    }
//  }

  private static String generateHolderEnhancement()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( JavaAnnotationInclusionTestGenerator.class.getPackage().getName() ).append( "\n" );
    sb.append( "\n" );
    sb.append( "public enhancement " );
    sb.append( ANNOTATION_HOLDER_ENHANCEMMENT_CLASS_NAME );
    sb.append( " :" );
    sb.append( ANNOTATION_HOLDER_CLASS_NAME );
    sb.append( "{\n" );
    for( String[] annotationDecls : ANNOTATIONS )
    {
      for( String[] feature : FEATURE_DECLS )
      {
        for( String accesibility : ACC )
        {
          for( String stc : STATIC )
          {
            String modifierString = makeModifierString( accesibility, stc );
            if( isEnhancementFeature( feature ) && modifierApplies( modifierString, feature ) )
            {
              sb.append( "  " );
              sb.append( annotationDecls[1] );
              sb.append( "\n" );
              sb.append( "  " );
              sb.append( feature[1]
                .replace( "%NAME%", annotationDecls[0] + "_" + modifierString + "_enhancement" )
                .replace( "%MODIFIER%", accesibility + " " + stc ) );
              sb.append( "\n" );
              sb.append( "\n" );
            }
          }
        }
      }
    }
    sb.append( "\n" );
    sb.append( "}" );
    return sb.toString();
  }

  private static boolean isEnhancementFeature( String[] feature )
  {
    String s = feature[0];
    return !(s.startsWith( "var" ) || s.startsWith( "class" ) || s.startsWith( "iface" ));
  }

  private static void writeFile( String name, String content ) throws Exception
  {
    File file = new File( name );
    if( !file.exists() )
    {
      file.createNewFile();
    }
    System.out.println( Runtime.getRuntime().exec( new String[]{"p4", "edit", name}) );
    Writer out = StreamUtil.getOutputStreamWriter( new FileOutputStream( file, false ) );
    out.write( content );
    StreamUtil.close( out );
  }
}