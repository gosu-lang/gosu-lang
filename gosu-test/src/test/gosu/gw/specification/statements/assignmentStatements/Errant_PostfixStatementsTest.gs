package gw.specification.statements.assignmentStatements


uses java.util.ArrayList
uses java.lang.Integer
uses java.util.HashMap
uses java.math.BigDecimal
uses java.math.BigInteger
uses java.util.concurrent.atomic.AtomicInteger
uses java.util.concurrent.atomic.AtomicLong
uses java.lang.Byte
uses java.lang.Double
uses java.lang.Float
uses java.lang.Long
uses java.lang.Short
uses java.lang.Character

class Errant_PostfixStatementsTest {
  function testPreIncreDecrement(){
    var x = 10
    x++
    x--
    ++x  //## issuekeys: MSG_UNEXPECTED_TOKEN
    --x  //## issuekeys: MSG_NOT_A_STATEMENT
  }

  function testPostIncreDecrementNotAnExpression(){
    var x = 10
    if(x++ > 10){    //## issuekeys: MSG_EXPECTING_RIGHTPAREN_IF, MSG_IMPLICIT_COERCION_ERROR, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
       print(x++)    //## issuekeys: MSG_EXPECTING_FUNCTION_CLOSE, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    }
    if(x-- < 10){   //## issuekeys: MSG_EXPECTING_RIGHTPAREN_IF, MSG_IMPLICIT_COERCION_ERROR, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
      print(x--)    //## issuekeys: MSG_EXPECTING_FUNCTION_CLOSE, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    }
    if(++x > 10){   //## issuekeys: MSG_EXPECTING_RIGHTPAREN_IF, MSG_UNEXPECTED_TOKEN, MSG_SYNTAX_ERROR, MSG_NOT_A_STATEMENT, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
      print(++x)    //## issuekeys: MSG_EXPECTING_FUNCTION_CLOSE, MSG_SYNTAX_ERROR, MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_UNEXPECTED_TOKEN
    }
    if(--x < 10){   //## issuekeys: MSG_EXPECTING_RIGHTPAREN_IF, MSG_UNEXPECTED_TOKEN, MSG_SYNTAX_ERROR, MSG_NOT_A_STATEMENT, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
      print(--x)    //## issuekeys: MSG_EXPECTING_FUNCTION_CLOSE, MSG_SYNTAX_ERROR, MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_UNEXPECTED_TOKEN
    }
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
    x2++;
    x3++;
    x4++;
    x5++;
    x6++;
    x7++;
    x8++;  //## issuekeys: MSG_TYPE_MISMATCH

    x1--;
    x2--;
    x3--;
    x4--;
    x5--;
    x6--;
    x7--;
    x8++;  //## issuekeys: MSG_TYPE_MISMATCH
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
    x2++;
    x3++;
    x4++;
    x5++;
    x6++;
    x7++;
    x8++;  //## issuekeys: MSG_TYPE_MISMATCH

    x1--;
    x2--;
    x3--;
    x4--;
    x5--;
    x6--;
    x7--;
    x8++;  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testOtherTypesInPostfix(){
    var x1 : BigDecimal = 0bd
    var x2 : BigDecimal = 0BD
    var x3 : BigInteger = 0bi
    var x4 : BigInteger = 0BI
    var x5 = 0x7b
    var x6 = -0X7F               //## KB(IDE-2536)
    var x7 : AtomicInteger = new AtomicInteger(0)
    var x8 : AtomicLong = new AtomicLong(0)
    var x9 : Object

    x1++;
    x2++;
    x3++;
    x4++;
    x5++;
    x6++;


    x1--;
    x2--;
    x3--;
    x4--;
    x5--;
    x6--;

    x7++;  //## issuekeys: MSG_TYPE_MISMATCH         //## KB(IDE-2537)
    x7--;  //## issuekeys: MSG_TYPE_MISMATCH         //## KB(IDE-2537)
    x8++;  //## issuekeys: MSG_TYPE_MISMATCH         //## KB(IDE-2537)
    x8--;  //## issuekeys: MSG_TYPE_MISMATCH         //## KB(IDE-2537)
    x9++;  //## issuekeys: MSG_TYPE_MISMATCH
    x9--;  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function test1DArrayElementInPostfix(){
    var array1D = {1,2,3}
    array1D[0]++;
    array1D[1]--;
  }

  function test2DArrayElementInPostfix(){
    var array2D = {{1,2},{3,4,5}}
    array2D[0][1]++;
    array2D[1][2]--;
  }

  function testListElementInPostfix(){
    var list = new ArrayList<Integer>(){1,2,3,4,5}
    list[3]++;
    list.get(3)++;  //## issuekeys: MSG_UNEXPECTED_TOKEN
  }

  function testMapElementInPostfix(){
    var map = new HashMap<Integer, Integer>(){1->10, 2->20}
    map[1]++
    map.get(1)++  //## issuekeys: MSG_UNEXPECTED_TOKEN
  }


  function test3DArrayElementInPostfix(){
    var array3D = {{{1,2,3},{4,5,6}},{{3,4,5}}}
    array3D[0][1][1]++;
    array3D[1][0][2]--;
  }

  function testEvaluateOnceInPostfix(){
    var arr : int[] = {1, 2}
    var i : int = 0
    var fun = \->{
      i++
      return 1
    }
    arr[fun()]++
  }


}