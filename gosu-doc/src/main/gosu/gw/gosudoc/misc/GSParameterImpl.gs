package gw.gosudoc.misc

uses  gw.gosudoc.com.sun.javadoc.*

/**
 * Created by carson on 4/17/15.
 */
class GSParameterImpl implements  gw.gosudoc.com.sun.javadoc.Parameter {

  var _name : String as Name
  var _type :  gw.gosudoc.com.sun.javadoc.Type as Type

  construct(name : String, type :  gw.gosudoc.com.sun.javadoc.Type) {
    _name = name
    _type = type
  }

  override function type():  gw.gosudoc.com.sun.javadoc.Type{
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