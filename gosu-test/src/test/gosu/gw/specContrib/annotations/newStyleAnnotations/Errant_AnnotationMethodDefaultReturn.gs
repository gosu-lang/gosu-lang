package gw.specContrib.annotations.newStyleAnnotations

class Errant_AnnotationMethodDefaultReturn {

  annotation MyAnnotationA1 {
    function foo3(): String = "Sdf" {
      return null
    }
  }

  interface MyInterface1 {
    function foo3(): String = "Sdf" { return null }      //## issuekeys: UNEXPECTED TOKENS
  }
}