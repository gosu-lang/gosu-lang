package gw.gosudoc.doc

uses com.sun.javadoc.AnnotationDesc
uses com.sun.javadoc.FieldDoc
uses com.sun.javadoc.SerialFieldTag
uses gw.gosudoc.tags.TagsTokenizer
uses gw.lang.reflect.IPropertyInfo
uses gw.lang.reflect.IPropertyInfoDelegate
uses gw.lang.reflect.IType

class GSFieldDocImpl extends GSMemberDocImpl implements FieldDoc{

  //==========PRIVATE FIELDS==========//
  var _propertyInfo: IPropertyInfo
  var _type: com.sun.javadoc.Type

  //==========PUBLIC CONSTRUCTORS==========//
  construct( ownersIntrinsicType: IType, propertyInfo: IPropertyInfo, rootDoc: GSRootDocImpl, clazz: GSClassDocImpl ){
    super( propertyInfo.getDisplayName(), rootDoc, ownersIntrinsicType )
    _propertyInfo = propertyInfo
    ClassDoc = clazz
  }

  //==========PUBLIC METHODS IMPLEMENTING INTERFACES==========//
  override function type(): com.sun.javadoc.Type{
    return _type
  }

  override property get Transient(): boolean{
    return false
  }

  override property get Volatile(): boolean{
    return false
  }

  override function serialFieldTags(): SerialFieldTag[]{
    return new SerialFieldTag[0]
  }

  override function constantValue(): Object{
    return null
  }

  override function constantValueExpression(): String{
    return null
  }

  override property get Static(): boolean{
    return _propertyInfo.isStatic()
  }

  override property get Field(): boolean{
    return true
  }

  override function annotations(): AnnotationDesc[]{
    return new AnnotationDesc[0] //TODO cgross - implement this
  }

  override property get Public(): boolean{
    return _propertyInfo.Public
  }

  override property get Protected(): boolean{
    return _propertyInfo.Protected
  }

  override property get Private(): boolean{
    return _propertyInfo.Private
  }

  override property get PackagePrivate(): boolean{
    return _propertyInfo.Internal
  }

  override property get Final(): boolean{
    return _propertyInfo.Final
  }

  //==========PUBLIC METHODS==========//
  override function shouldBeIncluded(): boolean{
    var b = super.shouldBeIncluded()

    b &&= getRootDoc().shouldDocumentProperty( _propertyInfo )

    if( _propertyInfo typeis IPropertyInfoDelegate ){
      b &&= (OwnersType.equals( _propertyInfo.getSource().getOwnersType() ))
    }

    b &&= (_propertyInfo.isProtected() || _propertyInfo.isPublic())

    // Exclude fields that are inherited
    b &&= (OwnersType.equals( _propertyInfo.getOwnersType() ))

    return b
  }

  //==========PROTECTED METHODS==========//
  function initialize(){
    addTextComments( new TagsTokenizer(_propertyInfo.Description, _propertyInfo, getRootDoc()).processTags() )
    var gosuType = _propertyInfo.getFeatureType()
    _type = getRootDoc().getType( gosuType, this )
  }
}