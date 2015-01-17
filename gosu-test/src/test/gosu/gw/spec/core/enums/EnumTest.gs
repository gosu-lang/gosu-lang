package gw.spec.core.enums
uses gw.test.TestClass
uses java.lang.Comparable
uses gw.lang.reflect.IEnumValue
uses java.io.Serializable
uses java.lang.IllegalArgumentException
uses java.lang.NullPointerException
uses java.lang.UnsupportedOperationException
uses java.io.ObjectOutputStream
uses java.io.ByteArrayOutputStream
uses java.io.ObjectInputStream
uses java.io.ByteArrayInputStream
uses gw.lang.reflect.IType
uses java.lang.Exception

class EnumTest extends TestClass 
{
  function testAllValuesOnSimpleEnum() {
    assertListEquals({SimpleEnum.RED, SimpleEnum.GREEN, SimpleEnum.BLUE}, SimpleEnum.AllValues)  
  }
  
  function testAllValuesOnComplexEnum() {
    assertListEquals({ComplexEnum.RED, ComplexEnum.GREEN, ComplexEnum.BLUE}, ComplexEnum.AllValues)  
  }

  function testValuesOnSimpleEnum() {
    assertArrayEquals({SimpleEnum.RED, SimpleEnum.GREEN, SimpleEnum.BLUE}, SimpleEnum.values())  
  }
  
  function testValuesOnComplexEnum() {
    assertArrayEquals({ComplexEnum.RED, ComplexEnum.GREEN, ComplexEnum.BLUE}, ComplexEnum.values())  
  }
  
  function testModifyingAllValuesPropertyDoesntModifyTheStaticVersion() {
    assertThrowsUnsupportedOperationException(\ -> SimpleEnum.AllValues.add(SimpleEnum.RED))    
    assertThrowsUnsupportedOperationException(\ -> SimpleEnum.AllValues.add(1, SimpleEnum.RED))    
    assertThrowsUnsupportedOperationException(\ -> SimpleEnum.AllValues.remove(SimpleEnum.RED))    
    assertThrowsUnsupportedOperationException(\ -> SimpleEnum.AllValues.remove(0))    
    SimpleEnum.AllValues.set(1, SimpleEnum.RED)
    assertEquals(SimpleEnum.GREEN, SimpleEnum.AllValues.get(1))    
  }

  function testModifyingValuesPropertyDoesntModifyTheStaticVersion() {    
    SimpleEnum.values()[1] = SimpleEnum.RED
    assertEquals(SimpleEnum.GREEN, SimpleEnum.values()[1])    
  }
  
  function testValueOfOnSimpleEnum() {
    assertEquals(SimpleEnum.RED, SimpleEnum.valueOf("RED"))
    assertEquals(SimpleEnum.GREEN, SimpleEnum.valueOf("GREEN"))
    assertEquals(SimpleEnum.BLUE, SimpleEnum.valueOf("BLUE"))
  }
  
  function testValueOfOnComplexEnum() {
    assertEquals(ComplexEnum.RED, ComplexEnum.valueOf("RED"))
    assertEquals(ComplexEnum.GREEN, ComplexEnum.valueOf("GREEN"))
    assertEquals(ComplexEnum.BLUE, ComplexEnum.valueOf("BLUE"))
  }
  
  function testValueOfThrowsIllegalArgumentExceptionForInvalidName() {
    assertThrowsIllegalArgumentException(\ -> SimpleEnum.valueOf("NoSuchEnum"))   
  }
  
  function testValueOfThrowsNullPointerExceptionForNullName() {
    assertThrowsNullPointerException(\ -> SimpleEnum.valueOf(null) )  
  }
 
  function testValueOfIsCaseSensitive() {
    assertEquals(SimpleEnum.RED, SimpleEnum.valueOf("RED"))
    assertThrowsIllegalArgumentException(\ -> SimpleEnum.valueOf("red"))    
    assertThrowsIllegalArgumentException(\ -> SimpleEnum.valueOf("Red"))    
  }
  
  function testNamePropertyOnSimpleEnum() {
    assertEquals("RED", SimpleEnum.RED.Name)
    assertEquals("GREEN", SimpleEnum.GREEN.Name)
    assertEquals("BLUE", SimpleEnum.BLUE.Name)
  }
  
  function testNamePropertyOnComplexEnum() {
    assertEquals("RED", ComplexEnum.RED.Name)
    assertEquals("GREEN", ComplexEnum.GREEN.Name)
    assertEquals("BLUE", ComplexEnum.BLUE.Name)
  }
  
  function testNameFunctionOnSimpleEnum() {
    assertEquals("RED", SimpleEnum.RED.name())
    assertEquals("BLUE", SimpleEnum.BLUE.name())
    assertEquals("GREEN", SimpleEnum.GREEN.name())  
  }
  
  function testOrdinalPropertyOnSimpleEnum() {
    assertEquals(0, SimpleEnum.RED.Ordinal)  
    assertEquals(1, SimpleEnum.GREEN.Ordinal)  
    assertEquals(2, SimpleEnum.BLUE.Ordinal)  
  }
  
  function testOrdinalPropertyOnComplexEnum() {
    assertEquals(0, ComplexEnum.RED.Ordinal)  
    assertEquals(1, ComplexEnum.GREEN.Ordinal)  
    assertEquals(2, ComplexEnum.BLUE.Ordinal)  
  }
  
  function testOrdinalFunctionOnSimpleEnum() {
    assertEquals(0, SimpleEnum.RED.ordinal())  
    assertEquals(1, SimpleEnum.GREEN.ordinal())  
    assertEquals(2, SimpleEnum.BLUE.ordinal())    
  }
  
  function testToStringOnSimpleEnum() {
    assertEquals("RED", SimpleEnum.RED.toString())
    assertEquals("GREEN", SimpleEnum.GREEN.toString())
    assertEquals("BLUE", SimpleEnum.BLUE.toString())  
  }
  
  function testToStringOnComplexEnum() {
    assertEquals("RED", ComplexEnum.RED.toString())
    assertEquals("GREEN", ComplexEnum.GREEN.toString())
    assertEquals("BLUE", ComplexEnum.BLUE.toString())  
  }
  
  function testEqualsMethodOnSimpleEnum() {
    assertTrue(SimpleEnum.RED.equals(SimpleEnum.RED))
    assertFalse(SimpleEnum.RED.equals(SimpleEnum.GREEN))
    assertFalse(SimpleEnum.RED.equals(SimpleEnum.BLUE))
  }
  
  function testEqualsMethodOnComplexEnum() {
    assertTrue(ComplexEnum.RED.equals(ComplexEnum.RED))
    assertFalse(ComplexEnum.RED.equals(ComplexEnum.GREEN))
    assertFalse(ComplexEnum.RED.equals(ComplexEnum.BLUE))
  }
  
  function testEqualsOperatorOnSimpleEnum() {
    assertTrue(SimpleEnum.RED == SimpleEnum.RED)
    assertFalse(SimpleEnum.RED == SimpleEnum.GREEN)  
    assertFalse(SimpleEnum.RED == SimpleEnum.BLUE)
  }
  
  function testEqualsOperatorOnComplexEnum() {
    assertTrue(ComplexEnum.RED == ComplexEnum.RED)
    assertFalse(ComplexEnum.RED == ComplexEnum.GREEN)  
    assertFalse(ComplexEnum.RED == ComplexEnum.BLUE)
  }
  
  function testObjectIdentityOnSimpleEnum() {
    assertTrue(SimpleEnum.RED === SimpleEnum.RED)
    assertFalse(SimpleEnum.RED === SimpleEnum.GREEN)  
    assertFalse(SimpleEnum.RED === SimpleEnum.BLUE)
  }
  
  function testObjectIdentityOnComplexEnum() {
    assertTrue(ComplexEnum.RED === ComplexEnum.RED)
    assertFalse(ComplexEnum.RED === ComplexEnum.GREEN)  
    assertFalse(ComplexEnum.RED === ComplexEnum.BLUE)
  }
  
  function testHashCodeOnSimpleEnum() {
    assertTrue(SimpleEnum.RED.hashCode() == SimpleEnum.RED.hashCode()) 
    assertFalse(SimpleEnum.RED.hashCode() == SimpleEnum.GREEN.hashCode()) 
    assertFalse(SimpleEnum.RED.hashCode() == SimpleEnum.BLUE.hashCode()) 
  }
  
  function testHashCodeOnComplexEnum() {
    assertTrue(ComplexEnum.RED.hashCode() == ComplexEnum.RED.hashCode()) 
    assertFalse(ComplexEnum.RED.hashCode() == ComplexEnum.GREEN.hashCode()) 
    assertFalse(ComplexEnum.RED.hashCode() == ComplexEnum.BLUE.hashCode()) 
  }
  
  function testCompareToOnSimpleEnum() {
    assertEquals(0, SimpleEnum.RED.compareTo(SimpleEnum.RED))  
    assertTrue(SimpleEnum.RED.compareTo(SimpleEnum.GREEN) < 0)  
    assertTrue(SimpleEnum.RED.compareTo(SimpleEnum.BLUE) < 0)
    
    assertEquals(0, SimpleEnum.GREEN.compareTo(SimpleEnum.GREEN))  
    assertTrue(SimpleEnum.GREEN.compareTo(SimpleEnum.RED) > 0)  
    assertTrue(SimpleEnum.GREEN.compareTo(SimpleEnum.BLUE) < 0)
    
    assertEquals(0, SimpleEnum.BLUE.compareTo(SimpleEnum.BLUE))  
    assertTrue(SimpleEnum.BLUE.compareTo(SimpleEnum.RED) > 0)  
    assertTrue(SimpleEnum.BLUE.compareTo(SimpleEnum.GREEN) > 0)
  }
  
  function testCompareToOnComplexEnum() {
    assertEquals(0, ComplexEnum.RED.compareTo(ComplexEnum.RED))  
    assertTrue(ComplexEnum.RED.compareTo(ComplexEnum.GREEN) < 0)  
    assertTrue(ComplexEnum.RED.compareTo(ComplexEnum.BLUE) < 0)
    
    assertEquals(0, ComplexEnum.GREEN.compareTo(ComplexEnum.GREEN))  
    assertTrue(ComplexEnum.GREEN.compareTo(ComplexEnum.RED) > 0)  
    assertTrue(ComplexEnum.GREEN.compareTo(ComplexEnum.BLUE) < 0)
    
    assertEquals(0, ComplexEnum.BLUE.compareTo(ComplexEnum.BLUE))  
    assertTrue(ComplexEnum.BLUE.compareTo(ComplexEnum.RED) > 0)  
    assertTrue(ComplexEnum.BLUE.compareTo(ComplexEnum.GREEN) > 0)
  }
  
  // TODO - Test comparing different enums
  
  function testSimpleEnumImplementsComparableAndIEnumValueAndSerializable() {
    assertTrue(SimpleEnum.RED typeis Comparable)  
    assertTrue(SimpleEnum.RED typeis IEnumValue)  
    assertTrue(SimpleEnum.RED typeis Serializable)  
  }
  
  function testComplexEnumImplementsComparableAndIEnumValueAndSerializable() {
    assertTrue(ComplexEnum.RED typeis Comparable)  
    assertTrue(ComplexEnum.RED typeis IEnumValue)  
    assertTrue(ComplexEnum.RED typeis Serializable)  
  }
  
  function testSimpleEnumExtendsFromJavaEnum() {
    assertTrue(SimpleEnum.RED typeis java.lang.Enum)    
  }
  
  function testGetDeclaringClassOnSimpleEnum() {
    assertEquals(SimpleEnum as java.lang.Class, SimpleEnum.RED.DeclaringClass)  
  }
  
  function testSerializationOnSimpleEnum() {
    assertTrue(SimpleEnum.RED === serializeAndDeserialize(SimpleEnum.RED))
    assertTrue(SimpleEnum.GREEN === serializeAndDeserialize(SimpleEnum.GREEN))
    assertTrue(SimpleEnum.BLUE === serializeAndDeserialize(SimpleEnum.BLUE))
  }
  
  function testSerializationOnComplexEnum() {
    assertTrue(ComplexEnum.RED === serializeAndDeserialize(ComplexEnum.RED))
    assertTrue(ComplexEnum.GREEN === serializeAndDeserialize(ComplexEnum.GREEN))
    assertTrue(ComplexEnum.BLUE === serializeAndDeserialize(ComplexEnum.BLUE))
  }
  
  function testValueOnSimpleEnum() {
    assertEquals(SimpleEnum.RED, SimpleEnum.RED.Value) 
    assertEquals(SimpleEnum.GREEN, SimpleEnum.GREEN.Value) 
    assertEquals(SimpleEnum.BLUE, SimpleEnum.BLUE.Value) 
  }
  
  function testValueOnComplexEnum() {
    assertEquals(ComplexEnum.RED, ComplexEnum.RED.Value) 
    assertEquals(ComplexEnum.GREEN, ComplexEnum.GREEN.Value) 
    assertEquals(ComplexEnum.BLUE, ComplexEnum.BLUE.Value) 
  }
  
  function testDisplayNameOnSimpleEnum() {
    assertEquals("RED", SimpleEnum.RED.DisplayName)
    assertEquals("GREEN", SimpleEnum.GREEN.DisplayName)
    assertEquals("BLUE", SimpleEnum.BLUE.DisplayName)  
  }
  
  function testDisplayNameOnComplexEnum() {
    assertEquals("RED", ComplexEnum.RED.DisplayName)
    assertEquals("GREEN", ComplexEnum.GREEN.DisplayName)
    assertEquals("BLUE", ComplexEnum.BLUE.DisplayName)  
  }
  
  function testCodeOnSimpleEnum() {
    assertEquals("RED", SimpleEnum.RED.Code)
    assertEquals("GREEN", SimpleEnum.GREEN.Code)
    assertEquals("BLUE", SimpleEnum.BLUE.Code)  
  }
  
  function testCodeOnComplexEnum() {
    assertEquals("RED", ComplexEnum.RED.Code)
    assertEquals("GREEN", ComplexEnum.GREEN.Code)
    assertEquals("BLUE", ComplexEnum.BLUE.Code)  
  }
  
  function testIntValueOnComplexEnum() {
    assertEquals(10, ComplexEnum.RED.IntValue)
    assertEquals(15, ComplexEnum.GREEN.IntValue)
    assertEquals(20, ComplexEnum.BLUE.IntValue)    
  }
  
  function testReflectiveAccessToSimpleEnum() {
    assertEquals("RED", SimpleEnum.RED["Name"])
    assertEquals("RED", SimpleEnum.RED["Code"])
    assertEquals("RED", SimpleEnum.RED["DisplayName"])
    assertEquals(SimpleEnum.RED, SimpleEnum.RED["Value"])
    assertEquals(0, SimpleEnum.RED["Ordinal"])
    
    var temp = SimpleEnum
    assertListEquals({SimpleEnum.RED, SimpleEnum.GREEN, SimpleEnum.BLUE}, temp["AllValues"] as List<SimpleEnum>)
    
    assertEquals(SimpleEnum.RED, invokeStaticMethod(SimpleEnum, "valueOf", {"RED"}))
    
    assertEquals( "RED", SimpleEnum.Type.TypeInfo.getMethod( "name", {} ).CallHandler.handleCall( SimpleEnum.RED, {} ) )
    assertEquals( 1, SimpleEnum.Type.TypeInfo.getMethod( "ordinal", {} ).CallHandler.handleCall( SimpleEnum.GREEN, {} ) )    
  }
  
  function testReflectiveAccessToComplexEnum() {
    assertEquals("RED", ComplexEnum.RED["Name"])
    assertEquals("RED", ComplexEnum.RED["Code"])
    assertEquals("RED", ComplexEnum.RED["DisplayName"])
    assertEquals(ComplexEnum.RED, ComplexEnum.RED["Value"])
    assertEquals(0, ComplexEnum.RED["Ordinal"])
    assertEquals(10, ComplexEnum.RED["IntValue"])
    
    var temp = ComplexEnum
    assertListEquals({ComplexEnum.RED, ComplexEnum.GREEN, ComplexEnum.BLUE}, temp["AllValues"] as List<ComplexEnum>)
    
    assertEquals(ComplexEnum.RED, invokeStaticMethod(ComplexEnum, "valueOf", {"RED"}))
    
    assertEquals( "RED", ComplexEnum.Type.TypeInfo.getMethod( "name", {} ).CallHandler.handleCall( SimpleEnum.RED, {} ) )
    assertEquals( 1, ComplexEnum.Type.TypeInfo.getMethod( "ordinal", {} ).CallHandler.handleCall( SimpleEnum.GREEN, {} ) )    
  }
  
  function testReflectiveInstantiationOfEnumFails() {
    var cons = SimpleEnum.Type.TypeInfo.DeclaredConstructors.single()
    try {
      cons.Constructor.newInstance({})
      fail("Expected accessing an enum constructor to throw an Exception")
    } catch (e : Exception) {
      // gulp
    }
  }
  
  // ----------------- ChainedConstructorEnum
  
  function testChainedConstructorEnum() {
    assertListEquals({ChainedConstructorEnum.RED, 
                      ChainedConstructorEnum.GREEN,
                      ChainedConstructorEnum.BLUE,
                      ChainedConstructorEnum.ORANGE}, 
                      ChainedConstructorEnum.AllValues)
                      
    assertEnumProperties(ChainedConstructorEnum.RED, 0, "RED")
    assertEnumProperties(ChainedConstructorEnum.GREEN, 1, "GREEN")
    assertEnumProperties(ChainedConstructorEnum.BLUE, 2, "BLUE")
    assertEnumProperties(ChainedConstructorEnum.ORANGE, 3, "ORANGE") 
    
    assertEquals(10, ChainedConstructorEnum.RED.IntValue) 
    assertEquals(15, ChainedConstructorEnum.GREEN.IntValue) 
    assertEquals(20, ChainedConstructorEnum.BLUE.IntValue) 
    assertEquals(25, ChainedConstructorEnum.ORANGE.IntValue) 
  }
  
  // ----------------- ClassWithInnerEnums
  
  function testClassWithInnerEnums() {

    assertTrue(ClassWithInnerEnums.InnerEnum.Type.Static)
    assertTrue(ClassWithInnerEnums.InnerEnum.ReallyInnerEnum.Type.Static)
    assertTrue(ClassWithInnerEnums.InnerEnumWithConstructor.Type.Static)
    
    assertListEquals({ClassWithInnerEnums.InnerEnum.SQUARE, 
                      ClassWithInnerEnums.InnerEnum.CIRCLE}, 
                      ClassWithInnerEnums.InnerEnum.AllValues)
                      
    assertListEquals({ClassWithInnerEnums.InnerEnum.ReallyInnerEnum.DOG, 
                      ClassWithInnerEnums.InnerEnum.ReallyInnerEnum.CAT, 
                      ClassWithInnerEnums.InnerEnum.ReallyInnerEnum.MOUSE}, 
                      ClassWithInnerEnums.InnerEnum.ReallyInnerEnum.AllValues)
                      
    assertListEquals({ClassWithInnerEnums.InnerEnumWithConstructor.FOO, 
                      ClassWithInnerEnums.InnerEnumWithConstructor.BAR, 
                      ClassWithInnerEnums.InnerEnumWithConstructor.BAZ}, 
                      ClassWithInnerEnums.InnerEnumWithConstructor.AllValues)
                      
    assertEquals(10, ClassWithInnerEnums.InnerEnumWithConstructor.FOO.IntValue)
    assertEquals(15, ClassWithInnerEnums.InnerEnumWithConstructor.BAR.IntValue)
    assertEquals(20, ClassWithInnerEnums.InnerEnumWithConstructor.BAZ.IntValue) 
  }
  
  // -------------------- ComplexEnumWithExtraComma
  
  function testComplexEnumWithExtraComma() {
    assertListEquals({ComplexEnumWithExtraComma.RED, ComplexEnumWithExtraComma.GREEN, ComplexEnumWithExtraComma.BLUE}, ComplexEnumWithExtraComma.AllValues)    
  }
  
  // ------------------- EmptyEnum
  
  function testAllValuesOnEmptyEnum() {
    assertTrue(EmptyEnum.AllValues.Empty)  
  }

  // ------------------- EnumImplementingGosuInterface
  
  function testEnumImplementingGosuInterface() {
    assertTrue(EnumImplementingGosuInterface.RED typeis Comparable)  
    assertTrue(EnumImplementingGosuInterface.RED typeis IEnumValue)  
    assertTrue(EnumImplementingGosuInterface.RED typeis Serializable) 
    assertTrue(EnumImplementingGosuInterface.RED typeis EnumGosuInterface) 
    
    var x : EnumGosuInterface
    x = EnumImplementingGosuInterface.RED
    assertEquals(10, x.IntValue) 
  }
  
  // ------------------- EnumImplementingInterfacesMatchingAutoAddedMethods
  
  function testEnumImplementingInterfacesMatchingAutoAddedMethods() {
    assertTrue(EnumImplementingInterfacesMatchingAutoAddedMethods.RED typeis gw.spec.core.enums.EnumInterfaceWithNameProperty)     
    assertTrue(EnumImplementingInterfacesMatchingAutoAddedMethods.RED typeis gw.spec.core.enums.EnumInterfaceWithCodeProperty)  
    
    var nameInterface : gw.spec.core.enums.EnumInterfaceWithNameProperty = EnumImplementingInterfacesMatchingAutoAddedMethods.RED 
    assertEquals("RED", nameInterface.Name)
    var codeInterface : gw.spec.core.enums.EnumInterfaceWithCodeProperty = EnumImplementingInterfacesMatchingAutoAddedMethods.RED  
    assertEquals("RED", codeInterface.Code)
  }
  
  // ------------------- EnumImplementingJavaInterface
  
  function testEnumImplementingJavaInterface() {
    assertTrue(EnumImplementingJavaInterface.RED typeis Comparable)  
    assertTrue(EnumImplementingJavaInterface.RED typeis IEnumValue)  
    assertTrue(EnumImplementingJavaInterface.RED typeis Serializable) 
    assertTrue(EnumImplementingJavaInterface.RED typeis gw.spec.core.enums.EnumJavaInterface) 
    
    var x : gw.spec.core.enums.EnumJavaInterface
    x = EnumImplementingJavaInterface.RED
    assertEquals(10, x.IntValue) 
  }

  // ------------------- EnumImplementingMultipleInterfaces
  
  function testEnumImplementingMultipleInterfaces() {
    assertTrue(EnumImplementingMultipleInterfaces.RED typeis Comparable)  
    assertTrue(EnumImplementingMultipleInterfaces.RED typeis IEnumValue)  
    assertTrue(EnumImplementingMultipleInterfaces.RED typeis Serializable) 
    assertTrue(EnumImplementingMultipleInterfaces.RED typeis gw.spec.core.enums.EnumJavaInterface) 
    assertTrue(EnumImplementingMultipleInterfaces.RED typeis gw.spec.core.enums.EnumJavaInterface2) 
    
    var x : gw.spec.core.enums.EnumJavaInterface
    x = EnumImplementingMultipleInterfaces.RED
    assertEquals(10, x.IntValue) 
    
    var y : gw.spec.core.enums.EnumJavaInterface2
    y = EnumImplementingMultipleInterfaces.RED
    assertEquals("Hello", y.Message) 
    y.doStuff()
    assertEquals(100, x.IntValue) 
  }
  
  // ------------------ EnumOverridingToString
  
  function testEnumOverridingToString() {
    assertListEquals({EnumOverridingToString.RED, 
                      EnumOverridingToString.GREEN,
                      EnumOverridingToString.BLUE}, 
                      EnumOverridingToString.AllValues)
                      
    assertEquals("RED", EnumOverridingToString.RED.Name)
    assertEquals("RED", EnumOverridingToString.RED.Code)
    assertEquals("alt-red", EnumOverridingToString.RED.DisplayName)
    assertEquals("alt-red", EnumOverridingToString.RED.toString())
    assertEquals(0, EnumOverridingToString.RED.Ordinal) 
  }
  
  // ------------------ EnumWithInnerClasses
  
  function testEnumWithInnerClasses() {
    assertEquals(10, EnumWithInnerClasses.RED.createInnerClass(1).OuterIntValue)  
    assertEquals(15, EnumWithInnerClasses.GREEN.createInnerClass(2).OuterIntValue)  
    assertEquals(20, EnumWithInnerClasses.BLUE.createInnerClass(3).OuterIntValue)  
    assertEquals(1, EnumWithInnerClasses.RED.createInnerClass(1).InnerIntValue)  
    assertEquals(2, EnumWithInnerClasses.GREEN.createInnerClass(2).InnerIntValue)  
    assertEquals(3, EnumWithInnerClasses.BLUE.createInnerClass(3).InnerIntValue)  
    assertEquals("foo", EnumWithInnerClasses.BLUE.createStaticInnerClass("foo").Arg) 
  }
  
  // ------------------ EnumWithInnerEnums
  
  function testEnumWithInnerEnums() {
    assertFalse(EnumWithInnerEnums.Type.Static)
    assertTrue(EnumWithInnerEnums.InnerEnum.Type.Static)
    assertTrue(EnumWithInnerEnums.InnerEnum.ReallyInnerEnum.Type.Static)
    
    assertListEquals({EnumWithInnerEnums.RED, 
                      EnumWithInnerEnums.GREEN,
                      EnumWithInnerEnums.BLUE}, 
                      EnumWithInnerEnums.AllValues)
    
    assertListEquals({EnumWithInnerEnums.InnerEnum.SQUARE, 
                      EnumWithInnerEnums.InnerEnum.CIRCLE}, 
                      EnumWithInnerEnums.InnerEnum.AllValues)
                      
    assertListEquals({EnumWithInnerEnums.InnerEnum.ReallyInnerEnum.DOG, 
                      EnumWithInnerEnums.InnerEnum.ReallyInnerEnum.CAT, 
                      EnumWithInnerEnums.InnerEnum.ReallyInnerEnum.MOUSE}, 
                      EnumWithInnerEnums.InnerEnum.ReallyInnerEnum.AllValues)
  }

  // ------------------ EnumWithMemberFunctionsAndProperties

  function testEnumWithMemberFunctionsAndProperties() {
    assertEquals(10, EnumWithMemberFunctionsAndProperties.RED.IntValue)  
    EnumWithMemberFunctionsAndProperties.RED.IntValue = 11
    assertEquals(11, EnumWithMemberFunctionsAndProperties.RED.IntValue)   
    assertEquals("__11", EnumWithMemberFunctionsAndProperties.RED.doSomething())   
    assertNull(EnumWithMemberFunctionsAndProperties.RED.publicValue)   
    EnumWithMemberFunctionsAndProperties.RED.publicValue = "new"
    assertEquals("new", EnumWithMemberFunctionsAndProperties.RED.publicValue)   
    
    assertEquals(20, EnumWithMemberFunctionsAndProperties.BLUE.IntValue) 
    assertNull(EnumWithMemberFunctionsAndProperties.BLUE.publicValue) 
    
    assertEquals(0, EnumWithMemberFunctionsAndProperties.StaticIntValue)
    EnumWithMemberFunctionsAndProperties.StaticIntValue = 31
    assertEquals(31, EnumWithMemberFunctionsAndProperties.StaticIntValue)
    assertEquals("__31", EnumWithMemberFunctionsAndProperties.doSomethingStatic())   
    assertNull(EnumWithMemberFunctionsAndProperties.publicStaticValue)   
    EnumWithMemberFunctionsAndProperties.publicStaticValue = "publicStatic"
    assertEquals("publicStatic", EnumWithMemberFunctionsAndProperties.publicStaticValue) 
  }

  // ------------------ EnumWithStaticCyclicReferences
  
  function testEnumWithStaticCyclicReferences() {
    assertListEquals({EnumWithStaticCyclicReferences.RED, 
                      EnumWithStaticCyclicReferences.GREEN,
                      EnumWithStaticCyclicReferences.BLUE}, 
                      EnumWithStaticCyclicReferences.AllFields)
                      
    assertNull(EnumWithStaticCyclicReferences.RED.RedAtConstructTime)  
    assertNull(EnumWithStaticCyclicReferences.RED.GreenAtConstructTime)  
    assertNull(EnumWithStaticCyclicReferences.RED.BlueAtConstructTime)  
    
    assertEquals(EnumWithStaticCyclicReferences.RED, EnumWithStaticCyclicReferences.GREEN.RedAtConstructTime)  
    assertNull(EnumWithStaticCyclicReferences.GREEN.GreenAtConstructTime)  
    assertNull(EnumWithStaticCyclicReferences.GREEN.BlueAtConstructTime) 
    
    assertEquals(EnumWithStaticCyclicReferences.RED, EnumWithStaticCyclicReferences.BLUE.RedAtConstructTime)  
    assertEquals(EnumWithStaticCyclicReferences.GREEN, EnumWithStaticCyclicReferences.BLUE.GreenAtConstructTime)  
    assertNull(EnumWithStaticCyclicReferences.BLUE.BlueAtConstructTime) 
  }

  // ------------------ MixedCaseEnum

  function testMixedCaseNamesEnum() {
    assertListEquals({MixedCaseNamesEnum.red, 
                      MixedCaseNamesEnum.Green,
                      MixedCaseNamesEnum.bLuE,
                      MixedCaseNamesEnum.YELLOW,
                      MixedCaseNamesEnum.OrAnGe}, 
                      MixedCaseNamesEnum.AllValues)
                      
    assertEnumProperties(MixedCaseNamesEnum.red, 0, "red")
    assertEnumProperties(MixedCaseNamesEnum.Green, 1, "Green")
    assertEnumProperties(MixedCaseNamesEnum.bLuE, 2, "bLuE")
    assertEnumProperties(MixedCaseNamesEnum.YELLOW, 3, "YELLOW")
    assertEnumProperties(MixedCaseNamesEnum.OrAnGe, 4, "OrAnGe")
  }


  // -------------------- OverloadedConstructorEnum
  
  function testOverloadedConstructorEnum() {
    assertListEquals({OverloadedConstructorEnum.RED, 
                      OverloadedConstructorEnum.GREEN,
                      OverloadedConstructorEnum.BLUE}, 
                      OverloadedConstructorEnum.AllValues)
                      
    assertEnumProperties(OverloadedConstructorEnum.RED, 0, "RED")
    assertEnumProperties(OverloadedConstructorEnum.GREEN, 1, "GREEN")
    assertEnumProperties(OverloadedConstructorEnum.BLUE, 2, "BLUE")
    
    assertEquals(10, OverloadedConstructorEnum.RED.IntValue) 
    assertEquals(15, OverloadedConstructorEnum.GREEN.IntValue) 
    assertEquals(20, OverloadedConstructorEnum.BLUE.IntValue)
  }
  
  // -------------------- SimpleEnumWithExtraComma
  
  function testSimpleEnumWithExtraComma() {
    assertListEquals({SimpleEnumWithExtraComma.RED, SimpleEnumWithExtraComma.GREEN, SimpleEnumWithExtraComma.BLUE}, SimpleEnumWithExtraComma.AllValues)    
  }

  function testReflectiveAccessThroughGosuClass() {
    assertIterableEqualsIgnoreOrder( OverloadedConstructorEnum.AllValues, OverloadedConstructorEnum.Type.EnumValues )
    assertEquals( OverloadedConstructorEnum.RED, OverloadedConstructorEnum.Type.getEnumValue("RED") )
    assertEquals( OverloadedConstructorEnum.GREEN, OverloadedConstructorEnum.Type.getEnumValue("GREEN") )
    assertEquals( OverloadedConstructorEnum.BLUE, OverloadedConstructorEnum.Type.getEnumValue("BLUE") )
  }

  // ----------------- Private Helper Methods
  
  private function assertThrowsIllegalArgumentException(exp()) {
    try {
      exp()
      fail("Expected an IllegalArgumentException to be thrown")
    } catch (e : IllegalArgumentException) {
     // Expected  
    }
  }
  
  private function assertThrowsNullPointerException(exp()) {
    try {
      exp()
      fail("Expected a NullPointerException to be thrown")
    } catch (e : NullPointerException) {
     // Expected  
    }
  }
  
  private function assertThrowsUnsupportedOperationException(exp()) {
    try {
      exp()
      fail("Expected an UnsupportedOperationException to be thrown")
    } catch (e : UnsupportedOperationException) {
     // Expected  
    }
  }
  
  private function serializeAndDeserialize<T>(arg : T) : T {
    var byteArray = new ByteArrayOutputStream()
    var oos = new ObjectOutputStream(byteArray)
    oos.writeObject(arg)  
    oos.flush()
    oos.close()
    
    var ois = new ObjectInputStream(new ByteArrayInputStream(byteArray.toByteArray()))
    var result = ois.readObject()
    ois.close()
    return result as T
  }
    
  private function assertEnumProperties(value : IEnumValue & java.lang.Enum, ordinal : int, enumName : String) {
    assertEquals(enumName, value.name())
    assertEquals(enumName, value.Code)
    assertEquals(enumName, value.DisplayName)
    assertEquals(enumName, value.toString())
    assertEquals(ordinal, value.Ordinal)
    assertEquals(ordinal, value.ordinal())      
  }
  
  private function invokeMethod(target : Object, methodName : String, args : Object[]) : Object {
    var method = (typeof target).TypeInfo.Methods.singleWhere(\ i -> i.DisplayName == methodName and i.Parameters.length == args.length )
    return method.CallHandler.handleCall(target, args)  
  }
  
  private function invokeStaticMethod(target : IType, methodName : String, args : Object[]) : Object {
    var method = target.TypeInfo.Methods.singleWhere(\ i -> i.DisplayName == methodName and i.Parameters.length == args.length )
    return method.CallHandler.handleCall(null, args)  
  }

  
}
