package gw.gosudoc.misc

uses com.sun.javadoc.*

uses java.lang.Override

/**
 * Created by carson on 4/17/15.
 */
class GSParameterImpl implements Parameter {

  var _name : String as Name
  var _type : com.sun.javadoc.Type as Type

  construct(name : String, type : com.sun.javadoc.Type) {
    _name = name
    _type = type
  }

  @Override
  function type(): com.sun.javadoc.Type{
    return _type
  }

  @Override
  function name(): String{
    return _name
  }

  @Override
  function typeName(): String{
    return type().typeName()
  }

  @Override
  function annotations(): AnnotationDesc[]{
    return {}
  }
}