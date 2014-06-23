/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.annotations.instances;

import gw.util.StreamUtil;

import java.io.FileOutputStream;
import java.io.File;
import java.io.Writer;

public class AnnotationInstancesTestGenerator
{
  static String OUTPUT_DIR = "C:\\eng\\diamond\\pl2\\active\\core\\platform\\gosu-test\\src\\gw\\spec\\core\\annotations\\instances\\";

  static String TEST_CLASS_NAME = "GeneratedCoreAnnotationInstancesTest";
  static String TEST_CLASS_ENHANCEMENTS_DIRECTLY_NAME = "GeneratedCoreAnnotationInstancesOnEnhancementDirectlyTest";
  static String TEST_CLASS_ENHANCEMENTS_THROUGH_ENHANCED_NAME = "GeneratedCoreAnnotationInstancesOnEnhancementThroughEnhancedTest";
  static String TEST_CLASS_TOP_LEVEL_CLASS_ANNOTATIONS = "GeneratedCoreAnnotationInstancesOnTopLevelClassTest";
  static String TEST_CLASS_TOP_LEVEL_INTERFACE_ANNOTATIONS = "GeneratedCoreAnnotationInstancesOnTopLevelInterfacesTest";

  static String ANNOTATION_HOLDER_CLASS_NAME = "GeneratedAnnotationHolderClass";
  static String ANNOTATION_HOLDER_ENHANCEMMENT_CLASS_NAME = "GeneratedAnnotationHolderClassEnhancement";

  static String TOP_LEVEL_CLASS_NAME_PREFIX = "_GeneratedTopLevelHolderClass_";
  static String TOP_LEVEL_INTERFACE_NAME_PREFIX = "_GeneratedTopLevelHolderInterface_";

  static String[][] FEATURE_DECLS = {

    {"func", "%MODIFIER% function func_%NAME%() {}", "gsClass.getTypeInfo().getMethod(gsClass, \"func_%NAME%\")"},

    {"prop", "%MODIFIER% property get prop_%NAME%() : String { return null }", "gsClass.getTypeInfo().getProperty(gsClass, \"prop_%NAME%\")"},

    {"var", "%MODIFIER% var var_%NAME% : String", "gsClass.getTypeInfo().getProperty(gsClass, \"var_%NAME%\")"},

    {"var_w_prop", "%MODIFIER% var var_w_prop_%NAME% : String as var_prop_%NAME%", "gsClass.getTypeInfo().getProperty(gsClass, \"var_prop_%NAME%\")"},

    {"class", "%MODIFIER% class Class_%NAME%{}", "gsClass.getInnerClass(\"Class_%NAME%\").getTypeInfo()"},

    {"iface", "%MODIFIER% interface Interface_%NAME%{}", "gsClass.getInnerClass(\"Interface_%NAME%\").getTypeInfo()"},
  };

  static String[][] ANNOTATIONS = {

    {"gosu_deprecated_relative", "@Deprecated(\"foo\")",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_deprecated_relative_comment_before", "/* foo */ @Deprecated(\"foo\")",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_deprecated_relative_comment_after", "@Deprecated(\"foo\") /* foo */",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_deprecated_relative_comment_before_and_after", "/* foo */ @Deprecated(\"foo\") /* foo */",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_deprecated_fully_qualified", "@gw.lang.Deprecated(\"foo\")",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_deprecated_in_comment", "/* @deprecated foo */",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_deprecated_in_comment_w_text_before", "/* This is a test comment\n" +
                                                 " * \n" +
                                                 " * @deprecated foo */",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_deprecated_in_comment_multi_line", "/* @deprecated foo\n" +
                                              " *             bar */",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\\nbar\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n"},

    {"java_deprecated_fully_qualified_no_parens", "@java.lang.Deprecated",
      "    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );\n"},

    {"java_deprecated_fully_qualified_w_parens", "@java.lang.Deprecated()",
      "    assertEquals( 1, map.get( TypeSystem.get( java.lang.Deprecated.class) ).size() );\n"},

    {"java_local_relative_no_parens_no_args", "@LocalJavaAnnotation",
      "    assertEquals( 1, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalJavaAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"default\", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getValue()).value() );\n"},

    {"java_local_relative_parens_no_args", "@LocalJavaAnnotation()",
      "    assertEquals( 1, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalJavaAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"default\", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getValue()).value() );\n"},

    {"java_local_relative_parens_with_arg", "@LocalJavaAnnotation(\"foo\")",
      "    assertEquals( 1, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalJavaAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", ((LocalJavaAnnotation) map.get( TypeSystem.get( LocalJavaAnnotation.class) ).get( 0 ).getValue()).value() );\n"},

    {"gosu_local_relative_no_parens_no_args", "@LocalGosuAnnotation",
      "    assertEquals( 1, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"default\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n"},

    {"gosu_local_relative_parens_no_args", "@LocalGosuAnnotation()",
      "    assertEquals( 1, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"default\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n"},

    {"gosu_local_relative_parens_with_arg", "@LocalGosuAnnotation(\"foo\")",
      "    assertEquals( 1, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n"},

    {"gosu_multi_no_comment", "@LocalGosuAnnotation(\"foo\")\n" +
                              "@LocalGosuAnnotation(\"bar\")\n",
      "    assertEquals( 2, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n" +
      "    assertEquals( \"bar\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 1 ).getValue(), \"Value\" ) );\n"},

    {"gosu_multi_comment_before", "/* foo */ @LocalGosuAnnotation(\"foo\")\n" +
                                  "@LocalGosuAnnotation(\"bar\")\n",
      "    assertEquals( 2, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n" +
      "    assertEquals( \"bar\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 1 ).getValue(), \"Value\" ) );\n"},

    {"gosu_multi_comment_between", "@LocalGosuAnnotation(\"foo\")\n" +
                                   "/* foo */ @LocalGosuAnnotation(\"bar\")\n",
      "    assertEquals( 2, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n" +
      "    assertEquals( \"bar\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 1 ).getValue(), \"Value\" ) );\n"},

    {"gosu_multi_comment_after", "@LocalGosuAnnotation(\"foo\")\n" +
                                 "@LocalGosuAnnotation(\"bar\")/* foo */\n",
      "    assertEquals( 2, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n" +
      "    assertEquals( \"bar\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 1 ).getValue(), \"Value\" ) );\n"},

    {"gosu_multi_comment_before_w_annotation", "/* @deprecated foo */@LocalGosuAnnotation(\"foo\")\n" +
                                 "@LocalGosuAnnotation(\"bar\")\n",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n" +
      "    assertEquals( 2, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n" +
      "    assertEquals( \"bar\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 1 ).getValue(), \"Value\" ) );\n"},

    {"gosu_multi_comment_between_w_annotation", "@LocalGosuAnnotation(\"foo\")/* @deprecated foo */\n" +
                                 "@LocalGosuAnnotation(\"bar\")\n",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n" +
      "    assertEquals( 2, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n" +
      "    assertEquals( \"bar\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 1 ).getValue(), \"Value\" ) );\n"},

    {"gosu_multi_comment_after_w_annotation", "@LocalGosuAnnotation(\"foo\")\n" +
                                 "@LocalGosuAnnotation(\"bar\")/* @deprecated foo */\n",
      "    assertEquals( 1, map.get( TypeSystem.get( gw.lang.Deprecated.class) ).size() );\n" +
      "    assertEquals( \"foo\", ((gw.lang.Deprecated) map.get( TypeSystem.get( gw.lang.Deprecated.class) ).get( 0 ).getValue()).value() );\n" +
      "    assertEquals( 2, map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).size() );\n" +
      "    assertEquals( \"foo\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 0 ).getValue(), \"Value\" ) );\n" +
      "    assertEquals( \"bar\", gw.lang.reflect.ReflectUtil.getProperty( map.get( TypeSystem.getByFullName( \"gw.spec.core.annotations.instances.LocalGosuAnnotation\" ) ).get( 1 ).getValue(), \"Value\" ) );\n"},
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
    writeFile( OUTPUT_DIR + TEST_CLASS_ENHANCEMENTS_THROUGH_ENHANCED_NAME + ".java", generateTestClassForEnhancementAnnotationUsagesThroughEnhanced() );

    // Top Level tests
    generateTopLevelClassHolders();
    writeFile( OUTPUT_DIR + TEST_CLASS_TOP_LEVEL_CLASS_ANNOTATIONS + ".java", generateTestClassForTopLevelClassHolders() );

    generateTopLevelInterfaceHolders();
    writeFile( OUTPUT_DIR + TEST_CLASS_TOP_LEVEL_INTERFACE_ANNOTATIONS + ".java", generateTestClassForTopLevelInterfaceHolders() );
  }

  private static String generateTestClassForTopLevelClassHolders()
  {
    StringBuilder sb = new StringBuilder();
    genTestHeader( sb, TEST_CLASS_TOP_LEVEL_CLASS_ANNOTATIONS );
    for( String[] annotationDecls : ANNOTATIONS )
    {
      for( String accesibility : ACC )
      {
        if( !accesibility.contains( "private" ) && !accesibility.contains( "protected" ) )
        {
          // ============================================================================
          // regular test
          // ============================================================================
          String className = TOP_LEVEL_CLASS_NAME_PREFIX + annotationDecls[0] + "_" + accesibility;
          sb.append( "  public void test" );
          sb.append( "_" );
          sb.append( annotationDecls[0] );
          sb.append( "_" );
          sb.append( accesibility );
          sb.append( "(){\n" );

          // get type
          sb.append( "    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( \"" );
          sb.append( AnnotationInstancesTestGenerator.class.getPackage().getName() );
          sb.append( "." );
          sb.append( className );
          sb.append( "\" ).getTypeInfo().getAnnotations());\n" );

          // jam in the assertions
          sb.append( annotationDecls[2] );

          sb.append( "  }\n" );
          sb.append( "\n" );
        }
      }
    }
    sb.append( "}" );
    return sb.toString();
  }

  private static String generateTestClassForTopLevelInterfaceHolders()
  {
    StringBuilder sb = new StringBuilder();
    genTestHeader( sb, TEST_CLASS_TOP_LEVEL_INTERFACE_ANNOTATIONS );
    for( String[] annotationDecls : ANNOTATIONS )
    {
      // ============================================================================
      // regular test
      // ============================================================================
      String className = TOP_LEVEL_INTERFACE_NAME_PREFIX + annotationDecls[0];
      sb.append( "  public void test" );
      sb.append( "_" );
      sb.append( annotationDecls[0] );
      sb.append( "(){\n" );

      // get type
      sb.append( "    Map<IType, List<IAnnotationInfo>> map = map(TypeSystem.getByFullName( \"" );
      sb.append( AnnotationInstancesTestGenerator.class.getPackage().getName() );
      sb.append( "." );
      sb.append( className );
      sb.append( "\" ).getTypeInfo().getAnnotations());\n" );

      // jam in the assertions
      sb.append( annotationDecls[2] );

      sb.append( "  }\n" );
      sb.append( "\n" );
    }
    sb.append( "}" );
    return sb.toString();
  }

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
              sb.append( "(){\n" );

              // get type
              sb.append( "    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( \"" );
              sb.append( AnnotationInstancesTestGenerator.class.getPackage().getName() );
              sb.append( "." );
              sb.append( ANNOTATION_HOLDER_CLASS_NAME );
              sb.append( "\" );\n" );

              // get annotations
              sb.append( "    Map<IType, List<IAnnotationInfo>> map = map(" );
              sb.append( feature[2].replace( "%NAME%", annotationDecls[0] + "_" + modifierString ) );
              sb.append( ".getAnnotations());\n" );

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
                sb.append( "(){\n" );

                // get type
                sb.append( "    IGosuClass gsClass = (IGosuClass) TypeSystem.getByFullName( \"" );
                sb.append( AnnotationInstancesTestGenerator.class.getPackage().getName() );
                sb.append( "." );
                sb.append( ANNOTATION_HOLDER_ENHANCEMMENT_CLASS_NAME );
                sb.append( "\" );\n" );

                // get annotations
                sb.append( "    Map<IType, List<IAnnotationInfo>> map = map(" );
                sb.append( feature[2].replace( "%NAME%", annotationDecls[0] + "_" + modifierString + "_enhancement" ) );
                sb.append( ".getAnnotations());\n" );

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
                sb.append( AnnotationInstancesTestGenerator.class.getPackage().getName() );
                sb.append( "." );
                sb.append( ANNOTATION_HOLDER_CLASS_NAME );
                sb.append( "\" );\n" );

                // get annotations
                sb.append( "    Map<IType, List<IAnnotationInfo>> map = map(" );
                sb.append( feature[2].replace( "%NAME%", annotationDecls[0] + "_" + modifierString + "_enhancement" ) );
                sb.append( ".getAnnotations());\n" );

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
    sb.append( "package " ).append( AnnotationInstancesTestGenerator.class.getPackage().getName() ).append( ";\n" );
    sb.append( "\n" );
    sb.append( "import gw.test.TestClass;\n" );
    sb.append( "import gw.lang.*;\n" );
    sb.append( "import gw.lang.reflect.*;\n" );
    sb.append( "import gw.lang.reflect.gs.*;\n" );
    sb.append( "import java.util.*;\n" );
    sb.append( "import gw.internal.gosu.parser.GosuClass;\n" );
    sb.append( "\n" );
    sb.append( "public class " ).append( testName ).append( " extends TestClass {\n" );
    sb.append( "\n" );
    sb.append(
            "  private Map<IType, List<IAnnotationInfo>> map(List<IAnnotationInfo> annotations) {\n" +
            "    HashMap<IType, List<IAnnotationInfo>> map = new HashMap<IType, List<IAnnotationInfo>>();\n" +
            "    for (IAnnotationInfo annotation : annotations) {\n" +
            "      List<IAnnotationInfo> infoList = map.get(annotation.getType());\n" +
            "      if (infoList == null) {\n" +
            "        infoList = new ArrayList<IAnnotationInfo>();\n" +
            "        map.put(annotation.getType(), infoList);\n" +
            "      }\n" +
            "      infoList.add(annotation);\n" +
            "    }\n" +
            "    return map;\n" +
            "  }\n" );
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
    sb.append( "package " ).append( AnnotationInstancesTestGenerator.class.getPackage().getName() ).append( "\n" );
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

  private static void generateTopLevelClassHolders() throws Exception
  {
    for( String[] annotationDecls : ANNOTATIONS )
    {
      for( String accesibility : ACC )
      {
        if( !accesibility.contains( "private" ) && !accesibility.contains( "protected" ) )
        {
          String className = TOP_LEVEL_CLASS_NAME_PREFIX + annotationDecls[0] + "_" + accesibility;
          StringBuilder sb = new StringBuilder();
          sb.append( "package " ).append( AnnotationInstancesTestGenerator.class.getPackage().getName() ).append( "\n" );
          sb.append( "\n" );
          sb.append( annotationDecls[1] );
          sb.append( "\n" );
          sb.append( accesibility );
          sb.append( " class " ).append( className ).append( "{}\n" );
          String s = sb.toString();
          writeFile( OUTPUT_DIR + className + ".gs", s );
        }
      }
    }
  }

  private static void generateTopLevelInterfaceHolders() throws Exception
  {
    for( String[] annotationDecls : ANNOTATIONS )
    {
      String className = TOP_LEVEL_INTERFACE_NAME_PREFIX + annotationDecls[0];
      StringBuilder sb = new StringBuilder();
      sb.append( "package " ).append( AnnotationInstancesTestGenerator.class.getPackage().getName() ).append( "\n" );
      sb.append( "\n" );
      sb.append( annotationDecls[1] );
      sb.append( "\n" );
      sb.append( "interface " ).append( className ).append( "{}\n" );
      String s = sb.toString();
      writeFile( OUTPUT_DIR + className + ".gs", s );
    }
  }

  private static String generateHolderEnhancement()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( AnnotationInstancesTestGenerator.class.getPackage().getName() ).append( "\n" );
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
    System.out.println( Runtime.getRuntime().exec(new String[]{"p4", "edit", name}) );
    Writer out = StreamUtil.getOutputStreamWriter( new FileOutputStream( file, false ) );
    out.write( content );
    StreamUtil.close( out );
  }
}
