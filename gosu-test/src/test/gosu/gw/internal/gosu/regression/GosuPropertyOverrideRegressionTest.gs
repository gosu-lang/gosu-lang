package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class GosuPropertyOverrideRegressionTest extends TestClass 
{
  function testErrant_OverrideVarPropertyWithDifferentType()
  {
    assertFalse( Errant_OverrideVarPropertyWithDifferentType.Type.Valid )
    var errors = Errant_OverrideVarPropertyWithDifferentType.Type.ParseResultsException.ParseExceptions
    assertEquals( Res.MSG_PROPERTIES_MUST_AGREE_ON_TYPE, errors.get( 0 ).MessageKey )
  }
  
  function testAccessAllOverridesViaPropertiesAsPropertiesFromSubClass() {
    var x = new AllOverridesViaProperties() 
    
    x.BarBooleanWithSetter = true
    assertEquals(true, x.BarBooleanWithSetter)   
    assertEquals(true, x.BarBoolean)   
    
    x.BarPBooleanWithSetter = true
    assertEquals(true, x.BarPBooleanWithSetter)
    assertEquals(true, x.BarPBoolean)
    
    x.BarPIntWithSetter = 5
    assertEquals(5, x.BarPIntWithSetter)
    assertEquals(5, x.BarPInt)
    
    x.BarStringWithSetter = "test"
    assertEquals("test", x.BarStringWithSetter)
    assertEquals("test", x.BarString)
    
    x.FooBooleanWithSetter = true
    assertEquals(true, x.FooBooleanWithSetter)
    assertEquals(true, x.FooBoolean)
    
    x.FooPBooleanWithSetter = true
    assertEquals(true, x.FooPBooleanWithSetter)
    assertEquals(true, x.FooPBoolean)
    
    x.FooPIntWithSetter = 5
    assertEquals(5, x.isFooPIntWithSetter())
    assertEquals(5, x.isFooPInt())
    
    x.FooStringWithSetter = "test"
    assertEquals("test", x.isFooStringWithSetter())
    assertEquals("test", x.isFooString())
  }
  
  function testAccessAllOverridesViaPropertiesAsPropertiesFromSuperClass() {
    var x : GosuPropertyOverrideRegressionHelper = new AllOverridesViaProperties() 
    
    x.BarBooleanWithSetter = true
    assertEquals(true, x.BarBooleanWithSetter)   
    assertEquals(true, x.BarBoolean)   
    
    x.BarPBooleanWithSetter = true
    assertEquals(true, x.BarPBooleanWithSetter)
    assertEquals(true, x.BarPBoolean)
    
    x.BarPIntWithSetter = 5
    assertEquals(5, x.BarPIntWithSetter)
    assertEquals(5, x.BarPInt)
    
    x.BarStringWithSetter = "test"
    assertEquals("test", x.BarStringWithSetter)
    assertEquals("test", x.BarString)
    
    x.FooBooleanWithSetter = true
    assertEquals(true, x.FooBooleanWithSetter)
    assertEquals(true, x.FooBoolean)
    
    x.FooPBooleanWithSetter = true
    assertEquals(true, x.FooPBooleanWithSetter)
    assertEquals(true, x.FooPBoolean)
    
    x.setFooPIntWithSetter(5)
    assertEquals(5, x.isFooPIntWithSetter())
    assertEquals(5, x.isFooPInt())
    
    x.setFooStringWithSetter("test")
    assertEquals("test", x.isFooStringWithSetter())
    assertEquals("test", x.isFooString())  
  }
  
  function testAccessAllOverridesViaPropertiesAsFunctionsFromSubClass() {
    var x = new AllOverridesViaProperties() 
    
    x.BarBooleanWithSetter = true
    assertEquals(true, x.BarBooleanWithSetter)   
    assertEquals(true, x.BarBoolean)   
    
    x.BarPBooleanWithSetter = true
    assertEquals(true, x.BarPBooleanWithSetter)
    assertEquals(true, x.BarPBoolean)
    
    x.BarPIntWithSetter = 5
    assertEquals(5, x.BarPIntWithSetter)
    assertEquals(5, x.BarPInt)
    
    x.BarStringWithSetter = "test"
    assertEquals("test", x.BarStringWithSetter)
    assertEquals("test", x.BarString)
    
    x.FooBooleanWithSetter = true
    assertEquals(true, x.FooBooleanWithSetter)
    assertEquals(true, x.FooBoolean)
    
    x.FooPBooleanWithSetter = true
    assertEquals(true, x.FooPBooleanWithSetter)
    assertEquals(true, x.FooPBoolean)
    
    x.FooPIntWithSetter = 5
    assertEquals(5, x.isFooPIntWithSetter())
    assertEquals(5, x.isFooPInt())
    
    x.FooStringWithSetter = "test"
    assertEquals("test", x.isFooStringWithSetter())
    assertEquals("test", x.isFooString())    
  }
  
  function testAccessAllOverridesViaPropertiesAsFunctionsFromSuperClass() {
    var x : GosuPropertyOverrideRegressionHelper = new AllOverridesViaProperties() 
    
    x.BarBooleanWithSetter = true
    assertEquals(true, x.BarBooleanWithSetter)   
    assertEquals(true, x.BarBoolean)   
    
    x.BarPBooleanWithSetter = true
    assertEquals(true, x.getBarPBooleanWithSetter())
    assertEquals(true, x.isBarPBooleanWithSetter())
    assertEquals(true, x.getBarPBoolean())
    assertEquals(true, x.isBarPBoolean())
    
    x.BarPIntWithSetter = 5
    assertEquals(5, x.getBarPIntWithSetter())
    assertEquals(5, x.BarPIntWithSetter)
    assertEquals(5, x.getBarPInt())
    assertEquals(5, x.BarPInt)
    
    x.BarStringWithSetter = "test"
    assertEquals("test", x.getBarStringWithSetter())
    assertEquals("test", x.BarStringWithSetter)
    assertEquals("test", x.getBarString())
    assertEquals("test", x.BarString)
    
    x.FooBooleanWithSetter = true
    assertEquals(true, x.getFooBooleanWithSetter())
    assertEquals(true, x.isFooBooleanWithSetter())
    assertEquals(true, x.getFooBoolean())
    assertEquals(true, x.isFooBoolean())
    
    x.FooPBooleanWithSetter = true
    assertEquals(true, x.getFooPBooleanWithSetter())
    assertEquals(true, x.isFooPBooleanWithSetter())
    assertEquals(true, x.getFooPBoolean())
    assertEquals(true, x.isFooPBoolean())
    
    x.setFooPIntWithSetter(5)
    assertEquals(5, x.isFooPIntWithSetter())
    assertEquals(5, x.isFooPInt())
    
    x.setFooStringWithSetter("test")
    assertEquals("test", x.isFooStringWithSetter())
    assertEquals("test", x.isFooString())  
  }
  
  function testAccessPropertiesAsIdentifiers() {
    var x = new AllOverridesViaProperties() 
    
    x.localSetBarBoolean(true)
    assertEquals(true, x.localAccessBarBoolean())     
    
    x.localSetBarPBoolean(true)
    assertEquals(true, x.localAccessBarPBoolean())
    
    x.localSetBarPInt(5)
    assertEquals(5, x.localAccessBarPInt())
    
    x.localSetBarString("test")
    assertEquals("test", x.localAccessBarString())
    
    x.localSetFooBoolean(true)
    assertEquals(true, x.localAccessFooBoolean())
    
    x.localSetFooPBoolean(true)
    assertEquals(true, x.localAccessFooPBoolean())
  }
  
  function testStaticPropertiesDefinedInGosu() {
    AllOverridesViaProperties.StaticBoolean = true
    assertEquals(true, AllOverridesViaProperties.StaticBoolean)  
    
    AllOverridesViaProperties.StaticPBoolean = true
    assertEquals(true, AllOverridesViaProperties.StaticPBoolean)
    
    AllOverridesViaProperties.StaticPInt = 5
    assertEquals(5, AllOverridesViaProperties.StaticPInt)
    
    AllOverridesViaProperties.StaticString = "test"
    assertEquals("test", AllOverridesViaProperties.StaticString)
  }
  
  function testStaticPropertiesAsIdentifiers() {
    AllOverridesViaProperties.localStaticSetBoolean(true)
    assertEquals(true, AllOverridesViaProperties.localStaticGetBoolean())  
    
    AllOverridesViaProperties.localStaticSetPBoolean(true)
    assertEquals(true, AllOverridesViaProperties.localStaticGetPBoolean())
    
    AllOverridesViaProperties.localStaticSetPInt(5)
    assertEquals(5, AllOverridesViaProperties.localStaticGetPInt())
    
    AllOverridesViaProperties.localStaticSetString("test")
    assertEquals("test", AllOverridesViaProperties.localStaticGetString()) 
  }

  function testEnhancementProperties() {
    var x = new AllOverridesViaProperties() 
    
    // Note that the enhancement setters don't do anything, so the expected value after the get does not equal the set value
    x.EnhancementBoolean = false
    assertEquals(true, x.EnhancementBoolean)   
    
    x.EnhancementPBoolean = false
    assertEquals(true, x.EnhancementPBoolean)
    
    x.EnhancementPInt = 5
    assertEquals(11, x.EnhancementPInt)
    
    x.EnhancementString = "hello"
    assertEquals("test", x.EnhancementString)
  }
  
  function testStaticEnhancementProperties() {
    // Note that the enhancement setters don't do anything, so the expected value after the get does not equal the set value
    AllOverridesViaProperties.StaticEnhancementBoolean = false
    assertEquals(true, AllOverridesViaProperties.StaticEnhancementBoolean)   
    
    AllOverridesViaProperties.StaticEnhancementPBoolean = false
    assertEquals(true, AllOverridesViaProperties.StaticEnhancementPBoolean)
    
    AllOverridesViaProperties.StaticEnhancementPInt = 5
    assertEquals(11, AllOverridesViaProperties.StaticEnhancementPInt)
    
    AllOverridesViaProperties.StaticEnhancementString = "hello"
    assertEquals("test", AllOverridesViaProperties.StaticEnhancementString)  
  }
  
  function testEnhancementPropertiesReferencedAsIdentifiers() {
    var x = new AllOverridesViaProperties() 
    
    // Note that the enhancement setters don't do anything, so the expected value after the get does not equal the set value
    x.localEnhancementSetBoolean(false)
    assertEquals(true, x.localEnhancementAccessBoolean())   
    
    x.localEnhancementSetPBoolean(false)
    assertEquals(true, x.localEnhancementAccessPBoolean())
    
    x.localEnhancementSetPInt(5)
    assertEquals(11, x.localEnhancementAccessPInt())
    
    x.localEnhancementSetString("hello")
    assertEquals("test", x.localEnhancementAccessString())  
  }
  
  function testStaticEnhancementPropertiesReferencedAsIdentifiers() {
    AllOverridesViaProperties.localStaticEnhancementSetBoolean(false)
    assertEquals(true, AllOverridesViaProperties.localStaticEnhancementAccessBoolean())   
    
    AllOverridesViaProperties.localStaticEnhancementSetPBoolean(false)
    assertEquals(true, AllOverridesViaProperties.localStaticEnhancementAccessPBoolean())
    
    AllOverridesViaProperties.localStaticEnhancementSetPInt(5)
    assertEquals(11, AllOverridesViaProperties.localStaticEnhancementAccessPInt())
    
    AllOverridesViaProperties.localStaticEnhancementSetString("hello")
    assertEquals("test", AllOverridesViaProperties.localStaticEnhancementAccessString())  
  }
  
  public static class AllOverridesViaProperties extends GosuPropertyOverrideRegressionHelper {
    
    private static var _staticBoolean : Boolean as StaticBoolean
    private static var _staticPBoolean : boolean as StaticPBoolean
    private static var _staticPInt : int as StaticPInt
    private static var _staticString : String as StaticString
    
    private var _barBoolean : Boolean
    private var _barPBoolean : boolean
    private var _barPInt : int
    private var _barString : String
    
    private var _fooBoolean : Boolean
    private var _fooPBoolean : boolean
    private var _fooPInt : int
    private var _fooString : String
    
    override property get BarBoolean() : Boolean {
      return _barBoolean
    }

    override property get BarBooleanWithSetter() : Boolean {
      return _barBoolean
    }

    override property set BarBooleanWithSetter(value : Boolean) {      
      _barBoolean = value
    }

    override property get BarPBoolean() : boolean {
      return _barPBoolean
    }

    override property get BarPBooleanWithSetter() : boolean {
      return _barPBoolean
    }

    override property set BarPBooleanWithSetter(value : boolean) {
      _barPBoolean = value
    }

    override property get BarPInt() : int {
      return _barPInt
    }

    override property get BarPIntWithSetter() : int {
      return _barPInt
    }

    override property set BarPIntWithSetter(value : int) {
      _barPInt = value
    }

    override property get BarString() : String {
      return _barString
    }

    override property get BarStringWithSetter() : String {
      return _barString
    }

    override property set BarStringWithSetter(value : String) {
      _barString = value
    }

    override property get FooBoolean() : Boolean {
      return _fooBoolean
    }

    override property get FooBooleanWithSetter() : Boolean {
      return _fooBoolean
    }

    override property set FooBooleanWithSetter(value : Boolean) {
      _fooBoolean = value
    }

    override property get FooPBoolean() : boolean {
      return _fooPBoolean
    }

    override property get FooPBooleanWithSetter() : boolean {
      return _fooPBoolean
    }

    override property set FooPBooleanWithSetter(value : boolean) {
      _fooPBoolean = value
    }

    override function isFooString() : String {
      return _fooString
    }

    override function isFooPInt() : int {
      return _fooPInt
    }

    override function isFooStringWithSetter() : String {
      return _fooString
    }

    override function isFooPIntWithSetter() : int {
      return _fooPInt
    }

    override property get FooStringWithSetter() : String {
      return _fooString
    }
    override property set FooStringWithSetter(value : String) {
      _fooString = value
    }

    override property get FooPIntWithSetter() : int {
      return _fooPInt
    }
    override property set FooPIntWithSetter(value : int) {
      _fooPInt = value
    }
    
    function localAccessBarBoolean() : Boolean {
      return BarBoolean  
    }
    
    function localAccessBarPBoolean() : boolean {
      return BarPBoolean  
    }
    
    function localAccessBarPInt() : int {
      return BarPInt  
    }
    
    function localAccessBarString() : String {
      return BarString  
    }
    
    function localAccessFooBoolean() : Boolean {
      return FooBoolean   
    }
    
    function localAccessFooPBoolean() : boolean {
      return FooPBoolean  
    }
    
    function localSetBarBoolean(value : Boolean) {
      BarBooleanWithSetter = value  
    }
    
    function localSetBarPBoolean(value : boolean) {
      BarPBooleanWithSetter = value  
    }
    
    function localSetBarPInt(value : int) {
      BarPIntWithSetter = value  
    }
    
    function localSetBarString(value : String) {
      BarStringWithSetter = value  
    }
    
    function localSetFooBoolean(value : Boolean) {
      FooBooleanWithSetter = value   
    }
    
    function localSetFooPBoolean(value : boolean) {
      FooPBooleanWithSetter = value
    }
    
    static function localStaticGetBoolean() : Boolean {
      return StaticBoolean
    }
    
    static function localStaticGetPBoolean() : boolean {
      return StaticPBoolean  
    }
    
    static function localStaticGetPInt() : int {
      return StaticPInt  
    }
    
    static function localStaticGetString() : String {
      return StaticString  
    }
    
    static function localStaticSetBoolean(value : Boolean) {
      StaticBoolean = value  
    }
    
    static function localStaticSetPBoolean(value : boolean) {
      StaticPBoolean = value  
    }
    
    static function localStaticSetPInt(value : int) {
      StaticPInt = value  
    }
    
    static function localStaticSetString(value : String) {
      StaticString = value  
    }
  }

}
