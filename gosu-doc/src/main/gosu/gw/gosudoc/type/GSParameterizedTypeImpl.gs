package gw.gosudoc.type

uses  gw.gosudoc.com.sun.javadoc.ParameterizedType
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

class GSParameterizedTypeImpl extends GSClassTypeImpl implements ParameterizedType{

  //==========PROTECTED FIELDS==========//
  var _superclassType: GSTypeImpl as readonly SuperClassType
  var _typeArguments:  gw.gosudoc.com.sun.javadoc.Type[]

  //==========PUBLIC CONSTRUCTORS==========//
  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( type, rootDoc, owner )
  }

  override function asParameterizedType(): ParameterizedType{
    return this
  }

  override function typeArguments() :  gw.gosudoc.com.sun.javadoc.Type[] {
    return _typeArguments
  }

  override function initialize(){
    setSimpleNames()
    handleGenericType( GosuIType )
    if(GosuIType.Supertype == null) {
      _superclassType = null
    } else if( GosuIType.Enum == false && GosuIType.Supertype.Enum ){
      _superclassType = null
    } else{
      _superclassType = RootDoc.getType( GosuIType.Supertype, Owner )
    }
    _typeArguments = GosuIType.TypeParameters.map( \elt -> RootDoc.getType( elt, Owner ) )
    verify()
  }

}