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
uses java.lang.Override

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
  @Override
  function containingClass(): ClassDoc{
    return null
  }

  @Override
      property get Abstract(): boolean{
    return false
  }

  @Override
      property get Serializable(): boolean{
    return false
  }

  @Override
      property get Externalizable(): boolean{
    return false
  }

  @Override
  function serializationMethods(): MethodDoc[]{
    return {}
  }

  @Override
  function serializableFields(): FieldDoc[]{
    return {}
  }

  @Override
  function definesSerializableFields(): boolean{
    return false
  }

  @Override
  function superclass(): ClassDoc{
    return _superclass
  }

  @Override
  function superclassType(): com.sun.javadoc.Type{
    var retVal: com.sun.javadoc.Type = null
    if( _superclass != null ){
      retVal = _superclass.type()
    }
    return retVal
  }

  @Override
  function subclassOf( cd: ClassDoc ): boolean{
    if( _superclass == cd ){
      return true
    }
    if( _superclass == null ){
      return false
    }
    return _superclass.subclassOf( cd )
  }

  @Override
  function interfaces(): ClassDoc[]{
    return _interfaces.toTypedArray()
  }

  @Override
  function interfaceTypes(): com.sun.javadoc.Type[]{
    return interfaces().map( \elt -> (elt as GSClassDocImpl).type() )
  }

  @Override
  function typeParameters(): TypeVariable[] {
    return _typeVariables
  }

  @Override
  function typeParamTags(): ParamTag[]{
    return {}
  }

  @Override
  function fields(): FieldDoc[]{
    return _fields.toTypedArray()
  }

  @Override
  function fields( filter: boolean ): FieldDoc[]{
    return fields()
  }

  @Override
  function enumConstants(): FieldDoc[]{
    return {}
  }

  @Override
  function methods(): MethodDoc[]{
    return _methods.toTypedArray()
  }

  @Override
  function methods( filter: boolean ): MethodDoc[]{
    return methods()
  }

  @Override
  function constructors(): ConstructorDoc[]{
    return _constructors.toTypedArray()
  }

  @Override
  function constructors( filter: boolean ): ConstructorDoc[]{
    return constructors()
  }

  @Override
  function innerClasses(): ClassDoc[]{
    return {}
  }

  @Override
  function innerClasses( filter: boolean ): ClassDoc[]{
    return {}
  }

  @Override
  function findClass( className: String ): ClassDoc{
    return null
  }

  @Override
  function importedClasses(): ClassDoc[]{
    return {}
  }

  @Override
  function importedPackages(): PackageDoc[]{
    return {}
  }

  @Override
  function name(): String{
    return typeName()
  }

  @Override
      property get OrdinaryClass(): boolean{
    return true
  }

  @Override
      property get IsClassShimmed(): boolean{
    return true
  }

  @Override
      property get Included(): boolean{
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

  @Override
  function typeName(): String{
    return _iType.Name
  }

  @Override
  function qualifiedTypeName(): String{
    var t = _iType
    while( t.Array ){
      t = t.ComponentType
    }
    return t.Name
  }

  @Override
  function simpleTypeName(): String{
    return typeName()
  }

  @Override
  function dimension(): String{
    return "" //TODO cgross - does this need to work?  Didn't work previously
  }

  @Override
      property get Primitive(): boolean{
    return _iType.isPrimitive()
  }

  @Override
  function asClassDoc(): ClassDoc{
    return this
  }

  @Override
  function asParameterizedType(): ParameterizedType{
    return null
  }

  @Override
  function asTypeVariable(): TypeVariable{
    return null
  }

  @Override
  function asWildcardType(): WildcardType{
    return null
  }

  @Override
  function asAnnotationTypeDoc(): AnnotationTypeDoc{
    return null
  }

  //==========PROTECTED METHODS==========//
  @Override
  function initialize(){
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