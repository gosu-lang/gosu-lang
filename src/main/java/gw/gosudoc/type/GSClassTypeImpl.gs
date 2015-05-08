package gw.gosudoc.type

uses com.sun.javadoc.*
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

class GSClassTypeImpl extends GSTypeImpl implements com.sun.javadoc.Type, ClassDoc {

  var _baseType : GSTypeImpl
  delegate _classDoc represents ClassDoc

  construct(type : IType,  rootDoc : GSRootDocImpl,  owner : GSProgramElementDocImpl ) {
    super(type, rootDoc, owner)
  }

  function asClassDoc() : ClassDoc{
    return RootDoc.getOrCreateClass(GosuIType)
  }

  function asParameterizedType() : ParameterizedType{
    return null
  }

  //==========PROTECTED METHODS==========//
  function  handleGenericType(type : IType) {
    var baseType = type.getGenericType()
    if (baseType != null) {
      type = baseType
    }
    _baseType = RootDoc.getType(type, Owner)
    Dimension = _baseType.dimension()
  }

  override function initialize() {
    setSimpleNames()
    handleGenericType(GosuIType)
    verify()
    RootDoc.getOrCreateClass(GosuIType)
    _classDoc = asClassDoc()
  }
}