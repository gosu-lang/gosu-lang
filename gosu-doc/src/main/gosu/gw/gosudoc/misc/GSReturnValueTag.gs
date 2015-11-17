package gw.gosudoc.misc

uses com.sun.javadoc.Doc
uses com.sun.tools.doclets.internal.toolkit.util.TextTag

class GSReturnValueTag extends TextTag{

  construct( doc: Doc, str: String ){
    super( doc, str?:"" )
  }

  override function kind(): String{
    return "return"
  }
}