package gw.specContrib.typeinference

// IDE-4166

class Errant_DiamondInference {

    function hello<T>(t : T) : T { return null}

    function foo() {
      //FIXME IDE-4166 / GitHub #126
      var bar: ArrayList<Integer> = hello(new ArrayList())  // This is permitted, but there is a bug in the Gosu 1.14.7 compiler that flags it as an error
      var bar2: ArrayList<Integer> = hello(new ArrayList<String>( ))  //## issuekeys: The type "java.util.ArrayList<java.lang.String>" cannot be converted to "java.util.ArrayList<java.lang.Integer>"
    }

}
