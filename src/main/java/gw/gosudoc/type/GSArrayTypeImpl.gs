package gw.gosudoc.type

uses com.sun.javadoc.ClassDoc
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

uses java.lang.Override

class GSArrayTypeImpl extends GSTypeImpl{

  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( type, rootDoc, owner )
  }

  @Override
  function asClassDoc(): ClassDoc{
    var type = GosuIType.getComponentType()
    return RootDoc.getOrCreateClass( type )
  }

  @Override
  function initialize(){
    var componentType = RootDoc.getType( GosuIType.getComponentType(), Owner )
    ShortName = componentType.typeName()
    QualifiedName = componentType.qualifiedTypeName()
    Dimension = componentType.dimension() + "[]"
    verify()
  }
}