package gw.internal.gosu.regression
uses gw.test.TestClass

class DGreenIllegalJumpIssueTest extends TestClass {

  function testContinueInWhile() {
     assertEquals( 0, continueInWhile() )
   }

   function testBreakInWhile() {
     assertEquals( 42, breakInWhile() )
   }

   function testContinueInDoWhile() {
     assertEquals( 0, continueInDoWhile() )
   }

   function testContinueInDoWhile2() {
     assertEquals( 42, continueInDoWhile2(false) )
   }

   function testBreakInWhile2() {
     assertEquals( 42, breakInWhile2() )
   }

   function testBreakInDoWhile2() {
     assertEquals( 42, breakInDoWhile2() )
   }

   function testReturnInIf() {
     var x : int[] = {0}
     returnInIf(x)
     assertEquals( 42, x[0] )
   }

   function testNop3() {
     assertEquals( 1, nop3() )
   }

   function testBreakInDoWhile() {
     assertEquals( 42, breakInDoWhile() )
   }

  function testStatementList() {
    assertEquals( 42, statementList() )
  }

  function statementList() : int {
    if (false) return 0
    while (true) {
      if (true) return 42
    }
  }

   function continueInWhile() : int {
     var i = 10
     while( true ) {
       i--
       if( i > 0 ) {
         continue
       }
       return i
     }
   }

   function breakInWhile() : int {
     var i = 10
     while( true ) {
       i--
       if( i > 0 ) {
         break
       }
       return i
     }
     return 42
   }

   function continueInDoWhile() : int {
     var i = 10
     do {
       i--
       if( i > 0 ) {
         continue
       }
       return i
     } while( true )
   }

   function breakInDoWhile() : int {
     var i = 10
     do {
       i--
       if( i > 0 ) {
         break
       }
       return i
     } while( true )
     return 42
   }

   function testNop0() {
     var i : int = 0
     var b = true
     while(i != 2) {
       i++;
       if(true) {
         continue
       }
       b = false
     }
     assertTrue(b)
   }

   function testNop1()  {
     var a : Set<Integer>  = {1, 2}
     var i = 0
     for (b in a) {
       i++
       return
     }
     assertEquals(1, i)
   }

   function testNop2()  {
     var a : Set<Integer> = {1, 2}
     var i = 0
     while(!a.Empty) {
       i++
       return
     }
     assertEquals(1, i)
   }


   function nop3() : int {
     var a : Set<Integer> = {1, 2}
     var i = 0
     do {
       i++
       return i
     }
     while(!a.Empty)
   }

   function breakInWhile2() : int {
     var i = 10
     while( i != 0 ) {
       if( i > 0 ) {
         break
       }
       if(true){ return i }
     }
     return 42
   }

   function breakInDoWhile2() : int {
     var i = 10
     do {
       i--
       if( i > 0 ) {
         break
       }
       return i
     } while( i != 0 )
     return 42
   }

   function returnInIf( x : int[]) {
     if(false) {
       return
     } else {
       x[0] = 42
     }
   }

   function continueInDoWhile2(b : boolean) : int {
     var i = 10
     do {
       i--
       if( i > 0 ) {
         continue
       }
       return i
     } while( b )
     return 42
   }
}