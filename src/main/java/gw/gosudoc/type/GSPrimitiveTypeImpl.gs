package gw.gosudoc.type

uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

class GSPrimitiveTypeImpl extends GSTypeImpl {

  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( type, rootDoc, owner )
  }

  override property get Primitive(): boolean{
    return true
  }

  override function initialize(){
    setSimpleNames()
    verify()
  }

}