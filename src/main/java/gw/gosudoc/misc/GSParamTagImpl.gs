package gw.gosudoc.misc

uses com.sun.javadoc.Doc
uses com.sun.javadoc.ParamTag
uses com.sun.javadoc.SourcePosition
uses com.sun.javadoc.Tag
uses com.sun.tools.doclets.internal.toolkit.util.TextTag

uses java.lang.Override

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
  @Override
  function parameterName(): String{
    return _paramName
  }

  @Override
  function parameterComment(): String{
    return "parameterComment"
  }


  @Override
  property get TypeParameter(): boolean{
    return false
  }

  @Override
  function name(): String{
    return kind()
  }

  @Override
  function holder(): Doc{
    return _holder
  }

  @Override
  function kind(): String{
    return "param"
  }

  @Override
  function text(): String{
    return "${kind()} ${_paramName} ${_content}"
  }

  @Override
  function inlineTags(): Tag[]{
    return {_content}
  }

  @Override
  function firstSentenceTags(): Tag[]{
    return {}
  }

  @Override
  function position(): SourcePosition{
    return null
  }
}