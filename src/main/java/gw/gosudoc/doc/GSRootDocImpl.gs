package gw.gosudoc.doc

uses com.sun.javadoc.RootDoc

uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.PackageDoc
uses com.sun.javadoc.SourcePosition
uses gw.gosudoc.type.GSArrayTypeImpl
uses gw.gosudoc.type.GSClassTypeImpl
uses gw.gosudoc.type.GSFunctionalTypeImpl
uses gw.gosudoc.type.GSParameterizedTypeImpl
uses gw.gosudoc.type.GSPrimitiveTypeImpl
uses gw.gosudoc.type.GSTypeImpl
uses gw.gosudoc.type.GSTypeVariableImpl
uses gw.gosudoc.type.GSVoidTypeImpl
uses gw.lang.reflect.IFunctionType
uses gw.lang.reflect.IType
uses gw.lang.reflect.ITypeVariableType
uses gw.lang.reflect.TypeSystem

uses java.io.File
uses java.lang.IllegalArgumentException
uses java.lang.System
uses java.util.ArrayList
uses java.util.HashMap
uses java.util.IdentityHashMap
uses java.util.Map
uses java.util.Properties
uses java.util.regex.Pattern

class GSRootDocImpl extends GSDocImpl implements RootDoc{

  var _voidType = new GSVoidTypeImpl( this )
  var _typesByType = new IdentityHashMap<IType, GSTypeImpl>()
  var _packagesByName = new HashMap<String, GSPackageDocImpl>().toAutoMap( \name -> new GSPackageDocImpl( name, this ) )

  // Config info
  var _externalDocs : Map<String, List<Pattern>> as ExternalDocs = new HashMap<String, List<Pattern>>().toAutoMap( \k -> ({}) )
  var _entryPoints : List<Pattern> as EntryPoints = {}
  var _exclusions : List<Pattern> as Exclusions = {
      Pattern.compile( "com.sun.*" ),
      Pattern.compile( "javax.*" ),
      Pattern.compile( "java.*" ),
      Pattern.compile( "gw.*" ),
      Pattern.compile( "Type" )
  }
  var _outputDirectory : File as OutputDirectory

  construct( outputDir: File ){
    super( "Root", null )
    _outputDirectory = outputDir
  }

  function initWithPropertiesFile( propsFile : File ) {
    if( propsFile != null ){
      readPropFile( propsFile )
    }
  }

  function readPropFile( propFile: File ){
    for( entry in Properties.readFromPropertiesFile( propFile ).entrySet() ){
      var key = entry.getKey()
      var val = entry.getValue()
      if( key.startsWith( "external." ) ){
        handleExternalJavadoc( key, val )
      } else if( key.startsWith( "exclude." ) ){
        _exclusions.add( getPattern( val ) )
      } else if( key.startsWith( "entrypoint." ) ){
        _entryPoints.add( getPattern( val ) )
      }
    }
  }

  function isEntryPoint( name: String ): boolean{
    return _entryPoints.hasMatch( \p -> p.matcher( name ).find() ) && not isExcluded( name )
  }

  function isExcluded( name: String ): boolean{
    return _exclusions.hasMatch( \p -> p.matcher( name ).find() ) || isExternal( name ) || isSynthetic( name )
  }

  function isSynthetic( name: String ): boolean{
    return name.endsWith( ".PLACEHOLDER" ) || name.startsWith( "_proxy_" )
  }

  function isExternal( name: String ): boolean{
    return _externalDocs.values().hasMatch( \l -> l.hasMatch( \p -> p.matcher( name ).find() ) )
  }

  function getPattern( val: String ): Pattern{
    return Pattern.compile( "^" + val + "$" )
  }

  function handleExternalJavadoc( key: String, val: String ){
    var urlAndPrefixes = val.split( "," )
    if( urlAndPrefixes.length != 2 ){
      throw new IllegalArgumentException( "Illegal line in properties file: " + key + "=" + val )
    }
    var url = urlAndPrefixes.first()
    var prefixes = urlAndPrefixes.last()
    var splitPrefixes = prefixes.split( "" ).toList()
    _externalDocs[url].addAll( splitPrefixes.map( \elt -> getPattern( elt ) ) )
  }


  override function options(): String[][]{
    var l = new ArrayList<String[]>()
    //TODO cgross - reenable external javadocs!
//    var externalJavadocs = _classManager.getExternalJavadocs()
//    for( externalJavadoc in externalJavadocs ){
//      l.add( {"-link", externalJavadoc.getUrl()} )
//    }
    l.add( {"-d", _outputDirectory.getCanonicalPath()} )
    return l.toTypedArray()
  }

  function genDocs(){
    printNotice( "Loading types to generate GosuDoc" )
    for( typeName in TypeSystem.getAllTypeNames() ){
      if( isEntryPoint( typeName.toString() ) ){
        try{
          getOrCreateClass( TypeSystem.getByFullName( typeName.toString() ) )
        } catch( e ){
          e.printStackTrace()
          printWarning( "Could not load type ${typeName}: ${e.Message}" )
        }
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
      return getBaseClassType( iType.getBoundingType() )
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
}
