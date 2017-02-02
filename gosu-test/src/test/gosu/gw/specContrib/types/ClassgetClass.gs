package gw.specContrib.types

class ClassgetClass {

  var test1 = new ClassgetClass()

  function make<T extends ClassgetClass>(type: Type<T>) {
    make(test1.getClass()) // Correct: No error from compiler or Studio
    make(test1.Class) // Correct: No error from compiler or Studio
  }

  function f() {
    var s = "hello"
    print(String.Class)
    print(String.getClass())
    print(s.Class)
    print(s.getClass())
  }

}
