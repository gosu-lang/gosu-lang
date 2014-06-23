package gw.spec.core.annotations.instances

public class LocalGosuAnnotation implements IAnnotation {

  var _value : String as Value = "default"

  construct() {
  }

  construct( s : String ) {
    _value = s
  }

}