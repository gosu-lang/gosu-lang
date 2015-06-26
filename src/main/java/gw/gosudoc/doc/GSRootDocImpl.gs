package gw.gosudoc.doc

uses com.sun.javadoc.RootDoc

uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.PackageDoc
uses com.sun.javadoc.SourcePosition
uses gw.config.CommonServices
uses gw.gosudoc.filter.ConstructorFilter
uses gw.gosudoc.filter.FeatureFilter
uses gw.gosudoc.filter.MethodFilter
uses gw.gosudoc.filter.PropertyFilter
uses gw.gosudoc.filter.TypeFilter
uses gw.gosudoc.type.GSArrayTypeImpl
uses gw.gosudoc.type.GSClassTypeImpl
uses gw.gosudoc.type.GSFunctionalTypeImpl
uses gw.gosudoc.type.GSParameterizedTypeImpl
uses gw.gosudoc.type.GSPrimitiveTypeImpl
uses gw.gosudoc.type.GSTypeImpl
uses gw.gosudoc.type.GSTypeVariableImpl
uses gw.gosudoc.type.GSVoidTypeImpl
uses gw.lang.reflect.IConstructorInfo
uses gw.lang.reflect.IFunctionType
uses gw.lang.reflect.IMethodInfo
uses gw.lang.reflect.IPropertyInfo
uses gw.lang.reflect.IType
uses gw.lang.reflect.ITypeVariableType
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.gs.IGosuClass

uses java.io.File
uses java.lang.IllegalArgumentException
uses java.lang.System
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.IdentityHashMap
uses java.util.Map
uses java.util.regex.Pattern

class GSRootDocImpl extends GSDocImpl implements RootDoc{

  var _voidType = new GSVoidTypeImpl( this )
  var _typesByType = new IdentityHashMap<IType, GSTypeImpl>()
  var _packagesByName = new HashMap<String, GSPackageDocImpl>().toAutoMap( \name -> new GSPackageDocImpl( name, this ) )

  // Config info
  var _externalDocs : List<String> as ExternalDocs = {}
  var _inputDirs : List<File> as InputDirs = {}
  var _exclusions : List<Pattern> as Exclusions = {}
  var _outputDirectory : File as OutputDirectory
  var _typeFilters : List<TypeFilter> as readonly TypeFilters = {}
  var _methodFilters : List<MethodFilter> as readonly MethodFilters  = {}
  var _propertyFilters : List<PropertyFilter> as readonly PropertyFilters  = {}
  var _constructorFilters : List<ConstructorFilter> as readonly ConstructorFilters = {}
  var _featureFilters : List<FeatureFilter> as readonly FeatureFilters = {}

  construct( inputDirs : List<File>, outputDir: File, filters : List = null, externalDocs : List<String> = null ){
    super( "Root", null )
    _inputDirs = inputDirs
    _outputDirectory = outputDir
    _externalDocs = externalDocs
    if(filters != null) {
      initFilters(filters)
    }
  }

  private function initFilters( filters: List<Object> ){
    for(o in filters) {
      if(o typeis TypeFilter) TypeFilters.add(o)
      if(o typeis MethodFilter ) MethodFilters.add(o)
      if(o typeis PropertyFilter ) PropertyFilters.add(o)
      if(o typeis ConstructorFilter ) ConstructorFilters.add(o)
      if(o typeis FeatureFilter ) FeatureFilters.add(o)
    }
  }

  function shouldDocumentType( iType : Type ): boolean{

    if(isExcluded( iType.getName() )) {
      return false
    }

    for(filter in TypeFilters) {
      if(not filter.shouldIncludeType( iType )) {
        return false
      }
    }

    if(iType typeis IGosuClass ) {
      var file = iType.SourceFileHandle.File
      for(f in _inputDirs) {
        var dir = CommonServices.FileSystem.getIDirectory( f )
        if(file.isDescendantOf( dir )) {
          return true
        }
      }
    }
    return false
  }

  function isExcluded( name: String ): boolean{
    return _exclusions.hasMatch( \p -> p.matcher( name ).find() ) || isSynthetic( name )
  }

  function isSynthetic( name: String ): boolean{
    return name.endsWith( ".PLACEHOLDER" ) ||
        name.startsWith( "_proxy_" ) ||
        name.contains( ".block_" ) ||
        name.contains( ".AnonymouS__" ) ||
        name.contains( ".ProxyFor_" ) ||
        name.equals( 'Key' )
  }

  function getPattern( val: String ): Pattern{
    return Pattern.compile( "^" + val + "$" )
  }

  override function options(): String[][]{
    var l = new ArrayList<String[]>()
    for( externalJavadoc in _externalDocs ){
      l.add( {"-link", externalJavadoc} )
    }
    l.add( {"-d", _outputDirectory.getCanonicalPath()} )
    return l.toTypedArray()
  }

  function genDocs(){
    printNotice( "Loading types to generate GosuDoc" )
    for( typeName in TypeSystem.getAllTypeNames() ){
        try{
          if(!isExcluded( typeName.toString() )) {
            var iType = TypeSystem.getByFullName( typeName.toString() )
            if( shouldDocumentType( iType ) ){
              getOrCreateClass( iType )
            }
          }
        } catch( e ){
          printWarning( "Could not load type ${typeName}: ${e.Message}" )
        }
    }
  }

  override function specifiedPackages(): PackageDoc[]{
    return getAllPackages()
  }

  override function specifiedClasses(): ClassDoc[]{
    return classes()
  }

  override function classes(): ClassDoc[]{
    return specifiedPackages().flatMap( \elt -> elt.allClasses() )
  }

  override function packageNamed( name: String ): GSPackageDocImpl{
    return _packagesByName.get( name )
  }

  override function classNamed( qualifiedName: String ): ClassDoc{
    var packageName = getPackageNameFromTypeName( qualifiedName )
    return packageNamed( packageName ).findClass( qualifiedName )
  }

  override function printError( msg: String ){
    System.err.println( "ERROR: ${msg}}" )
  }

  override function printError( pos: SourcePosition, msg: String ){
    System.err.println( "ERROR: ${msg} ${processPos( pos )}" )
  }

  override function printWarning( msg: String ){
    if( warningIsUnexpected( msg ) ){
      System.err.println( "WARNING: ${msg}}" )
    }
  }

  override function printWarning( pos: SourcePosition, msg: String ){
    if( warningIsUnexpected( msg ) ){
      System.err.println( "WARNING: ${msg} ${processPos( pos )}" )
    }
  }

  override function printNotice( msg: String ){
    System.err.println( msg )
  }

  override function printNotice( pos: SourcePosition, msg: String ){
    System.err.println( "${msg} ${pos}" )
  }

  function getOrCreateClass( iType: IType ): GSClassDocImpl {
    var baseType = getBaseClassType( iType )
    var className = iType.Name
    var pack = packageNamed( baseType.getNamespace() ?: "" )
    var clazz = pack.findClass( className )
    if( clazz == null ){
      clazz = pack.addClass( baseType )
    }
    return clazz
  }

  // Returns empty string for default package.
  function getPackageNameFromTypeName( fqtn: String ): String{
    var i = fqtn.lastIndexOf( "." )
    var packagePortion = i >= 0 ? fqtn.substring( 0, i ) : ""
    return packagePortion
  }

  function getType( type: IType, owner: GSProgramElementDocImpl ): GSTypeImpl {
    if( type == null ){
      // there are some weird methods that have null return type.
      return _voidType
    }
    var typeImpl = _typesByType.get( type )
    if( typeImpl == null ){
      if( type.isArray() ){
        typeImpl = new GSArrayTypeImpl( type, this, owner )
      } else if( isVoid( type ) ){
        typeImpl = _voidType
      } else if( type.isPrimitive() ){
        typeImpl = new GSPrimitiveTypeImpl( type, this, owner )
      } else if( type typeis IFunctionType ){ // handle blocks, etc.
        typeImpl = new GSFunctionalTypeImpl( type, this, owner )
      } else if( type typeis ITypeVariableType ){
        var vt = type as ITypeVariableType
        typeImpl = new GSTypeVariableImpl( vt.Name, vt.BoundingType, this, owner )
      } else if( type.isParameterizedType() ){
        typeImpl = new GSParameterizedTypeImpl( type, this, owner )
      } else{
        typeImpl = new GSClassTypeImpl( type, this, owner )
      }
      _typesByType.put( type, typeImpl )
      typeImpl.initialize()
    }
    return typeImpl
  }

  function getAllPackages(): PackageDoc[]{
    return _packagesByName.values().where( \elt -> elt.isIncluded() ).toTypedArray()
  }

  private function getBaseClassType( iType: IType ): IType {
    if( iType == null ){
      return null
    }
    if( iType.isArray() ){
      return getBaseClassType( iType.getComponentType() )
    }
    if( iType.isPrimitive() ){
      return iType
    }
    if( iType typeis ITypeVariableType ){
      return getBaseClassType( iType.BoundingType )
    }
    if( iType.isParameterizedType() ){
      return getBaseClassType( iType.getGenericType() )
    }
    return iType
  }

  private function processPos( pos: SourcePosition ) : Object {
    return pos ?: ""
  }

  private function warningIsUnexpected( msg: String ): boolean{
    return !(msg.startsWith( "Parameter" ) && msg.contains( " is documented more than once" ))
  }

  function shouldDocumentConstructor( iConstructorInfo: IConstructorInfo ): boolean{
    for(filter in ConstructorFilters) {
      if(not filter.shouldIncludeConstructor( iConstructorInfo )) {
        return false
      }
    }
    for(filter in FeatureFilters) {
      if(not filter.shouldIncludeFeature( iConstructorInfo )) {
        return false
      }
    }
    return true
  }

  function shouldDocumentProperty( iPropertyInfo: IPropertyInfo ): boolean{
    for(filter in PropertyFilters) {
      if(not filter.shouldIncludeProperty( iPropertyInfo )) {
        return false
      }
    }
    for(filter in FeatureFilters) {
      if(not filter.shouldIncludeFeature( iPropertyInfo )) {
        return false
      }
    }
    return true
  }

  function shouldDocumentMethod( iMethodInfo: IMethodInfo ): boolean{
    for(filter in MethodFilters) {
      if(not filter.shouldIncludeMethod( iMethodInfo )) {
        return false
      }
    }
    for(filter in FeatureFilters) {
      if(not filter.shouldIncludeFeature( iMethodInfo )) {
        return false
      }
    }
    return true
  }
}