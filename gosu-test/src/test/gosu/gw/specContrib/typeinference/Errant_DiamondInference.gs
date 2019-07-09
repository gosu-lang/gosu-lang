package gw.specContrib.typeinference

// IDE-4166

class Errant_DiamondInference {

    function hello<T>(t : T) : T { return null}

    function foo() {
      var bar: ArrayList<Integer> = hello(new ArrayList())  // Okay
      var bar2: ArrayList<Integer> = hello(new ArrayList<String>( ))  //## issuekeys: The type "java.util.ArrayList<java.lang.String>" cannot be converted to "java.util.ArrayList<java.lang.Integer>"
    }

}
