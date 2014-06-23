package gw.internal.gosu.compiler.sample.annotations

@gw.testharness.DoNotVerifyResource
class PropertyAnnotationsHelper {

  @java.lang.Deprecated
  property get StringProp1() : String {
    return null
  }

  @java.lang.Deprecated
  property set StringProp2( s : String ) {
  }

  @java.lang.Deprecated
  var _field0 : String as StringProp3

  @java.lang.Deprecated
  property get GetFirstWithAnnotationProp() : String {
    return null
  }

  property set GetFirstWithAnnotationProp( s : String ) {
  }

  property get GetFirstWithoutAnnotationProp() : String {
    return null
  }

  @java.lang.Deprecated
  property set GetFirstWithoutAnnotationProp( s : String ) {
  }

  @java.lang.Deprecated
  property set SetFirstWithAnnotationProp( s : String ) {
  }

  property get SetFirstWithAnnotationProp() : String{
    return null
  }

  property get SetFirstWithoutAnnotationProp() : String{
    return null
  }

  @java.lang.Deprecated
  property set SetFirstWithoutAnnotationProp( s : String ) {
  }

  @java.lang.Deprecated
  var _field1 : String as FieldFirstWithAnnotationAndGetter

  property get FieldFirstWithAnnotationAndGetter() : String {
    return null
  }

  var _field2 : String as FieldFirstWithoutAnnotationAndGetter

  @java.lang.Deprecated
  property get FieldFirstWithoutAnnotationAndGetter() : String {
    return null
  }

  @java.lang.Deprecated
  var _field3 : String as FieldFirstWithAnnotationAndSetter

  property set FieldFirstWithAnnotationAndSetter( s : String ) {}

  var _field4 : String as FieldFirstWithoutAnnotationAndSetter

  @java.lang.Deprecated
  property set FieldFirstWithoutAnnotationAndSetter( s : String ) {}

  @java.lang.Deprecated
  var _field5 : String as FieldGetterAndSetterAllAnnotated
  @java.lang.Deprecated
  property get FieldGetterAndSetterAllAnnotated() : String {
    return null
  }
  @java.lang.Deprecated
  property set FieldGetterAndSetterAllAnnotated( s : String ) {}

}