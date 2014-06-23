package gw.internal.gosu.compiler.blocks
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_BadBreakContinuesInBlocks {

  function badBreaks() {
    for( i in 1..10 ){ var blk = \-> {break} }
    while( true ){ var blk = \-> {break} }
    do{ var blk = \-> {break} } while(true)
    switch(true){ case true : var blk = \-> {break} }
  }

  function badContinues() {
    for( i in 1..10 ){ var blk = \-> {continue} }
    while( true ){ var blk = \-> {continue} }
    do{ var blk = \-> {continue} } while(true)
  }
  
  function goodBreaks() {
    var blk1 = \-> { for( i in 1..10 ){ break } }
    var blk2 = \-> { while( true ){ break } } 
    var blk3 = \-> { do{ break } while(true) }
    var blk4 = \-> { switch(true){ case true : break } }
  }

  function continues() {
    var blk1 = \-> { for( i in 1..10 ){ continue } }
    var blk2 = \-> { while( true ){ continue } } 
    var blk3 = \-> { do{ continue } while(true) }
  }
}
