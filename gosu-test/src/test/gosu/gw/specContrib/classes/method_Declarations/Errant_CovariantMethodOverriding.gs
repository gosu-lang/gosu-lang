package gw.specContrib.classes.method_Declarations

class Errant_CovariantMethodOverriding {
  class GosuClass implements Comparable<String> {      //## issuekeys: CLASS 'GOSUCLASS' MUST EITHER BE DECLARED ABSTRACT OR IMPLEMENT ABSTRACT METHOD 'COMPARETO(T)' IN 'COMPARABLE'
    function compareTo(o: CharSequence): int {
      return 0
    }
  }
}