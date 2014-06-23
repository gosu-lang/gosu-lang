package gw.internal.gosu.compiler.sample.annotations

class SampleAnnotationHolder {

  //Functions

  @NoArgJavaAnnotation()
  function javaNoArgAnnotationsMethod() {}

  @NoArgGosuAnnotation()
  function gosuNoArgAnnotationsMethod() {}

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  function mixedNoArgAnnotationsMethod() {}

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  @NoArgGosuAnnotation()
  function mixedNoArgAnnotationsMethodWithTwoGosuAnnotations() {}

  @OneArgJavaAnnotation( "javaArg" )
  function javaOneArgAnnotationsMethod() {}

  @OneArgGosuAnnotation( "gosuArg" )
  function gosuOneArgAnnotationsMethod() {}

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg" )
  function mixedOneArgAnnotationsMethod() {}

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg1" )
  @OneArgGosuAnnotation( "gosuArg2" )
  function mixedOneArgAnnotationsMethodWithTwoGosuAnnotations() {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  function javaMultiArgAnnotationsMethod() {}

  @MultiArgGosuAnnotation( "gosuArg", 42 )
  function gosuMultiArgAnnotationsMethod() {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg", 42 )
  function mixedMultiArgAnnotationsMethod() {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg1", 42 )
  @MultiArgGosuAnnotation( "gosuArg2", 43 )
  function mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod() {}

  @AllTypesJavaAnnotation( true,
                           {true, false, true},
                           1 ,
                           {1 , 0 , 1 },
                           'a',
                           {'a', 'b', 'c'},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42.0,
                           {40.0, 41.0, 42.0},
                           42.0,
                           {40.0, 41.0, 42.0},
                           "foo",
                           {"foo", "bar", "doh"},
                           FIRST_VAL,
                           {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
  function allJavaAnnotationFieldTypesAnnotationMethod() {}

  @AnyArgGosuAnnotation( 1 + 1 )
  function whereExprIsAdditiveExprMethod() {}

  @AnyArgGosuAnnotation( 1 + 1 + 1 )
  function whereExprIsChainedAdditiveExprMethod() {}

  @AnyArgGosuAnnotation( "a" + "b" + "c" )
  function whereExprIsStrAdditiveExprMethod() {}

  @AnyArgGosuAnnotation( \-> "test" )
  function whereExprIsNoArgBlockMethod() {}

  @AnyArgGosuAnnotation( \ o : Object -> o )
  function whereExprIsOneArgBlockMethod() {}

  @AnyArgGosuAnnotation( \ o : Object -> {
    var x = "temp"
    print( x )
    if( true == o ) {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return false
        }
      }
    }  else {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return true
        }
      }
    }
  })
  function whereExprIsNoArgBlockWithProgramAndInnerClassMethod() {}

  @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
    override function call() : Object {
      return "From callable"
    }
  })
  function whereExprIsCallableMethod() {}

// TODO cgross - enable test
//  @AnyArgGosuAnnotation( "Test ${42}" )
//  function whereExprIsStringTemplateMethod() {}

  // Properties

  @NoArgJavaAnnotation()
  property get JavaNoArgAnnotationsProperty()  : Object { return null }

  @NoArgGosuAnnotation()
  property get GosuNoArgAnnotationsProperty()  : Object { return null }

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  property get MixedNoArgAnnotationsProperty()  : Object { return null }

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  @NoArgGosuAnnotation()
  property get MixedNoArgAnnotationsPropertyWithTwoGosuAnnotations()  : Object { return null }

  @OneArgJavaAnnotation( "javaArg" )
  property get JavaOneArgAnnotationsProperty()  : Object { return null }

  @OneArgGosuAnnotation( "gosuArg" )
  property get GosuOneArgAnnotationsProperty()  : Object { return null }

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg" )
  property get MixedOneArgAnnotationsProperty()  : Object { return null }

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg1" )
  @OneArgGosuAnnotation( "gosuArg2" )
  property get MixedOneArgAnnotationsPropertyWithTwoGosuAnnotations()  : Object { return null }

  @MultiArgJavaAnnotation( 42, "javaArg" )
  property get JavaMultiArgAnnotationsProperty()  : Object { return null }

  @MultiArgGosuAnnotation( "gosuArg", 42 )
  property get GosuMultiArgAnnotationsProperty()  : Object { return null }

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg", 42 )
  property get MixedMultiArgAnnotationsProperty()  : Object { return null }

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg1", 42 )
  @MultiArgGosuAnnotation( "gosuArg2", 43 )
  property get MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty()  : Object { return null }

  @AllTypesJavaAnnotation( true,
                           {true, false, true},
                           1 ,
                           {1 , 0 , 1 },
                           'a',
                           {'a', 'b', 'c'},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42.0,
                           {40.0, 41.0, 42.0},
                           42.0,
                           {40.0, 41.0, 42.0},
                           "foo",
                           {"foo", "bar", "doh"},
                           FIRST_VAL,
                           {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
  property get AllJavaAnnotationFieldTypesAnnotationProperty()  : Object { return null }

  @AnyArgGosuAnnotation( 1 + 1 )
  property get WhereExprIsAdditiveExprProperty()  : Object { return null }

  @AnyArgGosuAnnotation( 1 + 1 + 1 )
  property get WhereExprIsChainedAdditiveExprProperty()  : Object { return null }

  @AnyArgGosuAnnotation( "a" + "b" + "c" )
  property get WhereExprIsStrAdditiveExprProperty()  : Object { return null }

  @AnyArgGosuAnnotation( \-> "test" )
  property get WhereExprIsNoArgBlockProperty()  : Object { return null }

  @AnyArgGosuAnnotation( \ o : Object -> o )
  property get WhereExprIsOneArgBlockProperty()  : Object { return null }

  @AnyArgGosuAnnotation( \ o : Object -> {
    var x = "temp"
    print( x )
    if( true == o ) {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return false
        }
      }
    }  else {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return true
        }
      }
    }
  })
  property get WhereExprIsNoArgBlockWithProgramAndInnerClassProperty()  : Object { return null }

  @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
    override function call() : Object {
      return "From callable"
    }
  })
  property get WhereExprIsCallableProperty()  : Object { return null }

// TODO cgross - enable test
//  @AnyArgGosuAnnotation( "Test ${42}" )
//  property get WhereExprIsStringTemplateProperty()  : Object { return null }

  // Constructors
   class Class1 {}
   class Class2 {}
   class Class3 {}
   class Class4 {}
   class Class5 {}
   class Class6 {}
   class Class7 {}
   class Class8 {}
   class Class9 {}
   class Class10 {}
   class Class11 {}
   class Class12 {}
   class Class13 {}
   class Class14 {}
   class Class15 {}
   class Class16 {}
   class Class17 {}
   class Class18 {}
   class Class19 {}
   class Class20 {}
   class Class21 {}

  construct() {} // just so the thing can be created...

  @NoArgJavaAnnotation()
  construct( c : Class1 ) {}

  @NoArgGosuAnnotation()
  construct( c : Class2 ) {}

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  construct( c : Class3 ) {}

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  @NoArgGosuAnnotation()
  construct( c : Class4 ) {}

  @OneArgJavaAnnotation( "javaArg" )
  construct( c : Class5 ) {}

  @OneArgGosuAnnotation( "gosuArg" )
  construct( c : Class6 ) {}

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg" )
  construct( c : Class7 ) {}

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg1" )
  @OneArgGosuAnnotation( "gosuArg2" )
  construct( c : Class8 ) {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  construct( c : Class9 ) {}

  @MultiArgGosuAnnotation( "gosuArg", 42 )
  construct( c : Class10 ) {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg", 42 )
  construct( c : Class11 ) {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg1", 42 )
  @MultiArgGosuAnnotation( "gosuArg2", 43 )
  construct( c : Class12 ) {}

  @AllTypesJavaAnnotation( true,
                           {true, false, true},
                           1 ,
                           {1 , 0 , 1 },
                           'a',
                           {'a', 'b', 'c'},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42.0,
                           {40.0, 41.0, 42.0},
                           42.0,
                           {40.0, 41.0, 42.0},
                           "foo",
                           {"foo", "bar", "doh"},
                           FIRST_VAL,
                           {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
  construct( c : Class13 ) {}

  @AnyArgGosuAnnotation( 1 + 1 )
  construct( c : Class14 ) {}

  @AnyArgGosuAnnotation( 1 + 1 + 1 )
  construct( c : Class15 ) {}

  @AnyArgGosuAnnotation( "a" + "b" + "c" )
  construct( c : Class16 ) {}

  @AnyArgGosuAnnotation( \-> "test" )
  construct( c : Class17 ) {}

  @AnyArgGosuAnnotation( \ o : Object -> o )
  construct( c : Class18 ) {}

  @AnyArgGosuAnnotation( \ o : Object -> {
    var x = "temp"
    print( x )
    if( true == o ) {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return false
        }
      }
    }  else {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return true
        }
      }
    }
  })
  construct( c : Class19 ) {}

  @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
    override function call() : Object {
      return "From callable"
    }
  })
  construct( c : Class20 ) {}

// TODO cgross - enable test
//  @AnyArgGosuAnnotation( "Test ${42}" )
//  construct( c : Class21 ) {}

  // Vars

  @NoArgJavaAnnotation()
  var JavaNoArgAnnotationsVar : Object 

  @NoArgGosuAnnotation()
  var GosuNoArgAnnotationsVar : Object 

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  var MixedNoArgAnnotationsVar : Object 

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  @NoArgGosuAnnotation()
  var MixedNoArgAnnotationsVarWithTwoGosuAnnotations() : Object 

  @OneArgJavaAnnotation( "javaArg" )
  var JavaOneArgAnnotationsVar : Object 

  @OneArgGosuAnnotation( "gosuArg" )
  var GosuOneArgAnnotationsVar : Object 

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg" )
  var MixedOneArgAnnotationsVar : Object 

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg1" )
  @OneArgGosuAnnotation( "gosuArg2" )
  var MixedOneArgAnnotationsVarWithTwoGosuAnnotations() : Object 

  @MultiArgJavaAnnotation( 42, "javaArg" )
  var JavaMultiArgAnnotationsVar : Object 

  @MultiArgGosuAnnotation( "gosuArg", 42 )
  var GosuMultiArgAnnotationsVar : Object 

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg", 42 )
  var MixedMultiArgAnnotationsVar : Object 

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg1", 42 )
  @MultiArgGosuAnnotation( "gosuArg2", 43 )
  var MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar : Object 

  @AllTypesJavaAnnotation( true,
                           {true, false, true},
                           1 ,
                           {1 , 0 , 1 },
                           'a',
                           {'a', 'b', 'c'},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42.0,
                           {40.0, 41.0, 42.0},
                           42.0,
                           {40.0, 41.0, 42.0},
                           "foo",
                           {"foo", "bar", "doh"},
                           FIRST_VAL,
                           {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
  var AllJavaAnnotationFieldTypesAnnotationVar : Object 

  @AnyArgGosuAnnotation( 1 + 1 )
  var WhereExprIsAdditiveExprVar : Object 

  @AnyArgGosuAnnotation( 1 + 1 + 1 )
  var WhereExprIsChainedAdditiveExprVar : Object 

  @AnyArgGosuAnnotation( "a" + "b" + "c" )
  var WhereExprIsStrAdditiveExprVar : Object 

  @AnyArgGosuAnnotation( \-> "test" )
  var WhereExprIsNoArgBlockVar : Object 

  @AnyArgGosuAnnotation( \ o : Object -> o )
  var WhereExprIsOneArgBlockVar : Object 

  @AnyArgGosuAnnotation( \ o : Object -> {
    var x = "temp"
    print( x )
    if( true == o ) {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return false
        }
      }
    }  else {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return true
        }
      }
    }
  })
  var WhereExprIsNoArgBlockWithProgramAndInnerClassVar : Object 

  @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
    override function call() : Object {
      return "From callable"
    }
  })
  var WhereExprIsCallableVar : Object 

// TODO cgross - enable test
//  @AnyArgGosuAnnotation( "Test ${42}" )
//  var _WhereExprIsStringTemplateVar : Object 


  //Classes

  @NoArgJavaAnnotation()
  class JavaNoArgAnnotationsClass {}

  @NoArgGosuAnnotation()
  class GosuNoArgAnnotationsClass {}

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  class MixedNoArgAnnotationsClass {}

  @NoArgJavaAnnotation()
  @NoArgGosuAnnotation()
  @NoArgGosuAnnotation()
  class MixedNoArgAnnotationsClassWithTwoGosuAnnotationsClass {}

  @OneArgJavaAnnotation( "javaArg" )
  class JavaOneArgAnnotationsClass {}

  @OneArgGosuAnnotation( "gosuArg" )
  class GosuOneArgAnnotationsClass {}

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg" )
  class MixedOneArgAnnotationsClass {}

  @OneArgJavaAnnotation( "javaArg" )
  @OneArgGosuAnnotation( "gosuArg1" )
  @OneArgGosuAnnotation( "gosuArg2" )
  class MixedOneArgAnnotationsClassWithTwoGosuAnnotationsClass {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  class JavaMultiArgAnnotationsClass {}

  @MultiArgGosuAnnotation( "gosuArg", 42 )
  class GosuMultiArgAnnotationsClass {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg", 42 )
  class MixedMultiArgAnnotationsClass {}

  @MultiArgJavaAnnotation( 42, "javaArg" )
  @MultiArgGosuAnnotation( "gosuArg1", 42 )
  @MultiArgGosuAnnotation( "gosuArg2", 43 )
  class MixedMultiArgAnnotationsClassWithTwoGosuAnnotationsClass {}

  @AllTypesJavaAnnotation( true,
                           {true, false, true},
                           1 ,
                           {1 , 0 , 1 },
                           'a',
                           {'a', 'b', 'c'},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42,
                           {40, 41, 42},
                           42.0,
                           {40.0, 41.0, 42.0},
                           42.0,
                           {40.0, 41.0, 42.0},
                           "foo",
                           {"foo", "bar", "doh"},
                           FIRST_VAL,
                           {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
  class AllJavaAnnotationFieldTypesAnnotationClass {}

  @AnyArgGosuAnnotation( 1 + 1 )
  class WhereExprIsAdditiveExprClass {}

  @AnyArgGosuAnnotation( 1 + 1 + 1 )
  class WhereExprIsChainedAdditiveExprClass {}

  @AnyArgGosuAnnotation( "a" + "b" + "c" )
  class WhereExprIsStrAdditiveExprClass {}

  @AnyArgGosuAnnotation( \-> "test" )
  class WhereExprIsNoArgBlockClass {}

  @AnyArgGosuAnnotation( \ o : Object -> o )
  class WhereExprIsOneArgBlockClass {}

  @AnyArgGosuAnnotation( \ o : Object -> {
    var x = "temp"
    print( x )
    if( true == o ) {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return false
        }
      }
    }  else {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return true
        }
      }
    }
  })
  class WhereExprIsNoArgBlockWithProgramAndInnerClassClass {}

  @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
    override function call() : Object {
      return "From callable"
    }
  })
  class WhereExprIsCallableClass {}

  function getAnnotatedAnonymousInnerClass() : Object {
    return new Object() {
      //Functions
      @MultiArgJavaAnnotation( 42, "javaArg" )
      @MultiArgGosuAnnotation( "gosuArg1", 42 )
      @MultiArgGosuAnnotation( "gosuArg2", 43 )
      function anon_mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod() {}

      @AllTypesJavaAnnotation( true,
                               {true, false, true},
                               1 ,
                               {1 , 0 , 1 },
                               'a',
                               {'a', 'b', 'c'},
                               42,
                               {40, 41, 42},
                               42,
                               {40, 41, 42},
                               42,
                               {40, 41, 42},
                               42.0,
                               {40.0, 41.0, 42.0},
                               42.0,
                               {40.0, 41.0, 42.0},
                               "foo",
                               {"foo", "bar", "doh"},
                               FIRST_VAL,
                               {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
      function anon_allJavaAnnotationFieldTypesAnnotationMethod() {}

      @AnyArgGosuAnnotation( \-> "test" )
      function anon_whereExprIsNoArgBlockMethod() {}

      @AnyArgGosuAnnotation( \ o : Object -> o )
      function anon_whereExprIsOneArgBlockMethod() {}

      @AnyArgGosuAnnotation( \ o : Object -> {
        var x = "temp"
        print( x )
        if( true == o ) {
          return new java.util.concurrent.Callable() {
            override function call() : Object {
              return false
            }
          }
        }  else {
          return new java.util.concurrent.Callable() {
            override function call() : Object {
              return true
            }
          }
        }
      })
      function anon_whereExprIsNoArgBlockWithProgramAndInnerClassMethod() {}

      @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
        override function call() : Object {
          return "From callable"
        }
      })
      function anon_whereExprIsCallableMethod() {}

      // TODO cgross - enable test
      //  @AnyArgGosuAnnotation( "Test ${42}" )
      //  function whereExprIsStringTemplateMethod() {}

      // Properties

      @MultiArgJavaAnnotation( 42, "javaArg" )
      @MultiArgGosuAnnotation( "gosuArg1", 42 )
      @MultiArgGosuAnnotation( "gosuArg2", 43 )
      property get anon_MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty()  : Object { return null }

      @AllTypesJavaAnnotation( true,
                               {true, false, true},
                               1 ,
                               {1 , 0 , 1 },
                               'a',
                               {'a', 'b', 'c'},
                               42,
                               {40, 41, 42},
                               42,
                               {40, 41, 42},
                               42,
                               {40, 41, 42},
                               42.0,
                               {40.0, 41.0, 42.0},
                               42.0,
                               {40.0, 41.0, 42.0},
                               "foo",
                               {"foo", "bar", "doh"},
                               FIRST_VAL,
                               {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
      property get anon_AllJavaAnnotationFieldTypesAnnotationProperty()  : Object { return null }

      @AnyArgGosuAnnotation( \-> "test" )
      property get anon_WhereExprIsNoArgBlockProperty()  : Object { return null }

      @AnyArgGosuAnnotation( \ o : Object -> o )
      property get anon_WhereExprIsOneArgBlockProperty()  : Object { return null }

      @AnyArgGosuAnnotation( \ o : Object -> {
        var x = "temp"
        print( x )
        if( true == o ) {
          return new java.util.concurrent.Callable() {
            override function call() : Object {
              return false
            }
          }
        }  else {
          return new java.util.concurrent.Callable() {
            override function call() : Object {
              return true
            }
          }
        }
      })
      property get anon_WhereExprIsNoArgBlockWithProgramAndInnerClassProperty()  : Object { return null }

      @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
        override function call() : Object {
          return "From callable"
        }
      })
      property get anon_WhereExprIsCallableProperty()  : Object { return null }

      // TODO cgross - enable test
      //  @AnyArgGosuAnnotation( "Test ${42}" )
      //  property get WhereExprIsStringTemplateProperty()  : Object { return null }

      // Vars

      @MultiArgJavaAnnotation( 42, "javaArg" )
      @MultiArgGosuAnnotation( "gosuArg1", 42 )
      @MultiArgGosuAnnotation( "gosuArg2", 43 )
      var anon_MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar : Object

      @AllTypesJavaAnnotation( true,
                               {true, false, true},
                               1 ,
                               {1 , 0 , 1 },
                               'a',
                               {'a', 'b', 'c'},
                               42,
                               {40, 41, 42},
                               42,
                               {40, 41, 42},
                               42,
                               {40, 41, 42},
                               42.0,
                               {40.0, 41.0, 42.0},
                               42.0,
                               {40.0, 41.0, 42.0},
                               "foo",
                               {"foo", "bar", "doh"},
                               FIRST_VAL,
                               {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
      var anon_AllJavaAnnotationFieldTypesAnnotationVar : Object

      @AnyArgGosuAnnotation( \-> "test" )
      var anon_WhereExprIsNoArgBlockVar : Object

      @AnyArgGosuAnnotation( \ o : Object -> o )
      var anon_WhereExprIsOneArgBlockVar : Object

      @AnyArgGosuAnnotation( \ o : Object -> {
        var x = "temp"
        print( x )
        if( true == o ) {
          return new java.util.concurrent.Callable() {
            override function call() : Object {
              return false
            }
          }
        }  else {
          return new java.util.concurrent.Callable() {
            override function call() : Object {
              return true
            }
          }
        }
      })
      var anon_WhereExprIsNoArgBlockWithProgramAndInnerClassVar : Object

      @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
        override function call() : Object {
          return "From callable"
        }
      })
      var anon_WhereExprIsCallableVar : Object

      // TODO cgross - enable test
      //  @AnyArgGosuAnnotation( "Test ${42}" )
      //  var anon_WhereExprIsStringTemplateVar : Object
    }
  }

  function getAnnotatedInnerClass() : Object {
    return new InnerAnnotationHolder()
  }

  class InnerAnnotationHolder {

    //Functions
    @MultiArgJavaAnnotation( 42, "javaArg" )
    @MultiArgGosuAnnotation( "gosuArg1", 42 )
    @MultiArgGosuAnnotation( "gosuArg2", 43 )
    function inner_mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod() {}

    @AllTypesJavaAnnotation( true,
                             {true, false, true},
                             1 ,
                             {1 , 0 , 1 },
                             'a',
                             {'a', 'b', 'c'},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42.0,
                             {40.0, 41.0, 42.0},
                             42.0,
                             {40.0, 41.0, 42.0},
                             "foo",
                             {"foo", "bar", "doh"},
                             FIRST_VAL,
                             {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
    function inner_allJavaAnnotationFieldTypesAnnotationMethod() {}

    @AnyArgGosuAnnotation( \-> "test" )
    function inner_whereExprIsNoArgBlockMethod() {}

    @AnyArgGosuAnnotation( \ o : Object -> o )
    function inner_whereExprIsOneArgBlockMethod() {}

    @AnyArgGosuAnnotation( \ o : Object -> {
      var x = "temp"
      print( x )
      if( true == o ) {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return false
          }
        }
      }  else {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return true
          }
        }
      }
    })
    function inner_whereExprIsNoArgBlockWithProgramAndInnerClassMethod() {}

    @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
      override function call() : Object {
        return "From callable"
      }
    })
    function inner_whereExprIsCallableMethod() {}

    // TODO cgross - enable test
    //  @AnyArgGosuAnnotation( "Test ${42}" )
    //  function whereExprIsStringTemplateMethod() {}

    // Properties

    @MultiArgJavaAnnotation( 42, "javaArg" )
    @MultiArgGosuAnnotation( "gosuArg1", 42 )
    @MultiArgGosuAnnotation( "gosuArg2", 43 )
    property get inner_MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty()  : Object { return null }

    @AllTypesJavaAnnotation( true,
                             {true, false, true},
                             1 ,
                             {1 , 0 , 1 },
                             'a',
                             {'a', 'b', 'c'},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42.0,
                             {40.0, 41.0, 42.0},
                             42.0,
                             {40.0, 41.0, 42.0},
                             "foo",
                             {"foo", "bar", "doh"},
                             FIRST_VAL,
                             {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
    property get inner_AllJavaAnnotationFieldTypesAnnotationProperty()  : Object { return null }

    @AnyArgGosuAnnotation( \-> "test" )
    property get inner_WhereExprIsNoArgBlockProperty()  : Object { return null }

    @AnyArgGosuAnnotation( \ o : Object -> o )
    property get inner_WhereExprIsOneArgBlockProperty()  : Object { return null }

    @AnyArgGosuAnnotation( \ o : Object -> {
      var x = "temp"
      print( x )
      if( true == o ) {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return false
          }
        }
      }  else {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return true
          }
        }
      }
    })
    property get inner_WhereExprIsNoArgBlockWithProgramAndInnerClassProperty()  : Object { return null }

    @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
      override function call() : Object {
        return "From callable"
      }
    })
    property get inner_WhereExprIsCallableProperty()  : Object { return null }

    // TODO cgross - enable test
    //  @AnyArgGosuAnnotation( "Test ${42}" )
    //  property get WhereExprIsStringTemplateProperty()  : Object { return null }

    // Vars

    @MultiArgJavaAnnotation( 42, "javaArg" )
    @MultiArgGosuAnnotation( "gosuArg1", 42 )
    @MultiArgGosuAnnotation( "gosuArg2", 43 )
    var inner_MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar : Object

    @AllTypesJavaAnnotation( true,
                             {true, false, true},
                             1 ,
                             {1 , 0 , 1 },
                             'a',
                             {'a', 'b', 'c'},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42.0,
                             {40.0, 41.0, 42.0},
                             42.0,
                             {40.0, 41.0, 42.0},
                             "foo",
                             {"foo", "bar", "doh"},
                             FIRST_VAL,
                             {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
    var inner_AllJavaAnnotationFieldTypesAnnotationVar : Object

    @AnyArgGosuAnnotation( \-> "test" )
    var inner_WhereExprIsNoArgBlockVar : Object

    @AnyArgGosuAnnotation( \ o : Object -> o )
    var inner_WhereExprIsOneArgBlockVar : Object

    @AnyArgGosuAnnotation( \ o : Object -> {
      var x = "temp"
      print( x )
      if( true == o ) {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return false
          }
        }
      }  else {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return true
          }
        }
      }
    })
    var inner_WhereExprIsNoArgBlockWithProgramAndInnerClassVar : Object

    @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
      override function call() : Object {
        return "From callable"
      }
    })
    var inner_WhereExprIsCallableVar : Object

    // TODO cgross - enable test
    //  @AnyArgGosuAnnotation( "Test ${42}" )
    //  var inner_WhereExprIsStringTemplateVar : Object

  }

  static class StaticInnerAnnotationHolder {

    //Functions
    @MultiArgJavaAnnotation( 42, "javaArg" )
    @MultiArgGosuAnnotation( "gosuArg1", 42 )
    @MultiArgGosuAnnotation( "gosuArg2", 43 )
    function static_inner_mixedMultiArgAnnotationsMethodWithTwoGosuAnnotationsMethod() {}

    @AllTypesJavaAnnotation( true,
                             {true, false, true},
                             1 ,
                             {1 , 0 , 1 },
                             'a',
                             {'a', 'b', 'c'},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42.0,
                             {40.0, 41.0, 42.0},
                             42.0,
                             {40.0, 41.0, 42.0},
                             "foo",
                             {"foo", "bar", "doh"},
                             FIRST_VAL,
                             {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
    function static_inner_allJavaAnnotationFieldTypesAnnotationMethod() {}

    @AnyArgGosuAnnotation( \-> "test" )
    function static_inner_whereExprIsNoArgBlockMethod() {}

    @AnyArgGosuAnnotation( \ o : Object -> o )
    function static_inner_whereExprIsOneArgBlockMethod() {}

    @AnyArgGosuAnnotation( \ o : Object -> {
      var x = "temp"
      print( x )
      if( true == o ) {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return false
          }
        }
      }  else {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return true
          }
        }
      }
    })
    function static_inner_whereExprIsNoArgBlockWithProgramAndInnerClassMethod() {}

    @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
      override function call() : Object {
        return "From callable"
      }
    })
    function static_inner_whereExprIsCallableMethod() {}

    // TODO cgross - enable test
    //  @AnyArgGosuAnnotation( "Test ${42}" )
    //  function whereExprIsStringTemplateMethod() {}

    // Properties

    @MultiArgJavaAnnotation( 42, "javaArg" )
    @MultiArgGosuAnnotation( "gosuArg1", 42 )
    @MultiArgGosuAnnotation( "gosuArg2", 43 )
    property get static_inner_MixedMultiArgAnnotationsPropertyWithTwoGosuAnnotationsProperty()  : Object { return null }

    @AllTypesJavaAnnotation( true,
                             {true, false, true},
                             1 ,
                             {1 , 0 , 1 },
                             'a',
                             {'a', 'b', 'c'},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42.0,
                             {40.0, 41.0, 42.0},
                             42.0,
                             {40.0, 41.0, 42.0},
                             "foo",
                             {"foo", "bar", "doh"},
                             FIRST_VAL,
                             {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
    property get static_inner_AllJavaAnnotationFieldTypesAnnotationProperty()  : Object { return null }

    @AnyArgGosuAnnotation( \-> "test" )
    property get static_inner_WhereExprIsNoArgBlockProperty()  : Object { return null }

    @AnyArgGosuAnnotation( \ o : Object -> o )
    property get static_inner_WhereExprIsOneArgBlockProperty()  : Object { return null }

    @AnyArgGosuAnnotation( \ o : Object -> {
      var x = "temp"
      print( x )
      if( true == o ) {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return false
          }
        }
      }  else {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return true
          }
        }
      }
    })
    property get static_inner_WhereExprIsNoArgBlockWithProgramAndInnerClassProperty()  : Object { return null }

    @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
      override function call() : Object {
        return "From callable"
      }
    })
    property get static_inner_WhereExprIsCallableProperty()  : Object { return null }

    // TODO cgross - enable test
    //  @AnyArgGosuAnnotation( "Test ${42}" )
    //  property get WhereExprIsStringTemplateProperty()  : Object { return null }

    // Vars

    @MultiArgJavaAnnotation( 42, "javaArg" )
    @MultiArgGosuAnnotation( "gosuArg1", 42 )
    @MultiArgGosuAnnotation( "gosuArg2", 43 )
    var static_inner_MixedMultiArgAnnotationsVarWithTwoGosuAnnotationsVar : Object

    @AllTypesJavaAnnotation( true,
                             {true, false, true},
                             1 ,
                             {1 , 0 , 1 },
                             'a',
                             {'a', 'b', 'c'},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42,
                             {40, 41, 42},
                             42.0,
                             {40.0, 41.0, 42.0},
                             42.0,
                             {40.0, 41.0, 42.0},
                             "foo",
                             {"foo", "bar", "doh"},
                             FIRST_VAL,
                             {FIRST_VAL, SECOND_VAL, THIRD_VAL} )
    var static_inner_AllJavaAnnotationFieldTypesAnnotationVar : Object

    @AnyArgGosuAnnotation( \-> "test" )
    var static_inner_WhereExprIsNoArgBlockVar : Object

    @AnyArgGosuAnnotation( \ o : Object -> o )
    var static_inner_WhereExprIsOneArgBlockVar : Object

    @AnyArgGosuAnnotation( \ o : Object -> {
      var x = "temp"
      print( x )
      if( true == o ) {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return false
          }
        }
      }  else {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return true
          }
        }
      }
    })
    var static_inner_WhereExprIsNoArgBlockWithProgramAndInnerClassVar : Object

    @AnyArgGosuAnnotation( new java.util.concurrent.Callable() {
      override function call() : Object {
        return "From callable"
      }
    })
    var static_inner_WhereExprIsCallableVar : Object

    // TODO cgross - enable test
    //  @AnyArgGosuAnnotation( "Test ${42}" )
    //  var static_inner_WhereExprIsStringTemplateVar : Object

  }

}