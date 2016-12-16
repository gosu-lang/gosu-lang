package gw.specContrib.classes.property_Declarations.new_syntax
uses gw.lang.ir.Internal

@MyNoTargetAnno  
@get:MyMethodAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@accessors:MyMethodAnno2( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
@receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
class Errant_TestTargets
{ 
  //
  // Vars
  // 

  // Static 
  
  /** desc */
  @MyNoTargetAnno  
  @get:MyMethodAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  static var STATIC_FIELD_VAR: int
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected
  @get:internal
  @field:internal
  @param:protected  //## issuekeys: MSG_INVALID_TYPE
  @accessors:protected
  static var STATIC_FIELD: int as STATIC_FIELD_PROP  
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal
  @field:internal
  static var STATIC_READONLY_FIELD: int as readonly STATIC_READONLY_FIELD_PROP
     
  /** desc */   
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal
  @field:internal
  static final var STATIC_FINAL_FIELD: int as STATIC_FINAL_FIELD_PROP = 8
  
  
  // non-static
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @field:internal    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  var field_var: int
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected
  @get:internal
  @field:internal  
  var field: int as field_prop  //## issuekeys: MSG_IMPROPER_USE_OF_KEYWORD
     
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal
  @field:internal  
  var readonly_field: int as readonly readonly_field_prop
     
  /** desc */   
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal
  @field:internal  
  final var final_field: int as final_field_prop = 8  
  
  //
  // Properties
  // 

  // Static 
  
  /** desc */
  @MyNoTargetAnno  
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected
  @get:internal
  @field:internal
  static property STATIC_PROP: int
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal
  @field:internal
  static property get STATIC_READONLY_PROP: int
      
  // non-static 
  
  /** desc */
  @MyNoTargetAnno  
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected
  @get:internal
  @field:internal
  property Prop: int
  
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal
  @field:internal
  property get Get_Prop: int
      
  /** desc */
  @MyNoTargetAnno
  @get:MyMethodAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno2( 1 )    
  @field:MyFieldAnno( 1 )
  @param:MyParamAnno( 1 )  
  @receiver:MyParamAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected
  @get:internal  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @field:internal
  property set Set_Prop: int
     
  //
  // Functions
  //
  /** desc */
  @MyNoTargetAnno  
  @get:MyMethodAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:MyMethodAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @accessors:MyMethodAnno2( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @field:MyFieldAnno( 1 )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @param:MyParamAnno( 1 )    //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @receiver:MyParamAnno( 1 )      //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:protected  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @get:internal  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @field:internal  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  function func(): int {
    return 0
  }

  // Too many
  
   /** desc */
  @get:MyMethodAnno( 1 )  
  @accessors:MyMethodAnno( 1 )    
  abstract property Prop_0: int    //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno( 1 )    
  property Prop_1: int    //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @get:MyMethodAnno( 1 )  
  @set:MyMethodAnno( 1 )    
  @accessors:MyMethodAnno( 1 )    
  property Prop_2: int    //## issuekeys: MSG_TOO_MANY_ANNOTATIONS, MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @set:MyMethodAnno( 1 )    
  @set:MyMethodAnno( 1 )    
  property Prop_3: int      //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @set:MyMethodAnno( 1 )    
  @set:MyMethodAnno2( 1 )    
  property Prop_4: int
    
  /** desc */
  @get:MyMethodAnno( 1 )    
  @get:MyMethodAnno2( 1 )    
  property Prop_5: int
  
  /** desc */
  @get:MyMethodAnno( 1 )    
  @get:MyMethodAnno( 1 )    
  property Prop_6: int  //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @get:MyMethodAnno( 1 )    
  @MyNoTargetAnno    
  property Prop_7: int
  
  /** desc */
  @get:MyNoTargetAnno    
  @set:MyNoTargetAnno
  property Prop_8: int
  
  /** desc */
  @set:MyNoTargetAnno    
  @set:MyNoTargetAnno
  property Prop_9: int  //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
  /** desc */
  @MyNoTargetAnno
  @set:MyNoTargetAnno    
  property Prop_10: int  //## issuekeys: MSG_TOO_MANY_ANNOTATIONS
  
     
  //
  // Misc
  //    
  
  @SuppressWarnings("all") 
  @MyFieldAnno( STATIC_FINAL_FIELD )
  @get:MyFieldAnno( STATIC_FINAL_FIELD )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  @set:MyFieldAnno( STATIC_FINAL_FIELD )  //## issuekeys: MSG_ANNOTATION_USE_SITE_TARGET_NOT_ALLOWED_HERE
  var _long = Date.parse( "" + STATIC_FINAL_FIELD ) // this is deprecated and should masked by suppresswarnings
  
  @SuppressWarnings("all") 
  @MyFieldAnno( STATIC_FINAL_FIELD )
  @get:MyFieldAnno( STATIC_FINAL_FIELD )
  @set:MyFieldAnno( STATIC_FINAL_FIELD )
  property MuhDate: long = Date.parse( "" + STATIC_FINAL_FIELD ) // this is deprecated and should masked by suppresswarnings

  //
  // Test final modifier targeting
  //

  class Bar {
    // final on getter-only applies to field only (because this is usually the intention on a property get declaration)
    final property get Hi: String

    @get:final property get Bye: String
    @get:final final property get Hey: String = "hey"
    @get:final final property get Konnichiwa: String //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

    construct() {
      _Hi = ""
    }
  }
  class Far {
    // final on getter-only applies to field only (because this is usually the intention on a property get declaration)
    final property get Hi: String   //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
  }
  class Har extends Bar {
    override property get Hi: String

    override property get Bye: String   //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
    override property get Hey: String   //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
  }
  class Mar {
    // final on regular property applies to methods only (because setter modifies field)
    final property Hi: String
  }

  class Xar extends Mar {
    override property Hi: String   //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL, MSG_CANNOT_OVERRIDE_FINAL
  }
  class Zar extends Mar {
    override property get Hi() : String {   //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
      return "hi"
    }
    override property set Hi( v: String ) {  //## issuekeys: MSG_CANNOT_OVERRIDE_FINAL
    }
  }
}
