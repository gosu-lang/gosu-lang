package gw.gosudoc.misc

uses com.sun.javadoc.Doc
uses gw.gosudoc.tags.TextTag

class GSReturnValueTag extends TextTag{

  construct( doc: Doc, str: String ){
    super( doc, str?:"" )
  }

  override function kind(): String{
    return "return"
  }
}