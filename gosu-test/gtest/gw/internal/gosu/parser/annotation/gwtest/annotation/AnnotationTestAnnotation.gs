package gw.internal.gosu.parser.annotation.gwtest.annotation

@AnnotationTestMetaAnnotation
class AnnotationTestAnnotation implements IAnnotation, gw.lang.annotation.IInherited {
  private var _value : String

  construct(value : String) {
    _value = value
  }

  construct() {
    this("EMPTY")
  }

  function toString() : String {
    return _value
  }
}
