package gw.specification.comments

class Errant_CommentsTest {
  function hello() {
    /**/
    /***/
    /*/**/*/
    /*/***/*/
    /* hello */
    /* * hello */
    /* /* hello */ */
    /* /* * hello * */ */

    /* hello
             */
    /* * hello
               */
    /*
        /* hello */
    */
    /*
          /*
              // hello
              * hello *
                        */

                                 */
    var a = 0   // hello
  }


/*
  /** Doc first line
  *
   *
   * Second
   *
   *
   * Third
   * */
  function docMe() {

  }
*/

  function world(x : boolean) {
    // if x is true
    if(x) {
      print("Hello!") /* print */
    } else {
      /* x is not true
         let's print world
       */
      print("world")
    }

    /*
    // if x is true
    if(x) {
      print("Hello!") /* print */
    } else {
      /* x is not true
         let's print world
       */
      print("world")
    }
    */
    x = true
  }
}
