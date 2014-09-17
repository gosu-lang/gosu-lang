package gw.specContrib.typeinference

class Errant_SwitchTypeNarrowing {
  private enum MyEnum  { ONE, TWO }

  private function foo1(x: Object) {
    switch (typeof(x)) {
      case String:
      default:
        x.contains("ne")         //## issuekeys: MSG_
    }
  }

  private function foo2(x: Object) {
    switch (typeof(x)) {
      case MyEnum:
        switch (x) {
          case ONE:
            return
        }
      case String:
        x.contains("ne")         //## issuekeys: MSG_
    }
  }

  private function foo3(x: Object) {
    switch (typeof(x)) {
      case MyEnum:
        if (x == ONE) {
          break
        }
      case String:
        x.contains("ne")         //## issuekeys: MSG_
    }
  }

  private function foo4(x: Object) {
    switch (typeof(x)) {
      case MyEnum:
        print("Enum")
      case String:
        x.contains("ne")         //## issuekeys: MSG_
    }
  }
}