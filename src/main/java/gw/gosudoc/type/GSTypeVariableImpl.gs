package gw.gosudoc.type

uses com.sun.javadoc.AnnotationDesc
uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.ProgramElementDoc
uses com.sun.javadoc.TypeVariable
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

uses java.lang.Override

class GSTypeVariableImpl extends GSTypeImpl implements TypeVariable{

  var _boundingType: IType
  var _name: String

  construct( name: String, boundingType: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( null, rootDoc, owner )
    _boundingType = boundingType
    _name = name
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  @Override
  function bounds(): com.sun.javadoc.Type[]{
    return {getBoundingType()}
  }

  function getBoundingType(): com.sun.javadoc.Type{
    return RootDoc.getType( _boundingType, Owner )
  }

  @Override
  function owner(): ProgramElementDoc{
    return Owner
  }

  @Override
  function annotations(): AnnotationDesc[]{
    return {}
  }

  @Override
  function initialize(){
    ShortName = _name
    QualifiedName = _name
    verify()
  }

  @Override
  function asTypeVariable(): TypeVariable{
    return this
  }

  @Override
  function asClassDoc(): ClassDoc{
    if( _boundingType != null ){
      return RootDoc.getOrCreateClass( _boundingType )
    } else{
      return Owner.containingClass()
    }
  }
}