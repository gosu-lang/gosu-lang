package gw.internal.gosu.compiler.sample.expression.blocks

class SimpleBlocks {

  function mostBasicBlock() : String {
    var blk = \-> "yay"
    return blk()
  }

  function blockWithOneArg() : String {
    var blk = \ s : String -> s
    return blk( "yay" )
  }

  function blockWithCapturedSymbol() : String {
    var str = "yay"
    var blk = \-> str
    return blk()
  }

  function argIsProperlyDowncast() : String {
    var blk = \ strList : java.util.List<String> -> strList.get(0)
    return blk( {"yay"} )
  }

  function argIsProperlyDowncast2() : String {
    var blk = \ str : String -> str.substring( 0 )
    return blk( "yay" )
  }

  function nestedBlocksWork1() : String {
    var blk = \ str : String -> \-> str
    var blk2 = blk( "yay" )
    return blk2()
  }

  function nestedBlocksWork2() : String {
    var str = "yay"
    var blk = \-> \-> str
    var blk2 = blk()
    return blk2()
  }

  function nestedBlocksWork3() : String {
    var blk = \ s1 : String -> \ s2 : String-> s1 + s2
    var blk2 = blk("y")
    return blk2("ay")
  }

  function mostBasicBlockInt() : int {
    var blk = \-> 42
    return blk()
  }

  function blockWithOneArgInt() : int {
    var blk = \ s : int -> s
    return blk( 42 )
  }

  function blockWithCapturedSymbolInt() : int {
    var i = 42
    var blk = \-> i
    return blk()
  }

  function argIsProperlyDowncastInt() : int {
    var blk = \ intArr : int[] -> intArr.length
    return blk( new int[42] )
  }

  function argIsProperlyDowncast2Int() : int {
    var blk = \ i : int -> i + i
    return blk( 21 )
  }

  function nestedBlocksWork1Int() : int {
    var blk = \ i : int -> \-> i
    var blk2 = blk( 42 )
    return blk2()
  }

  function nestedBlocksWork2Int() : int {
    var i = 42
    var blk = \-> \-> 42
    var blk2 = blk()
    return blk2()
  }

  function nestedBlocksWork3Int() : int {
    var blk = \ i1 : int -> \ i2 : int-> i1 + i2
    var blk2 = blk( 21 )
    return blk2( 21 )
  }

  function mostBasicBlockDouble() : double {
    var blk = \-> 42.0
    return blk()
  }

  function blockWithOneArgDouble() : double {
    var blk = \ s : double -> s
    return blk( 42.0 )
  }

  function blockWithCapturedSymbolDouble() : double {
    var i = 42.0
    var blk = \-> i
    return blk()
  }

  function argIsProperlyDowncastDouble() : double {
    var blk = \ doubleArr : double[] -> doubleArr.length
    return blk( new double[42] )
  }

  function argIsProperlyDowncast2Double() : double {
    var blk = \ i : double -> i + i
    return blk( 21 )
  }

  function nestedBlocksWork1Double() : double {
    var blk = \ i : double -> \-> i
    var blk2 = blk( 42.0 )
    return blk2()
  }

  function nestedBlocksWork2Double() : double {
    var i = 42.0
    var blk = \-> \-> 42.0
    var blk2 = blk()
    return blk2()
  }

  function nestedBlocksWork3Double() : double {
    var blk = \ i1 : double -> \ i2 : double-> i1 + i2
    var blk2 = blk( 21.0 )
    return blk2( 21.0 )
  }

  function testBlockWithMultiArgsOfDifferingTypes() : String {
    var blk = \ i : int, s : String, d : double -> s + (i + (d as int))
    return blk( 40, "The answer is ", 2.0 )
  }

  function testDifferentTypeCapture1() : String {
    var i = 40
    var blk = \ s : String, d : double -> s + (i + (d as int))
    return blk( "The answer is ", 2.0 )
  }

  function testDifferentTypeCapture2() : String {
    var i = 40
    var s = "The answer is "
    var blk = \ d : double -> s + (i + (d as int))
    return blk( 2.0 )
  }

  function testDifferentTypeCapture3() : String {
    var i = 40
    var s = "The answer is "
    var d = 2.0
    var blk = \-> s + (i + (d as int))
    return blk()
  }

  function testDifferentTypeCapture4() : String {
    var s = "The answer is "
    var blk = \ i : int, d : double -> s + (i + (d as int))
    return blk( 40, 2.0 )
  }

  function testDifferentTypeCapture5() : String {
    var s = "The answer is "
    var d = 2.0
    var blk = \ i : int -> s + (i + (d as int))
    return blk( 40 )
  }

  function testDifferentTypeCapture6() : String {
    var d = 2.0
    var blk = \ i : int, s : String -> s + (i + (d as int))
    return blk( 40, "The answer is " )
  }

  function callMethodThatCallsBlockNoCapture() : String {
    return callsBlock( \-> "yay" )
  }

  function callMethodThatCallsBlockWithCapture() : String {
    var str = "yay"
    return callsBlock( \-> str )
  }

  function callMethodThatCallsBlockWithCaptureUsingSideEffects() : String {
    var str = "boo"
    callsBlock( \-> { str = "yay"; return str } )
    return str
  }

  function callMethodThatCallsObjBlockNoCapture() : String {
    return callsBlock2( \-> "yay" ) as String
  }

  function callMethodThatCallsObjBlockWithCapture() : String {
    var str = "yay"
    return callsBlock2( \-> str ) as String
  }

  function callMethodThatCallsObjBlockWithCaptureUsingSideEffects() : String {
    var str = "boo"
    callsBlock2( \-> { str = "yay"; return str } )
    return str
  }

  function callMethodThatCallsGenericfuncBlockNoCapture() : String {
    return genericCallsBlock( \-> "yay" )
  }

  function callMethodThatCallsGenericfuncBlockWithCapture() : String {
    var str = "yay"
    return genericCallsBlock( \-> str )
  }

  function callMethodThatCallsGenericfuncBlockWithCaptureUsingSideEffects() : String {
    var str = "boo"
    genericCallsBlock( \-> { str = "yay"; return str } )
    return str
  }

  function callMethodThatCallsBlockNoCaptureInt() : int {
    return callsBlock( \-> 42 as String ).toInt()
  }

  function callMethodThatCallsBlockWithCaptureInt() : int {
    var i = 42
    return callsBlock( \-> i as String ).toInt()
  }

  function callMethodThatCallsBlockWithCaptureUsingSideEffectsInt() : int {
    var i = 0
    callsBlock( \-> { i = 42; return i as String } )
    return i
  }

  function callMethodThatCallsObjBlockNoCaptureInt() : int {
    return callsBlock2( \-> 42 ) as java.lang.Integer
  }

  function callMethodThatCallsObjBlockWithCaptureInt() : int {
    var i = 42
    return callsBlock2( \-> i ) as java.lang.Integer
  }

  function callMethodThatCallsObjBlockWithCaptureUsingSideEffectsInt() : int {
    var i = 0
    callsBlock2( \-> { i = 42; return i } )
    return i
  }

  function callMethodThatCallsGenericfuncBlockNoCaptureInt() : int {
    return genericCallsBlock( \-> 42 )
  }

  function callMethodThatCallsGenericfuncBlockWithCaptureInt() : int {
    var i = 42
    return genericCallsBlock( \-> i )
  }

  function callMethodThatCallsGenericfuncBlockWithCaptureUsingSideEffectsInt() : int {
    var i = 0
    genericCallsBlock( \-> { i = 42; return i } )
    return i
  }

  function callMethodThatCallsBlockNoCaptureDouble() : double {
    return callsBlock( \-> 42.0 as String ).toDouble()
  }

  function callMethodThatCallsBlockWithCaptureDouble() : double {
    var i = 42.0
    return callsBlock( \-> i as String ).toDouble()
  }

  function callMethodThatCallsBlockWithCaptureUsingSideEffectsDouble() : double {
    var i = 0.0
    callsBlock( \-> { i = 42.0; return i as String } )
    return i
  }

  function callMethodThatCallsObjBlockNoCaptureDouble() : double {
    var x = callsBlock2( \-> 42.0 ) as String
    return  x.toDouble()
  }

  function callMethodThatCallsObjBlockWithCaptureDouble() : double {
    var i = 42.0
    var s = callsBlock2( \-> i ) as String
    return s.toDouble()
  }

  function callMethodThatCallsObjBlockWithCaptureUsingSideEffectsDouble() : double {
    var i = 0.0
    callsBlock2( \-> { i = 42.0; return i } )
    return i
  }

  function callMethodThatCallsGenericfuncBlockNoCaptureDouble() : double {
    return genericCallsBlock( \-> 42.0 )
  }

  function callMethodThatCallsGenericfuncBlockWithCaptureDouble() : double {
    var i = 42.0
    return genericCallsBlock( \-> i )
  }

  function callMethodThatCallsGenericfuncBlockWithCaptureUsingSideEffectsDouble() : double {
    var i = 0.0
    genericCallsBlock( \-> { i = 42.0; return i } )
    return i
  }

  function callsProducesStringBlock() : String {
    var blk = producesStringBlock()
    return blk()
  }

  function callsProducesIntBlock() : int {
    var blk = producesIntBlock()
    return blk()
  }

  function callsProducesDoubleBlock() : double {
    var blk = producesDoubleBlock()
    return blk()
  }

  function callsProducesCapturedStringBlock() : String {
    var blk = producesCapturedStringBlock( "yay" )
    return blk()
  }

  function callsProducesCapturedIntBlock() : int {
    var blk = producesCapturedIntBlock( 42 )
    return blk()
  }

  function callsProducesCapturedDoubleBlock() : double {
    var blk = producesCapturedDoubleBlock( 42.0 )
    return blk()
  }

  function producesStringBlock() : block():String {
    return \-> "yay"
  }

  function producesIntBlock() : block():int {
    return \-> 42
  }

  function producesDoubleBlock() : block():double {
    return \-> 42.0
  }

  function producesCapturedStringBlock( s : String ) : block():String {
    return \-> s
  }

  function producesCapturedIntBlock( i : int ) : block():int {
    return \-> i
  }

  function producesCapturedDoubleBlock( d : double ) : block():double {
    return \-> d
  }

  function producesOneArgStringBlock() : block(String):String {
    return \ s : String -> s
  }

  function producesOneArgIntBlock() : block(int):int {
    return \ i : int -> i
  }

  function producesOneArgDoubleBlock() : block(double):double {
    return \ d : double -> d
  }

  function callsBlock( blk() : String ) : String {
    return blk()
  }

  function callsBlock2( blk() : Object ) : Object {
    return blk()
  }

  function genericCallsBlock<T>( blk : block():T ) : T {
    return blk()
  }

  function callParamThenInnerThenBlock() : java.util.concurrent.Callable {
    return paramThenInnerThenBlock( "yay" )
  }

  function paramThenInnerThenBlock( str : String ) : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> str
      }
    }
  }

  function localThenInnerThenBlock() : java.util.concurrent.Callable {
    var str = "yay"
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> str
      }
    }
  }

  function innerThenFieldThenBlock() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      var str = "yay"
      override function call() : Object {
        return \-> str
      }
    }
  }

  function innerThenLocalThenBlock() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var str = "yay"
        return \-> str
      }
    }
  }

  function callParamThenInnerThenInnerThenBlock() : java.util.concurrent.Callable {
    return paramThenInnerThenInnerThenBlock( "yay" )
  }

  function paramThenInnerThenInnerThenBlock( str : String ) : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> str
          }
        }
      }
    }
  }

  function localThenInnerThenInnerThenBlock() : java.util.concurrent.Callable {
    var str = "yay"
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> str
          }
        }
      }
    }
  }

  function innerThenFieldThenInnerThenBlock() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      var str = "yay"
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> str
          }
        }
      }
    }
  }

  function innerThenLocalThenInnerThenBlock() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var str = "yay"
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> str
          }
        }
      }
    }
  }

  function innerThenInnerThenFieldThenBlock() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          var str = "yay"
          override function call() : Object {
            return \-> str
          }
        }
      }
    }
  }

  function innerThenInnerThenLocalThenBlock() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            var str = "yay"
            return \-> str
          }
        }
      }
    }
  }

  function callParamThenBlockTheInner() : Object {
    return paramThenBlockThenInner( "yay" )
  }

  function paramThenBlockThenInner( str : String ) : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return str
      }
    }
  }

  function localThenBlockThenInner() : Object {
    var str = "yay"
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return str
      }
    }
  }

  function blockThenLocalThenInner() : Object {
    return \-> {
      var str = "yay"
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return str
        }
      }
    }
  }

  function callParamThenBlockThenInnerThenBlock() : Object {
    return blockThenInnerThenInnerThenBlock( "yay" )
  }

  function blockThenInnerThenInnerThenBlock( str : String ) : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> str
      }
    }
  }

  function localThenBlockThenInnerThenBlock() : Object {
    var str = "yay"
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> str
      }
    }
  }

  function blockThenLocalThenInnerThenBlock() : Object {
    return \-> {
      var str = "yay"
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return \-> str
        }
      }
    }
  }

  function blockThenInnerThenFieldThenBlock() : Object {
    return \-> {
      return new java.util.concurrent.Callable() {
        var str = "yay"
        override function call() : Object {
          return \-> str
        }
      }
    }
  }

  function blockThenInnerThenLocalThenBlock() : Object {
    return \-> {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          var str = "yay"
          return \-> str
        }
      }
    }
  }

  function localThenBlockThenLocalThenInnerThenLocalThenBlock() : Object {
    var str1 = "yay"
    return \-> {
      var str2 = str1
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          var str = str2
          return \-> str
        }
      }
    }
  }

  function localThenBlockThenLocalThenInnerThenFieldThenBlock() : Object {
    var str1 = "yay"
    return \-> {
      var str2 = str1
      return new java.util.concurrent.Callable() {
        var str = str2
        override function call() : Object {
          return \-> str
        }
      }
    }
  }

  function callParamThenInnerThenBlockInt() : java.util.concurrent.Callable {
    return paramThenInnerThenBlockInt( 42 )
  }

  function paramThenInnerThenBlockInt( i : int ) : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function localThenInnerThenBlockInt() : java.util.concurrent.Callable {
    var i = 42
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function innerThenFieldThenBlockInt() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      var i = 42
      override function call() : Object {
        return \-> i
      }
    }
  }

  function innerThenLocalThenBlockInt() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var i = 42
        return \-> i
      }
    }
  }

  function callParamThenInnerThenInnerThenBlockInt() : java.util.concurrent.Callable {
    return paramThenInnerThenInnerThenBlockInt( 42 )
  }

  function paramThenInnerThenInnerThenBlockInt( i : int ) : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function localThenInnerThenInnerThenBlockInt() : java.util.concurrent.Callable {
    var i = 42
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenFieldThenInnerThenBlockInt() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      var i = 42
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenLocalThenInnerThenBlockInt() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var i = 42
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenInnerThenFieldThenBlockInt() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          var i = 42
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenInnerThenLocalThenBlockInt() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            var i = 42
            return \-> i
          }
        }
      }
    }
  }

  function callParamThenBlockTheInnerInt() : Object {
    return paramThenBlockThenInnerInt( 42 )
  }

  function paramThenBlockThenInnerInt( i : int ) : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return i
      }
    }
  }

  function localThenBlockThenInnerInt() : Object {
    var i = 42
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return i
      }
    }
  }

  function blockThenLocalThenInnerInt() : Object {
    return \-> {
      var i = 42
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return i
        }
      }
    }
  }

  function callParamThenBlockThenInnerThenBlockInt() : Object {
    return blockThenInnerThenInnerThenBlockInt( 42 )
  }

  function blockThenInnerThenInnerThenBlockInt( i : int ) : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function localThenBlockThenInnerThenBlockInt() : Object {
    var i = 42
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function blockThenLocalThenInnerThenBlockInt() : Object {
    return \-> {
      var i = 42
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return \-> i
        }
      }
    }
  }

  function blockThenInnerThenFieldThenBlockInt() : Object {
    return \-> {
      return new java.util.concurrent.Callable() {
        var i = 42
        override function call() : Object {
          return \-> i
        }
      }
    }
  }

  function blockThenInnerThenLocalThenBlockInt() : Object {
    return \-> {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          var i = 42
          return \-> i
        }
      }
    }
  }

  function localThenBlockThenLocalThenInnerThenLocalThenBlockInt() : Object {
    var i1 = 42
    return \-> {
      var i2 = i1
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          var i = i2
          return \-> i
        }
      }
    }
  }

  function localThenBlockThenLocalThenInnerThenFieldThenBlockInt() : Object {
    var i1 = 42
    return \-> {
      var i2 = i1
      return new java.util.concurrent.Callable() {
        var i = i2
        override function call() : Object {
          return \-> i
        }
      }
    }
  }

  function callParamThenInnerThenBlockDouble() : java.util.concurrent.Callable {
    return paramThenInnerThenBlockDouble( 42.0 )
  }

  function paramThenInnerThenBlockDouble( i : double ) : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function localThenInnerThenBlockDouble() : java.util.concurrent.Callable {
    var i = 42.0
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function innerThenFieldThenBlockDouble() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      var i = 42.0
      override function call() : Object {
        return \-> i
      }
    }
  }

  function innerThenLocalThenBlockDouble() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var i = 42.0
        return \-> i
      }
    }
  }

  function callParamThenInnerThenInnerThenBlockDouble() : java.util.concurrent.Callable {
    return paramThenInnerThenInnerThenBlockDouble( 42.0 )
  }

  function paramThenInnerThenInnerThenBlockDouble( i : double ) : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function localThenInnerThenInnerThenBlockDouble() : java.util.concurrent.Callable {
    var i = 42.0
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenFieldThenInnerThenBlockDouble() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      var i = 42.0
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenLocalThenInnerThenBlockDouble() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var i = 42.0
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenInnerThenFieldThenBlockDouble() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          var i = 42.0
          override function call() : Object {
            return \-> i
          }
        }
      }
    }
  }

  function innerThenInnerThenLocalThenBlockDouble() : java.util.concurrent.Callable {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            var i = 42.0
            return \-> i
          }
        }
      }
    }
  }

  function callParamThenBlockTheInnerDouble() : Object {
    return paramThenBlockThenInnerDouble( 42.0 )
  }

  function paramThenBlockThenInnerDouble( i : double ) : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return i
      }
    }
  }

  function localThenBlockThenInnerDouble() : Object {
    var i = 42.0
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return i
      }
    }
  }

  function blockThenLocalThenInnerDouble() : Object {
    return \-> {
      var i = 42.0
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return i
        }
      }
    }
  }

  function callParamThenBlockThenInnerThenBlockDouble() : Object {
    return blockThenInnerThenInnerThenBlockDouble( 42.0 )
  }

  function blockThenInnerThenInnerThenBlockDouble( i : double ) : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function localThenBlockThenInnerThenBlockDouble() : Object {
    var i = 42.0
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> i
      }
    }
  }

  function blockThenLocalThenInnerThenBlockDouble() : Object {
    return \-> {
      var i = 42.0
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          return \-> i
        }
      }
    }
  }

  function blockThenInnerThenFieldThenBlockDouble() : Object {
    return \-> {
      return new java.util.concurrent.Callable() {
        var i = 42.0
        override function call() : Object {
          return \-> i
        }
      }
    }
  }

  function blockThenInnerThenLocalThenBlockDouble() : Object {
    return \-> {
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          var i = 42.0
          return \-> i
        }
      }
    }
  }

  function localThenBlockThenLocalThenInnerThenLocalThenBlockDouble() : Object {
    var i1 = 42.0
    return \-> {
      var i2 = i1
      return new java.util.concurrent.Callable() {
        override function call() : Object {
          var i = i2
          return \-> i
        }
      }
    }
  }

  function localThenBlockThenLocalThenInnerThenFieldThenBlockDouble() : Object {
    var i1 = 42.0
    return \-> {
      var i2 = i1
      return new java.util.concurrent.Callable() {
        var i = i2
        override function call() : Object {
          return \-> i
        }
      }
    }
  }

  function localThenInnerCreatesBlockInvokedByInner() : Object {
    var str = "yay"
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var blk = \-> str
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return blk()
          }
        }
      }
    }
  }

  function localThenInnerCreatesInnerInvokedByBlock() : Object {
    var str = "yay"
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var innerCallable = new java.util.concurrent.Callable() {
          override function call() : Object {
            return str
          }
        }
        return \-> innerCallable.call()
      }
    }
  }

  function localThenInnerCreatesBlockInvokedByInnerInt() : Object {
    var str = 42
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var blk = \-> str
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return blk()
          }
        }
      }
    }
  }

  function localThenInnerCreatesInnerInvokedByBlockInt() : Object {
    var str = 42
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var innerCallable = new java.util.concurrent.Callable() {
          override function call() : Object {
            return str
          }
        }
        return \-> innerCallable.call()
      }
    }
  }

  function localThenInnerCreatesBlockInvokedByInnerDouble() : Object {
    var str = 42.0
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var blk = \-> str
        return new java.util.concurrent.Callable() {
          override function call() : Object {
            return blk()
          }
        }
      }
    }
  }

  function localThenInnerCreatesInnerInvokedByBlockDouble() : Object {
    var str = 42.0
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        var innerCallable = new java.util.concurrent.Callable() {
          override function call() : Object {
            return str
          }
        }
        return \-> innerCallable.call()
      }
    }
  }

  function returnYay() : String {
    return "yay"
  }

  function outerSymbolTestBlockThenInner() : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return outer.returnYay()
      }
    }
  }

  function outerSymbolTestInnerThenBlock() : Object {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> outer.returnYay()
      }
    }
  }

  function outerSymbolTestBlockThenBlockThenInner() : Object {
    return \->\-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return outer.returnYay()
      }
    }
  }

  function outerSymbolTestInnerThenBlockThenBlock() : Object {
    return new java.util.concurrent.Callable() {
      override function call() : Object {
        return \->\-> outer.returnYay()
      }
    }
  }

  function outerSymbolTestBlockThenInnerThenBlock() : Object {
    return \-> new java.util.concurrent.Callable() {
      override function call() : Object {
        return \-> outer.returnYay()
      }
    }
  }

  static var _staticNoArgBlock = \-> "foo"
  static function staticFieldNoArgBlock() : Object {
    return _staticNoArgBlock
  }

  static var _staticOneArgBlock = \ o : Object -> o
  static function staticFieldOneArgBlock() : Object {
    return _staticOneArgBlock
  }

  static var _staticInt = 42
  static var _staticOneArgBlockWithIntCapture = \ o : Object -> o.toString() + _staticInt
  static function staticFieldOneArgBlockWithIntCapture() : Object {
    return _staticOneArgBlockWithIntCapture
  }

  static var _staticString = "StaticString"
  static var _staticOneArgBlockWithStringCapture = \ o : Object -> o + _staticString
  static function staticFieldOneArgBlockWithStringCapture() : Object {
    return _staticOneArgBlockWithStringCapture
  }

  static var _staticOneArgBlockWithBlockCapture = \ o : Object -> o.toString() + _staticOneArgBlock( o )
  static function staticFieldOneArgBlockWithBlockCapture() : Object {
    return _staticOneArgBlockWithBlockCapture
  }

  static var _staticOnePrimitiveArgBlock = \ i : int -> i
  static function staticFieldOnePrimitiveArgBlock() : Object {
    return _staticOnePrimitiveArgBlock
  }

  static function staticFunctionNoArgBlock() : Object {
    return \-> "foo"
  }

  static function staticFunctionOneArgBlock() : Object {
    return \ o : Object -> o
  }

  static function staticFunctionOneArgBlockWithIntCapture() : Object {
    return \ o : Object -> o.toString() + _staticInt
  }

  static function staticFunctionOneArgBlockWithStringCapture() : Object {
    return \ o : Object -> o + _staticString
  }

  static function staticFunctionOneArgBlockWithBlockCapture() : Object {
    return \ o : Object -> o.toString() + _staticOneArgBlock( o )
  }

  static function staticFunctionOnePrimitiveArgBlock() : Object {
    return \ i : int -> i
  }

  static property get StaticPropertyNoArgBlock() : Object {
    return \-> "foo"
  }

  static property get StaticPropertyOneArgBlock() : Object {
    return \ o : Object -> o
  }

  static property get StaticPropertyOneArgBlockWithIntCapture() : Object {
    return \ o : Object -> o.toString() + _staticInt
  }

  static property get StaticPropertyOneArgBlockWithStringCapture() : Object {
    return \ o : Object -> o + _staticString
  }

  static property get StaticPropertyOneArgBlockWithBlockCapture() : Object {
    return \ o : Object -> o.toString() + _staticOneArgBlock( o )
  }

  static property get StaticPropertyOnePrimitiveArgBlock() : Object {
    return \ i : int -> i
  }
  
   var _NoArgBlock = \-> "foo"
   function FieldNoArgBlock() : Object {
    return _NoArgBlock
  }

   var _OneArgBlock = \ o : Object -> o
   function FieldOneArgBlock() : Object {
    return _OneArgBlock
  }

   var _Int = 42
   var _OneArgBlockWithIntCapture = \ o : Object -> o.toString() + _Int
   function FieldOneArgBlockWithIntCapture() : Object {
    return _OneArgBlockWithIntCapture
  }

   var _String = "String"
   var _OneArgBlockWithStringCapture = \ o : Object -> o + _String
   function FieldOneArgBlockWithStringCapture() : Object {
    return _OneArgBlockWithStringCapture
  }

   var _OneArgBlockWithBlockCapture = \ o : Object -> o.toString() + _OneArgBlock( o )
   function FieldOneArgBlockWithBlockCapture() : Object {
    return _OneArgBlockWithBlockCapture
  }

   var _OnePrimitiveArgBlock = \ i : int -> i
   function FieldOnePrimitiveArgBlock() : Object {
    return _OnePrimitiveArgBlock
  }

   function FunctionNoArgBlock() : Object {
    return \-> "foo"
  }

   function FunctionOneArgBlock() : Object {
    return \ o : Object -> o
  }

   function FunctionOneArgBlockWithIntCapture() : Object {
    return \ o : Object -> o.toString() + _Int
  }

   function FunctionOneArgBlockWithStringCapture() : Object {
    return \ o : Object -> o + _String
  }

   function FunctionOneArgBlockWithBlockCapture() : Object {
    return \ o : Object -> o.toString() + _OneArgBlock( o )
  }

   function FunctionOnePrimitiveArgBlock() : Object {
    return \ i : int -> i
  }

   property get PropertyNoArgBlock() : Object {
    return \-> "foo"
  }

   property get PropertyOneArgBlock() : Object {
    return \ o : Object -> o
  }

   property get PropertyOneArgBlockWithIntCapture() : Object {
    return \ o : Object -> o.toString() + _Int
  }

   property get PropertyOneArgBlockWithStringCapture() : Object {
    return \ o : Object -> o + _String
  }

   property get PropertyOneArgBlockWithBlockCapture() : Object {
    return \ o : Object -> o.toString() + _OneArgBlock( o )
  }

   property get PropertyOnePrimitiveArgBlock() : Object {
    return \ i : int -> i
  }

  function getVoidBlock1() : Object {
    return \-> {}
  }

  function getVoidBlock2() : Object {
    return \-> { var x = 10 }
  }

  function getVoidBlock3() : Object {
    return \-> { var x = 10 return }
  }

  function getVoidBlock4() : Object {
    return \-> { return }
  }

  function getVoidBlock5() : Object {
    return \-> { print( "foo" ) }
  }

  function getVoidBlock6() : Object {
    return \-> print( "foo" )
  }

  function getVoidBlock7() : Object {
    return \-> null
  }

  function testPrimitiveReturnBlockWorks1() : Object {
    return \-> 10
  }

  function testPrimitiveReturnBlockWorks2() : Object {
    return \-> 10.0
  }

  function testPrimitiveReturnBlockWorks3() : Object {
    return \-> true
  }

  function testPrimitiveReturnBlockWorks4() : Object {
    return \-> 10 as long
  }

  function testBlockToCallableCoercionWorks1() : Object {
    return (\-> "block as callable") as java.util.concurrent.Callable
  }

  function testBlockToCallableCoercionWorks2() : Object {
    var callable = (\-> String) as java.util.concurrent.Callable<java.lang.Class>
    return callable.call()
  }

  function testBlockToPredicateCoercionWorks() : Object {
    return (\ s : String -> s == "test" ) as gw.util.Predicate<String>
  }

  function testToString1() : Object {
    return \-> 10
  }

  function testToString2() : Object {
    return \-> print( true )
  }

  function testToString3() : Object {
    return \-> { var x = 10 }
  }

  function testToString4() : Object {
    return \-> { for( x in {} ) {  print( x ) } }
  }

  function invokeIntBlock() : Object {
    return takesNumberBlock( \ i : Object -> 10 )
  }

  function invokesIntBlockInStringBlockContext() : Object {
    var blk = (\-> 10 as String) as block():String
    return blk()
  }

  function takesNumberBlock<T>( blk(T):java.lang.Number ) : java.lang.Number {
    return blk( null )
  }

}