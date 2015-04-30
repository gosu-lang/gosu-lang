package gw.gosudoc.type

uses gw.gosudoc.doc.GSRootDocImpl

class GSVoidTypeImpl extends GSPrimitiveTypeImpl {

  //==========PUBLIC CONSTRUCTORS==========//
  construct( rootDoc: GSRootDocImpl ){
    super( null, rootDoc, null )
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  override function typeName(): String{
    return "void"
  }

  override function qualifiedTypeName(): String{
    return "void"
  }

  //==========PUBLIC METHODS==========//
  override function initialize(){
    // do nothing.
  }
}
