package gw.gosudoc.doc

uses com.sun.javadoc.ClassDoc
uses com.sun.javadoc.MethodDoc
uses gw.gosudoc.misc.GSReturnValueTag
uses gw.gosudoc.type.GSTypeImpl
uses gw.lang.reflect.IMethodInfo
uses gw.lang.reflect.IType
uses gw.lang.reflect.MethodInfoDelegate
uses gw.lang.reflect.gs.IGosuEnhancement

uses java.lang.NullPointerException

class GSMethodDocImpl extends GSExecutableMemberDocImpl implements MethodDoc{

  var _iMethodInfo: IMethodInfo
  var _returnType: GSTypeImpl

  //==========PUBLIC CONSTRUCTORS==========//
  construct( ownersIntrinsicType: IType, iMethodInfo: IMethodInfo, rootDoc: GSRootDocImpl, clazz: GSClassDocImpl ){
    super( iMethodInfo, rootDoc, clazz, ownersIntrinsicType )
    _iMethodInfo = iMethodInfo
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  override property get Abstract(): boolean{
    return false  //To change body of implemented methods use File | Settings | File Templates.
  }

  /*override*/ property get Default(): boolean{
    return false  //To change body of implemented methods use File | Settings | File Templates.
  }

  override function returnType(): com.sun.javadoc.Type{
    return _returnType
  }

  override function overriddenClass(): ClassDoc{
    return null
  }

  override function overriddenType(): com.sun.javadoc.Type{
    return null
  }

  override function overriddenMethod(): MethodDoc{
    return null
  }

  override function overrides( meth: MethodDoc ): boolean{
    return false
  }

  override function thrownExceptionTypes(): com.sun.javadoc.Type[]{
    return _iMethodInfo.getExceptions().map( \elt -> getRootDoc().getType( elt.getExceptionType(), this ) ).toTypedArray()
  }

  /*override*/ function receiverType(): com.sun.javadoc.Type{
    return null  //To change body of implemented methods use File | Settings | File Templates.
  }

  override function signature(): String{
    var returnTypeString = returnType()?.qualifiedTypeName()?:"void"
    return returnTypeString + " " + super.signature()
  }

  override function flatSignature(): String{
    var returnTypeString = returnType()?.simpleTypeName()?:"void"
    return returnTypeString + " " + super.flatSignature()
  }

  override property get Method(): boolean{
    return true
  }

  //==========PUBLIC METHODS==========//
  override function shouldBeIncluded(): boolean{
    var b = super.shouldBeIncluded()
    var ownersIType = _iMethodInfo.getOwnersType()
    var isFromEnhancement = (ownersIType typeis IGosuEnhancement)
    // Exclude methods starting with @ (these are just equivalents of properties)
    b &&= (!name().startsWith( "@" ))

    b &&= getRootDoc().shouldDocumentMethod( _iMethodInfo )

    // Excluded methods that are inherited
    b &&= (isFromEnhancement || OwnersType.equals( ownersIType ))

    if( _iMethodInfo typeis MethodInfoDelegate ){
      b &&= (OwnersType.equals( _iMethodInfo.getSource().getOwnersType() ))
    }

    // Exclude methods annotated @hidden.
    b &&= (!_iMethodInfo.isHidden())

    // only include public and protected methods.
    b &&= (_iMethodInfo.isProtected() || _iMethodInfo.isPublic())

    return b
  }

  //==========PROTECTED METHODS==========//
  function initialize(){
    var parameterInfos = _iMethodInfo.getParameters()
    var parameters = processParameterInfos( parameterInfos )
    var desc = _iMethodInfo.getDescription()
    var comments = createParamTags( parameterInfos )
    initialize( parameters, desc, comments )
    var returnType = _iMethodInfo.getReturnType()
    _returnType = getRootDoc().getType( returnType, this )
    if( !isVoid( returnType ) ){
      addTag( new GSReturnValueTag( getRootDoc(), _iMethodInfo.getReturnDescription() ) )
    }
    verify()
  }

  function verify(){
    if( _returnType == null ){
      throw new NullPointerException()
    }
  }

}