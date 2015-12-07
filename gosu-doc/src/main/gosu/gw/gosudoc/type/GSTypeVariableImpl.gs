package gw.gosudoc.type

uses com.sun.javadoc.AnnotationDesc
uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.ProgramElementDoc
uses com.sun.javadoc.TypeVariable
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

class GSTypeVariableImpl extends GSTypeImpl implements TypeVariable{

  var _boundingType: IType
  var _name: String

  construct( name: String, boundingType: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( null, rootDoc, owner )
    _boundingType = boundingType
    _name = name
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  override function bounds(): com.sun.javadoc.Type[]{
    return {getBoundingType()}
  }

  function getBoundingType(): com.sun.javadoc.Type{
    return RootDoc.getType( _boundingType, Owner )
  }

  override function owner(): ProgramElementDoc{
    return Owner
  }

  override function annotations(): AnnotationDesc[]{
    return {}
  }

  override function initialize(){
    ShortName = _name
    QualifiedName = _name
    verify()
  }

  override function asTypeVariable(): TypeVariable{
    return this
  }

  override function asClassDoc(): ClassDoc{
    if( _boundingType != null ){
      return RootDoc.getOrCreateClass( _boundingType )
    } else{
      return Owner.containingClass()
    }
  }
}