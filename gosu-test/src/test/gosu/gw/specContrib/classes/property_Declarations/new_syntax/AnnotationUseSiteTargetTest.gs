package gw.specContrib.classes.property_Declarations.new_syntax
uses gw.test.TestClass

class AnnotationUseSiteTargetTest extends TestClass {
  static final var ONE = 1
  static final var TWO = 2
  static final var THREE = 3
  static final var FOUR = 4
  
  function testAll() {
    verifyField( TestMe, "_field" )
    verifyField( TestMe, "_Prop" )
    verifyField( TestMe, "_Abstract_Prop" )
    verifyField( TestMe, "_Interface_Prop" ) 
    verifyField( TestMe, "_GetProp" ) 
    verifyField( TestMe, "_SetProp" ) 
    
    verifyGet( TestMe, "Field_Prop" )
    verifyGet( TestMe, "Prop" )
    verifyGet( TestMe, "Abstract_Prop" )
    verifyGet( TestMe, "Interface_Prop" )
    verifyGet( TestMe, "GetProp" )
    
    verifySet( TestMe, "Field_Prop" )
    verifySet( TestMe, "Prop" )
    verifySet( TestMe, "Abstract_Prop" )
    verifySet( TestMe, "Interface_Prop" )
    verifySet( TestMe, "SetProp" )
   
    verifyGet( ITestMe, "Interface_Prop" )
    verifySet( ITestMe, "Interface_Prop" )
 
    verifyGet( AbstractTestMe, "Abstract_Prop" )
    verifySet( AbstractTestMe, "Abstract_Prop" )
  }
  
  function verifyProp( cls: Class ) {
    verifyGet( cls, "Field_Prop" )
    verifyGet( cls, "Prop" )
    verifyGet( cls, "Abstract_Prop" )
    verifyGet( cls, "Interface_Prop" )
    
    verifySet( cls, "Field_Prop" )
    verifySet( cls, "Prop" )
    verifySet( cls, "Abstract_Prop" )
    verifySet( cls, "Interface_Prop" )      
  }
  
  function verifyField( cls: Class, name: String ) {
    var f = cls.getDeclaredField( name )
    assertEquals( TWO, f.Annotations.length )
    assertEquals( FOUR, f.getAnnotation( MyNoTargetAnno ).value() )      
    assertEquals( ONE, f.getAnnotation( MyFieldAnno ).value() )
  }  
  
  function verifyGet( cls: Class, name: String ) {
    var method = cls.getDeclaredMethod( "get"+name, {} )
    assertEquals( THREE, method.Annotations.length )
    assertEquals( FOUR, method.getAnnotation( MyNoTargetAnno ).value() )      
    assertEquals( ONE, method.getAnnotation( MyMethodAnno2 ).value() )
    assertEquals( ONE, method.getAnnotation( MyMethodAnno ).value() )
    
    assertEquals( 0, method.ParameterAnnotations.length )
  }
  
  function verifySet( cls: Class, name: String ) {
    var method = cls.getDeclaredMethod( "set"+name, {int} )
    assertEquals( THREE, method.Annotations.length )
    assertEquals( FOUR, method.getAnnotation( MyNoTargetAnno ).value() )      
    assertEquals( ONE, method.getAnnotation( MyMethodAnno2 ).value() )
    assertEquals( TWO, method.getAnnotation( MyMethodAnno ).value() )
    
    assertEquals( ONE, method.ParameterAnnotations.length )
    assertEquals( ONE, method.ParameterAnnotations[0].length )
    assertEquals( ONE, (method.ParameterAnnotations[0][0] as MyParamAnno).value() )
  }
  
  interface ITestMe {
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( ONE )  
    @set:MyMethodAnno( TWO )    
    @accessors:MyMethodAnno2( ONE )    
    @param:MyParamAnno( ONE )  
    property Interface_Prop: int        
  }
  
  static abstract class AbstractTestMe {
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( ONE )  
    @set:MyMethodAnno( TWO )    
    @accessors:MyMethodAnno2( ONE )    
    @param:MyParamAnno( ONE )  
    abstract property Abstract_Prop: int       
  }
  
  static class TestMe extends AbstractTestMe implements ITestMe {
    
    /** desc */
    @MyNoTargetAnno
    @get:MyMethodAnno( ONE )  
    @set:MyMethodAnno( TWO )    
    @accessors:MyMethodAnno2( ONE )    
    @field:MyFieldAnno( ONE )
    @param:MyParamAnno( ONE )  
    var _field: int as Field_Prop
    
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( ONE )  
    @set:MyMethodAnno( TWO )    
    @accessors:MyMethodAnno2( ONE )    
    @field:MyFieldAnno( ONE )
    @param:MyParamAnno( ONE )  
    property Prop: int 
    
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( ONE )  
    @accessors:MyMethodAnno2( ONE )    
    @field:MyFieldAnno( ONE )
    property get GetProp: int 

     /** desc */
    @MyNoTargetAnno  
    @set:MyMethodAnno( TWO )    
    @accessors:MyMethodAnno2( ONE )    
    @field:MyFieldAnno( ONE )
    @param:MyParamAnno( ONE )  
    property set SetProp: int 
       
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( ONE )  
    @set:MyMethodAnno( TWO )    
    @accessors:MyMethodAnno2( ONE )    
    @field:MyFieldAnno( ONE )
    @param:MyParamAnno( ONE )      
    override property Abstract_Prop: int
    
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( ONE )  
    @set:MyMethodAnno( TWO )    
    @accessors:MyMethodAnno2( ONE )
    @field:MyFieldAnno( ONE )    
    @param:MyParamAnno( ONE )  
    override property Interface_Prop: int     
  }
  
  
}