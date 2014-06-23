package gw.internal.gosu.parser.annotation

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.IAttributedFeatureInfo
uses gw.lang.reflect.IAnnotationInfo
uses gw.lang.reflect.features.IMethodReference
uses gw.lang.reflect.features.IPropertyReference
uses gw.lang.reflect.gs.IGosuPropertyInfo
uses gw.lang.reflect.IMethodInfo

class ParameterAnnotationTest extends BaseVerifyErrantTest {

  function testErrant_ParameterAnnotations() {
    processErrantType( Errant_ParameterAnnotations )
  }

  function testValid_ParameterAnnotations() {
    // Method param annotation
    var annos = getAnnotationsForParam( ParameterAnnotationClass#asdf(Object), "a" )
    assertEquals( 1, annos.size() )
    var anno = annos.get( 0 )
    assertEquals( "java.lang.SuppressWarnings", anno.getName() )
    assertEquals( "blah", anno.getFieldValue( "value" ) )

    // Property Setter param annotation
    annos = getAnnotationsForParam( ParameterAnnotationClass#F, "a" )
    assertEquals( 2, annos.size() )
    anno = annos.get( 0 )
    assertEquals( "java.lang.Deprecated", anno.getName() )
    anno = annos.get( 1 )
    assertEquals( "java.lang.SuppressWarnings", anno.getName() )
    assertArrayEquals( new String[] {"settervalue"}, anno.getFieldValue( "value" ) as String[] )
  }

  function getAnnotationsForParam( fr: IMethodReference, param: String ) : List<IAnnotationInfo> {
    var annos = {} as List<IAnnotationInfo>
    for( pi in fr.MethodInfo.Parameters ) {
      var gpi = pi as IAttributedFeatureInfo
      if( pi.Name.equals( param ) ) {
        for( anno in gpi.DeclaredAnnotations ) {
          annos.add( anno )
        }
      }
    }
    return annos
  }

  function getAnnotationsForParam( fr: IPropertyReference, param: String ) : List<IAnnotationInfo> {
    var annos = {} as List<IAnnotationInfo>
    var prop = fr.PropertyInfo as IGosuPropertyInfo
    for( pi in (prop.Dps.SetterDfs.MethodOrConstructorInfo as IMethodInfo).Parameters ) {
      var gpi = pi as IAttributedFeatureInfo
      if( pi.Name.equals( param ) ) {
        for( anno in gpi.DeclaredAnnotations ) {
          annos.add( anno )
        }
      }
    }
    return annos
  }
}