package gw.gosudoc.type

uses gw.gosudoc.doc.GSRootDocImpl

uses java.lang.Override

class GSVoidTypeImpl extends GSPrimitiveTypeImpl {

  //==========PUBLIC CONSTRUCTORS==========//
  construct( rootDoc: GSRootDocImpl ){
    super( null, rootDoc, null )
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  @Override
  function typeName(): String{
    return "void"
  }

  @Override
  function qualifiedTypeName(): String{
    return "void"
  }

  //==========PUBLIC METHODS==========//
  @Override
  function initialize(){
    // do nothing.
  }
}
