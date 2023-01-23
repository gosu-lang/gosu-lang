package gw.specContrib.classes.method_Scoring

class AvoidWrongNumberOfArgsErrorWhileScoring {

  function test() {
    var list = {"hi", "bye"}
    Collections.sort(list, Comparator.comparing<String, String>(\e-> e)) // no error
  }

}
