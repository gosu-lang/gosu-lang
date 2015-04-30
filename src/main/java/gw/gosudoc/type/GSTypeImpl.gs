package gw.gosudoc.type

uses com.sun.javadoc.*
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

uses java.lang.Override

abstract class GSTypeImpl implements com.sun.javadoc.Type {

  var _owner: GSProgramElementDocImpl as readonly Owner
  var _rootDoc: GSRootDocImpl as readonly RootDoc
  var _iType: IType as readonly GosuIType
  var _dimension : String as Dimension = ""
  var _qualifiedName: String as QualifiedName
  var _shortName: String as ShortName

  //==========PUBLIC CONSTRUCTORS==========//
  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    _rootDoc = rootDoc
    _iType = type
    _owner = owner
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  function typeName(): String{
    return _shortName
  }

  @Override
      property get ElementType(): com.sun.javadoc.Type{
    return null
  }


  function qualifiedTypeName(): String{
    return _qualifiedName
  }

  function simpleTypeName(): String{
    return typeName()
  }


  function dimension(): String{
    return _dimension
  }

  @Override
      property get Primitive(): boolean{
    return false
  }

  function asClassDoc(): ClassDoc{
    return null
  }

  function asParameterizedType(): ParameterizedType{
    return null
  }

  function asTypeVariable(): TypeVariable{
    return null
  }

  function asWildcardType(): WildcardType{
    return null
  }

  function asAnnotationTypeDoc(): AnnotationTypeDoc{
    return null
  }

  //==========PUBLIC METHODS==========//
  function containingType(): com.sun.javadoc.Type{
    return null
  }

  // All implementations should call verify().
  abstract function initialize()

  function interfaceTypes(): Type[]{
    return {}
  }

  //==========PROTECTED METHODS==========//
  protected function setSimpleNames(){
    _shortName = _iType.getRelativeName()
    _qualifiedName = _iType.getName()
  }

  protected function verify(){
    if( _qualifiedName == null ){
      throw "Qualified name is null for Type " + _iType
    }
    if( _shortName == null ){
      throw "Short name is null for Type" + _iType
    }
  }
}