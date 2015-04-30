package gw.gosudoc.misc

uses com.sun.javadoc.Doc
uses com.sun.tools.doclets.internal.toolkit.util.TextTag

uses java.lang.Override

class GSReturnValueTag extends TextTag{

  construct( doc: Doc, str: String ){
    super( doc, str?:"" )
  }

  @Override
  function kind(): String{
    return "return"
  }
}