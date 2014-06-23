package gw.spec.core.expressions.arithmetic
uses java.lang.RuntimeException

class ArithmeticOrderOfOperationsTest extends ArithmeticTestBase {

  function testThreeAdditions() {
    assertEquals(6, 1 + 2 + 3)  
    assertEquals(6, 1 + (2 + 3))  
    assertEquals(-3, 0 + -1 + -2)  
    assertEquals(-3, 0 + (-1 + -2)) 
    
    assertException(1, \ -> e1() + e2() + e3()) 
  }
  
  function testThreeSubtractions() {
    assertEquals(0, 3 - 2 - 1)   
    assertEquals(0, (3 - 2) - 1)   
    assertEquals(2, 3 - (2 - 1))   
    assertEquals(-4, 1 - 2 - 3)   
    assertEquals(-4, (1 - 2) - 3)   
    assertEquals(2, 1 - (2 - 3))  
    
    assertException(1, \ -> e1() - e2() - e3()) 
  }
  
  function testThreeMultiplications() {
    assertEquals(24, 2 * 3 * 4)  
    assertEquals(24, (2 * 3) * 4)  
    assertEquals(24, 2 * (3 * 4))  
    assertEquals(0, 0 * -1 * 2)  
    assertEquals(0, (0 * -1) * 2)  
    assertEquals(0, 0 * (-1 * 2))
    
    assertException(1, \ -> e1() * e2() * e3()) 
  }
  
  function testThreeDivisions() {
    assertEquals(2, 24 / 6 / 2)    
    assertEquals(2, (24 / 6) / 2)    
    assertEquals(8, 24 / (6 / 2)) 
    
    assertException(1, \ -> e1() / e2() / e3()) 
  }
  
  function testThreeRemainders() {
    assertEquals(1, 24 % 9 % 5)  
    assertEquals(1, (24 % 9) % 5)      
    assertEquals(0, 24 % (9 % 5))     
    
    assertException(1, \ -> e1() % e2() % e3())  
  }
  
  
  function testAdditionAndSubtraction() {
    assertEquals(0, 1 + 2 - 3)  
    assertEquals(0, (1 + 2) - 3)  
    assertEquals(0, 1 + (2 - 3))  
    assertEquals(4, 3 - 1 + 2)  
    assertEquals(4, (3 - 1) + 2)  
    assertEquals(0, 3 - (1 + 2))  
    
    assertEquals(4, 1 - 2 + 3 - 4 + 5 - 6 + 7)  
    assertEquals(4, (1 - 2) + (3 - 4) + (5 - 6) + 7)  
    assertEquals(-26, 1 - (2 + 3) - (4 + 5) - (6 + 7))  
    assertEquals(-1, 1 + 2 + 3 - 4 - 5 + 6 + 7 + 8 - 9 - 10) 
    
    assertException(1, \ -> e1() + e2() - e3()) 
    assertException(1, \ -> e1() - e2() + e3()) 
  }
  
  function testAdditionAndMultiplication() {
    assertEquals(14, 2 + 3 * 4)    
    assertEquals(20, (2 + 3) * 4)    
    assertEquals(14, 2 + (3 * 4))    
    assertEquals(10, 2 * 3 + 4)    
    assertEquals(10, (2 * 3) + 4) 
    assertEquals(14, 2 * (3 + 4))    
  
    assertEquals(141, 1 + 2 * 3 + 4 * 5 + 6 * 7 + 8 * 9) 
    assertEquals(141, 1 + (2 * 3) + (4 * 5) + (6 * 7) + (8 * 9)) 
    assertEquals(31185, (1 + 2) * (3 + 4) * (5 + 6) * (7 + 8) * 9)  
    assertEquals(796, 1 + 2 + 3 * 4 * 5 + 6 + 7 + 8 * 9 * 10) 
    
    assertException(1, \ -> e1() + e2() * e3()) 
    assertException(1, \ -> e1() * e2() + e3()) 
  }
  
  function testAdditionAndDivision() {
    assertEquals(2.5, 2.0 + 4.0 / 8.0)  
    assertEquals(0.75, (2.0 + 4.0) / 8.0)  
    assertEquals(2.5, 2.0 + (4.0 / 8.0))  
    
    assertEquals(8.5, 2.0 / 4.0 + 8.0)  
    assertEquals(8.5, (2.0 / 4.0) + 8.0)  
    assertEquals(0.16666666666666666, 2.0 / (4.0 + 8.0))  
  
    assertEquals(4.212698412698413, 1.0 + 2.0 / 3.0 + 4.0 / 5.0 + 6.0 / 7.0 + 8.0 / 9.0) 
    assertEquals(4.212698412698413, 1.0 + (2.0 / 3.0) + (4.0 / 5.0) + (6.0 / 7.0) + (8.0 / 9.0)) 
    assertEquals(0.0002886002886002886, (1.0 + 2.0) / (3.0 + 4.0) / (5.0 + 6.0) / (7.0 + 8.0) / 9.0) 
    assertEquals(16.238888888888887, 1.0 + 2.0 + 3.0 / 4.0 / 5.0 + 6.0 + 7.0 + 8.0 / 9.0 / 10.0) 

    assertException(1, \ -> e1() + e2() / e3())  
    assertException(1, \ -> e1() / e2() + e3())  
  }
  
  function testAdditionAndRemainder() {
    assertEquals(6, 2 + 10 % 6)    
    assertEquals(0, (2 + 10) % 6)    
    assertEquals(6, 2 + (10 % 6))    
    assertEquals(9, 23 % 10 + 6)    
    assertEquals(9, (23 % 10) + 6) 
    assertEquals(7, 23 % (10 + 6))    
  
    assertEquals(23, 15 + 13 % 10 + 20 % 5 + 17 % 7 + 18 % 8) 
    assertEquals(23, 15 + (13 % 10) + (20 % 5) + (17 % 7) + (18 % 8)) 
    assertEquals(6, (15 + 13) % (10 + 20) % (5 + 17) % (7 + 18) % 8) 
    assertEquals(54, 15 + 13 + 10 % 20 % 5 + 17 + 7 + 18 % 8 % 3) 
    
    assertException(1, \ -> e1() + e2() % e3()) 
    assertException(1, \ -> e1() % e2() + e3())   
  }
  
//  override function assertEquals(expected : int, actual : int) {
//    print("Expected " + expected + " was " + actual) 
//  }
//  
//  override function assertEquals(expected : double, actual : double) {
//    print("Expected " + expected + " was " + actual) 
//  }
//  
  
  function testSubtractionAndMultiplication() {
    assertEquals(-10, 2 - 3 * 4)    
    assertEquals(-4, (2 - 3) * 4)    
    assertEquals(-10, 2 - (3 * 4))    
    assertEquals(2, 2 * 3 - 4)    
    assertEquals(2, (2 * 3) - 4) 
    assertEquals(-2, 2 * (3 - 4))    
  
    assertEquals(-139, 1 - 2 * 3 - 4 * 5 - 6 * 7 - 8 * 9) 
    assertEquals(-139, 1 - (2 * 3) - (4 * 5) - (6 * 7) - (8 * 9)) 
    assertEquals(9, (1 - 2) * (3 - 4) * (5 - 6) * (7 - 8) * 9)  
    assertEquals(-794, 1 - 2 - 3 * 4 * 5 - 6 - 7 - 8 * 9 * 10) 
    
    assertException(1, \ -> e1() - e2() * e3()) 
    assertException(1, \ -> e1() * e2() - e3())   
  }
  
  function testSubtractionAndDivision() {
    assertEquals(1.5, 2.0 - 4.0 / 8.0)  
    assertEquals(-0.25, (2.0 - 4.0) / 8.0)  
    assertEquals(1.5, 2.0 - (4.0 / 8.0))  
    
    assertEquals(-7.5, 2.0 / 4.0 - 8.0)  
    assertEquals(-7.5, (2.0 / 4.0) - 8.0)  
    assertEquals(-0.5, 2.0 / (4.0 - 8.0))  
  
    assertEquals(-2.212698412698413, 1.0 - 2.0 / 3.0 - 4.0 / 5.0 - 6.0 / 7.0 - 8.0 / 9.0) 
    assertEquals(-2.212698412698413, 1.0 - (2.0 / 3.0) - (4.0 / 5.0) - (6.0 / 7.0) - (8.0 / 9.0)) 
    assertEquals(0.1111111111111111, (1.0 - 2.0) / (3.0 - 4.0) / (5.0 - 6.0) / (7.0 - 8.0) / 9.0) 
    assertEquals(-14.238888888888889, 1.0 - 2.0 - 3.0 / 4.0 / 5.0 - 6.0 - 7.0 - 8.0 / 9.0 / 10.0) 

    assertException(1, \ -> e1() - e2() / e3())  
    assertException(1, \ -> e1() / e2() - e3())    
  }
  
  function testSubtractionAndRemainder() {
    assertEquals(1, 2 - 7 % 6)    
    assertEquals(-5, (2 - 7) % 6)    
    assertEquals(1, 2 - (7 % 6))    
    assertEquals(-4, 23 % 7 - 6)    
    assertEquals(-4, (23 % 7) - 6) 
    assertEquals(0, 23 % (7 - 6))    
  
    assertEquals(7, 15 - 13 % 10 - 20 % 5 - 17 % 7 - 18 % 8) 
    assertEquals(7, 15 - (13 % 10) - (20 % 5) - (17 % 7) - (18 % 8)) 
    assertEquals(2, (15 - 13) % (10 - 20) % (5 - 17) % (7 - 18) % 8) 
    assertEquals(-24, 15 - 13 - 10 % 20 % 5 - 17 - 7 - 18 % 8 % 3) 
    
    assertException(1, \ -> e1() - e2() % e3()) 
    assertException(1, \ -> e1() % e2() - e3())     
  }
    
  
  function testMultiplicationAndDivision() {
    assertEquals(1.5, 2.0 * 6.0 / 8.0)  
    assertEquals(1.5, (2.0 * 6.0) / 8.0)  
    assertEquals(1.5, 2.0 * (6.0 / 8.0))  
    assertEquals(15.0, 2.0 * 6.0 / (8.0 / 10.0))  
    assertEquals(0.15, 2.0 * (6.0 / 8.0) / 10.0)  
    assertEquals(0.15, (2.0 * 6.0) / 8.0 / 10.0)  
    
    assertEquals(2.6666666666666665, 2.0 / 6.0 * 8.0)  
    assertEquals(2.6666666666666665, (2.0 / 6.0) * 8.0)  
    assertEquals(0.041666666666666664, 2.0 / (6.0 * 8.0))  
  
    assertEquals(0.4571428571428572, 1.0 * 2.0 / 3.0 * 4.0 / 5.0 * 6.0 / 7.0) 
    assertEquals(0.45714285714285713, 1.0 * (2.0 / 3.0) * (4.0 / 5.0) * (6.0 / 7.0)) 
    assertEquals(0.000011022927689594355, (1.0 * 2.0) / (3.0 * 4.0) / (5.0 * 6.0) / (7.0 * 8.0) / 9.0) 
    assertEquals(1.1199999999999997, 1.0 * 2.0 * 3.0 / 4.0 / 5.0 * 6.0 * 7.0 * 8.0 / 9.0 / 10.0) 

    assertException(1, \ -> e1() * e2() / e3())  
    assertException(1, \ -> e1() / e2() * e3())      
  }
  
  function testMultiplicationAndRemainder() {
    assertEquals(1, 3 * 7 % 5)    
    assertEquals(1, (3 * 7) % 5)    
    assertEquals(6, 3 * (7 % 5))    
    assertEquals(12, 23 % 7 * 6)    
    assertEquals(12, (23 % 7) * 6) 
    assertEquals(23, 23 % (7 * 6))    
  
    assertEquals(0, 15 * 13 % 10 * 20 % 5 * 17 % 7 * 18 % 8) 
    assertEquals(0, 15 * (13 % 10) * (20 % 5) * (17 % 7) * (18 % 8)) 
    assertEquals(1, (15 * 13) % (10 * 20) % (5 * 17) % (7 * 18) % 8) 
    assertEquals(0, 15 * 13 * 10 % 20 % 5 * 17 * 7 * 18 % 8 % 3) 
    
    assertException(1, \ -> e1() * e2() % e3()) 
    assertException(1, \ -> e1() % e2() * e3())       
  }
  
  function testDivisionAndRemainder() {
    assertEquals(0.25, 2.0 % 6.0 / 8.0)  
    assertEquals(0.25, (2.0 % 6.0) / 8.0)  
    assertEquals(0.5, 2.0 % (6.0 / 8.0))  
    assertEquals(2.5, 2.0 % 6.0 / (8.0 / 10.0))  
    assertEquals(0.05, 2.0 % (6.0 / 8.0) / 10.0)  
    assertEquals(0.025, (2.0 % 6.0) / 8.0 / 10.0)  
    
    assertEquals(0.3333333333333333, 2.0 / 6.0 % 8.0)  
    assertEquals(0.3333333333333333, (2.0 / 6.0) % 8.0)  
    assertEquals(1.0, 2.0 / (6.0 % 4.0))  
  
    assertEquals(0.001058201058201058, 1.0 % 2.0 / 3.0 % 4.0 / 5.0 % 6.0 / 7.0 % 8.0 / 9.0) 
    assertEquals(0.33333333333333337, 1.0 % (2.0 / 3.0) % (4.0 / 5.0) % (6.0 / 7.0) % (8.0 / 9.0)) 
    assertEquals(0.001058201058201058, (1.0 % 2.0) / (3.0 % 4.0) / (5.0 % 6.0) / (7.0 % 8.0) / 9.0) 
    assertEquals(0.0005555555555555556, 1.0 % 2.0 % 3.0 / 4.0 / 5.0 % 6.0 % 7.0 % 8.0 / 9.0 / 10.0) 

    assertException(1, \ -> e1() % e2() / e3())  
    assertException(1, \ -> e1() / e2() % e3())        
  }
  
  function testLargeComplicatedExpression() {
    assertEquals(-23.080357142857142, 11.0 + 2.0 * 4.0 - 5.0 - 6.0 / 8.0 % 23.0 * 56.0 + 5.0 + 4.0 % 2.5 * -3.0 / 56.0)   
    assertEquals(-23.080357142857142, 11.0 + (2.0 * 4.0) - 5.0 - (((6.0 / 8.0) % 23.0) * 56.0) + 5.0 + (((4.0 % 2.5) * -3.0) / 56.0))   
  }
  
  // --- Helpers
  
  private function assertException(number : int, exp()) {
    try {
      exp()
      fail()
    } catch (e : RuntimeException) {
      assertEquals(number as String, e.Message)  
    }
  }
  
  private function e1() : int {
    throw new RuntimeException("1")
  }
  
  private function e2() : int {
    throw new RuntimeException("2")
  }
  
  private function e3() : int {
    throw new RuntimeException("3")
  }
  
  private function e4() : int {
    throw new RuntimeException("4")
  }

}
