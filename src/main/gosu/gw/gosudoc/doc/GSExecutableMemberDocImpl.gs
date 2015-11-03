package gw.gosudoc.doc

uses com.sun.javadoc.AnnotationDesc
uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.ExecutableMemberDoc
uses com.sun.javadoc.ParamTag
uses com.sun.javadoc.Parameter
uses com.sun.javadoc.ThrowsTag
uses com.sun.javadoc.TypeVariable
uses gw.gosudoc.misc.GSParamTagImpl
uses gw.gosudoc.misc.GSParameterImpl
uses gw.lang.reflect.IAttributedFeatureInfo
uses gw.lang.reflect.IFeatureInfo
uses gw.lang.reflect.IParameterInfo
uses gw.lang.reflect.IType

abstract class GSExecutableMemberDocImpl extends GSMemberDocImpl implements ExecutableMemberDoc {

  var _parameters: Parameter[]
  var _featureInfo : IAttributedFeatureInfo as FeatureInfo

  //==========PUBLIC CONSTRUCTORS==========//
  construct( featureInfo: IAttributedFeatureInfo, rootDoc: GSRootDocImpl, clazz: GSClassDocImpl, ownersIntrinsicType: IType ){
    super( calculateFeatureName( featureInfo ), rootDoc, ownersIntrinsicType )
    ClassDoc = clazz
    FeatureInfo = featureInfo
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  override function thrownExceptions(): ClassDoc[]{
    return new ClassDoc[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  override function thrownExceptionTypes(): com.sun.javadoc.Type[]{
    return new com.sun.javadoc.Type[0]  //To change body of implemented methods use File | Settings | File Templates.
  }

  override property get Native(): boolean{
    return false
  }

  override property get Synchronized(): boolean{
    return false
  }

  override property get VarArgs(): boolean{
    return false
  }

  override function parameters(): Parameter[]{
    return _parameters
  }

  override function throwsTags(): ThrowsTag[]{
    return tags().whereTypeIs( ThrowsTag )
  }

  override function paramTags(): ParamTag[]{
    return tags().whereTypeIs( ParamTag )
  }

  override function typeParamTags(): ParamTag[]{
    return new ParamTag[0]
  }

  override function annotations(): AnnotationDesc[]{
    return new AnnotationDesc[0] //TODO cgross - implement this
  }

  override property get Public(): boolean{
    return FeatureInfo.Public
  }

  override property get Protected(): boolean{
    return FeatureInfo.Protected
  }

  override property get Private(): boolean{
    return FeatureInfo.Private
  }

  override property get PackagePrivate(): boolean{
    return FeatureInfo.Internal
  }

  override property get Static(): boolean{
    return FeatureInfo.Static
  }

  override property get Final(): boolean{
    return FeatureInfo.Final
  }

  // We don't have the return type here, since it isn't used for constructors.  This is added in tbe subclass where
  // necessary.
  override function signature(): String{
    var params = parameters()
        .map( \p -> p.type().qualifiedTypeName() + " " + p.name() )
        .join( ", " )
    return "(${params})"
  }

  // We don't have the return type here, since it isn't used for constructors.  This is added in the subclass where
  // necessary.
  override function flatSignature(): String{
    var params = parameters()
        .map( \p -> p.type().simpleTypeName() + " " + p.name() )
        .join( ", " )
    return "(${params})"
  }

  override function typeParameters(): TypeVariable[]{
    return new TypeVariable[0]
  }

  //==========PROTECTED METHODS==========//
  function createParamTags( parameterInfos: IParameterInfo[] ): ParamTag[]{
    return parameterInfos.map( \elt -> new GSParamTagImpl( getRootDoc(), elt.Name, elt.Description ) )
  }

  function initialize( parameters: Parameter[], comments: String, paramTags: ParamTag[] ){
    _parameters = parameters
    addTextComments( comments )
    for( tag in paramTags ){
      addTag( tag )
    }
  }

  function processParameterInfos( parameterInfos: IParameterInfo[] ) : Parameter[] {
    return parameterInfos.map( \ elt -> new GSParameterImpl( elt.Name, getRootDoc().getType( elt.FeatureType, this ) ) )
  }

  //==========PROTECTED STATIC METHODS==========//
  private static function calculateFeatureName( featureInfo: IAttributedFeatureInfo ): String{
    var name = featureInfo.getName()
    if( name == null ){
      return "null"
    }
    // Strip parentheses from name
    var i = name.indexOf( "(" )
    if( i >= 0 ){
      name = name.substring( 0, i ).trim()
    }
    // Strip parameters from name
    i = name.indexOf( "<" )
    if( i >= 0 ){
      name = name.substring( 0, i ).trim()
    }
    return name
  }


}