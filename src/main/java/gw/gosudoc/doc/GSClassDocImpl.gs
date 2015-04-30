package gw.gosudoc.doc

uses com.sun.javadoc.AnnotationTypeDoc
uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.ConstructorDoc
uses com.sun.javadoc.FieldDoc
uses com.sun.javadoc.MethodDoc
uses com.sun.javadoc.PackageDoc
uses com.sun.javadoc.ParamTag
uses com.sun.javadoc.ParameterizedType
uses com.sun.javadoc.TypeVariable
uses com.sun.javadoc.WildcardType
uses gw.gosudoc.type.GSTypeVariableImpl
uses gw.lang.reflect.IType
uses gw.lang.reflect.ITypeInfo
uses gw.lang.reflect.gs.IGosuEnhancement

uses java.lang.NullPointerException

class GSClassDocImpl extends GSProgramElementDocImpl implements ClassDoc{

  var _superclass: GSClassDocImpl
  var _iType: IType
  var _interfaces: List<GSClassDocImpl> = {}
  var _constructors: List<GSConstructorDocImpl> = {}
  var _fields: List<GSFieldDocImpl> = {}
  var _methods: List<GSMethodDocImpl> = {}
  var _typeVariables: TypeVariable[] = {}
  var _typeImpl: com.sun.javadoc.Type

  //==========PUBLIC CONSTRUCTORS==========//
  construct( type: IType, rootDoc: GSRootDocImpl ){
    super( type.getName(), rootDoc, type )
    _iType = type
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  function containingPackage(): PackageDoc{
    var nameSpace = getRootDoc().getPackageNameFromTypeName( _iType.getName() )
    if( nameSpace == null ){
      nameSpace = ""
    }
    return getRootDoc().packageNamed( nameSpace )
  }

  // VITAL this returns null: otherwise we get an infinite loop during HTML generation.
  override function containingClass(): ClassDoc{
    return null
  }

  override property get Abstract(): boolean{
    return false
  }

  override property get Serializable(): boolean{
    return false
  }

  override property get Externalizable(): boolean{
    return false
  }

  override function serializationMethods(): MethodDoc[]{
    return {}
  }

  override function serializableFields(): FieldDoc[]{
    return {}
  }

  override function definesSerializableFields(): boolean{
    return false
  }

  override function superclass(): ClassDoc{
    return _superclass
  }

  override function superclassType(): com.sun.javadoc.Type{
    var retVal: com.sun.javadoc.Type = null
    if( _superclass != null ){
      retVal = _superclass.type()
    }
    return retVal
  }

  override function subclassOf( cd: ClassDoc ): boolean{
    if( _superclass == cd ){
      return true
    }
    if( _superclass == null ){
      return false
    }
    return _superclass.subclassOf( cd )
  }

  override function interfaces(): ClassDoc[]{
    return _interfaces.toTypedArray()
  }

  override function interfaceTypes(): com.sun.javadoc.Type[]{
    return interfaces().map( \elt -> (elt as GSClassDocImpl).type() )
  }

  override function typeParameters(): TypeVariable[] {
    return _typeVariables
  }

  override function typeParamTags(): ParamTag[]{
    return {}
  }

  override function fields(): FieldDoc[]{
    return _fields.toTypedArray()
  }

  override function fields( filter: boolean ): FieldDoc[]{
    return fields()
  }

  override function enumConstants(): FieldDoc[]{
    return {}
  }

  override function methods(): MethodDoc[]{
    return _methods.toTypedArray()
  }

  override function methods( filter: boolean ): MethodDoc[]{
    return methods()
  }

  override function constructors(): ConstructorDoc[]{
    return _constructors.toTypedArray()
  }

  override function constructors( filter: boolean ): ConstructorDoc[]{
    return constructors()
  }

  override function innerClasses(): ClassDoc[]{
    return {}
  }

  override function innerClasses( filter: boolean ): ClassDoc[]{
    return {}
  }

  override function findClass( className: String ): ClassDoc{
    return null
  }

  override function importedClasses(): ClassDoc[]{
    return {}
  }

  override function importedPackages(): PackageDoc[]{
    return {}
  }

  override function name(): String{
    return typeName()
  }

  override property get OrdinaryClass(): boolean{
    return true
  }

  override property get IsClassShimmed(): boolean{
    return true
  }

  override property get Included(): boolean{
    if( !super.isIncluded() ){
      return false
    }
    // exclude primitives
    if( isPrimitive() ){
      return false
    }
    // exclude enhancements
    if( _iType typeis IGosuEnhancement ){
      return false
    }

    return not getRootDoc().isExcluded( _iType.getName() )
  }

  override function typeName(): String{
    return _iType.Name
  }

  override function qualifiedTypeName(): String{
    var t = _iType
    while( t.Array ){
      t = t.ComponentType
    }
    return t.Name
  }

  override function simpleTypeName(): String{
    return typeName()
  }

  override function dimension(): String{
    return "" //TODO cgross - does this need to work?  Didn't work previously
  }

  override property get Primitive(): boolean{
    return _iType.isPrimitive()
  }

  override function asClassDoc(): ClassDoc{
    return this
  }

  override function asParameterizedType(): ParameterizedType{
    return null
  }

  override function asTypeVariable(): TypeVariable{
    return null
  }

  override function asWildcardType(): WildcardType{
    return null
  }

  override function asAnnotationTypeDoc(): AnnotationTypeDoc{
    return null
  }

  //==========PROTECTED METHODS==========//
  override function initialize(){
    _typeImpl = getRootDoc().getType( _iType, null )
    ClassDoc = this
    addInterfaces()
    var ti = _iType.getTypeInfo()
    if( ti != null ){
      addMethods( ti )
      addConstructors( ti )
      addFields( ti )
      addClassJavadoc( ti )
    }
    setTypeVariables()
    verify()
  }

  function verify(){
    if( name() == null ){
      throw new NullPointerException( "Name is null for class " + _iType )
    }
    if( _typeImpl == null ){
      throw new NullPointerException( "Type is null for class " + name() )
    }
  }

  //==========PACKAGE-PRIVATE METHODS==========//
  function setSuperClass( superclass: GSClassDocImpl ){
    _superclass = superclass
  }

  function type(): com.sun.javadoc.Type{
    return _typeImpl
  }

  //==========PRIVATE METHODS==========//
  function addClassJavadoc( ti: ITypeInfo ){
    var classJavadoc = ti.getDescription()
    if( classJavadoc != null ){
      addTextComments( classJavadoc )
    }
  }

  function addConstructors( ti: ITypeInfo ){
    var list = ti.getConstructors()
    for( iConstructorInfo in list ){
      var constructorDoc = new GSConstructorDocImpl( _iType, iConstructorInfo, getRootDoc(), this )
      if( constructorDoc.shouldBeIncluded() ){
        constructorDoc.initialize()
        _constructors.add( constructorDoc )
      }
    }
  }

  function addFields( ti: ITypeInfo ){
    var propertyInfos = ti.getProperties()
    for( iPropertyInfo in propertyInfos ){
      var fieldDoc = new GSFieldDocImpl( _iType, iPropertyInfo, getRootDoc(), this )
      if( fieldDoc.shouldBeIncluded() ){
        try{
          fieldDoc.initialize()
          _fields.add( fieldDoc )
        } catch( e ){
          getRootDoc().printWarning( "Could not add field " + iPropertyInfo.getName() + " to class " + name() + ": " + e.getMessage() )
        }
      }
    }
  }

  property get ElementType(): com.sun.javadoc.Type{
    return null
  }

  function addInterfaces(){
    var interfaces = _iType.getInterfaces()
    for( interfaiss in interfaces ){
      if(not interfaiss.Name.startsWith( "_proxy_" )) {
        var e = getRootDoc().getOrCreateClass( interfaiss )
        _interfaces.add( e )
      }
    }
  }

  function addMethods( ti: ITypeInfo ){
    for( iMethodInfo in ti.getMethods() ){
      var methodDoc = new GSMethodDocImpl( _iType, iMethodInfo, getRootDoc(), this )
      if( methodDoc.shouldBeIncluded() ){
        methodDoc.initialize()
        _methods.add( methodDoc )
      }
    }
  }

  function setTypeVariables(){
    if(_iType.GenericTypeVariables != null) {
      _typeVariables = _iType.GenericTypeVariables.map( \elt -> {
        var tv = new GSTypeVariableImpl( elt.getName(), elt.getBoundingType(), getRootDoc(), this )
        tv.initialize()
        return tv
      } )
    }
  }

}