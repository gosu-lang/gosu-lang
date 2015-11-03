package gw.gosudoc.doc

uses com.sun.javadoc.MemberDoc
uses gw.lang.reflect.IType

abstract class GSMemberDocImpl extends GSProgramElementDocImpl implements MemberDoc{

  construct( name: String, rootDoc: GSRootDocImpl, ownersIntrinsicType: IType ){
    super( name, rootDoc, ownersIntrinsicType )
  }

  override property get Synthetic(): boolean{
    return false
  }

}