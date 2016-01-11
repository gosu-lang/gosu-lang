package gw.lang.spec_old.statements.class_stmt.inner_classes.anonymous_classes
uses gw.testharness.DoNotVerifyResource
uses java.lang.Runnable
@DoNotVerifyResource
class Errant_BadBreaksContinueInAnonClass {

  function badBreaks() {
    for( i in 1..10 ){ var blk = new Runnable() { function run() {break} } }
    while( true ){ var blk = new Runnable() { function run() {break} } }
    do{ var blk = new Runnable() { function run() {break} } while(true) }
    switch(true){ case true : var blk = new Runnable() { function run() {break} } }
  }

  function badContinues() {
    for( i in 1..10 ){ var blk = new Runnable() { function run() {continue} } }
    while( true ){ var blk = new Runnable() { function run() {continue} } }
    do{ var blk = new Runnable() { function run() {continue} } while(true) }
  }
  
  function goodBreaks() {
    var blk1 = new Runnable() { function run() { for( i in 1..10 ){ break } } }
    var blk2 = new Runnable() { function run() { while( true ){ break } } }
    var blk3 = new Runnable() { function run() { do{ break } while(true) } }
    var blk4 = new Runnable() { function run() { switch(true){ case true : break } } }
  }

  function continues() {
    var blk1 = new Runnable() { function run() { for( i in 1..10 ){ continue } } }
    var blk2 = new Runnable() { function run() { while( true ){ continue } }  }
    var blk3 = new Runnable() { function run() { do{ continue } while(true) } }
  }
}
