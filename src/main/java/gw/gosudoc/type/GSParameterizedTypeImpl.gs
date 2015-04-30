package gw.gosudoc.type

uses com.sun.javadoc.ParameterizedType
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

uses java.lang.Override


class GSParameterizedTypeImpl extends GSClassTypeImpl implements ParameterizedType{

  //==========PROTECTED FIELDS==========//
  var _superclassType: GSTypeImpl as readonly SuperClassType
  var _typeArguments: com.sun.javadoc.Type[]

  //==========PUBLIC CONSTRUCTORS==========//
  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( type, rootDoc, owner )
  }

  @Override
  function asParameterizedType(): ParameterizedType{
    return this
  }

  function typeArguments() : com.sun.javadoc.Type[] {
    return _typeArguments
  }

  @Override
  function initialize(){
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