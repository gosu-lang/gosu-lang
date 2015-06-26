package gw.gosudoc.misc

uses com.sun.javadoc.*

/**
 * Created by carson on 4/17/15.
 */
class GSParameterImpl implements com.sun.javadoc.Parameter {

  var _name : String as Name
  var _type : com.sun.javadoc.Type as Type

  construct(name : String, type : com.sun.javadoc.Type) {
    _name = name
    _type = type
  }

  override function type(): com.sun.javadoc.Type{
    return _type
  }

  override function name(): String{
    return _name
  }

  override function typeName(): String{
    return type().typeName()
  }

  override function annotations(): AnnotationDesc[]{
    return {}
  }
}