package gw.gosudoc.doc

uses com.sun.javadoc.FieldDoc
uses com.sun.javadoc.SerialFieldTag
uses gw.lang.reflect.IPropertyInfo
uses gw.lang.reflect.IPropertyInfoDelegate
uses gw.lang.reflect.IType

uses java.lang.Override

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
  @Override
  function type(): com.sun.javadoc.Type{
    return _type
  }

  @Override
      property get Transient(): boolean{
    return false
  }

  @Override
      property get Volatile(): boolean{
    return false
  }

  @Override
  function serialFieldTags(): SerialFieldTag[]{
    return new SerialFieldTag[0]
  }

  @Override
  function constantValue(): Object{
    return null
  }

  @Override
  function constantValueExpression(): String{
    return null
  }

  @Override
      property get Static(): boolean{
    return _propertyInfo.isStatic()
  }

  @Override
      property get Field(): boolean{
    return true
  }

  //==========PUBLIC METHODS==========//
  @Override
  function shouldBeIncluded(): boolean{
    var b = super.shouldBeIncluded()
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
    var comments = _propertyInfo.getDescription()
    addTextComments( comments )
    var gosuType = _propertyInfo.getFeatureType()
    _type = getRootDoc().getType( gosuType, this )
  }

}