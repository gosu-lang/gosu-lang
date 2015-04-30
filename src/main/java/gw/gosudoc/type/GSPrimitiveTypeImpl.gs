package gw.gosudoc.type

uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

uses java.lang.Override

class GSPrimitiveTypeImpl extends GSTypeImpl {

  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( type, rootDoc, owner )
  }

  @Override
      property get Primitive(): boolean{
    return true
  }

  @Override
  function initialize(){
    setSimpleNames()
    verify()
  }

}