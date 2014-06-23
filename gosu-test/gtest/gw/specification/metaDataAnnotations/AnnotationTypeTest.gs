package gw.specification.metaDataAnnotations

uses gw.BaseVerifyErrantTest

class AnnotationTypeTest extends BaseVerifyErrantTest {

  function testErrant_AnnotationTypes() {
    processErrantType( Errant_AnnotationTypes )
  }

  function testAnnotationSticks() {
    assertEquals( 1, MyClass.Type.TypeInfo.DeclaredAnnotations.Count )
    var anno = MyClass.Type.TypeInfo.DeclaredAnnotations[0]
    var stuff = anno.getFieldValue( "stuff" )
    assertEquals( "class", stuff )

    var annotations = MyClass.Type.TypeInfo.DeclaredConstructors[0].DeclaredAnnotations
    assertEquals( 1, annotations.Count )
    stuff = annotations[0].getFieldValue( "stuff" )
    assertEquals( "constructor", stuff )

    annotations = MyClass.Type.TypeInfo.DeclaredMethods[0].DeclaredAnnotations
    assertEquals( 1, annotations.Count )
    stuff = annotations[0].getFieldValue( "stuff" )
    assertEquals( "method", stuff )

    annotations = MyClass.Type.TypeInfo.getProperty( MyClass, "_x" ).DeclaredAnnotations
    assertEquals( 1, annotations.Count )
    stuff = annotations[0].getFieldValue( "stuff" )
    assertEquals( "field", stuff )

    annotations = MyClass.Type.TypeInfo.getProperty( MyClass, "Fred" ).DeclaredAnnotations
    assertEquals( 1, annotations.Count )
    stuff = annotations[0].getFieldValue( "stuff" )
    assertEquals( "property", stuff )
  }

  function testAnnotationBinaryCompatibleWithJavaAnnotation() {
    var cls = MyClass.Type.BackingClass
    assertEquals( 1, cls.DeclaredAnnotations.Count )
    var anno = cls.DeclaredAnnotations[0] as MyAnno
    assertEquals( "class", anno.stuff() )

    var annotations = cls.DeclaredConstructors[0].DeclaredAnnotations
//    assertEquals( 1, annotations.Count )
//    anno = annotations[0] as MyAnno
//    assertEquals( "constructor", anno.stuff() )

    annotations = cls.getDeclaredMethod( "foo", {} ).DeclaredAnnotations
    assertEquals( 1, annotations.Count )
    anno = annotations[0] as MyAnno
    assertEquals( "method", anno.stuff() )

    annotations = cls.getDeclaredField( "_x" ).DeclaredAnnotations
    assertEquals( 1, annotations.Count )
    anno = annotations[0] as MyAnno
    assertEquals( "field", anno.stuff() )

    annotations = cls.getDeclaredMethod( "getFred", {} ).DeclaredAnnotations
    assertEquals( 1, annotations.Count )
    anno = annotations[0] as MyAnno
    assertEquals( "property", anno.stuff() )

  }


}