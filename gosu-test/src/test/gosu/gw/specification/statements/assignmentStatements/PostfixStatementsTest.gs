package gw.specification.statements.assignmentStatements

uses gw.BaseVerifyErrantTest
uses java.lang.Byte
uses java.lang.Double
uses java.lang.Float
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Short
uses java.lang.Character
uses java.math.BigInteger
uses java.math.BigDecimal
uses java.util.ArrayList
uses java.util.concurrent.atomic.AtomicInteger
uses java.util.concurrent.atomic.AtomicLong
uses java.util.HashMap

class PostfixStatementsTest extends BaseVerifyErrantTest {
  function testErrant_PostfixStatementsTest() {
    processErrantType(Errant_PostfixStatementsTest)
  }

  function testPrimitiveTypesInPostfix(){
    var x1 : byte = 0
    var x2 : double = 0
    var x3 : float = 0
    var x4 : int = 0
    var x5 : long = 0
    var x6 : short = 0
    var x7 : char = '0'
    var x8 : boolean = true

    x1++;
    assertEquals(x1, 1b)
    x2++;
    assertEquals(x2, 1d)
    x3++;
    assertEquals(x3, 1f)
    x4++;
    assertEquals(x4, 1)
    x5++;
    assertEquals(x5, 1l)
    x6++;
    assertEquals(x6, 1s)
    x7++;
    assertEquals(x7, '1')

    x1--;
    assertEquals(x1, 0b)
    x2--;
    assertEquals(x2, 0d)
    x3--;
    assertEquals(x3, 0f)
    x4--;
    assertEquals(x4, 0)
    x5--;
    assertEquals(x5, 0l)
    x6--;
    assertEquals(x6, 0s)
    x7--;
    assertEquals(x7, '0')
  }

  function testWrapperTypesInPostfix(){
    var x1 : Byte = 0
    var x2 : Double = 0
    var x3 : Float = 0
    var x4 : Integer = 0
    var x5 : Long = 0
    var x6 : Short = 0
    var x7 : Character = '0'
    var x8 : Boolean = true

    x1++;
    assertEquals(x1, 1B)
    x2++;
    assertEquals(x2, 1D)
    x3++;
    assertEquals(x3, 1F)
    x4++;
    assertEquals(x4, 1)
    x5++;
    assertEquals(x5, 1L)
    x6++;
    assertEquals(x6, 1S)
    x7++;
    assertEquals(x7, '1')

    x1--;
    assertEquals(x1, 0B)
    x2--;
    assertEquals(x2, 0D)
    x3--;
    assertEquals(x3, 0F)
    x4--;
    assertEquals(x4, 0)
    x5--;
    assertEquals(x5, 0L)
    x6--;
    assertEquals(x6, 0S)
    x7--;
    assertEquals(x7, '0')
  }

  function testOtherTypesInPostfix(){
    var x1 : BigDecimal = 0bd
    var x2 : BigDecimal = 0BD
    var x3 : BigInteger = 0bi
    var x4 : BigInteger = 0BI
    var x5 = 0x7b
    var x6 = -0X7F                  //IDE-2536
    var x7 : AtomicInteger = new AtomicInteger(0)
    var x8 : AtomicLong = new AtomicLong(0)
    var x9 : Object

    x1++;
    assertEquals(x1, 1bd)
    x2++;
    assertEquals(x2, 1BD)
    x3++;
    assertEquals(x3, 1bi)
    x4++;
    assertEquals(x4, 1BI)
    x5++;
    assertEquals(x5, 0x7c)
    x6++;
    assertEquals(x6, -0X7E)          //IDE-2536


    x1--;
    assertEquals(x1, 0bd)
    x2--;
    assertEquals(x2, 0BD)
    x3--;
    assertEquals(x3, 0bi)
    x4--;
    assertEquals(x4, 0BI)
    x5--;
    assertEquals(x5, 0x7b)
    x6--;
    assertEquals(x6, -0X7F)          //IDE-2536
  }

  function test1DArrayElementInPostfix(){
    var array1D = {1,2,3}
    array1D[0]++;
    assertEquals(array1D[0], 2)

    array1D[1]--;
    assertEquals(array1D[1], 1)
  }

  function test2DArrayElementInPostfix(){
    var array2D = {{1,2},{3,4,5}}
    array2D[0][1]++;
    assertEquals(array2D[0][1], 3)

    array2D[1][2]--;
    assertEquals(array2D[1][2], 4)
  }

  function test3DArrayElementInPostfix(){
    var array3D = {{{1,2,3},{4,5,6}},{{3,4,5}}}
    array3D[0][1][1]++;
    assertEquals(array3D[0][1][1], 6)

    array3D[1][0][2]--;
    assertEquals(array3D[1][0][2], 4)
  }

  function testListElementInPostfix(){
    var list = new ArrayList<Integer>(){1,2,3,4,5}
    list[3]++;
    assertEquals(list[3], 5)
  }

  function testMapElementInPostfix(){
    var map = new HashMap<Integer, Integer>(){1->10, 2->20}
    map[1]++
    assertEquals(map[1], 11)
  }

  function testEvaluateOnceInPostfix(){
    var arr : int[] = {1, 2}
    var i : int = 0
    var fun = \->{
      i++
      return 1
    }
    arr[fun()]++
    assertEquals(i, 1)
    assertEquals(arr[1], 3)
  }
}