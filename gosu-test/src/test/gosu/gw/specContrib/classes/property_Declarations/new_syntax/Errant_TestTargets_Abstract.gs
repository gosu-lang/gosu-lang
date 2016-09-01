package gw.specContrib.classes.property_Declarations.new_syntax
uses javafx.scene.media.MediaPlayer._BufferListener  //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS

@MyNoTargetAnno  
@get:MyMethodAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@accessors:MyMethodAnno2( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
abstract class Errant_TestTargets_Abstract
{ 
  //
  // Properties
  // 

  // Static 
  
  /** desc */
  @MyNoTargetAnno  
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  static abstract property STATIC_PROP: int
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  abstract static property get STATIC_READONLY_PROP: int
      
  // non-static 
  
  /** desc */
  @MyNoTargetAnno  
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  abstract property Prop: int
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  abstract property get Get_Prop: int
      
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  abstract property set Set_Prop: int
  
  /** desc */
  @get:MyMethodAnno( 1 )  
  @accessors:MyMethodAnno( 1 )    
  abstract property Prop_0: int    //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno( 1 )    
  abstract property Prop_1: int    //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno( 1 )    
  abstract property Prop_2: int    //## issuekeys: MSG_TOO_MANY_ANNOTATIONS, MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @set:MyMethodAnno( 1 )    
  @set:MyMethodAnno( 1 )    
  abstract property Prop_3: int      //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @set:MyMethodAnno( 1 )    
  @set:MyMethodAnno2( 1 )    
  abstract property Prop_4: int
    
  /** desc */
  @get:MyMethodAnno( 1 )    
  @get:MyMethodAnno2( 1 )    
  abstract property Prop_5: int
  
  /** desc */
  @get:MyMethodAnno( 1 )    
  @get:MyMethodAnno( 1 )    
  abstract property Prop_6: int  //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @get:MyMethodAnno( 1 )    
  @MyNoTargetAnno    
  abstract property Prop_7: int
  
  /** desc */
  @get:MyNoTargetAnno    
  @set:MyNoTargetAnno
  abstract property Prop_8: int
  
  /** desc */
  @set:MyNoTargetAnno    
  @set:MyNoTargetAnno
  abstract property Prop_9: int  //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @MyNoTargetAnno
  @set:MyNoTargetAnno    
  abstract property Prop_10: int  //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
}
