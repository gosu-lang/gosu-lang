package gw.gosudoc.doc

uses com.sun.javadoc.MemberDoc
uses gw.lang.reflect.IType

uses java.lang.Override

abstract class GSMemberDocImpl extends GSProgramElementDocImpl implements MemberDoc{

  construct( name: String, rootDoc: GSRootDocImpl, ownersIntrinsicType: IType ){
    super( name, rootDoc, ownersIntrinsicType )
  }

  @Override
      property get Synthetic(): boolean{
    return false
  }

}