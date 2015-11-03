package gw.gosudoc.type

uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

class GSFunctionalTypeImpl extends GSPrimitiveTypeImpl{

  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( type, rootDoc, owner )
  }

}