package gw.gosudoc.doc

uses com.sun.javadoc.Doc
uses com.sun.javadoc.SeeTag
uses com.sun.javadoc.SourcePosition
uses com.sun.javadoc.Tag
uses com.sun.tools.doclets.internal.toolkit.util.TextTag
uses gw.gosudoc.misc.GSSourcePositionImpl
uses gw.lang.reflect.IType
uses gw.lang.reflect.TypeSystem

uses java.lang.RuntimeException
uses java.util.ArrayList
uses java.util.HashMap

class GSDocImpl extends GSDocImplShim implements Doc{

  var _rootDoc: GSRootDocImpl
  var _commentText = ""
  var _rawText = ""
  var _name: String

  var _inlineTags = new ArrayList<Tag>()
  var _tagsByKind = new HashMap<String, List<Tag>>().toAutoMap( \k -> ({}) )

  construct( name: String, rootDoc: GSRootDocImpl ){
    _name = name
    _rootDoc = rootDoc
  }

  override function commentText(): String{
    return _commentText
  }

  override function tags(): Tag[]{
    return _tagsByKind.values().flatten().toTypedArray()
  }

  override function tags( tagname: String ): Tag[]{
    return _tagsByKind[tagname]?.toTypedArray()?:{}
  }

  override function seeTags(): SeeTag[]{
    return tags( "see" ).whereTypeIs( SeeTag )
  }

  override function inlineTags(): Tag[]{
    return _inlineTags.toTypedArray()
  }

  override function firstSentenceTags(): Tag[]{
    return inlineTags()
  }

  override property get RawCommentText(): String{
    return _rawText
  }

  override property set RawCommentText( rawDocumentation: String ){
    _rawText = rawDocumentation
  }

  override function name(): String{
    return _name
  }


  override property get Field(): boolean{
    return false
  }

  override property get EnumConstant(): boolean{
    return false
  }

  override property get Constructor(): boolean{
    return false
  }

  override property get Method(): boolean{
    return false
  }

  override property get AnnotationTypeElement(): boolean{
    return false
  }

  override property get Interface(): boolean{
    return false
  }

  override property get Exception(): boolean{
    return false
  }

  override property get Error(): boolean{
    return false
  }

  override property get Enum(): boolean{
    return false
  }

  override property get AnnotationType(): boolean{
    return false
  }

  override property get OrdinaryClass(): boolean{
    return false
  }

  override property get IsClassShimmed(): boolean{
    return false
  }

  override property get Included(): boolean{
    return true
  }

  override function position(): SourcePosition{
    return new GSSourcePositionImpl()
  }

  override function compareTo( obj: Object ): int{
    return name().compareTo( (obj as Doc).name() )
  }

  function getRootDoc(): GSRootDocImpl{
    return _rootDoc
  }

  function shouldBeIncluded(): boolean{
    return true
  }

  //==========PROTECTED METHODS==========//
  function addInlineTag( tag: Tag ){
    _inlineTags.add( tag )
    addTag( tag )
  }


  function addTag( tag: Tag ){
    _tagsByKind.get( tag.kind() ).add( tag )
  }

  function addTextComments( comments: String ){
    addInlineTag( new TextTag( this, comments?:"" ) )
  }

  function isVoid( returnType: IType ): boolean{
    return returnType == null || "void".equals( returnType.getName() )
  }

}