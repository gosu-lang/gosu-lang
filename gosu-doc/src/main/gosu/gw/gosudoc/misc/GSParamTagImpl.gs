package gw.gosudoc.misc

uses  gw.gosudoc.com.sun.javadoc.Doc
uses  gw.gosudoc.com.sun.javadoc.ParamTag
uses  gw.gosudoc.com.sun.javadoc.SourcePosition
uses  gw.gosudoc.com.sun.javadoc.Tag
uses gw.gosudoc.com.sun.tools.doclets.internal.toolkit.util.TextTag

class GSParamTagImpl implements ParamTag{

  //==========PRIVATE FIELDS==========//
  var _holder: Doc
  var _paramName: String
  var _content: TextTag

  construct( holder: Doc, name: String, comment: String ){
    _holder = holder
    _paramName = name
    _content = new ( holder, comment?:"" )
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  override function parameterName(): String{
    return _paramName
  }

  override function parameterComment(): String{
    return "parameterComment"
  }


  override property get TypeParameter(): boolean{
    return false
  }

  override function name(): String{
    return kind()
  }

  override function holder(): Doc{
    return _holder
  }

  override function kind(): String{
    return "param"
  }

  override function text(): String{
    return "${kind()} ${_paramName} ${_content}"
  }

  override function inlineTags(): Tag[]{
    return {_content}
  }

  override function firstSentenceTags(): Tag[]{
    return {}
  }

  override function position(): SourcePosition{
    return null
  }
}