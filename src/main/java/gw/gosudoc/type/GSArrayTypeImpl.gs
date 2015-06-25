package gw.gosudoc.type

uses com.sun.javadoc.AnnotatedType
uses com.sun.javadoc.ClassDoc
uses gw.gosudoc.doc.GSProgramElementDocImpl
uses gw.gosudoc.doc.GSRootDocImpl
uses gw.lang.reflect.IType

class GSArrayTypeImpl extends GSTypeImpl{

  construct( type: IType, rootDoc: GSRootDocImpl, owner: GSProgramElementDocImpl ){
    super( type, rootDoc, owner )
  }

  override function asClassDoc(): ClassDoc{
    var type = GosuIType.getComponentType()
    return RootDoc.getOrCreateClass( type )
  }


  override function initialize(){
    var componentType = RootDoc.getType( GosuIType.getComponentType(), Owner )
    ShortName = componentType.typeName()
    QualifiedName = componentType.qualifiedTypeName()
    Dimension = componentType.dimension() + "[]"
    verify()
  }
}