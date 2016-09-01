package gw.specContrib.classes.property_Declarations.new_syntax
uses gw.test.TestClass

class AnnotationUseSiteTargetTest extends TestClass {
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
    assertEquals( 2, f.Annotations.length )
    assertEquals( 9, f.getAnnotation( MyNoTargetAnno ).value() )      
    assertEquals( 1, f.getAnnotation( MyFieldAnno ).value() )
  }  
  
  function verifyGet( cls: Class, name: String ) {
    var method = cls.getDeclaredMethod( "get"+name, {} )
    assertEquals( 3, method.Annotations.length )
    assertEquals( 9, method.getAnnotation( MyNoTargetAnno ).value() )      
    assertEquals( 1, method.getAnnotation( MyMethodAnno2 ).value() )
    assertEquals( 1, method.getAnnotation( MyMethodAnno ).value() )
    
    assertEquals( 0, method.ParameterAnnotations.length )
  }
  
  function verifySet( cls: Class, name: String ) {
    var method = cls.getDeclaredMethod( "set"+name, {int} )
    assertEquals( 3, method.Annotations.length )
    assertEquals( 9, method.getAnnotation( MyNoTargetAnno ).value() )      
    assertEquals( 1, method.getAnnotation( MyMethodAnno2 ).value() )
    assertEquals( 2, method.getAnnotation( MyMethodAnno ).value() )
    
    assertEquals( 1, method.ParameterAnnotations.length )
    assertEquals( 1, method.ParameterAnnotations[0].length )
    assertEquals( 1, (method.ParameterAnnotations[0][0] as MyParamAnno).value() )
  }
  
  interface ITestMe {
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( 1 )  
    @set:MyMethodAnno( 2 )    
    @accessors:MyMethodAnno2( 1 )    
    @param:MyParamAnno( 1 )  
    property Interface_Prop: int        
  }
  
  static abstract class AbstractTestMe {
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( 1 )  
    @set:MyMethodAnno( 2 )    
    @accessors:MyMethodAnno2( 1 )    
    @param:MyParamAnno( 1 )  
    abstract property Abstract_Prop: int       
  }
  
  static class TestMe extends AbstractTestMe implements ITestMe {
    /** desc */
    @MyNoTargetAnno
    @get:MyMethodAnno( 1 )  
    @set:MyMethodAnno( 2 )    
    @accessors:MyMethodAnno2( 1 )    
    @field:MyFieldAnno( 1 )
    @param:MyParamAnno( 1 )  
    var _field: int as Field_Prop
    
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( 1 )  
    @set:MyMethodAnno( 2 )    
    @accessors:MyMethodAnno2( 1 )    
    @field:MyFieldAnno( 1 )
    @param:MyParamAnno( 1 )  
    property Prop: int 
    
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( 1 )  
    @accessors:MyMethodAnno2( 1 )    
    @field:MyFieldAnno( 1 )
    property get GetProp: int 

     /** desc */
    @MyNoTargetAnno  
    @set:MyMethodAnno( 2 )    
    @accessors:MyMethodAnno2( 1 )    
    @field:MyFieldAnno( 1 )
    @param:MyParamAnno( 1 )  
    property set SetProp: int 
       
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( 1 )  
    @set:MyMethodAnno( 2 )    
    @accessors:MyMethodAnno2( 1 )    
    @field:MyFieldAnno( 1 )
    @param:MyParamAnno( 1 )      
    override property Abstract_Prop: int
    
    /** desc */
    @MyNoTargetAnno  
    @get:MyMethodAnno( 1 )  
    @set:MyMethodAnno( 2 )    
    @accessors:MyMethodAnno2( 1 )
    @field:MyFieldAnno( 1 )    
    @param:MyParamAnno( 1 )  
    override property Interface_Prop: int     
  }
  
  
}